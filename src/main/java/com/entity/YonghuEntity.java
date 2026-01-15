package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("yonghu")
public class YonghuEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String yonghuming;
    private String mima;
    private String xingming;
    private String xingbie;
    private String shouji;
    private String touxiang;
    private BigDecimal money;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getYonghuming() { return yonghuming; }
    public void setYonghuming(String yonghuming) { this.yonghuming = yonghuming; }
    public String getMima() { return mima; }
    public void setMima(String mima) { this.mima = mima; }
    public String getXingming() { return xingming; }
    public void setXingming(String xingming) { this.xingming = xingming; }
    public String getXingbie() { return xingbie; }
    public void setXingbie(String xingbie) { this.xingbie = xingbie; }
    public String getShouji() { return shouji; }
    public void setShouji(String shouji) { this.shouji = shouji; }
    public String getTouxiang() { return touxiang; }
    public void setTouxiang(String touxiang) { this.touxiang = touxiang; }
    public BigDecimal getMoney() { return money; }
    public void setMoney(BigDecimal money) { this.money = money; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

