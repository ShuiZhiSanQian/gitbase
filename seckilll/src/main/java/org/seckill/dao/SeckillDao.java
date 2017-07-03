package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameter;
import org.seckill.entity.Seckill;

public interface SeckillDao {
    /*
     * 减库存
     * @return  如果影响行数>1,表示更新的记录行数，若为0则减库存失败
     *
     * */
    int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);//@Param是为了同时传入两个或两个以上参数时，mapper访问数据库中参数混淆
    /*
     * 根据ID查询秒杀对象
     * */
    Seckill queryById(long seckillId);
    /*
     * 根据偏移量查询秒杀商品列表
     * */
    List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
    
    //根据seckillId查询剩余库存
    long capacityById(@Param("seckillId")long seckillId);
    


}