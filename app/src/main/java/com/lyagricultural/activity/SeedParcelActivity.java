package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.EventBusListBean;
import com.lyagricultural.bean.ShopSeedAddBean;
import com.lyagricultural.bean.ShopSeedBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.gen.ShopSeedDao;
import com.lyagricultural.utils.Arith;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.view.AmountView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/5/31 0031 13:37
 */
public class SeedParcelActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SeedParcelActivity";
    private RecyclerView seed_parcel_rv;
    private RelativeLayout seed_parcel_all_cb_rl;
    private CheckBox seed_parcel_all_cb;
    private TextView seed_parcel_money_tv;
    private Button seed_parcel_plant_button;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private List<ShopSeedBean> mSeedList;
    private BaseRecyclerAdapter<ShopSeedBean> baseSeedRecyclerAdapter;
    private double tip;
    private double price;
    private double createPrice=0;
    private double clickPrice=0;
    private double amountPrice=0;
    private Boolean isShow=false;
    private Boolean isShowAllChecked=false;
    private Boolean isShowTwo=false;
    private Boolean isShowThere=false;
    private Boolean isShowOne=true;
    private Boolean isShowChecked=false;
    private double allPrice=0;
    private int allCount=0;
    private List<ShopSeedBean> mShowList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_seed_parcel);
        initView();
        setTitle("种子包裹");
        setHeadRightTxVisibility(View.VISIBLE);
        setRightTitle("编辑");
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void initView(){
        seed_parcel_rv= findViewById(R.id.seed_parcel_rv);
        seed_parcel_all_cb_rl=findViewById(R.id.seed_parcel_all_cb_rl);
        seed_parcel_all_cb= findViewById(R.id.seed_parcel_all_cb);
        seed_parcel_money_tv= findViewById(R.id.seed_parcel_money_tv);
        seed_parcel_plant_button= findViewById(R.id.seed_parcel_plant_button);
        seed_parcel_plant_button.setOnClickListener(this);
        mRlTxRight.setOnClickListener(this);
//        seed_parcel_all_cb.setOnCheckedChangeListener(this);
        seed_parcel_all_cb_rl.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        seed_parcel_rv.setLayoutManager(layoutManager);
        seed_parcel_money_tv.setText("0.0");
//        setLandRv();
        setShopSeedDao();
        setSeedRv();



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.seed_parcel_plant_button:
                if ("0.0".equals(seed_parcel_money_tv.getText().toString())){
                    LyToast.shortToast(this,"种子种植不能为空");
                    return;
                }
              /*  EventBusListBean eventBusListBean=new EventBusListBean();
                for (int i = 0; i <mSeedList.size(); i++) {
                    if (mSeedList.get(i).getIsChecked()){
                        *//*eventBusListBean.setNme(mSeedList.get(i).getNme());
                        eventBusListBean.setTip(mSeedList.get(i).getTip());
                        eventBusListBean.setPrice(mSeedList.get(i).getPrice());
                        eventBusListBean.setCount(mSeedList.get(i).getCount());
                        LyLog.i(TAG,"有没有人嗯在 = "+eventBusListBean.getPrice());*//*
//                        Bundle bundle=new Bundle();
                    }
                }*/
                mShowList.clear();
                for (int i = 0; i <mSeedList.size() ; i++) {
                    if (mSeedList.get(i).getIsChecked()){
                        LyLog.i(TAG,"你进来没得吗 = "+mSeedList.get(i));
                        mShowList.add(mSeedList.get(i));
                    }
                }
                EventBus.getDefault().postSticky(mShowList);
                startActivity(new Intent(SeedParcelActivity.this,SeedPlantActivity.class)
                .putExtra("allPrice",seed_parcel_money_tv.getText().toString())
                );
                break;
            case R.id.title_right_txt:

                break;
            case R.id.seed_parcel_all_cb_rl:
                if (seed_parcel_all_cb.isChecked()==false){
                    LyLog.i(TAG,"你进来没 0 " +seed_parcel_all_cb.isChecked());
                    seed_parcel_all_cb.setChecked(true);
                    setSelectAll(true);
                }else {
                    LyLog.i(TAG,"你进来没 1 "+seed_parcel_all_cb.isChecked());
                    seed_parcel_all_cb.setChecked(false);
                    setSelectAll(false);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.seed_parcel_all_cb:
                if (isShowOne){
                    if (isChecked){
                        isShowAllChecked=true;
                    }else {
                        isShowAllChecked=false;
                    }
                    isShowOne=false;
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        if (mSeedList!=null){
            mSeedList.clear();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("SeedOK".equals(bean.getMsg())){
            finish();
        }
    }


    /**
     * 测试数据库
     */
    private void setShopSeedDao(){
        mSeedList=new ArrayList<>();
        List<ShopSeedBean> shopSeedBeen = ShopSeedDao.queryAll();
        for (int i = 0; i <shopSeedBeen.size() ; i++) {
            shopSeedBeen.get(i).setIsChecked(false);
            shopSeedBeen.get(i).setCount(0);
        }
        mSeedList.clear();
        mSeedList.addAll(shopSeedBeen);
        if (mSeedList.size()==0){
            LyToast.shortToast(this,"您还未添加种子进入包裹。。。");
        }
    }

    private void setSeedRv(){
        baseSeedRecyclerAdapter=new BaseRecyclerAdapter<ShopSeedBean>(this,mSeedList,R.layout.ly_activity_seed_parcel_rv_itme) {
            @Override
            public void bindData(final BaseRecyclerViewHolder holder, final ShopSeedBean shopSeedBean, final int position) {

                if (isShowChecked==false){
                    holder.setImg(SeedParcelActivity.this,shopSeedBean.getUrl(),R.id.seed_parcel_rv_item_iv);
                    holder.setTxt(R.id.seed_parcel_rv_item_name_tv,shopSeedBean.getNme());
                    holder.setTxt(R.id.seed_parcel_rv_item_area_covered_tv,"占地面积:"+shopSeedBean.getTip()+"m");
                    holder.setTxt(R.id.seed_parcel_rv_item_money_tv,shopSeedBean.getPrice());
                    holder.setAmountViewTxt(R.id.seed_parcel_rv_item_av,1);
                    holder.setTxt(R.id.shop_land_rv_number_tv,shopSeedBean.getPrice());
                }

                holder.setAmountViewListener(R.id.seed_parcel_rv_item_av, new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        tip = Arith.mul(Double.parseDouble(shopSeedBean.getTip()), (double) amount);
//                        tip= Double.parseDouble(shopSeedBean.getTip()) * amount;
                        holder.setTxt(R.id.seed_parcel_rv_item_area_covered_tv,"占地面积:"+String.valueOf(tip)+"m");
                        price=Arith.mul(Double.parseDouble(shopSeedBean.getPrice()), (double) amount);
//                        price = Double.parseDouble(shopSeedBean.getPrice()) * amount;
                        holder.setTxt(R.id.shop_land_rv_number_tv,String.valueOf(price));
                        isShow=true;
                        shopSeedBean.setCount(amount);
//                        shopSeedBean.setPrice(shopSeedBean.getPrice());
                        if (holder.isChecked(R.id.seed_parcel_cb)){
                            setAllPrice();
                        }else {
                            setAllPrice();
                        }
                    }
                });

               /* holder.setCheckBockListener(R.id.seed_parcel_cb,new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (!isShow){
                            price = Double.parseDouble(shopSeedBean.getPrice());
                            if (isChecked){
                                createPrice=price+createPrice;
                                seed_parcel_money_tv.setText(createPrice+"");
                            }else {
                                createPrice=createPrice-price;
                                seed_parcel_money_tv.setText(createPrice+"");
                            }
                        }else {
                            price=Double.parseDouble(holder.getTxt(R.id.shop_land_rv_number_tv));
                            if (isChecked){
                                clickPrice=price+clickPrice;
                                seed_parcel_money_tv.setText(clickPrice+"");
                            }else {
                                clickPrice=clickPrice-price;
                                seed_parcel_money_tv.setText(clickPrice+"");
                            }
                        }
                    }
                });*/

                holder.setClick(R.id.seed_parcel_rv_rl,shopSeedBean,position,baseSeedRecyclerAdapter);

                if (isShowChecked==true){
                    if (shopSeedBean.getIsChecked()){
                        holder.setCheck(R.id.seed_parcel_cb,true);
                        shopSeedBean.setIsChecked(true);
                        setAllPrice();
                    }else {
                        holder.setCheck(R.id.seed_parcel_cb,false);
                        shopSeedBean.setIsChecked(false);
                        setAllPrice();
                    }
                }

               /* if (!isShowOne){
                    if (isShowAllChecked){
                        holder.setCheck(R.id.seed_parcel_cb,true);
                        LyLog.i(TAG,"isShowAllChecked true");
                        setAllPrice();
                    }else {
                        LyLog.i(TAG,"isShowAllChecked false");
                        setAllPrice();
                    }
                    isShowOne=true;
                }*/
            }

            @Override
            public void clickEvent(int viewId, ShopSeedBean shopSeedBean, int position) {
                super.clickEvent(viewId, shopSeedBean, position);
                switch (viewId){
                    case R.id.seed_parcel_rv_rl:
                        isShowChecked=true;
                        shopSeedBean.setIsChecked(!shopSeedBean.getIsChecked());
                        notifyDataSetChanged();
                        break;
                }
            }
        };

        seed_parcel_rv.setAdapter(baseSeedRecyclerAdapter);
    }

    /**
     * 全选
     */
    private void setSelectAll(boolean isChecked){
        for (int i = 0; i <mSeedList.size() ; i++) {
            if (isChecked){
                mSeedList.get(i).setIsChecked(true);
            }else {
                mSeedList.get(i).setIsChecked(false);
            }
        }
        isShowChecked=true;
        baseSeedRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置总的金额
     */
    private void setAllPrice(){
        allPrice=0;
        allCount=0;
        seed_parcel_money_tv.setText("");
        for (int i = 0; i <mSeedList.size() ; i++) {
              if (mSeedList.get(i).getIsChecked()){
                  if (mSeedList.get(i).getCount()==0){
                      mSeedList.get(i).setCount(1);
                  }
                  allCount++;
                  allPrice +=Arith.mul(Double.parseDouble(mSeedList.get(i).getPrice()), (double) mSeedList.get(i).getCount());
//                  allPrice += Double.parseDouble(mSeedList.get(i).getPrice()) * mSeedList.get(i).getCount();

//                  LyLog.i(TAG,"在哪里 = "+i+" 数量 "+mSeedList.get(i).getCount()+" 价钱 "+ mSeedList.get(i).getPrice()+" 总价 "+allPrice);
              }
        }
        if (allCount==mSeedList.size()){
            seed_parcel_all_cb.setChecked(true);
        }else {
            seed_parcel_all_cb.setChecked(false);
        }

        seed_parcel_money_tv.setText(String.valueOf(allPrice));
    }



    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_seed_parcel_rv_itme) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setBgResource(R.id.seed_parcel_rv_item_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.seed_parcel_rv_item_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.seed_parcel_rv_item_area_covered_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
              /*  holder.setAmountViewTxt(R.id.seed_parcel_rv_item_av,landDetailsNameBean.getLand_details_name_rv_o_iv());
                holder.setAmountViewAllTxt(R.id.seed_parcel_rv_item_av,landDetailsNameBean.getLand_details_name_rv_t_iv());
                holder.setAmountViewListener(R.id.seed_parcel_rv_item_av, new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {

                    }
                });*/
            }


        };
        seed_parcel_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_o,"特级番茄种子","占地面积: 100m",10,20);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_t,"黄瓜种子","占地面积: 30m",9,10);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_f,"番茄种子","占地面积: 25m",5,30);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_ff,"青菜种子","占地面积: 49m",3,40);
        mList.add(landDetailsNameBean);
        return mList;
    }





}
