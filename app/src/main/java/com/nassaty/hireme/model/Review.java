package com.nassaty.hireme.model;

public class Review {
    private String review;
    private String reply;
    private String sender_uid;
    private String job_id;
    private Boolean replied;


    public Review() {
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public Boolean isReplied() {
        return replied;
    }

    public void setReplied(Boolean replied) {
        this.replied = replied;
    }
}
