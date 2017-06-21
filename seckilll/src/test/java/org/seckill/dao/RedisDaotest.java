package org.seckill.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和junit4整合，junit启动时加载springIOC容器
 * */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class RedisDaotest {
	  // 注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;
    
    @Autowired
    private RedisDao redisDao;
    
    @Test
    public void testCapacity(){
    	long seckillId=1000;
    	long capacity=seckillDao.capacityById(seckillId);
    	System.out.println("capacity:"+capacity);
//    	String Stringresult=redisDao.putCapacity(seckillId, capacity);
//    	System.out.println(Stringresult);
//    	long result=redisDao.getCapacity(seckillId);
//    	System.out.println("redis获取到capcity:"+result);
    }
    
    
    @Test
    public void testSeckill(){
    	long seckillId=1000;

    	Seckill seckill=redisDao.getSeckill(seckillId);
//    	if(seckill==null){
    		 seckill=seckillDao.queryById(seckillId);
//    		if(seckill!=null){
    		String result=redisDao.putSeckill(seckill);
    		
    		System.out.println("result:"+result);
    	    seckill=redisDao.getSeckill(seckillId);
    	    System.out.println(seckill);
//    		}
//    	}else{
//   		System.out.println(seckill);
//   	}
    	Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
    	Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
           System.out.println("秒杀未开启");
        }else{
        	System.out.println("秒杀开启了");
        }
        seckill=redisDao.getSeckill(seckillId);
	    System.out.println(seckill);
    }
}
