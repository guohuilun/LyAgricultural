package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/6/27 0027 10:40
 */
public class AccountRechargeNumBean {

    /**
     * Status : OK
     * Msg : 获取到数据
     * numlist : [{"reNme":"100元","reRemark":"1000货币","reNum":"100.00"},{"reNme":"200元","reRemark":"2000货币","reNum":"200.00"},{"reNme":"500元","reRemark":"5000货币","reNum":"500.00"},{"reNme":"1000元","reRemark":"1万货币","reNum":"1000.00"},{"reNme":"2000元","reRemark":"2万货币","reNum":"2000.00"},{"reNme":"5000元","reRemark":"5万货币","reNum":"5000.00"}]
     */

    private String Status;
    private String Msg;
    private List<NumlistBean> numlist;

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

    public List<NumlistBean> getNumlist() {
        return numlist;
    }

    public void setNumlist(List<NumlistBean> numlist) {
        this.numlist = numlist;
    }

    public static class NumlistBean {
        /**
         * reNme : 100元
         * reRemark : 1000货币
         * reNum : 100.00
         */

        private String reNme;
        private String reRemark;
        private String reNum;
        private Boolean isShow=false;

        public String getReNme() {
            return reNme;
        }

        public void setReNme(String reNme) {
            this.reNme = reNme;
        }

        public String getReRemark() {
            return reRemark;
        }

        public void setReRemark(String reRemark) {
            this.reRemark = reRemark;
        }

        public String getReNum() {
            return reNum;
        }

        public void setReNum(String reNum) {
            this.reNum = reNum;
        }

        public Boolean getShow() {
            return isShow;
        }

        public void setShow(Boolean show) {
            isShow = show;
        }
    }
}
