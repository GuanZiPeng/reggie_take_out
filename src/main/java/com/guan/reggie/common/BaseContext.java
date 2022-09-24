package com.guan.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用户保存和获取当前用户的id
 *
 * @author GeZ
 * @date 2022/09/22
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<Long>();

    //设置用户id
     public static void setCurrentId(Long id){
         threadLocal.set(id);
     }

     //获取用户id
     public static Long getCurrentId(){
         return threadLocal.get();
     }
}
