package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/5 0005 15:29
 */
public class LandFragmentBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * laninfo : [{"goodsId":"201806130419420A0933F650410E9FF58AA0CAA600A3","landNme":"001土地","landImg":"/upload/GOODS/Image/201806131619559147.jpg","endDt":"2019/7/2 17:14:28","landArea":"60.00","useArea":"0","cropType":"0","cropNum":"0","isNew":"0"},{"goodsId":"20180702054834053850E6AB4F39AC8625414B885972","landNme":"003土地","landImg":"","endDt":"2019/7/3 10:08:10","landArea":"60.00","useArea":"0","cropType":"0","cropNum":"0","isNew":"0"},{"goodsId":"20180702054834053850E6AB4F39AC8625414B885972","landNme":"003土地","landImg":"","endDt":"2019/7/3 10:09:26","landArea":"60.00","useArea":"0","cropType":"0","cropNum":"0","isNew":"0"},{"goodsId":"201807031136938CE993F4E04DE3B2B1CCEFD9342A50","landNme":"璧山上的土地","landImg":"","endDt":"2019/7/5 16:07:43","landArea":"400.00","useArea":"0","cropType":"0","cropNum":"0","isNew":"0"}]
     */

    private String Status;
    private String Msg;
    private List<LaninfoBean> laninfo;

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

    public List<LaninfoBean> getLaninfo() {
        return laninfo;
    }

    public void setLaninfo(List<LaninfoBean> laninfo) {
        this.laninfo = laninfo;
    }

    public static class LaninfoBean {
        /**
         * goodsId : 201806130419420A0933F650410E9FF58AA0CAA600A3
         * landNme : 001土地
         * landImg : /upload/GOODS/Image/201806131619559147.jpg
         * endDt : 2019/7/2 17:14:28
         * landArea : 60.00
         * useArea : 0
         * cropType : 0
         * cropNum : 0
         * isNew : 0
         */

        private String goodsId;
        private String landNme;
        private String landImg;
        private String endDt;
        private String landArea;
        private String useArea;
        private String cropType;
        private String cropNum;
        private String isNew;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getLandNme() {
            return landNme;
        }

        public void setLandNme(String landNme) {
            this.landNme = landNme;
        }

        public String getLandImg() {
            return landImg;
        }

        public void setLandImg(String landImg) {
            this.landImg = landImg;
        }

        public String getEndDt() {
            return endDt;
        }

        public void setEndDt(String endDt) {
            this.endDt = endDt;
        }

        public String getLandArea() {
            return landArea;
        }

        public void setLandArea(String landArea) {
            this.landArea = landArea;
        }

        public String getUseArea() {
            return useArea;
        }

        public void setUseArea(String useArea) {
            this.useArea = useArea;
        }

        public String getCropType() {
            return cropType;
        }

        public void setCropType(String cropType) {
            this.cropType = cropType;
        }

        public String getCropNum() {
            return cropNum;
        }

        public void setCropNum(String cropNum) {
            this.cropNum = cropNum;
        }

        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }
    }
}
