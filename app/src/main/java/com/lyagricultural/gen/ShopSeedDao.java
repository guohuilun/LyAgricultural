package com.lyagricultural.gen;

import com.lyagricultural.app.LyAgriculturalApplication;
import com.lyagricultural.bean.ShopSeedBean;

import java.util.List;

/**
 * 作者Administrator on 2018/7/5 0005 09:38
 */
public class ShopSeedDao {

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param shop
     */
    public static void insert(ShopSeedBean shop) {
        LyAgriculturalApplication.getDaoInstant().getShopSeedBeanDao().insertOrReplace(shop);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void delete(String id) {
        LyAgriculturalApplication.getDaoInstant().getShopSeedBeanDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param shop
     */
    public static void update(ShopSeedBean shop) {
        LyAgriculturalApplication.getDaoInstant().getShopSeedBeanDao().update(shop);
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
  /*  public static List<ShopSeedBean> query() {
        return LyAgriculturalApplication.getDaoInstant().getShopSeedBeanDao().queryBuilder().where(ShopDao.Properties.Type.eq(Shop.TYPE_LOVE)).list();
    }*/

    /**
     * 查询全部数据
     */
    public static List<ShopSeedBean> queryAll() {
        return LyAgriculturalApplication.getDaoInstant().getShopSeedBeanDao().loadAll();
    }
}
