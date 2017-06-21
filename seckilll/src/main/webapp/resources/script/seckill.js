//存放主要的交互逻辑js代码
//javascript 模块化

var seckill = {

	// 封装秒杀相关ajax的url
	URL : {
           now :function(){
        	   return '/seckilll/time/now';
           },
           exposer: function(seckillId){
        	   return '/seckilll/'+seckillId+'/exposer';
           },
           execution: function(seckillId,md5){
        	   return '/seckilll/'+seckillId+'/'+md5+'/execution';
           }
           
	},
	
	
	/* 刷新生成验证码 */
	flushValidateCode: function (){
	 var validateImgObject = document.getElementById("codeValidateImg");
	 validateImgObject.src = "${pageContext.request.contextPath }/getSysManageLoginCode?time=" + new Date();
	 },
	 
	 /*校验验证码输入是否正确*/
	 checkImg: function (code){
//	 	alert("code:"+code);
		var imagecode = $.cookie('imagecode');
//		alert("imagecode:"+imagecode);
	 	if(imagecode===code){
//	 		alert("ok");
	 		return true;
	 	}else{
//	 		alert("no");
	 		return false;
	 	}
//	  var url = "${pageContext.request.contextPath}/checkimagecode";
//	  $.get(url,{"validateCode":code},function(data){
//	  if(data=="ok"){
//	  alert("ok!")
//		  return true;
//	  }else{
//	  alert("error!")
//		  return false;
//	  flushValidateCode();
//	  }
//	  })
	 },
	// 验证手机号
	validatePhone : function(phone) {
		if (phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		} else {
			return false;
		}
	},
	
	//获取秒杀地址，控制显示逻辑，执行秒杀
	handleSeckill: function(seckillId,node){
		node.hide()
		    .html('<button class="btn bg-primary btn-lg" id="killBtn">开始秒杀</button>');
		var ImageVerify = $('#ImageVerify');

		$.post(seckill.URL.exposer(seckillId),{},function(result){
			console.log(result.success);
			console.log(result.data);
			if(result&&result['success']){
            	var exposer=result['data'];
            	if(exposer['exposed']){
            		//开启秒杀
            		//获取秒杀地址
            		var md5=exposer['md5'];
            		var killurl=seckill.URL.execution(seckillId,md5);
            		console.log("killurl:"+killurl);
            		//只绑定一次click事件
            		$('#killBtn').one('click',function(){
            							
            			// 显示弹出层
            			ImageVerify.modal({
            				show : true,// 显示弹出层
            				backdrop : 'static',// 禁止位置关闭
            				keyboard : false
            			// 关闭键盘事件
            			});
            			$('#verifyBtn')
            			.click( function(){
            				var validateCode =document.getElementById("validateCode").value ;
            				
            			
            				if(seckill.checkImg(validateCode)){
            				//	alert("执行秒杀操作！！！");
            					//点击一次后，使之失效
                    		//	$(this).addClass('disabled');
                    			//提交秒杀
                    			$.post(killurl,{},function(result){
                    				if(result&&result['success']){
                    					var killResult=result['data'];
                    					var status=killResult['status'];
                    					var stateInfo=killResult['stateInfo'];
                    					
                    					node.html('<span class="label label-success">'+stateInfo+'</span>');
                    				}
                    			});      
                    			$('#ImageVerify').hide();
            				}else{
            					$('#killPhoneVerify')
								.html(
										'<label class="label label-danger">验证码错误，请重新输入！</label>')
								.show(300);
            					seckill.flushValidateCode();
            				}
            			}
            			);
            			
            			
            			
            						     		
            		});
            		node.show();
            	}else{
            		//再来一次倒计时
            		var now=exposer['now'];
            		var start=exposer['start'];
            		var end=exposer['end'];
            		seckill.countdown(seckillId,now,start,end);
            	}
			}else{
				console.log('result:'+result);
			}
		})
	},
	
	countdown: function(seckillId,nowTime,startTime,endTime){
	//	alert('6');
		//通过id获取到组件
		var seckillBox=$('#seckill-box');
		
		if(nowTime>endTime){
			seckillBox.html('秒杀结束！');
		}else if(nowTime<startTime){
			//把startTime转化成数字
			var killTime=new Date(Number(startTime)+1000);
			seckillBox.countdown(killTime,function(event){
				//时间格式
				var format=event.strftime('秒杀倒计时: %D天 %H时  %M分  %S秒');
				seckillBox.html(format);
				//时间完成后回掉事件
			}).on('finish.countdown',function(){
				//获取秒杀地址，控制显示逻辑，执行秒杀
				seckill.handleSeckill(seckillId,seckillBox);
			});
			
		}else{
			//秒杀开始
			
			
			seckill.handleSeckill(seckillId,seckillBox);
		}
		
	},
	// 详情页秒杀逻辑
	detail : {
		init : function(params) {
			// alert("进入js文件");
			// 手机验证和登陆，计时交互
			// 规划我们的交互流程
			// 在cookie中查找手机号
			var killPhone = $.cookie('killPhone');
			
			// 验证手机号
			if (!seckill.validatePhone(killPhone)) {
				// 绑定phone
				// 控制输出
				var killPhoneModal = $('#killPhoneModal');
				// 显示弹出层
				killPhoneModal.modal({
					show : true,// 显示弹出层
					backdrop : 'static',// 禁止位置关闭
					keyboard : false
				// 关闭键盘事件
				});

				$('#killPhoneBtn')
						.click(
								function() {
									var inputPhone = $('#killPhoneKey').val();
									if (seckill.validatePhone(inputPhone)) {
										// 电话写入cookie
										$.cookie('killPhone', inputPhone, {
											expires : 7,
											path : '/seckilll'
										});
										// 刷新页面
										window.location.reload();
									} else {
										$('#killPhoneMessage')
												.hide()
												.html(
														'<label class="label label-danger">手机号错误</label>')
												.show(300);

									}
								});
			}
			//已经登陆
			//计时交互
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
		//	alert('3');
			$.get(seckill.URL.now(),{},function(result){
		//		alert('4');
				if(result&&result['success']){
					var nowTime=result['data'];
			//		alert('5');
					//时间判断
					seckill.countdown(seckillId,nowTime,startTime,endTime);
				}else{
					console.log('result:'+result);
				}
			});
			
		}
	}
}