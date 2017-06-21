package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKillDao {
          /*
           * 插入购买明细，可过滤重复
           * @return 插入的行数，返回0则表示插入失败
           * */
    int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone,@Param("state")long state);
    /*
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckilled,@Param("phone")long phone);
}