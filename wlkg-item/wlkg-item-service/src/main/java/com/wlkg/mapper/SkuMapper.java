package com.wlkg.mapper;

import com.wlkg.common.mapper.BaseMapper;
import com.wlkg.item.pojo.Sku;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SkuMapper extends BaseMapper<Sku,Long> {
    @Select("SELECT * from tb_sku where spu_id = #{id}")
    public List<Sku> findSkuBySpuId(Long spuid);
}