package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/9/7 0007 11:29
 */
public class AccountPersonalInvitationBean {


    /**
     * Status : OK
     * Msg : 执行成功
     * userinfo : [{"userId":"20180724052160B638E3439943CFB5BD7A49A8527C7B","userNme":"ios用户2","inDt":"2018/9/7 10:57:52","inCount":"6"},{"userId":"20180724052160B638E3439943CFB5BD7A49A8527C7B","userNme":"ios用户2","inDt":"2018/9/7 10:57:52","inCount":"6"},{"userId":"20180724052160B638E3439943CFB5BD7A49A8527C7B","userNme":"ios用户2","inDt":"2018/9/7 10:57:52","inCount":"6"},{"userId":"20180724052160B638E3439943CFB5BD7A49A8527C7B","userNme":"ios用户2","inDt":"2018/9/7 10:57:52","inCount":"6"},{"userId":"20180724052160B638E3439943CFB5BD7A49A8527C7B","userNme":"ios用户2","inDt":"2018/9/7 10:57:52","inCount":"6"},{"userId":"20180724052160B638E3439943CFB5BD7A49A8527C7B","userNme":"ios用户2","inDt":"2018/9/7 10:57:52","inCount":"6"}]
     */

    private String Status;
    private String Msg;
    private List<UserinfoBean> userinfo;

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

    public List<UserinfoBean> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<UserinfoBean> userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * userId : 20180724052160B638E3439943CFB5BD7A49A8527C7B
         * userNme : ios用户2
         * inDt : 2018/9/7 10:57:52
         * inCount : 6
         */

        private String userId;
        private String userNme;
        private String inDt;
        private String inCount;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserNme() {
            return userNme;
        }

        public void setUserNme(String userNme) {
            this.userNme = userNme;
        }

        public String getInDt() {
            return inDt;
        }

        public void setInDt(String inDt) {
            this.inDt = inDt;
        }

        public String getInCount() {
            return inCount;
        }

        public void setInCount(String inCount) {
            this.inCount = inCount;
        }
    }
}
