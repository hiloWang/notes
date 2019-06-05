package com.mwqi.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CategoryServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String strResponse = "[{title:'游戏',infos:[{url1:'image/category_game_0.jpg',url2:'image/category_game_1.jpg',url3:'image/category_game_2.jpg',name1:'休闲',name2:'棋牌',name3:'益智'},{url1:'image/category_game_3.jpg',url2:'image/category_game_4.jpg',url3:'image/category_game_5.jpg',name1:'射击',name2:'体育',name3:'儿童'},{url1:'image/category_game_6.jpg',url2:'image/category_game_7.jpg',url3:'image/category_game_8.jpg',name1:'网游',name2:'角色',name3:'策略'},{url1:'image/category_game_9.jpg',url2:'image/category_game_10.jpg',url3:'',name1:'经营',name2:'竞速',name3:''}]},{title:'应用',infos:[{url1:'image/category_app_0.jpg',url2:'image/category_app_1.jpg',url3:'image/category_app_2.jpg',name1:'浏览器',name2:'输入法',name3:'健康'},{url1:'image/category_app_3.jpg',url2:'image/category_app_4.jpg',url3:'image/category_app_5.jpg',name1:'效率',name2:'教育',name3:'理财'},{url1:'image/category_app_6.jpg',url2:'image/category_app_7.jpg',url3:'image/category_app_8.jpg',name1:'阅读',name2:'个性化',name3:'购物'},{url1:'image/category_app_9.jpg',url2:'image/category_app_10.jpg',url3:'image/category_app_11.jpg',name1:'资讯',name2:'生活',name3:'工具'},{url1:'image/category_app_12.jpg',url2:'image/category_app_13.jpg',url3:'image/category_app_14.jpg',name1:'出行',name2:'通讯',name3:'拍照'},{url1:'image/category_app_15.jpg',url2:'image/category_app_16.jpg',url3:'image/category_app_17.jpg',name1:'社交',name2:'影音',name3:'安全'}]}]";
		byte[] b = strResponse.getBytes();
		resp.setContentLength(b.length);
		OutputStream out = resp.getOutputStream();
		out.write(b);
		out.close();
	}
}
