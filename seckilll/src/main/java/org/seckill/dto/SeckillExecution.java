package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStaEnum;

/*
 * 封装秒杀执行后的的结果
 * */
public class SeckillExecution {

    private long seckillId;

    // 秒杀执行结果状态
    private int state;

    // 状态表示
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStaEnum seckillstaEnum, SuccessKilled successKilled) {
        super();
        this.seckillId = seckillId;
        this.state = seckillstaEnum.getState();
        this.stateInfo = seckillstaEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStaEnum seckillstaEnum) {
        super();
        this.seckillId = seckillId;
        this.state = seckillstaEnum.getState();
        this.stateInfo = seckillstaEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

}