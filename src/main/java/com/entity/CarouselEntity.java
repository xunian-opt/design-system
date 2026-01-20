package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图实体
 */
@TableName("carousel")
public class CarouselEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    // 对应数据库 picture 字段，替代原 config 表的 value
    private String picture;

    private String url;

    // 新增：排序字段
    private Integer sort;

    // 新增：状态字段 (1启用 0禁用)
    private Integer status;

    private Date createTime;

    private Date updateTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}