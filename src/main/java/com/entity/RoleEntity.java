package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

// 修改点：给 role 加上反引号 `role`，防止与 MySQL 关键字冲突
@TableName("`role`")
public class RoleEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String name;

    private String code;

    private Date addtime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}