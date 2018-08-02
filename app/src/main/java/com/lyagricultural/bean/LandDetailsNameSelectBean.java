package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/30 0030 11:15
 */
public class LandDetailsNameSelectBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * cropinfo : [{"cropId":"20180726113739E7638FE78B4EA3914263385B6B0CCB","cropNme":"抱子甘蓝","cropType":"健康","cropNum":"1","cropImg":"http://113.207.26.23:22099/upload/GOODS/Image/201807171447252793.png"}]
     */

    private String Status;
    private String Msg;
    private List<CropinfoBean> cropinfo;

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

    public List<CropinfoBean> getCropinfo() {
        return cropinfo;
    }

    public void setCropinfo(List<CropinfoBean> cropinfo) {
        this.cropinfo = cropinfo;
    }

    public static class CropinfoBean {
        /**
         * cropId : 20180726113739E7638FE78B4EA3914263385B6B0CCB
         * cropNme : 抱子甘蓝
         * cropType : 健康
         * cropNum : 1
         * cropImg : http://113.207.26.23:22099/upload/GOODS/Image/201807171447252793.png
         */

        private String cropId;
        private String cropNme;
        private String cropType;
        private String cropNum;
        private String cropImg;
        private Boolean isShow=false;
        private int showNum;

        public String getCropId() {
            return cropId;
        }

        public void setCropId(String cropId) {
            this.cropId = cropId;
        }

        public String getCropNme() {
            return cropNme;
        }

        public void setCropNme(String cropNme) {
            this.cropNme = cropNme;
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

        public String getCropImg() {
            return cropImg;
        }

        public void setCropImg(String cropImg) {
            this.cropImg = cropImg;
        }

        public Boolean getShow() {
            return isShow;
        }

        public void setShow(Boolean show) {
            isShow = show;
        }

        public int getShowNum() {
            return showNum;
        }

        public void setShowNum(int showNum) {
            this.showNum = showNum;
        }
    }
}
