package com.wlkg.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.mapper.BaseMapper;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.*;
import com.wlkg.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    //分页查询
    public PageResult<Spu> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key) {
        // 1、查询SPU
        // 分页,最多允许查100条
        PageHelper.startPage(page, Math.min(rows, 100));
        // 创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 是否过滤上下架
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }
        // 是否模糊查询  StringUtils---判断输入的内容是否为空
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //默认排序
        example.setOrderByClause("last_update_time desc");

        //查询
        List<Spu> spus = this.spuMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(spus)) {
            throw new WlkgException(ExceptionEnums.GOODS_NOT_FOUND);
        }

        PageInfo<Spu> pageInfo = new PageInfo<Spu>(spus);
        for (Spu spu : spus) {
            // 2、查询spu的商品分类名称,要查三级分类
            List<String> names = this.categoryService.queryNameByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            // 将分类名称拼接后存入
            spu.setCname(StringUtils.join(names, "/"));

            // 3、查询spu的品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spu.setBname(brand.getName());
        }

        return new PageResult<>(pageInfo.getTotal(), spus);
    }


    //要对SPU新增以外，还要对SpuDetail、Sku、Stock进行保存------新增
    @Transactional
    public void save(Spu spu) {
        // 保存spu
        spu.setSaleable(true);   //上架
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());

//        System.out.println("spu:---"+spu);

        spuMapper.insert(spu);
        // 保存spu详情
        spu.getSpuDetail().setSpuId(spu.getId());
        spuDetailMapper.insert(spu.getSpuDetail());

        // 保存sku和库存信息
        saveSkuAndStock(spu.getSkus(), spu.getId());
    }

    //添加---这是个抽取出来的方法
    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku : skus) {
            if (!sku.getEnable()) {
                continue;
            }
            // 保存sku
            sku.setSpuId(spuId);
            // 默认不参与任何促销
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            //添加sku
            skuMapper.insert(sku);

            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock().intValue());
            stocks.add(stock);
        }
        //批量新增库存
        stockMapper.insertList(stocks);
    }
//查询---回显
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }
    //查询---回显
    public List<Sku> querySkuBySpuId(Long spuId) {
        // 查询sku
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        for (Sku sku : skus) {
            // 同时查询出库存
            sku.setStock(this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        }
        return skus;
    }
    //修改
    @Transactional
    public void update(Spu spu) {
        //1、修改spu
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);

        if(!CollectionUtils.isEmpty(skuList)){
            //删除sku和stock
            skuMapper.delete(sku);

            //删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }

        //修改spu---设置为空
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count!=1) {
            throw new WlkgException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        //修改spudetail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count!=1){
            throw new WlkgException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        saveSkuAndStock(spu.getSkus(),spu.getId());

        //修改删除，发送消息
        sendMessage(spu.getId(),"update");  //item.updat --在路由里面
    }

    //删除
    public void deleteGoods(Long id){
         Spu spu = spuMapper.selectByPrimaryKey(id);
//        System.out.println("spu+++"+spu);
         //通过spu_id获得spu_detail
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
//        System.out.println("spuDetail:"+spuDetail);
         //获得sku通过spu_id
        List<Sku> skus = skuMapper.findSkuBySpuId(id);
//        System.out.println("sku:---"+skus);
        for(Sku sku : skus){
            Long sku_id = sku.getId();
//            System.out.println("sku_id:"+sku_id);
            //获得sku_stock通过sku的id
            Stock stock = stockMapper.selectByPrimaryKey(sku_id);
//            System.out.println("stock:"+stock);
            //删除stock
            if(stock != null){
                stockMapper.deleteByPrimaryKey(sku_id);
            }
            //根据sku的id删除sku
            skuMapper.deleteByPrimaryKey(sku_id);
        }
        //删除spuDetail
        if(spuDetail != null){
            spuDetailMapper.deleteByPrimaryKey(id);
        }
        //删除spu
        if(spu != null){
            spuMapper.deleteByPrimaryKey(id);
        }
    }


    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null){
            throw new WlkgException(ExceptionEnums.GOODS_NOT_FOUND);
        }
        //查询sku
        spu.setSkus(querySkuBySpuId(id));
        //查询detail
        spu.setSpuDetail(querySpuDetailById(id));
//        System.out.println("spu------"+spu);
        return spu;
    }

    /*封装一个发送消息到mq的方法：
    - 这里没有指定交换机，因此默认发送到了配置中的：`wlkg.item.exchange`
注意：这里要把所有异常都try起来，不能让消息的发送影响到正常的业务逻辑
然后在新增的时候调用：
     */
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}