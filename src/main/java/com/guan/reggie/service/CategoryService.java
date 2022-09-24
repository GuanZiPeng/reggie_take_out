package com.guan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guan.reggie.entity.Category;

/**
 * 分类服务
 *
 * @author GeZ
 * @date 2022/09/22
 */
public interface CategoryService extends IService<Category> {
    //删除分类
    void remove(Long ids);
}
