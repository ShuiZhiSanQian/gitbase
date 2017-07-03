package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                       "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception{
        List<Seckill> list=seckillService.getSeckillList();
        logger.info("list={}",list);

    }

    @Test
    public void testGetById() throws Exception{
        long id=1001;
        Seckill seckill=seckillService.getById(id);
        logger.info("seckill={}",seckill);

    }

    @Test
    public void testExportSeckillUrl() throws Exception{
        long id=1000;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
        /*
         * exposer=Exposer [exposed=true,
         *  md5=1a677398002f880414087d54be8c4ad5,
         * seckillId=1000, now=0, start=0, end=0]
         */
    }

    @Test
    public void testSeckillLogic() throws Exception{
        long id=1001;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone=15858590145L;
            String md5=exposer.getMd5();
            try {
                SeckillExecution execution=seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}",execution);

            } catch(RepeatKillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }

        }else{
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    
    
    @Test
    public void testSaveuser() throws Exception{
    	 long userphone=15850190142L;
         long password=123456L;
         String vocation="程序员";
         String favorate="打篮球";
      int re=seckillService.saveUserInformation(userphone, password, vocation, favorate);
      System.out.println("re:"+re);
    }

}
