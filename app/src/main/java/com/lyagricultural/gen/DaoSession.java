package com.lyagricultural.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lyagricultural.bean.ShopSeedBean;

import com.lyagricultural.gen.ShopSeedBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig shopSeedBeanDaoConfig;

    private final ShopSeedBeanDao shopSeedBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        shopSeedBeanDaoConfig = daoConfigMap.get(ShopSeedBeanDao.class).clone();
        shopSeedBeanDaoConfig.initIdentityScope(type);

        shopSeedBeanDao = new ShopSeedBeanDao(shopSeedBeanDaoConfig, this);

        registerDao(ShopSeedBean.class, shopSeedBeanDao);
    }
    
    public void clear() {
        shopSeedBeanDaoConfig.clearIdentityScope();
    }

    public ShopSeedBeanDao getShopSeedBeanDao() {
        return shopSeedBeanDao;
    }

}
