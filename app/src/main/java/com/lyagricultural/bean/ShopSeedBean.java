package com.lyagricultural.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者Administrator on 2018/7/5 0005 09:36
 */
@Entity
public class ShopSeedBean {

    @Id
    private String GId;
    private String Nme;
    private String Url;
    private String Price;
    private String Tip;
    private String Area;
    private Boolean isChecked=false;
    private int count;

    @Generated(hash = 295634138)
    public ShopSeedBean(String GId, String Nme, String Url, String Price,
            String Tip, String Area, Boolean isChecked, int count) {
        this.GId = GId;
        this.Nme = Nme;
        this.Url = Url;
        this.Price = Price;
        this.Tip = Tip;
        this.Area = Area;
        this.isChecked = isChecked;
        this.count = count;
    }
    @Generated(hash = 1732536546)
    public ShopSeedBean() {
    }
    public String getGId() {
        return this.GId;
    }
    public void setGId(String GId) {
        this.GId = GId;
    }
    public String getNme() {
        return this.Nme;
    }
    public void setNme(String Nme) {
        this.Nme = Nme;
    }
    public String getUrl() {
        return this.Url;
    }
    public void setUrl(String Url) {
        this.Url = Url;
    }
    public String getPrice() {
        return this.Price;
    }
    public void setPrice(String Price) {
        this.Price = Price;
    }
    public String getTip() {
        return this.Tip;
    }
    public void setTip(String Tip) {
        this.Tip = Tip;
    }
    public String getArea() {
        return this.Area;
    }
    public void setArea(String Area) {
        this.Area = Area;
    }
    public Boolean getIsChecked() {
        return this.isChecked;
    }
    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    
}
