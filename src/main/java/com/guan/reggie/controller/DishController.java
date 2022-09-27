package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guan.reggie.common.R;
import com.guan.reggie.dto.DishDto;
import com.guan.reggie.entity.Category;
import com.guan.reggie.entity.Dish;
import com.guan.reggie.entity.DishFlavor;
import com.guan.reggie.service.CategoryService;
import com.guan.reggie.service.DishFlavorService;
import com.guan.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品控制器
 *
 * @author GeZ
 * @date 2022/09/23
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        //清除redis缓存
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("新增菜品成功！");
    }

    //菜品分页查询
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        //创建分页对象
        Page<Dish> pageInfo = new Page(page, pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.like(StringUtils.isNotBlank(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //进行查询
        dishService.page(pageInfo, queryWrapper);
        //创建DishDto page
        Page<DishDto> pageDto = new Page<>(page, pageSize);
        //进行数据拷贝
        BeanUtils.copyProperties(pageInfo, pageDto, "records");
        //进行数据处理
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//菜品分类id
            //根据id查询分类信息
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        pageDto.setRecords(list);
        return R.success(pageDto);
    }

    //删除菜品
    @DeleteMapping
    public R<String> delete(String ids) {
        //获取删除菜品id
        String[] split = ids.split(",");
        try {
            dishService.deleteByDishId(split);
            //清除redis缓存
            Set keys = redisTemplate.keys("dish_*");
            redisTemplate.delete(keys);
            return R.success("删除菜品成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("删除菜品失败！");

        }
    }

    //修改菜品状态
    @PostMapping("/status/{status}")
    public R<String> upStatus(@PathVariable int status, String ids) {
        String[] split = ids.split(",");
        try {
            dishService.upStatus(split, status);
            //清除redis缓存
            Set keys = redisTemplate.keys("dish_*");
            redisTemplate.delete(keys);
            return R.success("修改状态成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改状态失败");
        }
    }

    //修改菜品信息
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id) {
        try {
            DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
            //清除redis缓存
            String key="dish_"+byIdWithFlavor.getCategoryId()+"_1";
            redisTemplate.delete(key);
            return R.success(byIdWithFlavor);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询菜品失败！");
        }
    }

    //修改菜品
    @PutMapping
    public R<String> editDish(@RequestBody DishDto dishDto) {

        dishService.updateWithFlavor(dishDto);
        //清除redis缓存
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("修改菜品成功！");
    }

   /* //根据条件查询菜品 1.0
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {

        LambdaQueryWrapper<Dish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrap.orderByDesc(Dish::getUpdateTime);
        try {
            List<Dish> list = dishService.list(queryWrap);
            return R.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询菜品失败");
        }

    }*/

    //根据条件查询菜品 1.1
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;

        //构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        //从redis中查询是否存在菜品分类
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            //存在直接返回前端
            return R.success(dishDtoList);

        } else {
            //不存在查询数据库，然后缓存到redis
            LambdaQueryWrapper<Dish> queryWrap = new LambdaQueryWrapper<>();
            queryWrap.eq(Dish::getCategoryId, dish.getCategoryId());
            queryWrap.eq(Dish ::getStatus,"1");
            queryWrap.orderByDesc(Dish::getUpdateTime);
            try {
                List<Dish> list = dishService.list(queryWrap);

                dishDtoList = list.stream().map((item) -> {
                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);
                    Long categoryId = item.getCategoryId();//菜品分类id
                    //根据id查询分类信息
                    Category category = categoryService.getById(categoryId);
                    if (category != null) {
                        String categoryName = category.getName();
                        dishDto.setCategoryName(categoryName);
                    }
                    Long dishId = item.getId();
                    LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(DishFlavor::getDishId, dishId);
                    List<DishFlavor> list1 = dishFlavorService.list(queryWrapper);
                    dishDto.setFlavors(list1);
                    return dishDto;
                }).collect(Collectors.toList());
                //将查询的数据缓存到redis中
                redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
                return R.success(dishDtoList);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error("查询菜品失败");
            }
        }
    }
}
