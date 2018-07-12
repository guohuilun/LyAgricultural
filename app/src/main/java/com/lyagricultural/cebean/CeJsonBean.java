package com.lyagricultural.cebean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者Administrator on 2018/6/28 0028 17:26
 */
public class CeJsonBean {
    public String userId;
    public String totalAmt;
    public String payType;
    public String Remark;
    public String landId;
    public List<Map<String,String>> goodsList=new ArrayList<>();
}
