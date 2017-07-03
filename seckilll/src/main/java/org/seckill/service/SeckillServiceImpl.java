package org.seckill.service;

import java.util.Date;
import java.util.List;

import org.seckill.dao.RedisDao;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.dao.userInformationDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.entity.userInformation;
import org.seckill.enums.SeckillStaEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
/*
 * @Component   @Seervice  @Dao   @Controller
 * */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入Service依赖  @Autowired @Resource @Inject

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKillDao successKillDao;
    
    @Autowired
    private userInformationDao userinformationDao;
    
    @Autowired
    private RedisDao redisDao;

    // md5盐值字符串，用于混淆MD5
    private final String slat = "hdfinwefn92r8u2%$039ru*23rfn";

    public List<Seckill> getSeckillList() {
        // TODO Auto-generated method stub
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        // TODO Auto-generated method stub
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
    	try {
			
		
    	//优化点：缓存优化：超时的基础上维护一致性
    	//1.访问redis
    	Seckill seckill=redisDao.getSeckill(seckillId);
    	if(seckill==null){
    		//2.访问数据库
    		seckill = seckillDao.queryById(seckillId);
    		
    		if (seckill == null) {
                return new Exposer(false, seckillId);
            }else{
            	//3.放入redis
            	redisDao.putSeckill(seckill);
            }
    	}
    	        
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());

        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    	} finally {
    		long capacity=redisDao.getCapacity(seckillId);
    		// logger.info("0000000000000");
    	//	long capacity=-1L;
    		if(capacity<=0){
    	// logger.info("111111111111");
    		capacity=seckillDao.capacityById(seckillId);
    	//	logger.info("222222222222:",capacity);
    		  if(capacity!=0){
    			  redisDao.putCapacity(seckillId, capacity);
    		  }else{
    			 // return new Exposer(false, seckillId);
    		  }
    		}
//			long capacity=seckillDao.capacityById(seckillId);
//		    redisDao.putCapacity(seckillId, capacity);
    		long capacity1=redisDao.getCapacity(seckillId);
			 logger.info("capacity={}",capacity1);
		}
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    
    public static void main(String[] args) {
    	SeckillServiceImpl m=new SeckillServiceImpl();
		System.out.println(m.getMD5(1002));
	}
    @Transactional
    /*
     * 使用注解控制事务方法的优点：
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     * */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑：减库存+记录购买行为
        Date nowTime = new Date();
//try {
	

        try {
            // 减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                // 没有更新到记录，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                // 记录购买行为
                int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone, 0);
                // 唯一：seckillId，userPhone
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
                   long capacity= redisDao.getCapacity(seckillId);
                   redisDao.putCapacity(seckillId, (capacity-1));
                    return new SeckillExecution(seckillId, SeckillStaEnum.SUCCESS, successKilled);

                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常 转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
      
//   } finally {
//	long capacity=seckillDao.capacityById(seckillId);
//    redisDao.putCapacity(seckillId, capacity);
//	 logger.info("capacity={}",capacity);
//           } 
    }

	public int saveUserInformation(long userphone, long password, String vocation, String favorate) {
		
		
		  return userinformationDao.saveUserInformation(userphone, password, vocation, favorate);
	}


}