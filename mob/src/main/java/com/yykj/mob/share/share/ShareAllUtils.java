package com.yykj.mob.share.share;

import android.content.Context;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 创建时间： on 2016/5/10.
 * 类描述：一键分享到多个平台（供选择）
 */
public class ShareAllUtils {

    /**
     * 分享内容说明
     *
     * @param context
     * @param title    标题
     * @param titleUrl 链接
     * @param imgUrl   图片地址
     * @param info     分享内容
     */
    public static void showShare(Context context, String title, String titleUrl,
                                 String imgUrl, String info) {
        ShareSDK.isDebug();
        OnekeyShare oks = new OnekeyShare();
        //关闭SSO授权
        oks.disableSSOWhenAuthorize();
        // oks.addHiddenPlatform(ShortMessage.NAME);
        //titile标题：在邮箱、人人网、微信、QQ空间、信息中使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(info);
        // imagePath是图片的路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imgUrl);
        // URL仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(titleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(info);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(titleUrl);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享内容说明
     *
     * @param context
     * @param title                  标题
     * @param titleUrl               链接
     * @param imgUrl                 图片地址
     * @param info                   分享内容
     * @param platformActionListener 分享成功回掉
     */
    public static void showShare(Context context, String title, String titleUrl,
                                 String imgUrl, String info, PlatformActionListener platformActionListener) {

        OnekeyShare oks = new OnekeyShare();
        //关闭SSO授权
        oks.disableSSOWhenAuthorize();
        // oks.addHiddenPlatform(ShortMessage.NAME);
        //titile标题：在邮箱、人人网、微信、QQ空间、信息中使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(info);
        // imagePath是图片的路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imgUrl);
        // URL仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(titleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(info);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(titleUrl);
        // 启动分享GUI
        oks.setCallback(platformActionListener);
        oks.show(context);
    }

}
