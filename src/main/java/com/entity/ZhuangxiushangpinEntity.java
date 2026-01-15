package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("zhuangxiushangpin")
public class ZhuangxiushangpinEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String shangpinmingcheng;
    private String shangpinfenlei;
    private String shangpintupian;
    private String shangpinguige;
    private String shangpinpinpai;
    private String youhuihuodong;
    private Integer onelimittimes;
    private Integer alllimittimes;
    private String shangpinxiangqing;
    private Date clicktime;
    private Integer clicknum;
    private BigDecimal price;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShangpinmingcheng() { return shangpinmingcheng; }
    public void setShangpinmingcheng(String shangpinmingcheng) { this.shangpinmingcheng = shangpinmingcheng; }
    public String getShangpinfenlei() { return shangpinfenlei; }
    public void setShangpinfenlei(String shangpinfenlei) { this.shangpinfenlei = shangpinfenlei; }
    public String getShangpintupian() { return shangpintupian; }
    public void setShangpintupian(String shangpintupian) { this.shangpintupian = shangpintupian; }
    public String getShangpinguige() { return shangpinguige; }
    public void setShangpinguige(String shangpinguige) { this.shangpinguige = shangpinguige; }
    public String getShangpinpinpai() { return shangpinpinpai; }
    public void setShangpinpinpai(String shangpinpinpai) { this.shangpinpinpai = shangpinpinpai; }
    public String getYouhuihuodong() { return youhuihuodong; }
    public void setYouhuihuodong(String youhuihuodong) { this.youhuihuodong = youhuihuodong; }
    public Integer getOnelimittimes() { return onelimittimes; }
    public void setOnelimittimes(Integer onelimittimes) { this.onelimittimes = onelimittimes; }
    public Integer getAlllimittimes() { return alllimittimes; }
    public void setAlllimittimes(Integer alllimittimes) { this.alllimittimes = alllimittimes; }
    public String getShangpinxiangqing() { return shangpinxiangqing; }
    public void setShangpinxiangqing(String shangpinxiangqing) { this.shangpinxiangqing = shangpinxiangqing; }
    public Date getClicktime() { return clicktime; }
    public void setClicktime(Date clicktime) { this.clicktime = clicktime; }
    public Integer getClicknum() { return clicknum; }
    public void setClicknum(Integer clicknum) { this.clicknum = clicknum; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

