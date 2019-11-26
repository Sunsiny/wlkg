package com.wlkg.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.Brand;
import com.wlkg.mapper.BrandMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page,Integer rows,String sortBy,Boolean desc,String key
    ){
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);

        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);

    }
    //插入---
    @Transactional
    public void insertByBrandCids(Brand brand, List<Long> cids) {
        if(brand.getId() != null){
            brandMapper.deleteByPrimaryKey(brand.getId());
        }
        brandMapper.insertSelective(brand);
        for (Long cid: cids) {
            brandMapper.insertByBrandidCategoryId(cid,brand.getId());
        }
    }

    public void deleteBrand(Long id) {
       int i = brandMapper.deleteByPrimaryKey(id);
        if(i == 0){
           throw new WlkgException(ExceptionEnums.Brand_is_not);
        }
    }
    //根据分类查询品牌
    public List<Brand> queryBrandByCategory(Long cid) {
        return this.brandMapper.queryByCategoryId(cid);
    }

    public Brand queryBrandById(Long id){
           Brand brand = brandMapper.selectByPrimaryKey(id);
//        System.out.println("brands:+++"+brands);
        return brand;
    }
}
