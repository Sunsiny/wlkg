package com.wlkg.service;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.item.pojo.Category;
import com.wlkg.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    //查询
    public List<Category> queryCategoryByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if(CollectionUtils.isEmpty(list)){
            throw new WlkgException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return list;
    }
    //添加
    public int queryCategoryAdd(Category category){
        category.setIsParent(false);
        int res = categoryMapper.insertSelective(category);
        if(res == 0){
            throw new WlkgException(ExceptionEnums.INSERT_INTO_IS_NOT);
        }
        return res;
    }

    public Category changeIsParentId(Long id) {
        Category categoryfather=categoryMapper.seleceByParentId(id);
        System.out.println("categoryfather:-----"+categoryfather);
        categoryfather.setIsParent(true);
        System.out.println("categoryfather:+++"+categoryfather.toString());
        categoryMapper.updateByPrimaryKeySelective(categoryfather);
        return categoryfather;
    }

    //修改
    public void updateByIdName(Category category) {
        Category category1 = categoryMapper.selectByPrimaryKey(category);
        //System.out.println("category1-----:"+category1);
        category1.setName(category.getName());

        categoryMapper.updateByPrimaryKey(category1);
       // System.out.println("category1+++:"+category1);
    }
    //删除
    public void deleteCategory(Long id){
        Category category = new Category();
        category.setId(id);
        int i = categoryMapper.delete(category);
        if(i == 0){
            throw new WlkgException(ExceptionEnums.INSERT_INTO_IS_NOT);
        }
    }
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }
    //页面需要商品的分类名称
    public List<String> queryNameByIds(List<Long> ids) {
        return this.categoryMapper.selectByIdList(ids).stream().map(Category::getName).collect(Collectors.toList());
    }

    /**
     * 根据分类id集合查询每个分类对象
     *
     * @param ids
     * @return
     */
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
//        System.out.println("list:"+list);
        if (CollectionUtils.isEmpty(list)) {
            throw new WlkgException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return list;
    }
}
