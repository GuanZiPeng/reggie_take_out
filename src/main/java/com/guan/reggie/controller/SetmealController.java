package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guan.reggie.common.R;
import com.guan.reggie.dto.SetmealDto;
import com.guan.reggie.entity.Category;
import com.guan.reggie.entity.Setmeal;
import com.guan.reggie.service.CategoryService;
import com.guan.reggie.service.SetmealDishService;
import com.guan.reggie.service.SetmealService;
import io.lettuce.core.api.async.RedisTransactionalAsyncCommands;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 套餐控制器
 *
 * @author GeZ
 * @date 2022/09/23
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private RedisTemplate redisTemplate;

    //新增套餐
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        try {
            //保存套餐信息
            setmealService.saveWithDish(setmealDto);
            //删除redis缓存数据
            Set keys = redisTemplate.keys("setmeal_*");
            redisTemplate.delete(keys);
            return R.success("添加套餐成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("添加套餐失败！");
        }

    }

    //分页查询
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {

        //创建分页对象
        Page<Setmeal> pageInfo = new Page<>();
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //构建条件对象
        LambdaQueryWrapper<Setmeal> queryWrap = new LambdaQueryWrapper();
        queryWrap.like(StringUtils.isNotBlank(name), Setmeal::getName, name);
        queryWrap.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrap);

        //进行文件拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                setmealDto.setCategoryName(byId.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    //修改检查组，回显内容
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        try {
            SetmealDto setmealDto = setmealService.getByIdWithDish(id);
            return R.success(setmealDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询套餐失败");
        }
    }

    //修改套餐
    @PutMapping
    public R<String> editSetmeal(@RequestBody SetmealDto setmealDto) {
        try {
            setmealService.editSetmeal(setmealDto);
            //删除redis缓存数据
            Set keys = redisTemplate.keys("setmeal_*");
            redisTemplate.delete(keys);
            return R.success("修改套餐成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改套餐失败！");
        }
    }

    //删除套餐
    @DeleteMapping
    public R<String> deleteSetmeal(String ids) {

        //分割字符串
        String[] split = ids.split(",");
        try {
            setmealService.deleteById(split);
            //删除redis缓存数据
            Set keys = redisTemplate.keys("setmeal_*");
            redisTemplate.delete(keys);
            return R.success("删除套餐成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("删除套餐失败");
        }
    }

    //设置套餐状态
    @PostMapping("/status/{status}")
    public R<String> editStatus(@PathVariable Integer status, String ids) {
        //分割字符串
        String[] split = ids.split(",");
        try {
            List<Setmeal> setmeals = new ArrayList<>();
            for (String id : split) {
                Setmeal setmeal = new Setmeal();
                setmeal.setId(Long.valueOf(id));
                setmeal.setStatus(status);
                setmeals.add(setmeal);
            }
            setmealService.updateBatchById(setmeals);
            //删除redis缓存数据
            Set keys = redisTemplate.keys("setmeal_*");
            redisTemplate.delete(keys);
            return R.success("修改套餐状态成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改套餐状态失败");
        }
    }

    //查询套餐信息
    @GetMapping("/list")
    public R<List<Setmeal>> list(Long categoryId, Integer status) {
        List<Setmeal> list = null;
        //从redis中查询是否存在数据
        String key = "setmeal_" + categoryId + "_" + status;
        list = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        //判断
        if (list != null) {
            return R.success(list);
        } else {
            LambdaQueryWrapper<Setmeal> queryWrap = new LambdaQueryWrapper<>();
            queryWrap.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
            queryWrap.eq(status != null, Setmeal::getStatus, status);
            queryWrap.orderByDesc(Setmeal::getUpdateTime);
            try {
                list = setmealService.list(queryWrap);
                //将数据保存到redis中
                redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
                return R.success(list);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error("查询套餐失败！");
            }
        }

    }
}
