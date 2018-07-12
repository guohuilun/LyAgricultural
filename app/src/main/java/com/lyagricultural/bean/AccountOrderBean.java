package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/9 0009 13:43
 */
public class AccountOrderBean {

    /**
     * Msg : 请求成功
     * Status : OK
     * orderlist : [{"orderId":"201807060332B2EA26298F9B45A18654CB405676ECB9","orderNo":"ORDER1807061532080005","totalAmt":"21.00","insertDt":"2018/7/6 15:32:08","orderType":"","orderUrl":""}]
     */

    private String Msg;
    private String Status;
    private List<OrderlistBean> orderlist;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public List<OrderlistBean> getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(List<OrderlistBean> orderlist) {
        this.orderlist = orderlist;
    }

    public static class OrderlistBean {
        /**
         * orderId : 201807060332B2EA26298F9B45A18654CB405676ECB9
         * orderNo : ORDER1807061532080005
         * totalAmt : 21.00
         * insertDt : 2018/7/6 15:32:08
         * orderType :
         * orderUrl :
         */

        private String orderId;
        private String orderNo;
        private String totalAmt;
        private String insertDt;
        private String orderType;
        private String orderUrl;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(String totalAmt) {
            this.totalAmt = totalAmt;
        }

        public String getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(String insertDt) {
            this.insertDt = insertDt;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderUrl() {
            return orderUrl;
        }

        public void setOrderUrl(String orderUrl) {
            this.orderUrl = orderUrl;
        }
    }
}
