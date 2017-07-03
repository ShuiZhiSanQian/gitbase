package org.seckill.dto;


//所有ajax請求放回類型，封裝json結果
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;

    public SeckillResult(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        super();
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setError(String error) {
        this.error = error;
    }


}