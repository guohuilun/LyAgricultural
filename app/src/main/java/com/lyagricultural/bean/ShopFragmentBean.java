package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/6/20 0020 11:39
 */
public class ShopFragmentBean {


    /**
     * Status : OK
     * Msg : 请求成功
     * Goodslist : [{"GId":"2018061409107974C258F2E7411F844EF2F27F85638A","Nme":"土豆幼苗","Url":"","Price":"1.00","Tip":""}]
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
         * GId : 2018061409107974C258F2E7411F844EF2F27F85638A
         * Nme : 土豆幼苗
         * Url :
         * Price : 1.00
         * Tip :
         */

        private String GId;
        private String Nme;
        private String Url;
        private String Price;
        private String Tip;

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
    }
}
