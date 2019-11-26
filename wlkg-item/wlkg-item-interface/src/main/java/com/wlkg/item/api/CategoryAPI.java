package com.wlkg.item.api;

import com.wlkg.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryAPI {
    @GetMapping("/category/list/ids")
    public List<Category> queryCategoryByIds(@RequestParam("pid")List<Long> ids);
}
