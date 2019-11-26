package com.wlkg.item.api;

import com.wlkg.item.pojo.Brand;
import com.wlkg.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface BrandAPI {
    //通过id查找品牌
    @GetMapping("/brand/{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);
}
