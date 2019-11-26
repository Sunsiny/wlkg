package com.wlkg.controller;

import com.wlkg.client.BrandClient;
import com.wlkg.client.CategoryClient;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.Brand;
import com.wlkg.item.pojo.Category;
import com.wlkg.pojo.Goods;
import com.wlkg.pojo.SearchRequest;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/getbrand/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandClient.queryBrandById(id));
    }
    @GetMapping("/category/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("pid")List<Long> ids){
        return ResponseEntity.ok(categoryClient.queryCategoryByIds(ids));
    }

/*
- 请求方式：Post
- 请求路径：/search/page，不过前面的/search应该是网关的映射路径，因此真实映射路径page，代表分页查询
- 请求参数：json格式，目前只有一个属性：key-搜索关键字，但是搜索结果页一定是带有分页查询的，所以将来肯定会有page属性，因此我们可以用一个对象来接收请求的json数据：
 */

    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(this.goodsService.search(request));
    }
}
