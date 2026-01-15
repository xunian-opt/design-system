package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("discusszhuangxiushangpin")
public class DiscusszhuangxiushangpinEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long refid;
    private Long userid;
    private String avatarurl;
    private String nickname;
    private String content;
    private String reply;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRefid() { return refid; }
    public void setRefid(Long refid) { this.refid = refid; }
    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }
    public String getAvatarurl() { return avatarurl; }
    public void setAvatarurl(String avatarurl) { this.avatarurl = avatarurl; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

