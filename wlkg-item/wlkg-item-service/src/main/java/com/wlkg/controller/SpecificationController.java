package com.wlkg.controller;

import com.wlkg.item.pojo.SpecGroup;
import com.wlkg.item.pojo.SpecParam;
import com.wlkg.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecGroups(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
    //添加
    @PostMapping("addGroup")
    public void addGroup(@RequestBody SpecGroup specGroup){
        specificationService.addGroup(specGroup);
    }
    //删除
    @DeleteMapping("deleteGroup")
    public void deleteGroup(@RequestParam("id") Long id){
        specificationService.deleteGroup(id);
    }


    /*规格查询----考虑到以后可能还会根据是否搜索、是否为通用属性等条件过滤，我们多添加几个过滤条件：
    如果param中有属性为null，则不会吧属性作为查询条件，因此该方法具备通用性，即可根据gid查询，也可根据cid查询
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic
    ){
        List<SpecParam> list =
                this.specificationService.querySpecParams(gid,cid,searching,generic);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    // 查询规格参数组，及组内参数
    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@RequestParam("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(list);
    }
}
