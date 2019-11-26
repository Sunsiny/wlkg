package com.wlkg.item.api;

import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.Sku;
import com.wlkg.item.pojo.Spu;
import com.wlkg.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsAPI {

    @GetMapping("/spu/page")
    public PageResult<Spu> querySpuByPage(
            //1-5页
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    @GetMapping("/spu/detail/{id}")
    public SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    @GetMapping("/sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);
}
