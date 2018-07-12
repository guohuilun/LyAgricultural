package com.lyagricultural.yuanjian.util;



import com.lyagricultural.yuanjian.model.CameraInfo;

import java.util.Comparator;

/**
 * 作者Administrator on 2018/2/6 0006 13:58
 */
public class PinyinComparator implements Comparator<CameraInfo> {
    @Override
    public int compare(CameraInfo o1, CameraInfo o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
       /* if (o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")) {
            return 1;
        } else {
            if (o1.getSortLetters().equals(o2.getSortLetters())){
                return o1.getSecondLetters().compareTo(o2.getSecondLetters());
            }else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }*/
        return o1.getPinying().compareTo(o2.getPinying());
    }
}
