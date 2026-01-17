package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.annotations.TableField;

@TableName("menu")
public class MenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String title;
    private String path;
    private String icon;
    private String perms;
    private Integer orderNum;
    private Integer type;

    // 非数据库字段，用于构建树形结构
    @TableField(exist = false)
    private List<MenuEntity> children;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getPerms() { return perms; }
    public void setPerms(String perms) { this.perms = perms; }
    public Integer getOrderNum() { return orderNum; }
    public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public List<MenuEntity> getChildren() { return children; }
    public void setChildren(List<MenuEntity> children) { this.children = children; }
}