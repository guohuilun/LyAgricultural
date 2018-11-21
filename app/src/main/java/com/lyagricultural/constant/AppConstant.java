package com.lyagricultural.constant;

/**
 * Created by Administrator on 2016/9/27.
 */
public class AppConstant {
    /**
     * 正式服务器
     */
//   public static final String BASE_URL = "http://113.207.26.23:22098/api";

    /**
     * 测试服务器
     */
    public static final String BASE_URL = "http://192.168.2.214:8091/api";

//   注册
      public static final String APP_USER_CREATE=BASE_URL+"/AppUser/AppUserCreate";

//    LoginActivity 登录
      public static final String APP_USER_LOGIN=BASE_URL+"/AppUser/AppUserLogin";

//    获取手机图片（通用）
      public static final String APP_IMG_LIST=BASE_URL+"/AppUser/AppImgList";

//    获取商品列表（通用）
      public static final String APP_GOODS_LIST=BASE_URL+"/AppMall/AppGoodsList";

//    获取商品详情(通用)
    public static final String APP_GOODS_DETAIL=BASE_URL+"/AppMall/AppGoodsDetail";

//    获取须知
    public static final String APP_TIP_SELECT=BASE_URL+"/AppUser/AppTipSelect";

//    获取土地价格
    public static final String GOODS_PRICE=BASE_URL+"/AppMall/GoodsPrice";

//    账户充值
    public static final String APP_USER_RECHARGE=BASE_URL+"/AppUser/AppUserRecharge";

//    获取充值数据
    public static final String APP_RECHARGE_NUM=BASE_URL+"/AppUser/AppRechargeNum";

//    获取收货地址
    public static final String APP_USER_ADDRESS=BASE_URL+"/AppUser/AppUserAddress";

//    新增收货地址
    public static final String APP_USER_ADDRESS_CREATE=BASE_URL+"/AppUser/AppUserAddressCreate";

//    修改收货地址
    public static final String APP_USER_ADDRESS_UPDATE=BASE_URL+"/AppUser/AppUserAddressUpdate";

//    创建订单
    public static final String APP_ORDER_CREATE=BASE_URL+"/AppMall/AppOrderCreate";

//    获取账户余额
    public static final String APP_USER_ACCOUNT=BASE_URL+"/AppUser/AppUserAccount";

//    获取用户土地
    public static final String APP_USER_LAND_SELECT=BASE_URL+"/AppUser/AppUserLandSelect";

//    获取道具商品列表
    public static final String APP_PROP_GOODS=BASE_URL+"/AppMall/AppPropGoods";

//    获取用户账户明细
    public static final String APP_USER_ACCOUNT_SELECT=BASE_URL+"/AppUser/AppUserAccountSelect";

//    获取历史订单
    public static final String APP_USER_ORDER=BASE_URL+"/AppMall/AppUserOrder";

//    修改用户头像
    public static final String APP_USER_IMG_UPDATE=BASE_URL+"/AppUser/AppUserImgUpdate";

//    获取用户信息
    public static final String APP_USER_INFO_SELECT=BASE_URL+"/AppUser/AppUserInfoSelect";

//    获取用户土地历史动态
    public static final String APP_USER_LAND_LOG_SELECT=BASE_URL+"/AppUser/AppUserLandLogSelect";

//    获取帮助
    public static final String APP_HELP_SELECT=BASE_URL+"/AppUser/AppHelpSelect";

//    检查充值订单状态
    public static final String CHECK_PAY_STATUS=BASE_URL+"/AppUser/CheckPayStatus";

//    获取土地作物
    public static final String APP_USER_LAND_CROP_SELECT=BASE_URL+"/AppUser/AppUserLandCropSelect";

//    确认收获
    public static final String APP_EX_CREATE=BASE_URL+"/AppUser/AppExCreate";

//    上传分享图片
    public static final String APP_SHARE_IMG_UPLOAD=BASE_URL+"/AppUser/AppShareImgUpload";

    //    获取被邀请人列表
    public static final String APP_IN_USER_INFO_SELECT=BASE_URL+"/AppUser/AppInUserInfoSelect";
}
