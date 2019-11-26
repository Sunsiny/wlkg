package com.wlkg;

import com.wlkg.client.GoodsClient;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.Spu;
import com.wlkg.pojo.Goods;
import com.wlkg.repository.GoodsRepository;
import com.wlkg.service.GoodsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BuildIndex {
    @Autowired
    private GoodsRepository repository;
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsService goodsService;

    @Test
    public void createIndex(){
        //创建索引
        template.createIndex(Goods.class);
        //配置映射
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        long begin = System.currentTimeMillis();
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询分页数据
            PageResult<Spu> result = this.goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spus = result.getItems();
            size = spus.size();
            // 创建Goods集合
            List<Goods> goodsList = new ArrayList<>();
            // 遍历spu
            for (Spu spu : spus) {
                Goods goods2 = goodsService.buildGoods(spu);
//                System.out.println("goods2-----"+goods2);
                try {
                    Goods goods = this.goodsService.buildGoods(spu);
                    goodsList.add(goods);
                } catch (Exception e) {
                    break;
                }
            }
//            System.out.println("goodsList:+++"+goodsList);
            this.repository.saveAll(goodsList);
            page++;
        } while (size == 100);

        long end = System.currentTimeMillis();
        System.out.println("耗时："+(end-begin)/1000);
    }
}
