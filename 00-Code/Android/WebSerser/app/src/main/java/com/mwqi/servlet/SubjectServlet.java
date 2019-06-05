package com.mwqi.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.os.SystemClock;

public class SubjectServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String strResponse0 = "[{des:'一周新锐游戏精选',url:'image/recommend_01.jpg'},{des:'文艺VS2B',url:'image/recommend_02.jpg'},{des:'用户特权中心',url:'image/recommend_03.jpg'},{des:'月度榜单-5月篇',url:'image/recommend_04.jpg'},{des:'我要过六一',url:'image/recommend_05.jpg'},{des:'有一种教育叫\"玩\"',url:'image/recommend_06.jpg'},{des:'再不疯狂我们就老了',url:'image/recommend_07.jpg'},{des:'5.1-放飞心情',url:'image/recommend_08.jpg'},{des:'舌尖上的中国',url:'image/recommend_09.jpg'},{des:'你好!我的英雄',url:'image/recommend_10.jpg'},{des:'致那些丢不掉的记忆',url:'image/recommend_11.jpg'},{des:'无\"三星\"不游戏',url:'image/recommend_12.jpg'},{des:'爱创意，给生活加点\"料\"',url:'image/recommend_13.jpg'},{des:'突破十面\"霾\"伏,还我小清新',url:'image/recommend_14.jpg'},{des:'来自星星的你们',url:'image/recommend_15.jpg'},{des:'聚焦海外,先发产品汇',url:'image/recommend_16.jpg'},{des:'开学新装备,学霸出击!',url:'image/recommend_17.jpg'},{des:'情人节浪漫攻略',url:'image/recommend_18.jpg'},{des:'马上有一切',url:'image/recommend_19.jpg'},{des:'年末贺岁之游戏派队',url:'image/recommend_20.jpg'}]";
		String strResponse1 = "[{des:'2013年度最佳应用',url:'image/recommend_21.jpg'},{des:'2013年度最佳游戏',url:'image/recommend_22.jpg'},{des:'小怪兽游戏集锦',url:'image/recommend_23.jpg'},{des:'感恩节游戏合集',url:'image/recommend_24.jpg'},{des:'史上最虐心游戏大盘点',url:'image/recommend_25.jpg'},{des:'运动健身软件合集',url:'image/recommend_26.jpg'},{des:'光棍不寂寞',url:'image/recommend_27.jpg'},{des:'万圣节:鬼魅狂欢',url:'image/recommend_28.jpg'},{des:'争当好奶爸',url:'image/recommend_29.jpg'},{des:'2013上半年游戏精选',url:'image/recommend_30.jpg'},{des:'金秋时节,话养生',url:'image/recommend_31.jpg'},{des:'涨姿势益智答题游戏集',url:'image/recommend_32.jpg'},{des:'安卓智能终端大赛',url:'image/recommend_33.jpg'},{des:'歌声响不停',url:'image/recommend_34.jpg'},{des:'玩转时尚',url:'image/recommend_35.jpg'},{des:'主发泄暴爽游戏推荐',url:'image/recommend_36.jpg'},{des:'雷人坑爹游戏大鉴赏',url:'image/recommend_37.jpg'},{des:'预热国庆近期游戏精选',url:'image/recommend_38.jpg'},{des:'小巧打飞机合集',url:'image/recommend_39.jpg'},{des:'解谜100系列专辑',url:'image/recommend_40.jpg'}]";
		String strResponse2 = "[{des:'台球游戏精选',url:'image/recommend_41.jpg'},{des:'GLU游戏精选',url:'image/recommend_42.jpg'},{des:'游走在黄灯边缘的...',url:'image/recommend_43.jpg'},{des:'开学倒计时',url:'image/recommend_44.jpg'},{des:'电视应用专区',url:'image/recommend_45.jpg'},{des:'商旅出行指南',url:'image/recommend_46.jpg'},{des:'交友SNS',url:'image/recommend_47.jpg'},{des:'GAMEVIL游戏合集',url:'image/recommend_48.jpg'},{des:'\"增强\"虚拟现实游戏精选',url:'image/recommend_49.jpg'},{des:'优秀地图导航大血拼',url:'image/recommend_50.jpg'},{des:'屌丝必备',url:'image/recommend_51.jpg'},{des:'纯中文经营类游戏精选',url:'image/recommend_52.jpg'},{des:'奋斗:学海无涯',url:'image/recommend_53.jpg'},{des:'激情逃亡跑酷合集',url:'image/recommend_54.jpg'},{des:'unity引擎游戏精选',url:'image/recommend_55.jpg'},{des:'Fire!劲爆打枪游戏',url:'image/recommend_56.jpg'},{des:'飞利浦平板推荐专区',url:'image/recommend_57.jpg'},{des:'益智解谜游戏合集',url:'image/recommend_58.jpg'},{des:'爱机美化必备',url:'image/recommend_59.jpg'},{des:'\"绳命\"在于运动',url:'image/recommend_60.jpg'}]";
		String strResponse3 = "[{des:'移动游戏',url:'image/recommend_61.jpg'},{des:'动态壁纸-唯美风',url:'image/recommend_62.jpg'},{des:'系统级玩机达人必备',url:'image/recommend_63.jpg'},{des:'轻松一刻,休闲游戏',url:'image/recommend_64.jpg'},{des:'C3游戏引擎专区',url:'image/recommend_65.jpg'},{des:'汉化精品区',url:'image/recommend_66.jpg'},{des:'经典RPG的逆袭',url:'image/recommend_67.jpg'},{des:'新一轮塔防全场hold住',url:'image/recommend_68.jpg'},{des:'上古魔幻游戏大搜罗',url:'image/recommend_69.jpg'},{des:'浩瀚天空战个痛快',url:'image/recommend_70.jpg'},{des:'天翼专区',url:'image/recommend_71.jpg'},{des:'极限运动游戏合集',url:'image/recommend_72.jpg'},{des:'新浪微博应用',url:'image/recommend_73.jpg'},{des:'便捷生活小工具',url:'image/recommend_74.jpg'},{des:'精品期刊',url:'image/recommend_75.jpg'},{des:'平板应用必备',url:'image/recommend_76.jpg'},{des:'谷歌应用大集合',url:'image/recommend_77.jpg'},{des:'手机安全顾问',url:'image/recommend_78.jpg'}]";
		String strResponse4 = "[]";
		String indexStr = req.getParameter("index");
		String strResponse = null;
		if(indexStr.equals("0")){
			strResponse = strResponse0;
		}else if(indexStr.equals("20")){
			strResponse = strResponse1;
		}else if(indexStr.equals("40")){
			strResponse = strResponse2;
		}else if(indexStr.equals("60")){
			strResponse = strResponse3;
		}else{
			strResponse = strResponse4;
		}
		//SystemClock.sleep(5000);
		byte[] b = strResponse.getBytes();
		resp.setContentLength(b.length);
		OutputStream out = resp.getOutputStream();
		out.write(b);
		out.close();
	}
}
