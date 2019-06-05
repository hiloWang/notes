package com.ztiany.mall.config;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.17 0:19
 */
public class AppConfig {

    public static final String ACTION = "action";

    ///////////////////////////////////////////////////////////////////////////
    // filed
    ///////////////////////////////////////////////////////////////////////////
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTO_LOGIN = "autoLogin";
    public static final String ACTIVE_CODE = "activeCode";

    ///////////////////////////////////////////////////////////////////////////
    // dao service names
    ///////////////////////////////////////////////////////////////////////////
    public static final String ADMIN_SERVICE = "adminService";
    public static final String ADMINDAO = "adminDao";

    public static final String USER_SERVICE = "userService";
    public static final String USER_DAO = "userDao";

    public static final String PRODUCT_SERVICE = "product_service";
    public static final String PRODUCT_DAO = "product_dao";

    ///////////////////////////////////////////////////////////////////////////
    // session cookie request
    ///////////////////////////////////////////////////////////////////////////
    public static final String USER = "user";
    public static final String LOGIN_ERROR = "login_error";

    public static final String COOKIE_USERNAME = "cookie_username";
    public static final String COOKIE_PASSWORD = "cookie_password";

    public static final String HOT_PRODUCT_LIST = "hotProductList";
    public static final String NEW_PRODUCT_LIST = "newProductList";

    public static final String COOKIE_PRODUCT_VIEW_HISTORY = "cookie_product_view_history";


    ///////////////////////////////////////////////////////////////////////////
    // 状态码
    ///////////////////////////////////////////////////////////////////////////
    public static int NO_ACTIVE = 1;//没有激活
    public static int ACTIVATED = 2;//没有激活

    public static int SUCCESS = 1;//操作成功
    public static int USER_EXIST = 2;//用户名已存在
    public static int ACTIVE_CODE_NOT_MATCH = 3;//激活码不匹配

    public static int HOT_PRODUCT = 1;//热门商品标识

    ///////////////////////////////////////////////////////////////////////////
    // 配置
    ///////////////////////////////////////////////////////////////////////////
    public static int PRODUCT_PAGE_SIZE = 12;//商品列表 page size

    ///////////////////////////////////////////////////////////////////////////
    // 缓存
    ///////////////////////////////////////////////////////////////////////////
    public static final String CATEGORY_LIST_KEY = "categoryListJson";
}
