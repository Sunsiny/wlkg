package com.wlkg.Controller;

import com.wlkg.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsController {

    @Autowired
    private PageService pageService;
    /*
    跳转到详情页
    @param  id
     */
    @GetMapping("item/{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long id){
        // 加载所需的数据
        Map<String, Object> modelMap = this.pageService.loadModel(id);
        // 放入模型
        model.addAllAttributes(modelMap);
        // 判断是否需要生成新的页面
        if(!this.pageService.exists(id)){
            this.pageService.syncCreateHtml(id);
        }
        return "item";
    }

//    @GetMapping("item/{id}.html")
//    @ResponseBody
//    public Map<String,Object> toItemPageback(Model model, @PathVariable("id")Long id){
//        //查询模型数据
//        Map<String,Object> result = pageService.loadModel(id);
//
//        return result;
//    }

}
