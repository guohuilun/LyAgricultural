package com.lyagricultural.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.activity.SeedParcelActivity;
import com.lyagricultural.activity.SeedVegetableNameActivity;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.bean.ImageBean;
import com.lyagricultural.bean.ShopFragmentBean;
import com.lyagricultural.bean.ShopSeedBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.gen.ShopSeedDao;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.BannerUtils;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/22 0022 12:27
 */
public class ShopSeedFragment extends Fragment implements View.OnTouchListener,View.OnClickListener{
    private static final String TAG = "ShopSeedFragment";
    private View shopSeedView;
    private FrameLayout ly_fragment_shop_seed_fl;
    private Banner shop_seed_banner;
    private RecyclerView shop_seed_rv;
    private  List<Integer> ImageData = new ArrayList<>();
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private List<ShopFragmentBean.GoodslistBean> mGoodsList;
    private BaseRecyclerAdapter<ShopFragmentBean.GoodslistBean> brGoodsAdapter;
    private List<String> ImageGoodData=new ArrayList<>();
    private ViewGroup shop_seed_rl;
    private RelativeLayout shop_seed_touch_rl;
    private ImageView shop_seed_iv;
    private TextView shop_seed_number_tv;
    private int xDelta;
    private int yDelta;
    private boolean isClick;
    private long startTime = 0;
    private long endTime = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        shopSeedView=inflater.inflate(R.layout.ly_fragment_shop_seed,null);
        initView();
        return shopSeedView;
    }

    private void initView(){
        ly_fragment_shop_seed_fl=shopSeedView.findViewById(R.id.ly_fragment_shop_seed_fl);
        shop_seed_banner=shopSeedView.findViewById(R.id.shop_seed_banner);
        shop_seed_rv=shopSeedView.findViewById(R.id.shop_seed_rv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
        shop_seed_rv.setLayoutManager(layoutManager);
        shop_seed_rl=shopSeedView.findViewById(R.id.shop_seed_rl);
        shop_seed_touch_rl=shopSeedView.findViewById(R.id.shop_seed_touch_rl);
//        shop_seed_iv=shopSeedView.findViewById(R.id.shop_seed_iv);
        shop_seed_number_tv=shopSeedView.findViewById(R.id.shop_seed_number_tv);
        shop_seed_touch_rl.setOnClickListener(this);

        initShopSeed();
        initShopSeedImg();
        setSeedGoodsRv();
        initTouch();
//        setSeedRv();
    }

    private void setBanner(){
        shop_seed_banner.setDelayTime(3000)
                .setImages(ImageGoodData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        shop_seed_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }


    private void setSeedGoodsRv(){
        mGoodsList=new ArrayList<ShopFragmentBean.GoodslistBean>();
        brGoodsAdapter=new BaseRecyclerAdapter<ShopFragmentBean.GoodslistBean>(getActivity(),mGoodsList,R.layout.ly_fragment_shop_seed_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, ShopFragmentBean.GoodslistBean goodslistBean, int position) {
                holder.setClick(R.id.shop_seed_rv_ll,goodslistBean,position,brGoodsAdapter);
                holder.setImg(getActivity(),goodslistBean.getUrl(),R.id.shop_seed_rv_iv);
                holder.setTxt(R.id.shop_seed_rv_name_tv,goodslistBean.getNme());
                holder.setTxt(R.id.shop_seed_rv_money_tv,goodslistBean.getPrice());
                holder.setClick(R.id.shop_seed_rv_add_rl,goodslistBean,position,brGoodsAdapter);
            }

            @Override
            public void clickEvent(int viewId, ShopFragmentBean.GoodslistBean goodslistBean, int position) {
                super.clickEvent(viewId, goodslistBean, position);
                switch (viewId){
                    case R.id.shop_seed_rv_ll:
                        startActivity(new Intent(getActivity(), SeedVegetableNameActivity.class)
                                .putExtra("seedName",goodslistBean.getNme())
                                .putExtra("goodsId",goodslistBean.getGId())
                                .putExtra("seedUrl",goodslistBean.getUrl())
                        );
                        break;
                    case R.id.shop_seed_rv_add_rl:
                        LyToast.shortToast(getActivity(),"加入包裹成功");
                        ShopSeedBean shopSeedBean=new ShopSeedBean();
                        shopSeedBean.setGId(goodslistBean.getGId());
                        shopSeedBean.setNme(goodslistBean.getNme());
                        shopSeedBean.setUrl(goodslistBean.getUrl());
                        shopSeedBean.setPrice(goodslistBean.getPrice());
                        shopSeedBean.setTip(goodslistBean.getTip());
                        ShopSeedDao.insert(shopSeedBean);
                        shop_seed_number_tv.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        shop_seed_rv.setAdapter(brGoodsAdapter);
    }



    /**
     *  获取商品 -种子 -网络请求
     */
    private void initShopSeed(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_GOODS_LIST)
                    .addParams("TypeId","SEED")
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
    private void initShopSeedImg(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_IMG_LIST)
                    .addParams("AdCid","Shop_Seed")
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


    private void initTouch(){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 20;
        layoutParams.topMargin = 1380;
        shop_seed_touch_rl.setLayoutParams(layoutParams);
        shop_seed_touch_rl.setOnTouchListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shop_seed_touch_rl:
                startActivity(new Intent(getActivity(), SeedParcelActivity.class));
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getRawX();
        final int y = (int) motionEvent.getRawY();

        LyLog.d(TAG, "onTouch: x= " + x + "y=" + y);
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isClick=false;
                startTime = System.currentTimeMillis();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                xDelta = x - params.leftMargin;
                yDelta = y - params.topMargin;
                LyLog.d(TAG, "ACTION_DOWN: xDelta= " + xDelta + "yDelta=" + yDelta);
                break;
            case MotionEvent.ACTION_MOVE:
                isClick=true;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                int xDistance = x - xDelta;
                int yDistance = y - yDelta;
                LyLog.d(TAG, "ACTION_MOVE: xDistance= " + xDistance + "yDistance=" + yDistance);
                if (yDistance<0){
                    yDistance=0;
                }

                if (xDistance<0){
                    xDistance=0;
                }

                int i = shop_seed_rl.getHeight()-160;
                int width = shop_seed_rl.getWidth()-shop_seed_touch_rl.getWidth();
                LyLog.d(TAG,"得到的高度 = "+i +" 得到的宽度 =  "+width);
                if (yDistance>i){
                    yDistance=i;
                }

                if (xDistance>width){
                    xDistance=width;
                }
                layoutParams.leftMargin = xDistance;
                layoutParams.topMargin = yDistance;
                view.setLayoutParams(layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                if ((endTime - startTime) > 0.1 * 1000L) {
                    isClick = true;
                } else {
                    isClick = false;
                }
                break;
        }
        shop_seed_rl.invalidate();
        return isClick;

    }

    /**
     * 测试数据
     */
    private void setSeedRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(getActivity(),getLandRv(),R.layout.ly_fragment_shop_seed_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setClick(R.id.shop_seed_rv_ll,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setBgResource(R.id.shop_seed_rv_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.shop_seed_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.shop_seed_rv_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.shop_seed_rv_ll:
                        startActivity(new Intent(getActivity(), SeedVegetableNameActivity.class)
                                .putExtra("seedName",landDetailsNameBean.getLand_details_name_rv_tv())
                        );

                        break;
                }
            }
        };
        shop_seed_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_o,"特级番茄种子","10");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_t,"番茄种子","5");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_tt,"土豆幼苗","8");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_f,"茄子幼苗","3");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_ff,"玉米幼苗","2");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_s,"黄瓜种子","1");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_ss,"空心菜幼苗","11");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_e,"番茄种子","12");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_n,"土豆幼苗","4");
        mList.add(landDetailsNameBean);
        return mList;
    }

    private List<Integer> setImageData(){
        ImageData.clear();
        ImageData.add(R.mipmap.ce_shop_seed_banner);
        return ImageData;
    }




}
