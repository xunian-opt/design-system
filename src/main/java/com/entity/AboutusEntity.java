package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("aboutus")
public class AboutusEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private String picture1;
    private String picture2;
    private String picture3;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getPicture1() { return picture1; }
    public void setPicture1(String picture1) { this.picture1 = picture1; }
    public String getPicture2() { return picture2; }
    public void setPicture2(String picture2) { this.picture2 = picture2; }
    public String getPicture3() { return picture3; }
    public void setPicture3(String picture3) { this.picture3 = picture3; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

