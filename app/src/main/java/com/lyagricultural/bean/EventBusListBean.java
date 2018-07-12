package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/5 0005 17:20
 */
public class EventBusListBean {
    private String Nme;
    private String Price;
    private String Tip;
    private int count;

    public String getNme() {
        return Nme;
    }

    public void setNme(String nme) {
        Nme = nme;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
