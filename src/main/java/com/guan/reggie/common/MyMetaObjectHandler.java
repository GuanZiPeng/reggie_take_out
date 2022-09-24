package com.guan.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元数据对象处理程序
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        //获取线程中的用户id
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    //更新时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
