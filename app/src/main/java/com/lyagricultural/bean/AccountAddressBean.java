package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/6/27 0027 17:01
 */
public class AccountAddressBean {

    /**
     * Status : OK
     * Msg : 执行成功
     * adlist : [{"adId":"201806270500F6F014493A9043958AEDAF3427C1EBAA","userId":"20180620105635EC7F64A86A4F76B9B7C3E91602E36E","adNme":"吴小强","adPhone":"15178719625","proNme":"广东省","cityNme":"深圳市","disNme":"龙岗区","adDetail":"买了两件","zipCode":"000000","isOk":"1"}]
     */

    private String Status;
    private String Msg;
    private List<AdlistBean> adlist;

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

    public List<AdlistBean> getAdlist() {
        return adlist;
    }

    public void setAdlist(List<AdlistBean> adlist) {
        this.adlist = adlist;
    }

    public static class AdlistBean {
        /**
         * adId : 201806270500F6F014493A9043958AEDAF3427C1EBAA
         * userId : 20180620105635EC7F64A86A4F76B9B7C3E91602E36E
         * adNme : 吴小强
         * adPhone : 15178719625
         * proNme : 广东省
         * cityNme : 深圳市
         * disNme : 龙岗区
         * adDetail : 买了两件
         * zipCode : 000000
         * isOk : 1
         */

        private String adId;
        private String userId;
        private String adNme;
        private String adPhone;
        private String proNme;
        private String cityNme;
        private String disNme;
        private String adDetail;
        private String zipCode;
        private String isOk;
        private Boolean isCheckShow=false;

        public String getAdId() {
            return adId;
        }

        public void setAdId(String adId) {
            this.adId = adId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAdNme() {
            return adNme;
        }

        public void setAdNme(String adNme) {
            this.adNme = adNme;
        }

        public String getAdPhone() {
            return adPhone;
        }

        public void setAdPhone(String adPhone) {
            this.adPhone = adPhone;
        }

        public String getProNme() {
            return proNme;
        }

        public void setProNme(String proNme) {
            this.proNme = proNme;
        }

        public String getCityNme() {
            return cityNme;
        }

        public void setCityNme(String cityNme) {
            this.cityNme = cityNme;
        }

        public String getDisNme() {
            return disNme;
        }

        public void setDisNme(String disNme) {
            this.disNme = disNme;
        }

        public String getAdDetail() {
            return adDetail;
        }

        public void setAdDetail(String adDetail) {
            this.adDetail = adDetail;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getIsOk() {
            return isOk;
        }

        public void setIsOk(String isOk) {
            this.isOk = isOk;
        }

        public Boolean getCheckShow() {
            return isCheckShow;
        }

        public void setCheckShow(Boolean checkShow) {
            isCheckShow = checkShow;
        }
    }
}
