<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="pannel-heading">
            <h1>${seckill.name}</h1>
        </div>

        <div class="panel-body">
            <h2 class="text-danger">
                <%--显示time图标--%>
                <span class="glyphicon glyphicon-time"></span>
                <%--展示倒计时--%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>


<%--登录弹出层 输入电话--%>
<div id="killPhoneModal" class="modal fade">

    <div class="modal-dialog">

        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"> </span>秒杀电话:
                </h3>
            </div>

            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                               placeholder="填写手机号^o^" class="form-control">
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <%--验证信息--%>
                <span id="killPhoneMessage" class="glyphicon"> </span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>

        </div>
    </div>

</div>

<%--点击秒杀按钮时，输入验证码--%>
<div id="ImageVerify" class="modal fade" style="margin-top:150px;text-align: center;">

    <div class="modal-dialog">

        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"> </span>验证码:
                </h3>
            </div>

            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="validateCode" id="validateCode"
                               placeholder="输入验证码(区分大小写)" class="form-control">
                    </div>
                </div>
            </div>

      <div >
     <span class="y_yzimg"><img id="codeValidateImg" onClick="javascript:flushValidateCode();" /></span>
     <p class="y_change"><a href="javascript:flushValidateCode();" >换一张</a></p>
     </div>
            <div class="modal-footer1">
                <%--验证信息--%>
                <span id="killPhoneVerify" class="glyph"> </span>
                <button type="button" id="verifyBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>

     </div>
   </div>
 
</div>


</body>
<%--jQery文件,务必在bootstrap.min.js之前引入--%>
 <script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<%--使用CDN 获取公共js http://www.bootcdn.cn/--%>
<%--jQuery Cookie操作插件--%>
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<%--jQuery countDown倒计时插件--%>
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<!-- 开始编写交互逻辑 -->
 <script src="/seckilll/resources/script/seckill.js" type="text/javascript"></script>

<script type="text/javascript">
 $(function(){
//	 alert("1");
	//使用EL表达式传入参数
	 seckill.detail.init({
			seckillId: "${seckill.seckillId}",
			startTime: "${seckill.startTime.time}",
			endTime: "${seckill.endTime.time}"
		});
 });
 
 
 $(document).ready(function() {
	 flushValidateCode();//进入页面就刷新生成验证码
	 });
 /* 刷新生成验证码 */
 function flushValidateCode(){
 var validateImgObject = document.getElementById("codeValidateImg");
 validateImgObject.src = "${pageContext.request.contextPath }/getSysManageLoginCode?time=" + new Date();
 }
 
 
</script>
</html>