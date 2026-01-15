package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("yuangongxinxi")
public class YuangongxinxiEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String gonghao;
    private String xingming;
    private String xingbie;
    private String touxiang;
    private String zhiwei;
    private String lianxifangshi;
    private String shenfenzhenghao;
    private String fuzexiangmu;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGonghao() { return gonghao; }
    public void setGonghao(String gonghao) { this.gonghao = gonghao; }
    public String getXingming() { return xingming; }
    public void setXingming(String xingming) { this.xingming = xingming; }
    public String getXingbie() { return xingbie; }
    public void setXingbie(String xingbie) { this.xingbie = xingbie; }
    public String getTouxiang() { return touxiang; }
    public void setTouxiang(String touxiang) { this.touxiang = touxiang; }
    public String getZhiwei() { return zhiwei; }
    public void setZhiwei(String zhiwei) { this.zhiwei = zhiwei; }
    public String getLianxifangshi() { return lianxifangshi; }
    public void setLianxifangshi(String lianxifangshi) { this.lianxifangshi = lianxifangshi; }
    public String getShenfenzhenghao() { return shenfenzhenghao; }
    public void setShenfenzhenghao(String shenfenzhenghao) { this.shenfenzhenghao = shenfenzhenghao; }
    public String getFuzexiangmu() { return fuzexiangmu; }
    public void setFuzexiangmu(String fuzexiangmu) { this.fuzexiangmu = fuzexiangmu; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

