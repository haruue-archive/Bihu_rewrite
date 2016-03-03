package com.helloworld.bihu_rewrite.DataClass;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class Detail implements Serializable{
    private String username;
    private String qtitle;
    private String qdetail;
    private String time;

    public Detail(String qtitle, String qdetail, String username, String time) {
        this.qdetail = qdetail;
        this.qtitle = qtitle;
        this.time = time;
        this.username = username;
    }

    public String getQdetail() {

        return qdetail;
    }

    public String getQtitle() {
        return qtitle;
    }

    public String getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }
}
