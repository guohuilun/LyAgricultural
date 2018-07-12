package com.lyagricultural.view;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 作者Administrator on 2018/5/31 0031 15:19
 */
public class TextSpan extends ClickableSpan {
    @Override
    public void onClick(View view) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
         ds.setColor(Color.parseColor("#77C34F"));
         ds.setUnderlineText(true);
    }
}
