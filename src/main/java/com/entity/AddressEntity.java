package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("address")
public class AddressEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userid;
    private String address;
    private String name;
    private String phone;
    private Integer isdefault;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getIsdefault() { return isdefault; }
    public void setIsdefault(Integer isdefault) { this.isdefault = isdefault; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

