package com.guan.reggie.common;

/**
 * 自定义异常
 *
 * @author GeZ
 * @date 2022/09/23
 */
public class CustomException extends RuntimeException {

    public CustomException(String message){
        super(message);
    }

}
