package me.ztiany.mvc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import me.ztiany.mvc.pojo.Items;
import me.ztiany.mvc.service.ItemService;

/**
 * 商品管理，ItemController只会被初始化一次
 */
@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/item/itemList.action")
    public ModelAndView itemList() {
        /*
          // 创建页面需要显示的商品数据
        List<Items> list = new ArrayList<>();
        list.add(new Items(1, "1华为 荣耀8", 2399f, new Date(), "质量好！1"));
        list.add(new Items(2, "2华为 荣耀8", 2399f, new Date(), "质量好！2"));
        list.add(new Items(3, "3华为 荣耀8", 2399f, new Date(), "质量好！3"));
        list.add(new Items(4, "4华为 荣耀8", 2399f, new Date(), "质量好！4"));
        list.add(new Items(5, "5华为 荣耀8", 2399f, new Date(), "质量好！5"));
        list.add(new Items(6, "6华为 荣耀8", 2399f, new Date(), "质量好！6"));

        //数据
        ModelAndView mav = new ModelAndView();
        mav.addObject("itemList", list);
        //mav.setViewName("/WEB-INF/jsp/itemList.jsp");
        //Spring配置的视图解析器可以设置前缀后缀
        mav.setViewName("itemList");
         */

        List<Items> list = itemService.selectItemsList();

        ModelAndView mav = new ModelAndView();
        mav.addObject("itemList", list);
        mav.setViewName("itemList");

        return mav;
    }


    @SuppressWarnings("all")
    @RequestMapping(value = "item/itemEdit.action")
    public ModelAndView toEdit(Integer id) {
        Items items = itemService.selectItemsById(id);

        ModelAndView mav = new ModelAndView();
        mav.addObject("item", items);
        mav.setViewName("editItem");

        return mav;
    }

    @RequestMapping(value = "item/updateItem.action")
    public String updateItem(Items items) {
        System.out.println(items);
        itemService.updateItemsById(items);
        return "redirect:/common/success.action";
    }

    //json数据交互
    @RequestMapping(value = "/json.action")
    public @ResponseBody
    Items json(@RequestBody Items items) {

        return items;
    }

    //RestFul风格的开发
    @RequestMapping(value = "/itemEdit/{id}.action")
    @SuppressWarnings("all")
    public ModelAndView restFulToEdit(@PathVariable Integer id) {
        Items items = itemService.selectItemsById(id);
        ModelAndView mav = new ModelAndView();
        mav.addObject("item", items);
        mav.setViewName("editItem");
        return mav;
    }


}
