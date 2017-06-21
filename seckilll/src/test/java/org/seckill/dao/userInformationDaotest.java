package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和junit4整合，junit启动时加载springIOC容器
 * */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class userInformationDaotest {
	//注入Dao实现类依赖
    @Resource
    private userInformationDao userinformationDao;

   @Test
   public void testSaveUser() throws Exception{
       long userphone=15858190142L;
       long password=123456L;
       String vocation="程序员";
       String favorate="打篮球";
       int insert=(int) userinformationDao.saveUserInformation(userphone, password, vocation, favorate);
       
       System.out.println("insert:"+insert);
   }
}
