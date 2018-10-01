package com.nassaty.hireme.model;

public class Notif {

    public static final int REVIEW_TYPE = 0;
    public static final int JOB_TYPE = 1;
    public static final int APPLICATION_TYPE = 2;

    private String id;
    public int type;
    public String text;
    public String time;
    private String sender_uid;
    private String receiver_uid;
    private String content_id;
    private Boolean isRead;

    public Notif() {
    }

    public Notif(String id, int type, String text, String time, String sender_uid, String receiver_uid, String content_id, Boolean isRead) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.time = time;
        this.sender_uid = sender_uid;
        this.receiver_uid = receiver_uid;
        this.content_id = content_id;
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getReceiver_uid() {
        return receiver_uid;
    }

    public void setReceiver_uid(String receiver_uid) {
        this.receiver_uid = receiver_uid;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
