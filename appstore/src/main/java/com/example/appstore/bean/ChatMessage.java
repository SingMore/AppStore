package com.example.appstore.bean;

import java.util.Date;

/**
 * Created by SingMore on 2017/3/20.
 */

public class ChatMessage {
    private Date date;
    private String msg;
    private Type type;

    public ChatMessage() {
    }

    public ChatMessage(String msg, Type type, Date date) {
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        INCOMING(0), OUTCOMING(1);
        int state;

        public int getState() {
            return state;
        }

        Type(int state) {
            this.state = state;
        }

    }


}