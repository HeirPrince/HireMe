package com.nassaty.hireme.model;

public class Notif {

    public static final int REVIEW_TYPE = 0;
    public static final int JOB_TYPE = 1;
    public static final int APPLICATION_TYPE = 2;

    public int type;
    public String text;
    public String time;
    private String sender_uid;
    private String receiver_uid;

    public Notif() {
    }

    public Notif(int type, String text, String time, String sender_uid, String receiver_uid) {
        this.type = type;
        this.text = text;
        this.time = time;
        this.sender_uid = sender_uid;
        this.receiver_uid = receiver_uid;
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
}
