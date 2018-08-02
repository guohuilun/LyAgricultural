package com.lyagricultural.bean;

/**
 * 作者Administrator on 2018/7/13 0013 09:28
 */
public class AccountPersonalBean {


    /**
     * Status : OK
     * Msg : 执行成功
     * userinfo : {"userNme":"android用户605007","userSex":"男","imagePath":"","loginPhone":"1234567893","loginWx":"","inCode":"6J4842"}
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
         * userNme : android用户605007
         * userSex : 男
         * imagePath :
         * loginPhone : 1234567893
         * loginWx :
         * inCode : 6J4842
         */

        private String userNme;
        private String userSex;
        private String imagePath;
        private String loginPhone;
        private String loginWx;
        private String inCode;

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
    }
}
