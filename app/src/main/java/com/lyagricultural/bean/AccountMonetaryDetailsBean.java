package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/6 0006 15:57
 */
public class AccountMonetaryDetailsBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * accinfo : [{"onOff":"False","acNum":"800.00","acRemark":"订单支付","insertDt":"2018/7/2 17:14:28","acType":"账户余额"},{"onOff":"False","acNum":"8000.00","acRemark":"订单支付","insertDt":"2018/7/3 10:08:10","acType":"账户余额"},{"onOff":"False","acNum":"8000.00","acRemark":"订单支付","insertDt":"2018/7/3 10:09:26","acType":"账户余额"},{"onOff":"False","acNum":"8000.00","acRemark":"订单支付","insertDt":"2018/7/5 16:07:43","acType":"账户余额"},{"onOff":"False","acNum":"21.00","acRemark":"订单支付","insertDt":"2018/7/6 14:38:56","acType":"账户余额"},{"onOff":"False","acNum":"21.00","acRemark":"订单支付","insertDt":"2018/7/6 15:26:41","acType":"账户余额"},{"onOff":"False","acNum":"84.00","acRemark":"订单支付","insertDt":"2018/7/6 15:26:06","acType":"账户余额"},{"onOff":"False","acNum":"101.00","acRemark":"订单支付","insertDt":"2018/7/6 15:29:07","acType":"账户余额"},{"onOff":"False","acNum":"8000.00","acRemark":"订单支付","insertDt":"2018/7/6 15:31:38","acType":"账户余额"},{"onOff":"False","acNum":"21.00","acRemark":"订单支付","insertDt":"2018/7/6 15:32:08","acType":"账户余额"}]
     */

    private String Status;
    private String Msg;
    private List<AccinfoBean> accinfo;

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

    public List<AccinfoBean> getAccinfo() {
        return accinfo;
    }

    public void setAccinfo(List<AccinfoBean> accinfo) {
        this.accinfo = accinfo;
    }

    public static class AccinfoBean {
        /**
         * onOff : False
         * acNum : 800.00
         * acRemark : 订单支付
         * insertDt : 2018/7/2 17:14:28
         * acType : 账户余额
         */

        private String onOff;
        private String acNum;
        private String acRemark;
        private String insertDt;
        private String acType;

        public String getOnOff() {
            return onOff;
        }

        public void setOnOff(String onOff) {
            this.onOff = onOff;
        }

        public String getAcNum() {
            return acNum;
        }

        public void setAcNum(String acNum) {
            this.acNum = acNum;
        }

        public String getAcRemark() {
            return acRemark;
        }

        public void setAcRemark(String acRemark) {
            this.acRemark = acRemark;
        }

        public String getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(String insertDt) {
            this.insertDt = insertDt;
        }

        public String getAcType() {
            return acType;
        }

        public void setAcType(String acType) {
            this.acType = acType;
        }
    }
}
