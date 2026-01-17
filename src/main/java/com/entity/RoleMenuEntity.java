package com.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

@TableName("role_menu")
public class RoleMenuEntity implements Serializable {
    private Long id;
    private Long roleId;
    private Long menuId;

    // Getters Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }
}