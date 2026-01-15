package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.util.Date;

@TableName("chat")
public class ChatEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userid;
    private Long adminid;
    private String ask;
    private String reply;
    private Integer isreply;
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }
    public Long getAdminid() { return adminid; }
    public void setAdminid(Long adminid) { this.adminid = adminid; }
    public String getAsk() { return ask; }
    public void setAsk(String ask) { this.ask = ask; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public Integer getIsreply() { return isreply; }
    public void setIsreply(Integer isreply) { this.isreply = isreply; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}

