package com.itheima.mobileguard.test;

import java.util.Random;

import android.content.Context;
import android.test.AndroidTestCase;

import com.itheima.mobileguard.db.dao.BlackNumberDao;

public class TestBlackNumnerDao extends AndroidTestCase {
	
	private Context  context;
	private BlackNumberDao dao;
	protected void setUp() throws Exception {
		context = getContext();//模拟出来的context
		super.setUp();
	}
	
	public void testInsert(){
		dao = new BlackNumberDao(context);
		Random r = new Random();
		for(int i=0;i<200;i++){
			dao.insert((13800000000l+(i*1000))+"", ""+(r.nextInt(3)+1));
		}
	}
	public void testUpdate(){
		dao = new BlackNumberDao(context);
		boolean result = dao.update("13800000000", "2");
		assertEquals(true, result);
	}
	public void testFindAll(){
		dao = new BlackNumberDao(context);
		System.out.println(dao.findAll());
	}
	public void testFindMode(){
		dao = new BlackNumberDao(context);
		System.out.println(dao.findModeByNumber("13800000000"));
	}
	public void testDelete(){
		dao = new BlackNumberDao(context);
		boolean result = dao.delete("13800000000");
		assertEquals(true, result);
	}
}
