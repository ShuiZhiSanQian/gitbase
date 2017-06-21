package org.seckill.web;

import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seckill.dao.RedisDao;
import org.seckill.dao.SeckillDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStaEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.seckill.util.RandomValidateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //@Service   @Component
@RequestMapping("/") 
public class SeckillController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public Queue<Long> queue;
	
    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisDao redisDao;
    
    @Autowired
    private SeckillDao seckillDao;
    
    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String login(Model model){
         //获取登陆页面
//        List<Seckill> list=seckillService.getSeckillList();
//        model.addAttribute("list",list);
        //list.jsp +model=ModelAndView
        return "login";//WEB-INF/jsp/"list".jsp
    }
    
    @RequestMapping(value="/register",method=RequestMethod.GET)
    public String register(Model model){
         //获取注册页面
//        List<Seckill> list=seckillService.getSeckillList();
//        model.addAttribute("list",list);
        //list.jsp +model=ModelAndView
        return "register";//WEB-INF/jsp/"list".jsp
    }
    
    
    @RequestMapping(value="/{userphone}/{password}/{vocation}/{favorate}/saveuser",
                     method=RequestMethod.POST,
                    produces={"application/json;charset=UTF-8"})
    public String registerStore(@PathVariable("userphone") String userphone,
    		                  @PathVariable("password") String password,
    		                  @PathVariable("vocation") String vocation,
    		                  @PathVariable("favorate") String favorate){
//    	String userphone=request.getParameter("userphone");
//    	String password=request.getParameter("password");
//    	String vocation=request.getParameter("vocation");
//    	String favorate=request.getParameter("favorate");
    	
  //  	int result=1;
    	long userphone1=Long.parseLong(userphone);
    	long password1=Long.parseLong(password);
   	
    	System.out.println("userphone:"+userphone);
    	System.out.println("password:"+password);
    	System.out.println("vocation:"+vocation);
    	System.out.println("favorate:"+favorate);
//         //存储新用户信息
       int result=seckillService.saveUserInformation(userphone1,password1,vocation,favorate);
           if(result==1){
        	   System.out.println("注册成功！");
        	   return "恭喜你，注册成功";
           }else{
        	   
        	   System.out.println("注册失败！");
        	   return "注册失败！！";
           }
    }
    
    @RequestMapping(value="/list",method=RequestMethod.GET)
    public String list(Model model){
         //获取列表页
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        //list.jsp +model=ModelAndView
        return "list";//WEB-INF/jsp/"list".jsp
    }


    @RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId,Model model){
        if (seckillId == null) {
            return "redirect:/seckilll/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckilll/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";

    }

    @RequestMapping(value="/{seckillId}/exposer",
            method=RequestMethod.POST,
            produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {

            result=new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;  
    }

    @RequestMapping(value="/{seckillId}/{md5}/execution",
                    method=RequestMethod.POST,
                    produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value="killPhone",required=false)Long phone){

        if(phone==null){
            return new SeckillResult<SeckillExecution>(false, "未註冊");
        }
        
        //设置队列容量为该商品剩余容量，如果容量为0，则该商品卖完
        long capacity=redisDao.getCapacity(seckillId);
        logger.info("capacity={}",capacity+1);
        queue=new LinkedBlockingQueue<Long>(((int)capacity+1));

        
        if(((LinkedBlockingQueue<Long>)queue).remainingCapacity()>1){
               try {
            	      SeckillExecution execution=seckillService.executeSeckill(seckillId, phone, md5);
            	       return new SeckillResult<SeckillExecution>(true, execution);
            	        }catch(RepeatKillException e){
            	            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.REPEAT_KILL);
            	            return new SeckillResult<SeckillExecution>(true, execution);
            	        } catch (SeckillCloseException e) {
            	            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.END);
            	            return new SeckillResult<SeckillExecution>(true, execution);
            	        }catch(Exception e){
            	            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.INNER_ERROR);
            	            return new SeckillResult<SeckillExecution>(true, execution);
            	        }
        }else{    
	            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.END);
	            return new SeckillResult<SeckillExecution>(true, execution);

        }
        
        
//        try {
// //       	while(queue.size()!=0){
//        		SeckillExecution execution=seckillService.executeSeckill(seckillId, phone, md5);
//        		return new SeckillResult<SeckillExecution>(true, execution);
// //       	}
// //               SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.END);
////                return new SeckillResult<SeckillExecution>(true, execution);
//        }catch(RepeatKillException e){
//            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.REPEAT_KILL);
//            return new SeckillResult<SeckillExecution>(true, execution);
//        } catch (SeckillCloseException e) {
//            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.END);
//            return new SeckillResult<SeckillExecution>(true, execution);
//        }catch(Exception e){
//            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStaEnum.INNER_ERROR);
//            return new SeckillResult<SeckillExecution>(true, execution);
//        }
        
    }

    @RequestMapping(value="/time/now",method=RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now=new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

    
    @RequestMapping(value="/toImg")
    public String toImg(){

     return "image/image";
    }
    
  //登录获取验证码
    @RequestMapping("/getSysManageLoginCode")
    @ResponseBody
    public String getSysManageLoginCode(HttpServletResponse response,
      HttpServletRequest request) {
     response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
     response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
     response.setHeader("Cache-Control", "no-cache");
     response.setHeader("Set-Cookie", "name=value; HttpOnly");//设置HttpOnly属性,防止Xss攻击
     response.setDateHeader("Expire", 0);
     RandomValidateCode randomValidateCode = new RandomValidateCode();
     try {
      randomValidateCode.getRandcode(request, response,"imagecode");// 输出图片方法
     } catch (Exception e) {
      e.printStackTrace();
     }
     return "";
    }

    //验证码验证
    @RequestMapping(value ="/checkimagecode")
    @ResponseBody
    public String checkTcode(HttpServletRequest request,HttpServletResponse response) {
     String validateCode = request.getParameter("validateCode");
     System.out.println("validateCode:---");
     String code = null;
     //1:获取cookie里面的验证码信息
     Cookie[] cookies = request.getCookies();
     for (Cookie cookie : cookies) {
      if ("imagecode".equals(cookie.getName())) {
       code = cookie.getValue();
       break;
      }
     }
     //1:获取session验证码的信息
    // String code1 = (String) request.getSession().getAttribute("");
     //2:判断验证码是否正确
     if(!StringUtils.isEmpty(validateCode) && validateCode.equals(code)){
      return "ok"; 

     }
     return "error";
     //这里我没有进行字母大小模糊的验证处理，感兴趣的你可以去试一下！
    }


}