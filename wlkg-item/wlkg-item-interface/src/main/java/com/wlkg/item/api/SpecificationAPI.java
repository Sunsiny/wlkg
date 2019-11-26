package com.wlkg.item.api;

import com.wlkg.item.pojo.SpecGroup;
import com.wlkg.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationAPI {
    @GetMapping("/spec/{id}")
    String querySpecificationByCategoryId(@PathVariable("id") Long id);

    @GetMapping("/spec/params")
    public List<SpecParam> querySpecParam(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic
    );

    // 查询规格参数组，及组内参数
    @GetMapping("/spec/group")
    public List<SpecGroup> querySpecsByCid(@RequestParam("cid") Long cid);
}
