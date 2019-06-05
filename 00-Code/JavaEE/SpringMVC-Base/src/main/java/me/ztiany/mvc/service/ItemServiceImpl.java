package me.ztiany.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import me.ztiany.mvc.dao.ItemsMapper;
import me.ztiany.mvc.pojo.Items;


/**
 * 查询商品信息
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemsMapper itemsMapper;

    @Autowired
    public ItemServiceImpl(ItemsMapper itemsMapper) {
        this.itemsMapper = itemsMapper;
    }

    //查询商品列表
    public List<Items> selectItemsList() {
        return itemsMapper.selectByExampleWithBLOBs(null);
    }

    public Items selectItemsById(Integer id) {
        return itemsMapper.selectByPrimaryKey(id);
    }

    //修改
    public void updateItemsById(Items items) {
        items.setCreatetime(new Date());
        itemsMapper.updateByPrimaryKeyWithBLOBs(items);
    }
}
