<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>用户登陆页面</title>
    <%@include file="common/head.jsp" %>
</head>
<body>

<div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>用户登陆页面</h2>
        </div>
        </div>
<div class="container"  style="margin-top:150px;text-align: center;">

		<div>
			<form>
				用户： <input type="text" name="user"> <br /> 
				密码： <input type="password" name="password"><br /> 
				 <input type="submit" value="Submit" />
			</form>
		</div>



</div>



<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</body>
</html>