package com.wlkg.mapper;

import com.wlkg.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {

    //修改
    @Update("UPDATE tb_category set name = #{name} WHERE id =#{id}")
    public int handleEdit(Long id,String name);
    @Select("SELECT * from tb_category where id=#{id}")
    Category seleceByParentId(Long id);

    //通过中间表进行子查询，
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);


}