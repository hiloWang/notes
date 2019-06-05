<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员登录</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/jquery.validate.min.js"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="css/style.css" type="text/css"/>

    <style>

        body {
            margin: 0 auto;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }

        font {
            color: #666;
            font-size: 22px;
            font-weight: normal;
            padding-right: 17px;
        }

        /*validate*/
        .error {
            color: red
        }

    </style>


    <script type="text/javascript">
        $(function () {
            $("#loginForm").validate({
                rules: {

                    "username": {
                        "required": true,
                        "rangelength": [6, 12]
                    },

                    "password": {
                        "required": true,
                        "rangelength": [6, 12]
                    },

                    "confirmPassword": {
                        "required": true,
                        "rangelength": [6, 12],
                        "equalTo": "#password"
                    }
                },
                messages: {
                    "username": {
                        "required": "用户名不能为空",
                        "rangelength": "用户名长度6-12位",
                    },
                    "password": {
                        "required": "密码不能为空",
                        "rangelength": "密码长度6-12位"
                    },
                    "confirmPassword": {
                        "required": "密码不能为空",
                        "rangelength": "密码长度6-12位",
                        "equalTo": "两次密码不一致"
                    }
                }
            });
        });
    </script>

</head>

<body>

<!-- 引入header.jsp 动态包含-->
<jsp:include page="/header.jsp"/>

<div class="container"
     style="width: 100%; height: 460px; background: #FF2C4C url('images/loginbg.jpg') no-repeat;">

    <div class="row">

        <div class="col-md-7">
            <!--<img src="./image/login.jpg" width="500" height="330" alt="会员登录" title="会员登录">-->
        </div>

        <div class="col-md-5">

            <div style="width: 440px; border: 1px solid #E7E7E7; padding: 20px 0 20px 30px; border-radius: 5px; margin-top: 60px; background: #fff;">

                <font>会员登录</font>USER LOGIN
                <div>&nbsp;</div>

                <form id="loginForm" class="form-horizontal" method="post"
                      action="${pageContext.request.contextPath}/AccountServlet">

                    <input type="hidden" name="action" value="login">

                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">用户名</label>
                        <div class="col-sm-6">
                            <input type="text" name="username" class="form-control" id="username"
                                   placeholder="请输入用户名">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-6">
                            <input type="password" class="form-control" name="password"
                                   id="password" placeholder="请输入密码">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword" class="col-sm-2 control-label">验证码</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" id="confirmPassword"
                                   placeholder="请输入验证码">
                        </div>
                        <div class="col-sm-3">
                            <img src="./image/captcha.jhtml"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <div class="checkbox">
                                <label> <input type="checkbox" name="autoLogin" value="autoLogin">自动登录</label>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label><input type="checkbox"> 记住用户名(没有实现)</label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="submit" width="100" value="登录" name="submit"
                                   style="background: url('./images/login.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
                        </div>

                        <div class="col-sm-offset-2 col-sm-10"><span
                                class="error">${requestScope.login_error}</span></div>

                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<!-- 引入footer.jsp -->
<jsp:include page="/footer.jsp"/>

</body>
</html>