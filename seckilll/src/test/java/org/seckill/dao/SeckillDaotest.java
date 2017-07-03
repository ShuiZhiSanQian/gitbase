package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

import javax.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和junit4整合，junit启动时加载springIOC容器
 * */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillDaotest {

    // 注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testReduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount=" + updateCount);
    }

    @Test
    public void testQueryById() throws Exception {
        long id = 1001;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }
    
    @Test
    public void capacityById() throws Exception {
        long id = 1001;
        long capacity = seckillDao.capacityById(id);
        System.out.println("capacity:"+capacity);
    }
    

    /*
     * java没有保存形参的记录 List<Seckill> queryAll(int offset,int limit)-->
     * List<Seckill> queryAll(arg0,arg1)
     */
    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }
}