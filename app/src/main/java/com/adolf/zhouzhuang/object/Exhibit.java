package com.adolf.zhouzhuang.object;

/**
 * Created by adolf on 2016/9/3.
 */
public class Exhibit {
    public long createTime;
    public String title;
    public String titleImgLocation;
    public int pid;
    public String brief;
    public String detailUrl;
    public int order;
    public int type;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImgLocation() {
        return titleImgLocation;
    }

    public void setTitleImgLocation(String titleImgLocation) {
        this.titleImgLocation = titleImgLocation;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
