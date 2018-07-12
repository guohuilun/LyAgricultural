package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/6/21 0021 10:29
 */
public class ShopDetailBean {


    /**
     * Status : OK
     * Msg : 请求成功
     * goods : {"GId":"201806130419420A0933F650410E9FF58AA0CAA600A3","Nme":"001土地","Price":"800.00","Remark":"33","Area":"60.00","Remark1":"33","Remark2":"","Remark3":""}
     * imglist : [{"ImgId":"20180613042017754550FBC642F4802BA9E53ADA0BD7","ImgUrl":"http://113.207.26.23:22099/upload/GOODS/Image/201806140925014025.jpg"},{"ImgId":"201806130420DC5FD41467BA41AC8D6F0E635B493C26","ImgUrl":"http://113.207.26.23:22099/upload/GOODS/Image/201806131620035561.jpg"}]
     */

    private String Status;
    private String Msg;
    private GoodsBean goods;
    private List<ImglistBean> imglist;

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

    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public List<ImglistBean> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImglistBean> imglist) {
        this.imglist = imglist;
    }

    public static class GoodsBean {
        /**
         * GId : 201806130419420A0933F650410E9FF58AA0CAA600A3
         * Nme : 001土地
         * Price : 800.00
         * Remark : 33
         * Area : 60.00
         * Remark1 : 33
         * Remark2 :
         * Remark3 :
         */

        private String GId;
        private String Nme;
        private String Price;
        private String Remark;
        private String Area;
        private String Remark1;
        private String Remark2;
        private String Remark3;

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

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getRemark1() {
            return Remark1;
        }

        public void setRemark1(String Remark1) {
            this.Remark1 = Remark1;
        }

        public String getRemark2() {
            return Remark2;
        }

        public void setRemark2(String Remark2) {
            this.Remark2 = Remark2;
        }

        public String getRemark3() {
            return Remark3;
        }

        public void setRemark3(String Remark3) {
            this.Remark3 = Remark3;
        }
    }

    public static class ImglistBean {
        /**
         * ImgId : 20180613042017754550FBC642F4802BA9E53ADA0BD7
         * ImgUrl : http://113.207.26.23:22099/upload/GOODS/Image/201806140925014025.jpg
         */

        private String ImgId;
        private String ImgUrl;

        public String getImgId() {
            return ImgId;
        }

        public void setImgId(String ImgId) {
            this.ImgId = ImgId;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String ImgUrl) {
            this.ImgUrl = ImgUrl;
        }
    }
}
