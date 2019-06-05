<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>

<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员注册</title>

    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>

    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="js/jquery.validate.min.js" type="text/javascript"></script>
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
            color: #3164af;
            font-size: 18px;
            font-weight: normal;
            padding: 0 10px;
        }

        .error {
            color: red;
        }

    </style>


    <%--Validate库校验是针对name的，需要校验的字段必须有name属性--%>
    <script type="text/javascript">

        //自定义校验规则
        $.validator.addMethod(
            //规则的名称
            "checkUsername",

            //校验的函数
            function (value, element, params) {
                //定义一个标志
                var flag = false;

                //value:输入的内容
                //element:被校验的元素对象
                //params：规则对应的参数值
                //目的：对输入的username进行ajax校验
                $.ajax({
                    "async": false,//同步调用
                    "url": "${pageContext.request.contextPath}/AccountServlet",
                    "data": {"username": value, "action": "checkUsername"},
                    "type": "POST",
                    "dataType": "json",
                    "success": function (data) {
                        flag = data.isExist;
                    }
                });
                console.log("--------------------------------" + flag);
                //返回false代表该校验器不通过
                return !flag;
            }
        );

        $(function () {
            $("#registerForm").validate({
                rules: {
                    "username": {
                        "required": true,
                        "checkUsername": true
                    },
                    "password": {
                        "required": true,
                        "rangelength": [6, 12]
                    },
                    "confirmPassword": {
                        "required": true,
                        "rangelength": [6, 12],
                        "equalTo": "#password"
                    },
                    "name": {
                        "required": true,
                        "rangelength": [2, 6]
                    },
                    "email": {
                        "required": true,
                        "email": true
                    },
                    "sex": {
                        "required": true
                    }
                },
                messages: {
                    "username": {
                        "required": "用户名不能为空",
                        "checkUsername": "用户名已存在"
                    },
                    "password": {
                        "required": "密码不能为空",
                        "rangelength": "密码长度6-12位"
                    },
                    "confirmPassword": {
                        "required": "密码不能为空",
                        "rangelength": "密码长度6-12位",
                        "equalTo": "两次密码不一致"
                    },
                    "name": {
                        "required": "姓名不能为空",
                        "rangelength": "姓名长度2-6位"
                    },
                    "email": {
                        "required": "邮箱不能为空",
                        "email": "邮箱格式不正确"
                    }
                }
            });
        });

    </script>


</head>

<body>

<!-- 引入header.jsp -->
<jsp:include page="/header.jsp"/>

<div class="container" style="width: 100%; background: url('image/regist_bg.jpg');">

    <%--row start--%>
    <div class="row">

        <div class="col-md-2"></div>

        <%--div from start--%>
        <div class="col-md-8"
             style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">

            <font>会员注册</font>USER REGISTER

            <form id="registerForm" class="form-horizontal" style="margin-top: 5px;"
                  method="post"
                  action="${pageContext.request.contextPath}/AccountServlet">

                <input type="hidden" name="action" value="register">

                <div class="form-group">
                    <label for="username" class="col-sm-2 control-label">用户名</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="username"
                               name="username" placeholder="请输入用户名">
                    </div>
                </div>

                <div class="form-group">
                    <label for="password" class="col-sm-2 control-label">密码</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="请输入密码">
                    </div>
                </div>

                <div class="form-group">
                    <label for="confirmPassword" class="col-sm-2 control-label">确认密码</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="confirmPassword"
                               name="confirmPassword" placeholder="请输入确认密码">
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-6">
                        <input type="email" name="email" class="form-control" id="inputEmail3"
                               placeholder="Email">
                    </div>
                </div>

                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">姓名</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="name" name="name"
                               placeholder="请输入姓名">
                    </div>
                </div>

                <div class="form-group opt">
                    <label for="sex1" class="col-sm-2 control-label">性别</label>

                    <div class="col-sm-6">
                        <label class="radio-inline">
                            <input type="radio"
                                   name="sex"
                                   id="sex1"
                                   value="option1">男
                        </label>

                        <label class="radio-inline">
                            <input type="radio"
                                   name="sex"
                                   id="sex2"
                                   value="option2">女
                        </label>

                        <%--让Validate库使用我们的错误表示，这里的sex是表单字段sex--%>
                        <label class="error" for="sex" style="display:none ">请选择性别</label>

                    </div>
                </div>

                <div class="form-group">
                    <label for="date" class="col-sm-2 control-label">出生日期</label>
                    <div class="col-sm-6">
                        <input type="date" id="date" class="form-control" name="birthday">
                    </div>
                </div>

                <div class="form-group">
                    <label for="code" class="col-sm-2 control-label">验证码</label>
                    <div class="col-sm-3">
                        <input id="code" type="text" class="form-control">
                    </div>
                    <div class="col-sm-2">
                        <img src="./image/captcha.jhtml"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="submit" width="100" value="注册" name="submit"
                               style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
                    </div>
                </div>

            </form>
            <%--div from end--%>
        </div>

        <div class="col-md-2"></div>

        <%--row end--%>
    </div>

</div>

<!-- 引入footer.jsp -->
<jsp:include page="/footer.jsp"/>

</body>
</html>




