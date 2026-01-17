package com.controller;

import com.entity.MenuEntity;
import com.service.MenuService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单树列表 (用于菜单管理页面)
     */
    @RequestMapping("/list")
    public R list() {
        List<MenuEntity> list = menuService.getTreeMenu();
        return R.ok().put("data", list);
    }

    /**
     * 保存菜单
     */
    @PostMapping("/save")
    public R save(@RequestBody MenuEntity menu) {
        menuService.insert(menu);
        return R.ok();
    }

    /**
     * 修改菜单
     */
    @PostMapping("/update")
    public R update(@RequestBody MenuEntity menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    /**
     * 删除菜单
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        // 实际逻辑中可能需要判断是否有子菜单，这里简化直接删除
        // 如果删除父菜单，建议级联删除子菜单或报错提示
        for (Long id : ids) {
            menuService.deleteById(id);
        }
        return R.ok();
    }
}