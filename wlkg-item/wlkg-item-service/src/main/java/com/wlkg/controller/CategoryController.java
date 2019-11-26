package com.wlkg.controller;

import com.wlkg.item.pojo.Category;
import com.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
一般用在Controller层，用来解决前端与后端参数不一致的问题，在参数上加上@RequestParam，那么前端的参数必须和后端参数一致，否则会报错。
当只有一个参数的时候，Controller层中@RequestParam和@Param可以互用，当有多个参数的时候，Controller层中@RequestParam和@Param不能互用
 */

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //查询
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam("pid") Long pid){
        List<Category> list = categoryService.queryCategoryByPid(pid);
        return ResponseEntity.ok(list);
    }
    //添加
    @PostMapping("/handleAdd")
    public int queryCategoryAdd(@RequestBody Category category){
        categoryService.changeIsParentId(category.getParentId());
        return categoryService.queryCategoryAdd(category);
    }

    //修改
    @PutMapping("/handleEdit")
    public String editCategoryName(@RequestBody Category category){
        categoryService.updateByIdName(category);
        return "forward:/item/category";
    }
    //删除
    @DeleteMapping("/handleDelete")
    public String handleDelete(@RequestParam("id")Long id){
        categoryService.deleteCategory(id);
        return "redirect:/item/category";
    }
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据Id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

}
