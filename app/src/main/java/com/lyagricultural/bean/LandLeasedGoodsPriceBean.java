package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/6/27 0027 09:14
 */
public class LandLeasedGoodsPriceBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * Pricelist : [{"Nme":"8000/年","Price":"8000.00","monthNum":"12"},{"Nme":"2000元/三年","Price":"20000.00","monthNum":"36"},{"Nme":"1300元/两年","Price":"13000.00","monthNum":"24"}]
     */

    private String Status;
    private String Msg;
    private List<PricelistBean> Pricelist;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<PricelistBean> getPricelist() {
        return Pricelist;
    }

    public void setPricelist(List<PricelistBean> Pricelist) {
        this.Pricelist = Pricelist;
    }

    public static class PricelistBean {
        /**
         * Nme : 8000/年
         * Price : 8000.00
         * monthNum : 12
         */

        private String Nme;
        private String Price;
        private Boolean isShow=false;
        private String monthNum;

        public String getNme() {
            return Nme;
        }

        public void setNme(String Nme) {
            this.Nme = Nme;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public Boolean getShow() {
            return isShow;
        }

        public void setShow(Boolean show) {
            isShow = show;
        }

        public String getMonthNum() {
            return monthNum;
        }

        public void setMonthNum(String monthNum) {
            this.monthNum = monthNum;
        }
    }
}
