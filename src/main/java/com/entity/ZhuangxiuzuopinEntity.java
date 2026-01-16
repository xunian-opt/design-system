package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("zhuangxiuzuopin")
public class ZhuangxiuzuopinEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String zuopinmingcheng;
    private String fenggeleixing;
    private String fengmianxinxi;
    private String fanganjieshao;
    private String neirongxiangqing;
    private String shejishixingming;
    private Date faburiqi;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getZuopinmingcheng() { return zuopinmingcheng; }
    public void setZuopinmingcheng(String zuopinmingcheng) { this.zuopinmingcheng = zuopinmingcheng; }
    public String getFenggeleixing() { return fenggeleixing; }
    public void setFenggeleixing(String fenggeleixing) { this.fenggeleixing = fenggeleixing; }
    public String getFengmianxinxi() { return fengmianxinxi; }
    public void setFengmianxinxi(String fengmianxinxi) { this.fengmianxinxi = fengmianxinxi; }
    public String getFanganjieshao() { return fanganjieshao; }
    public void setFanganjieshao(String fanganjieshao) { this.fanganjieshao = fanganjieshao; }
    public String getNeirongxiangqing() { return neirongxiangqing; }
    public void setNeirongxiangqing(String neirongxiangqing) { this.neirongxiangqing = neirongxiangqing; }
    public String getShejishixingming() { return shejishixingming; }
    public void setShejishixingming(String shejishixingming) { this.shejishixingming = shejishixingming; }
    public Date getFaburiqi() { return faburiqi; }
    public void setFaburiqi(Date faburiqi) { this.faburiqi = faburiqi; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
