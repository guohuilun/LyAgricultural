package com.lyagricultural.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.activity.LandNameActivity;
import com.lyagricultural.activity.LoginActivity;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.ImageBean;
import com.lyagricultural.bean.ShopFragmentBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.BannerUtils;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/22 0022 12:27
 */
public class ShopLandFragment extends Fragment {
    private static final String TAG = "ShopLandFragment";
    private View shopLandView;
    private Banner shop_land_banner;
    private RecyclerView shop_land_rv;
    private  List<Integer> ImageData = new ArrayList<>();
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private List<ShopFragmentBean.GoodslistBean> mGoodsList;
    private BaseRecyclerAdapter<ShopFragmentBean.GoodslistBean> brGoodsAdapter;
    private List<String> ImageGoodData=new ArrayList<>();
    private String ShopLandFragmentInit="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        shopLandView=inflater.inflate(R.layout.ly_fragment_shop_land,null);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initView();
        return shopLandView;
    }

    private void initView(){
        shop_land_banner=shopLandView.findViewById(R.id.shop_land_banner);
        shop_land_rv=shopLandView.findViewById(R.id.shop_land_rv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
        shop_land_rv.setLayoutManager(layoutManager);
        initShopLand();
        initShopLandImg();
        setLandGoodRv();
        setBanner();
        //        setLandRv();
    }



    private void setBanner(){
        shop_land_banner.setDelayTime(3000)
                .setImages(ImageGoodData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    private void setLandGoodRv(){
        mGoodsList=new ArrayList<ShopFragmentBean.GoodslistBean>();
        brGoodsAdapter=new BaseRecyclerAdapter<ShopFragmentBean.GoodslistBean>(getActivity(),mGoodsList,R.layout.ly_fragment_shop_land_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, ShopFragmentBean.GoodslistBean goodslistBean, int position) {
                holder.setClick(R.id.shop_land_rv_ll,goodslistBean,position,brGoodsAdapter);
                holder.setImg(getActivity(),goodslistBean.getUrl(),R.id.shop_land_rv_iv);
                holder.setTxt(R.id.shop_land_rv_name_tv,goodslistBean.getNme());
                holder.setTxt(R.id.shop_land_rv_number_tv,goodslistBean.getPrice());
            }

            @Override
            public void clickEvent(int viewId, ShopFragmentBean.GoodslistBean goodslistBean, int position) {
                super.clickEvent(viewId, goodslistBean, position);
                switch (viewId){
                    case R.id.shop_land_rv_ll:
                        startActivity(new Intent(getActivity(),LandNameActivity.class)
                                .putExtra("landName",goodslistBean.getNme())
                                .putExtra("goodsId",goodslistBean.getGId())
                        );
                        break;
                }
            }
        };
        shop_land_rv.setAdapter(brGoodsAdapter);
    }

    /**
     *  获取商品  -土地 -网络请求
     */
    private void initShopLand(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_GOODS_LIST)
                    .addParams("TypeId","LAND")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,response);
                            Gson gson=new Gson();
                            ShopFragmentBean parse=gson.fromJson(response,ShopFragmentBean.class);
                            if ("OK".equals(parse.getStatus())){
                                mGoodsList.clear();
                                mGoodsList.addAll(parse.getGoodslist());
                                brGoodsAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    /**
     *  获取商品  -土地图片 -网络请求
     */
    private void initShopLandImg(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_IMG_LIST)
                    .addParams("AdCid","Home_Index_Image")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"图片 = "+response);
                            Gson gson=new Gson();
                            ImageBean parse=gson.fromJson(response,ImageBean.class);
                            if ("OK".equals(parse.getStatus())){
                                ImageGoodData.clear();
                                for (int i = 0; i <parse.getImagelist().size() ; i++) {
                                    ImageGoodData.add(parse.getImagelist().get(i).getImgPath());
                                }
                            }
                        }
                    });
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("ShopLandFragmentInit".equals(bean.getMsg())){
            ShopLandFragmentInit=bean.getMsg();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("ShopLandFragmentInit".equals(ShopLandFragmentInit)){
            initShopLand();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 测试数据
     */

    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(getActivity(),getLandRv(),R.layout.ly_fragment_shop_land_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setClick(R.id.shop_land_rv_ll,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setBgResource(R.id.shop_land_rv_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.shop_land_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.shop_land_rv_number_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.shop_land_rv_ll:
                        startActivity(new Intent(getActivity(),LandNameActivity.class)
                                .putExtra("landName",landDetailsNameBean.getLand_details_name_rv_tv())
                        );
                        break;
                }
            }
        };
        shop_land_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_o,"风光1号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_t,"风光2号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_tt,"风光3号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_f,"风光3号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_ff,"风光4号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_s,"风光5号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_ss,"风光6号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_e,"风光7号","800");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_n,"风光8号","800");
        mList.add(landDetailsNameBean);
        return mList;
    }


    private List<Integer> setImageData(){
        ImageData.clear();
        ImageData.add(R.mipmap.ce_shop_land_banner_o);
        ImageData.add(R.mipmap.ce_shop_land_banner_t);
        return ImageData;
    }


}
