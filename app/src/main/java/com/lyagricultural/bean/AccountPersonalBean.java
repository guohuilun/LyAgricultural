package com.lyagricultural.bean;

/**
 * 作者Administrator on 2018/7/13 0013 09:28
 */
public class AccountPersonalBean {


    /**
     * Status : OK
     * Msg : 执行成功
     * userinfo : {"userNme":"张三","userSex":"男","imagePath":"http://113.207.26.23:22098/upload/Image/UserHead/2018071702349F69B4D1AA904ED3B3A5A96A9D4556B1.jpg","loginPhone":"15178719625","loginWx":"","inCode":"zhan12","inCount":"1"}
     */

    private String Status;
    private String Msg;
    private UserinfoBean userinfo;

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

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * userNme : 张三
         * userSex : 男
         * imagePath : http://113.207.26.23:22098/upload/Image/UserHead/2018071702349F69B4D1AA904ED3B3A5A96A9D4556B1.jpg
         * loginPhone : 15178719625
         * loginWx :
         * inCode : zhan12
         * inCount : 1
         */

        private String userNme;
        private String userSex;
        private String imagePath;
        private String loginPhone;
        private String loginWx;
        private String inCode;
        private String inCount;

        public String getUserNme() {
            return userNme;
        }

        public void setUserNme(String userNme) {
            this.userNme = userNme;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getLoginPhone() {
            return loginPhone;
        }

        public void setLoginPhone(String loginPhone) {
            this.loginPhone = loginPhone;
        }

        public String getLoginWx() {
            return loginWx;
        }

        public void setLoginWx(String loginWx) {
            this.loginWx = loginWx;
        }

        public String getInCode() {
            return inCode;
        }

        public void setInCode(String inCode) {
            this.inCode = inCode;
        }

        public String getInCount() {
            return inCount;
        }

        public void setInCount(String inCount) {
            this.inCount = inCount;
        }
    }
}
