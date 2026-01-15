package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("gongsixinxi")
public class GongsixinxiEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String gongsimingcheng;
    private String gongsizhaopian;
    private String gongsidizhi;
    private String jingyingfanwei;
    private String zixundianhua;
    private String gongsijieshao;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGongsimingcheng() { return gongsimingcheng; }
    public void setGongsimingcheng(String gongsimingcheng) { this.gongsimingcheng = gongsimingcheng; }
    public String getGongsizhaopian() { return gongsizhaopian; }
    public void setGongsizhaopian(String gongsizhaopian) { this.gongsizhaopian = gongsizhaopian; }
    public String getGongsidizhi() { return gongsidizhi; }
    public void setGongsidizhi(String gongsidizhi) { this.gongsidizhi = gongsidizhi; }
    public String getJingyingfanwei() { return jingyingfanwei; }
    public void setJingyingfanwei(String jingyingfanwei) { this.jingyingfanwei = jingyingfanwei; }
    public String getZixundianhua() { return zixundianhua; }
    public void setZixundianhua(String zixundianhua) { this.zixundianhua = zixundianhua; }
    public String getGongsijieshao() { return gongsijieshao; }
    public void setGongsijieshao(String gongsijieshao) { this.gongsijieshao = gongsijieshao; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

