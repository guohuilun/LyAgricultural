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
import com.lyagricultural.activity.PropsDetailsActivity;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/22 0022 12:27
 */
public class ShopPropsFragment extends Fragment {
    private static final String TAG = "ShopPropsFragment";
    private View shopPropsView;
    private Banner shop_props_banner;
    private RecyclerView shop_props_rv;
    private  List<Integer> ImageData = new ArrayList<>();
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private List<ShopFragmentBean.GoodslistBean> mGoodsList;
    private BaseRecyclerAdapter<ShopFragmentBean.GoodslistBean> brGoodsAdapter;
    private List<String> ImageGoodData=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        shopPropsView=inflater.inflate(R.layout.ly_fragment_shop_props,null);
        initView();
        return shopPropsView;
    }

    private void initView(){
        shop_props_banner=shopPropsView.findViewById(R.id.shop_props_banner);
        shop_props_rv=shopPropsView.findViewById(R.id.shop_props_rv);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(getActivity(),3);
        shop_props_rv.setLayoutManager(layoutManager);
        initShopProps();
        initShopPropsImg();
        setPropsGoodsRv();

//        setPropsRv();
    }


    private void setBanner(){
        shop_props_banner.setDelayTime(3000)
                .setImages(ImageGoodData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }


    private void setPropsGoodsRv(){
        mGoodsList=new ArrayList<ShopFragmentBean.GoodslistBean>();
        brGoodsAdapter=new BaseRecyclerAdapter<ShopFragmentBean.GoodslistBean>(getActivity(),mGoodsList,R.layout.ly_fragment_shop_props_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, ShopFragmentBean.GoodslistBean goodslistBean, int position) {
                holder.setClick(R.id.shop_props_rv_ll,goodslistBean,position,brGoodsAdapter);
                holder.setImg(getActivity(),goodslistBean.getUrl(),R.id.shop_props_rv_iv);
                holder.setTxt(R.id.shop_props_rv_name_tv,goodslistBean.getNme());
                holder.setTxt(R.id.shop_props_rv_money_tv,goodslistBean.getPrice());
                holder.setTxt(R.id.shop_props_rv_describe_tv,goodslistBean.getTip());
            }

            @Override
            public void clickEvent(int viewId, ShopFragmentBean.GoodslistBean goodslistBean, int position) {
                super.clickEvent(viewId, goodslistBean, position);
                switch (viewId){
                    case R.id.shop_props_rv_ll:
                        startActivity(new Intent(getActivity(), PropsDetailsActivity.class)
                                .putExtra("propName",goodslistBean.getNme())
                                .putExtra("goodsId",goodslistBean.getGId())
                        );
                        break;
                }
            }
        };
        shop_props_rv.setAdapter(brGoodsAdapter);
    }




    /**
     *  获取商品 -道具 -网络请求
     */
    private void initShopProps(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_GOODS_LIST)
                    .addParams("TypeId","PROP")
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
    private void initShopPropsImg(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_IMG_LIST)
                    .addParams("AdCid","Shop_Prop")
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
                                if (parse.getImagelist().size()>0&&parse.getImagelist()!=null){
                                    ImageGoodData.clear();
                                    for (int i = 0; i <parse.getImagelist().size() ; i++) {
                                        ImageGoodData.add(parse.getImagelist().get(i).getImgPath());
                                    }
                                    setBanner();
                                }
                            }
                        }
                    });
        }
    }



    /**
     * 测试数据
     */
    private void setPropsRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(getActivity(),getLandRv(),R.layout.ly_fragment_shop_props_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setClick(R.id.shop_props_rv_ll,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setBgResource(R.id.shop_props_rv_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.shop_props_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.shop_props_rv_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.shop_props_rv_ll:
                        startActivity(new Intent(getActivity(), PropsDetailsActivity.class)
                                .putExtra("propName",landDetailsNameBean.getLand_details_name_rv_tv())
                        );
                        break;
                }
            }
        };
        shop_props_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_props_o,"毛毛虫杀虫剂","10");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_props_t,"普通肥料","25");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_props_tt,"小草除草剂","10");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_props_f,"特级肥料","35");
        mList.add(landDetailsNameBean);
        return mList;
    }

    private List<Integer> setImageData(){
        ImageData.clear();
        ImageData.add(R.mipmap.ce_shop_props_banner);
        return ImageData;
    }

}
