package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("orders")
public class OrdersEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String orderid;
    private String tablename;
    private Long userid;
    private Long goodid;
    private String goodname;
    private String picture;
    private Integer buynumber;
    private BigDecimal price;
    private BigDecimal discountprice;
    private BigDecimal total;
    private BigDecimal discounttotal;
    private Integer type;

    // 修改处：将 Integer 改为 String
    private String status;

    private String address;
    private String tel;
    private String consignee;
    private String remark;
    private String logistics;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderid() { return orderid; }
    public void setOrderid(String orderid) { this.orderid = orderid; }
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
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getDiscounttotal() { return discounttotal; }
    public void setDiscounttotal(BigDecimal discounttotal) { this.discounttotal = discounttotal; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    // 修改处：Getter 和 Setter 也要改为 String
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getLogistics() { return logistics; }
    public void setLogistics(String logistics) { this.logistics = logistics; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

