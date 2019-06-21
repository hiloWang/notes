package me.ztiany.mvc.service;


import java.util.List;

import me.ztiany.mvc.pojo.Items;

public interface ItemService {

    //查询商品列表
    List<Items> selectItemsList();

    Items selectItemsById(Integer id);

    //修改
    void updateItemsById(Items items);

}
