package com.lyagricultural.bean;

/**
 * 作者Administrator on 2018/7/5 0005 09:17
 */
public class ShopSeedAddBean {
    private String GId;
    private String Nme;
    private String Url;
    private String Price;
    private String Tip;
    private String Area;
    private Boolean isChecked=false;
    private int count;

    public String getGId() {
        return GId;
    }

    public void setGId(String GId) {
        this.GId = GId;
    }

    public String getNme() {
        return Nme;
    }

    public void setNme(String nme) {
        Nme = nme;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
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

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
