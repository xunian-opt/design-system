package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.RoleMenuEntity;
import com.service.MenuService;
import com.dao.RoleMenuDao;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleMenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuDao roleMenuDao;

    /**
     * 获取角色已拥有的菜单ID (用于回显勾选状态)
     */
    @GetMapping("/menus/{roleId}")
    public R getRoleMenus(@PathVariable("roleId") Long roleId) {
        List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);
        return R.ok().put("data", menuIds);
    }

    /**
     * 保存角色-菜单权限
     */
    @PostMapping("/menu/save")
    @Transactional
    public R saveRoleMenu(@RequestBody Map<String, Object> params) {
        Long roleId = Long.parseLong(params.get("roleId").toString());
        List<Integer> menuIdsInt = (List<Integer>) params.get("menuIds");

        // 1. 先删除该角色原有的所有权限
        roleMenuDao.delete(new EntityWrapper<RoleMenuEntity>().eq("role_id", roleId));

        // 2. 批量插入新权限
        if (menuIdsInt != null && menuIdsInt.size() > 0) {
            for (Integer mid : menuIdsInt) {
                RoleMenuEntity rm = new RoleMenuEntity();
                rm.setRoleId(roleId);
                rm.setMenuId(Long.valueOf(mid));
                roleMenuDao.insert(rm);
            }
        }
        return R.ok();
    }
}