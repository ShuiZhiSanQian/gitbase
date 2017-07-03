package org.seckill.dto;
/*
 * 暴露秒杀地址DTO
 * */
public class Exposer {
    //是否开启秒杀
    private boolean exposed;

    //一种加密措施
    private String md5;
    //id
    private long seckillId;

    //系统当前时间
    private long now;
    //开启秒杀时间
    private long start;
    //结束秒杀时间
    private long end;

    public Exposer(boolean exposed, String md5, long seckillId) {
        super();
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId,long now, long start, long end) {
        super();
        this.exposed = exposed;
        this.seckillId=seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, long seckillId) {
        super();
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public String getMd5() {
        return md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public long getNow() {
        return now;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer [exposed=" + exposed + ", md5=" + md5 + ", seckillId=" + seckillId + ", now=" + now + ", start="
                + start + ", end=" + end + "]";
    }







}