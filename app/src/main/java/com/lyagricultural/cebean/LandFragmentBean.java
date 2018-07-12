package com.lyagricultural.cebean;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者Administrator on 2018/6/14 0014 09:34
 */
public class LandFragmentBean {

    private ImageView iv_default;
    private TextView land_rv_item_name_tv;
    private TextView land_rv_item_notify_tv;

    public ImageView getIv_default() {
        return iv_default;
    }

    public void setIv_default(ImageView iv_default) {
        this.iv_default = iv_default;
    }

    public TextView getLand_rv_item_name_tv() {
        return land_rv_item_name_tv;
    }

    public void setLand_rv_item_name_tv(TextView land_rv_item_name_tv) {
        this.land_rv_item_name_tv = land_rv_item_name_tv;
    }

    public TextView getLand_rv_item_notify_tv() {
        return land_rv_item_notify_tv;
    }

    public void setLand_rv_item_notify_tv(TextView land_rv_item_notify_tv) {
        this.land_rv_item_notify_tv = land_rv_item_notify_tv;
    }
}
