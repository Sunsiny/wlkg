package com.wlkg.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_sku")
@Data
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;   //主题
    private String images;
    private Long price;
    private String ownSpec;// 商品特殊规格的键值对
    private String indexes;// 商品特殊规格的下标
    private Boolean enable;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间

    //这里保存了一个库存字段，在数据库中是另外一张表保存的，方便查询
    @Transient
    private Integer stock;// 库存
}
