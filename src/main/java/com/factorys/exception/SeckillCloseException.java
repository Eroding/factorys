package com.factorys.exception;

/*
秒杀关闭异常（当库存卖完或者到了结束时间。还在有人下单）
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
