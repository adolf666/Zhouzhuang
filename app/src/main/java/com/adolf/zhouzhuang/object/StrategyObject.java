package com.adolf.zhouzhuang.object;

/**
 * Created by Administrator on 2016/9/25.
 */
public class StrategyObject {


    /**
     * url : http://baidu.com
     * title : 我的旅行攻略
     * pid : 1
     * picthumburl : http://139.196.217.52/zhouzhuang/scenictravels/mimi-001_1465987922309-genkxx11.jpg
     * creatorname : 拎着南瓜奔跑
     * creatorimgurl : http://139.196.217.52/zhouzhuang/scenictravels/mimi-001_1465987924152-jpe0j9on.jpg
     */

    private String url;
    private String title;
    private int pid;
    private String picthumburl;
    private String creatorname;
    private String creatorimgurl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPicthumburl() {
        return picthumburl;
    }

    public void setPicthumburl(String picthumburl) {
        this.picthumburl = picthumburl;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public void setCreatorname(String creatorname) {
        this.creatorname = creatorname;
    }

    public String getCreatorimgurl() {
        return creatorimgurl;
    }

    public void setCreatorimgurl(String creatorimgurl) {
        this.creatorimgurl = creatorimgurl;
    }
}
