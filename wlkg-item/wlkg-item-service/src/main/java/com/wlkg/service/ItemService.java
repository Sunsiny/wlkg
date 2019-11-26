package com.wlkg.service;

import com.wlkg.item.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ItemService {
    public Item saveItem(Item item){
//        if(item.getPrice() == null){
//            throw new WlkgException(ExceptionEnums.ITEM_IS_NOT_FIND);
//        }
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
