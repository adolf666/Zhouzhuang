package com.adolf.zhouzhuang.resBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午4:08
 */
public class BaseApiResponse<T> {
    public String message;
    public String success;
    public List<T> data;
    public String getMessage() {
        return message;
    }

    public void setMessage(String code) {
        this.message = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String msg) {
        this.success = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
