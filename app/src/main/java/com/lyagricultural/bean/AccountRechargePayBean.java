package com.lyagricultural.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/6 0006.
 */
public class AccountRechargePayBean {


    /**
     * LyStatus : OK
     * PayNo : PAY1806271019550001
     * LyData : {"appid":"wxae62783cd6a0ff6b","partnerid":"1491188852","prepayid":"wx27101938801739bd1dcb5e733296003808","package":"Sign=WXPay","noncestr":"zRhzTenxyGvMTQF7","timestamp":"1530065996","sign":"A3195460E34917006AE3278DAC221065"}
     */

    private String LyStatus;
    private String PayNo;
    private LyDataBean LyData;

    public String getLyStatus() {
        return LyStatus;
    }

    public void setLyStatus(String LyStatus) {
        this.LyStatus = LyStatus;
    }

    public String getPayNo() {
        return PayNo;
    }

    public void setPayNo(String PayNo) {
        this.PayNo = PayNo;
    }

    public LyDataBean getLyData() {
        return LyData;
    }

    public void setLyData(LyDataBean LyData) {
        this.LyData = LyData;
    }

    public static class LyDataBean {
        /**
         * appid : wxae62783cd6a0ff6b
         * partnerid : 1491188852
         * prepayid : wx27101938801739bd1dcb5e733296003808
         * package : Sign=WXPay
         * noncestr : zRhzTenxyGvMTQF7
         * timestamp : 1530065996
         * sign : A3195460E34917006AE3278DAC221065
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
