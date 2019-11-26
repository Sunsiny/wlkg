package com.wlkg.controller;

import com.wlkg.common.pojo.PageResult;
import com.wlkg.item.pojo.Brand;
import com.wlkg.service.BrandService;
import com.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    CategoryService categoryService;
   /*
    分页
    请求方式：GET
    请求参数：page=1,rows=5,key="",sortby="letter", desc=true
    请求的URL:/brand/page
    返回类型:List<Brands>, total, totalPages
    */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        PageResult<Brand> result = this.brandService.queryBrandByPageAndSort(page,rows,sortBy,desc, key);
        return ResponseEntity.ok(result);
    }

    //增加品牌
    //1、请求方式：POST
    //2、请求的参数：Brand对象，List<Integer> cids
    //3、url：/item/brand
    //4、返回类型：无返回 ResponseEntity<Void>
    @PostMapping("addBrand")
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.insertByBrandCids(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //删除商品
    @DeleteMapping("deleteBrand")
    public ResponseEntity<Void> deleteBrand(@RequestParam("id") Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    //根据分类查询品牌
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid") Long cid) {
        List<Brand> list = brandService.queryBrandByCategory(cid);
        if (CollectionUtils.isEmpty(list)) {
            // 响应404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(list);
    }

    //根据id查询品牌
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.queryBrandById(id));
    }
}
