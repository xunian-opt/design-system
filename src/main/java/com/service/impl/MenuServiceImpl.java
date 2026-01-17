package com.service.impl;

import com.service.MenuService;
import com.dao.MenuDao;
import com.dao.RoleMenuDao;
import com.entity.MenuEntity;
import com.entity.RoleMenuEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public List<MenuEntity> getTreeMenu() {
        // 查询所有菜单并排序
        List<MenuEntity> allMenus = this.selectList(new EntityWrapper<MenuEntity>().orderBy("order_num", true));
        return buildTree(allMenus, 0L);
    }

    @Override
    public List<MenuEntity> getTreeMenuByRoleId(Long roleId) {
        // 查询该角色拥有的菜单
        List<MenuEntity> menus = baseMapper.selectMenusByRoleId(roleId);
        return buildTree(menus, 0L);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        List<RoleMenuEntity> list = roleMenuDao.selectList(new EntityWrapper<RoleMenuEntity>().eq("role_id", roleId));
        return list.stream().map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
    }

    // 递归构建树形结构
    private List<MenuEntity> buildTree(List<MenuEntity> menus, Long parentId) {
        List<MenuEntity> tree = new ArrayList<>();
        for (MenuEntity menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                menu.setChildren(buildTree(menus, menu.getId()));
                tree.add(menu);
            }
        }
        return tree;
    }
}