package com.adolf.zhouzhuang.object;

import java.io.Serializable;

/**
 * Created by adolf on 2016/9/13.
 */
public class LoginObj implements Serializable{
    public String username;
    public String nickName;
    public String sex;
    public String area;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
