package com.itheima.mobileguard.test;

import java.util.List;

import android.content.Context;
import android.test.AndroidTestCase;

import com.itheima.mobileguard.db.dao.LockedAppDao;

public class TestAppLockDao extends AndroidTestCase {
	
	private Context  context;
	private LockedAppDao dao;
	protected void setUp() throws Exception {
		context = getContext();//模拟出来的context
		super.setUp();
	}
	
	public void testInsert(){
		dao = new LockedAppDao(context);
		boolean result = dao.add("haha");
		assertEquals(true, result);
	}
	
	public void testFindAll(){
		dao = new LockedAppDao(context);
		List<String> l = dao.findAll();
		System.out.println("::::::::"+l);
	}
	
}
