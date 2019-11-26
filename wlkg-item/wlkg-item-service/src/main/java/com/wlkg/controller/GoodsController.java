package com.wlkg.controller;

import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.Sku;
import com.wlkg.item.pojo.Spu;
import com.wlkg.item.pojo.SpuDetail;
import com.wlkg.service.BrandService;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BrandService brandService;

    //分页显示  http://api.wlkg.com/api/item/spu/page?key=&saleable=true&page=1&rows=5
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            //1-5页
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        // 分页查询spu信息
        PageResult<Spu> result = this.goodsService.querySpuByPageAndSort(page, rows, saleable, key);
        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
//        System.out.println(1+"+++++++");
        try {
            this.goodsService.save(spu);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
    编辑---页面回显
    - 请求方式：GET
- 请求路径：/spu/detail/{id}
- 请求参数：id，应该是spu的id
- 返回结果：SpuDetail对象
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        SpuDetail detail = this.goodsService.querySpuDetailById(id);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detail);
    }
    /*
    编辑---页面回显
     请求方式：Get
- 请求路径：/sku/list
- 请求参数：id，应该是spu的id
- 返回结果：sku的集合
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = this.goodsService.querySkuBySpuId(id);
        if (skus == null || skus.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skus);
    }
    /*
    修改
    请求方式：PUT
- 请求路径：/
- 请求参数：Spu对象
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        this.goodsService.update(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //删除
    @DeleteMapping("/goods/deleteGoods")
    public void deleteGoods(@RequestParam("id") Long id){
        goodsService.deleteGoods(id);
    }

    /*
     * 根据spu的id查询spu
     * @param id
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        return ResponseEntity.ok(spu);
    }

}
