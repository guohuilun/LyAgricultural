package com.lyagricultural.bean;

import java.util.List;

/**
 * 作者Administrator on 2018/6/20 0020 13:29
 */
public class ImageBean {


    /**
     * Status : OK
     * Imagelist : [{"ImgId":"20180619092345D45425210B4B16A414A6FC13C02134","ImgPath":""}]
     */

    private String Status;
    private List<ImagelistBean> Imagelist;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public List<ImagelistBean> getImagelist() {
        return Imagelist;
    }

    public void setImagelist(List<ImagelistBean> Imagelist) {
        this.Imagelist = Imagelist;
    }

    public static class ImagelistBean {
        /**
         * ImgId : 20180619092345D45425210B4B16A414A6FC13C02134
         * ImgPath :
         */

        private String ImgId;
        private String ImgPath;

        public String getImgId() {
            return ImgId;
        }

        public void setImgId(String ImgId) {
            this.ImgId = ImgId;
        }

        public String getImgPath() {
            return ImgPath;
        }

        public void setImgPath(String ImgPath) {
            this.ImgPath = ImgPath;
        }
    }
}
