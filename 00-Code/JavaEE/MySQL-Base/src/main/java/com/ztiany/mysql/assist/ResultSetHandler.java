package com.ztiany.mysql.assist;

import java.sql.ResultSet;

//抽象策略
public interface ResultSetHandler {

    Object handle(ResultSet rs);

}
