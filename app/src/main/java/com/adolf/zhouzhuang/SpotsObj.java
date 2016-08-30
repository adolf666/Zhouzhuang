package com.adolf.zhouzhuang;

/**
 * Created by zhl13045 on 2016/8/29.
 */
public class SpotsObj {

//    返回值解释：
//    在App启动时执行，如果不需要更新音频文件则返回值为空.
//    spotsId:景点Id
//    Lat:维度
//    Lng:经度
//    Name:景点名称
//    Title:博物馆简介
//    videoUrl:音频文件地址
//    VideoVersion:音频文件版本，决定是否需要更新
//    BasicInfoVersion:基本信息版本，决定是否需要更新
//    detailUrl:景点详情url

    public int pid;
    public int order;
    public long createTime;
    public String title;
    public String brief;
    public String detailUrl;
    public String lat;
    public String lng;
    public String videoLocation;
    public int videoVersion;
    public int basicInfoVersion;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        this.videoLocation = videoLocation;
    }

    public int getVideoVersion() {
        return videoVersion;
    }

    public void setVideoVersion(int videoVersion) {
        this.videoVersion = videoVersion;
    }

    public int getBasicInfoVersion() {
        return basicInfoVersion;
    }

    public void setBasicInfoVersion(int basicInfoVersion) {
        this.basicInfoVersion = basicInfoVersion;
    }
}
