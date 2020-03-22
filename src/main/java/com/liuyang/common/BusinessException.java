package com.liuyang.common;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    BusinessException() {
        super();
    }

    BusinessException(String msg) {
        super(msg);
    }

    BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
