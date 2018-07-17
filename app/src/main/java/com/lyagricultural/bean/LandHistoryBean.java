package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/13 0013 10:07
 */
public class LandHistoryBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * loginfo : [{"logId":"2","logType":"浇水","logRemark":"浇水浇水","insertDt":"2018/7/13 10:07:20"}]
     */

    private String Status;
    private String Msg;
    private List<LoginfoBean> loginfo;

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

    public List<LoginfoBean> getLoginfo() {
        return loginfo;
    }

    public void setLoginfo(List<LoginfoBean> loginfo) {
        this.loginfo = loginfo;
    }

    public static class LoginfoBean {
        /**
         * logId : 2
         * logType : 浇水
         * logRemark : 浇水浇水
         * insertDt : 2018/7/13 10:07:20
         */

        private String logId;
        private String logType;
        private String logRemark;
        private String insertDt;

        public String getLogId() {
            return logId;
        }

        public void setLogId(String logId) {
            this.logId = logId;
        }

        public String getLogType() {
            return logType;
        }

        public void setLogType(String logType) {
            this.logType = logType;
        }

        public String getLogRemark() {
            return logRemark;
        }

        public void setLogRemark(String logRemark) {
            this.logRemark = logRemark;
        }

        public String getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(String insertDt) {
            this.insertDt = insertDt;
        }
    }
}
