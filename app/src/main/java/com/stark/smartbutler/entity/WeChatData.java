package com.stark.smartbutler.entity;

/**
 * Created by stark on 2017/3/5.
 * 微信精选的数据类
 */

public class WeChatData {
    //标题
    private String title;
    //出处
    private String source;
    //图片的URL
    private String imgUrl;
    //新闻地址
    private String newUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }
}
