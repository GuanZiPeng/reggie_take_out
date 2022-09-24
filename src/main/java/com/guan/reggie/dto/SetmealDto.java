package com.guan.reggie.dto;

import com.guan.reggie.entity.Setmeal;
import com.guan.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
