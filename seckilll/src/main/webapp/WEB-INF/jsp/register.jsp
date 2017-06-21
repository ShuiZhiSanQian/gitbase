<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>用户注册页面</title>
    <%@include file="common/head.jsp" %>
</head>
<body>

       <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>用户注册页面</h2>
        </div>
        </div>
<div class="container"  style="margin-top:150px;text-align: center;">
  <form  id="formid"  name= "myform"  onsubmit = "return checkUser();"  >
            <table width="60%" border="0"><tr>
                <td width="400" height="40" align="right">电&nbsp;&nbsp;话：&nbsp;</td>
                <td><input type="text" value="" class="text2" name = "userphone" id = "userphone"/></td>
              </tr>

              <tr>
                <td width="20" height="40" align="right">密&nbsp;&nbsp;码：&nbsp;</td>
                <td><input type="password" value="" class="text2" name = "password" id = "password"/></td>
              </tr>
               <tr>
                <td width="60" height="40" align="right">职&nbsp;&nbsp;业：&nbsp;</td>
                <td><input type="text" value="" class="text3" name = "vocation" id = "vocation"/></td>
              </tr>
                <tr>
                <td width="60" height="40" align="right">爱&nbsp;&nbsp;好：&nbsp;</td>
                <td><input type="text" value="" class="text4" name = "favorate" id = "favorate"/></td>
              </tr>
              <tr>
                <td width="20" height="40" align="right">&nbsp;</td>
                <td><div class="c4">
                    <span class="glyphicon" id="seckillregister"></span>
                    <input type="submit" value="注册" class="btn2"  />
                    </div>
               </td>
              </tr>
            </table>
</form>
</div>



<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>


<script type="text/javascript">

function checkUser(){
	   var userphone = document.getElementById("userphone").value;
	   var password = document.getElementById("password").value;
	   var vocation = document.getElementById("vocation").value;
	   var favorate = document.getElementById("favorate").value;
	   
	  
	   
	   if(password == ""  ){
	    alert("密码不能为空");
	     return false;
	   }else{
	   return true;
	   }
	  if(vocation == ""  ){
		    alert("职业不能为空");
		     return false;
		   }else{
		   return true;
		   }
	  if(favorate == ""  ){
			    alert("爱好不能为空");
			     return false;
			   }else{
			   return true;
			   }
	  
	  if(!(userphone=="") && userphone.length == 11 && !isNaN(userphone)){
			$.post("/seckill/"+userphone+"/"+password+"/"+vocation+"/"+favorate+"/saveuser",{},function(result){
			});
			alert("success");
			return true;
	   }else{
		  
		   alert("电话格式错误，请 重新输入！");
		   return false;
	   }
	}
</script>
</body>
</html>