package com.lyagricultural.bean;

/**
 * 作者Administrator on 2018/8/21 0021 10:28
 */
public class LandDetailsNameShareBean {

    /**
     * Status : OK
     * Msg : 上传成功
     * ShareImg : http://113.207.26.23:22099/upload/Image/UserHead/201808211028027635.jpg
     * ShareContent : 描述
     * ShareUrl : http://113.207.26.23:22099/Share/ShareView
     */

    private String Status;
    private String Msg;
    private String ShareImg;
    private String ShareContent;
    private String ShareUrl;

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

    public String getShareImg() {
        return ShareImg;
    }

    public void setShareImg(String ShareImg) {
        this.ShareImg = ShareImg;
    }

    public String getShareContent() {
        return ShareContent;
    }

    public void setShareContent(String ShareContent) {
        this.ShareContent = ShareContent;
    }

    public String getShareUrl() {
        return ShareUrl;
    }

    public void setShareUrl(String ShareUrl) {
        this.ShareUrl = ShareUrl;
    }
}
