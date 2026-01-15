package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("storeup")
public class StoreupEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userid;
    private Long refid;
    private String tablename;
    private String name;
    private String picture;
    private Integer type;
    private Integer inteltype;
    private String remark;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }
    public Long getRefid() { return refid; }
    public void setRefid(Long refid) { this.refid = refid; }
    public String getTablename() { return tablename; }
    public void setTablename(String tablename) { this.tablename = tablename; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public Integer getInteltype() { return inteltype; }
    public void setInteltype(Integer inteltype) { this.inteltype = inteltype; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

