package com.stark.smartbutler.entity;

/**
 * Created by stark on 2017/3/9.
 */

public class ChatListData {
    //区分左右
    private int type;
    //文本
    private String text;

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
}
