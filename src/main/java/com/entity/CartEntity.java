package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("cart")
public class CartEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String tablename;
    private Long userid;
    private Long goodid;
    private String goodname;
    private String picture;
    private Integer buynumber;
    private BigDecimal price;
    private BigDecimal discountprice;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTablename() { return tablename; }
    public void setTablename(String tablename) { this.tablename = tablename; }
    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }
    public Long getGoodid() { return goodid; }
    public void setGoodid(Long goodid) { this.goodid = goodid; }
    public String getGoodname() { return goodname; }
    public void setGoodname(String goodname) { this.goodname = goodname; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public Integer getBuynumber() { return buynumber; }
    public void setBuynumber(Integer buynumber) { this.buynumber = buynumber; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getDiscountprice() { return discountprice; }
    public void setDiscountprice(BigDecimal discountprice) { this.discountprice = discountprice; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

