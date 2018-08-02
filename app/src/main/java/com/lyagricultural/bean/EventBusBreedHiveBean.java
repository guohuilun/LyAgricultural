package com.lyagricultural.bean;

/**
 * 作者Administrator on 2018/7/10 0010 11:21
 */
public class EventBusBreedHiveBean {
    private String msgLand;
    private long nid;
    private int cid;
    private boolean isOpen;
    private boolean isLive;
    private String name;

    public EventBusBreedHiveBean(String msgLand) {
        this.msgLand = msgLand;
    }

    public EventBusBreedHiveBean(String msgLand, long nid, int cid, boolean isOpen, boolean isLive, String name) {
        this.msgLand = msgLand;
        this.nid = nid;
        this.cid = cid;
        this.isOpen = isOpen;
        this.isLive = isLive;
        this.name = name;
    }

    public String getMsgLand() {
        return msgLand;
    }

    public void setMsgLand(String msgLand) {
        this.msgLand = msgLand;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
