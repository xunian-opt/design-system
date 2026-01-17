package com.service;
import com.baomidou.mybatisplus.service.IService;
import com.entity.MenuEntity;
import java.util.List;

public interface MenuService extends IService<MenuEntity> {
    // 获取完整的树形菜单
    List<MenuEntity> getTreeMenu();
    // 根据角色ID获取树形菜单
    List<MenuEntity> getTreeMenuByRoleId(Long roleId);
    // 根据角色ID获取拥有的菜单ID列表（用于前端回显）
    List<Long> getMenuIdsByRoleId(Long roleId);
}