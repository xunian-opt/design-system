package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("shangpinfenlei")
public class ShangpinfenleiEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String shangpinfenlei;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShangpinfenlei() { return shangpinfenlei; }
    public void setShangpinfenlei(String shangpinfenlei) { this.shangpinfenlei = shangpinfenlei; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

