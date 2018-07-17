package com.lyagricultural.bean;

/**
 * 作者Administrator on 2018/7/13 0013 09:28
 */
public class AccountPersonalBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * userinfo : {"userNme":"张三","userSex":"False","imagePath":"http://113.207.26.23:22099/upload/Image/UserHead/20180620105635EC7F64A86A4F76B9B7C3E91602E36E.jpg","loginPhone":"15178719625","loginWx":""}
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
         * userSex : False
         * imagePath : http://113.207.26.23:22099/upload/Image/UserHead/20180620105635EC7F64A86A4F76B9B7C3E91602E36E.jpg
         * loginPhone : 15178719625
         * loginWx :
         */

        private String userNme;
        private String userSex;
        private String imagePath;
        private String loginPhone;
        private String loginWx;

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
    }
}
