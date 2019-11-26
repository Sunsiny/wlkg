package com.wlkg.service;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.item.pojo.SpecGroup;
import com.wlkg.item.pojo.SpecParam;
import com.wlkg.mapper.SpecGroupMapper;
import com.wlkg.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroups(Long cid) {
        SpecGroup t = new SpecGroup();
        t.setCid(cid);
        return this.specGroupMapper.select(t);
    }
    //添加
    public void addGroup(SpecGroup specGroup){
        //System.out.println(specGroup+"11111");
        SpecGroup specGroup1 = specGroupMapper.selectByPrimaryKey(specGroup.getId());
       // System.out.println(specGroup1+"++++++");
        if(specGroup1 != null){
            specGroupMapper.delete(specGroup1);
        }
       // System.out.println(specGroup+"22222");
        specGroupMapper.insertSelective(specGroup);
    }
    //删除
    public void deleteGroup(Long id){
        specGroupMapper.deleteByPrimaryKey(id);
    }

    //查询规格
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam t = new SpecParam();
        t.setGroupId(gid);
        t.setCid(cid);
        t.setSearching(searching);
        t.setGeneric(generic);

        List<SpecParam> list = this.specParamMapper.select(t);
        if (CollectionUtils.isEmpty(list)) {
            throw new WlkgException(ExceptionEnums.GOODS_NOT_FOUND);
        }
        return list;
    }

    public List<SpecGroup> querySpecsByCid(Long cid) {
        //查询规格组
        List<SpecGroup> groups = querySpecGroups(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams = querySpecParams(null,cid,null,null);
        Map<Long,List<SpecParam>> map = new HashMap<>();

        for (SpecParam param : specParams){
            if(!map.containsKey(param.getGroupId())){
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(), new ArrayList<>());
            }

            map.get(param.getGroupId()).add(param);
        }
        //填充param到group
        for (SpecGroup specGroup: groups){
            //为空的话，就不添加
            if(map.get(specGroup.getId())!=null){
                specGroup.setParams(map.get(specGroup.getId()));
            }

        }
        return groups;
    }
}
