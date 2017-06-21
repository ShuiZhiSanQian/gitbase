package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;

import java.util.Date;
import java.util.List;

import javax.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和junit4整合，junit启动时加载springIOC容器
 * */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDaotest {

    //注入Dao实现类依赖
    @Resource
    private SuccessKillDao successKillDao;

   @Test
   public void testinsertSuccessKilled() throws Exception{
       long phonenumber=15858190140L;
       int insert=successKillDao.insertSuccessKilled(1002,phonenumber,0);
       System.out.println("insert"+insert);
   }
   @Test
   public void testqueryByIdWithSeckill() throws Exception{
       long phonenumber=15858190140L;
       SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(1002,phonenumber);
       System.out.println(successKilled);
       System.out.println(successKilled.getSeckill());
   }

}