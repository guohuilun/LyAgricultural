package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/5 0005 16:37
 */
public class PropGoodsDefaultBean {

    /**
     * Status : OK
     * Msg : 请求成功
     * Goodslist : [{"GId":"20180705043662FA9F24146B4E0AA64927AE1181FDAE","Nme":"悉心呵护套餐","Url":"","Price":"500.00","Tip":"123"}]
     */

    private String Status;
    private String Msg;
    private List<GoodslistBean> Goodslist;

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

    public List<GoodslistBean> getGoodslist() {
        return Goodslist;
    }

    public void setGoodslist(List<GoodslistBean> Goodslist) {
        this.Goodslist = Goodslist;
    }

    public static class GoodslistBean {
        /**
         * GId : 20180705043662FA9F24146B4E0AA64927AE1181FDAE
         * Nme : 悉心呵护套餐
         * Url :
         * Price : 500.00
         * Tip : 123
         */

        private String GId;
        private String Nme;
        private String Url;
        private String Price;
        private String Tip;
        private Boolean isChecked=false;

        public String getGId() {
            return GId;
        }

        public void setGId(String GId) {
            this.GId = GId;
        }

        public String getNme() {
            return Nme;
        }

        public void setNme(String Nme) {
            this.Nme = Nme;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getTip() {
            return Tip;
        }

        public void setTip(String Tip) {
            this.Tip = Tip;
        }

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }
    }
}
