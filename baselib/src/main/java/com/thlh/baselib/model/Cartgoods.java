package com.thlh.baselib.model;

import com.thlh.baselib.db.CartgoodsDao;
import com.thlh.baselib.db.DaoSession;
import com.thlh.baselib.db.GoodsDbDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class Cartgoods {
    @Id
    private Long cartgoods_id;

    private int goods_num = 1;
    private Boolean isSelect = true; //购物车中是否选择
    private Boolean isPayMjb = true;//订单确认页 是否支付美家币
    private Boolean isBuyImmediately = false; //是否为立即购买商品
    private String bunding_info = "";
    private String suppliername ="";
    private String supplierid = "0";


    @ToOne(joinProperty = "db_goods_id")
    private GoodsDb goodsdb;
    private Long db_goods_id;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 384206913)
    private transient CartgoodsDao myDao;
    @Generated(hash = 96930534)
    public Cartgoods(Long cartgoods_id, int goods_num, Boolean isSelect, Boolean isPayMjb,
            Boolean isBuyImmediately, String bunding_info, String suppliername,
            String supplierid, Long db_goods_id) {
        this.cartgoods_id = cartgoods_id;
        this.goods_num = goods_num;
        this.isSelect = isSelect;
        this.isPayMjb = isPayMjb;
        this.isBuyImmediately = isBuyImmediately;
        this.bunding_info = bunding_info;
        this.suppliername = suppliername;
        this.supplierid = supplierid;
        this.db_goods_id = db_goods_id;
    }
    @Generated(hash = 1902988556)
    public Cartgoods() {
    }
    public Long getCartgoods_id() {
        return this.cartgoods_id;
    }
    public void setCartgoods_id(Long cartgoods_id) {
        this.cartgoods_id = cartgoods_id;
    }
    public int getGoods_num() {
        return this.goods_num;
    }
    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }
    public Boolean getIsSelect() {
        return this.isSelect;
    }
    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }
    public String getBunding_info() {
        return this.bunding_info;
    }
    public void setBunding_info(String bunding_info) {
        this.bunding_info = bunding_info;
    }
    public Long getDb_goods_id() {
        return this.db_goods_id;
    }
    public void setDb_goods_id(Long db_goods_id) {
        this.db_goods_id = db_goods_id;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    @Generated(hash = 2013092804)
    private transient Long goodsdb__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1608266676)
    public GoodsDb getGoodsdb() {
        Long __key = this.db_goods_id;
        if (goodsdb__resolvedKey == null || !goodsdb__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GoodsDbDao targetDao = daoSession.getGoodsDbDao();
            GoodsDb goodsdbNew = targetDao.load(__key);
            synchronized (this) {
                goodsdb = goodsdbNew;
                goodsdb__resolvedKey = __key;
            }
        }
        return goodsdb;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 578059419)
    public void setGoodsdb(GoodsDb goodsdb) {
        synchronized (this) {
            this.goodsdb = goodsdb;
            db_goods_id = goodsdb == null ? null : goodsdb.getDbid();
            goodsdb__resolvedKey = db_goods_id;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 780261235)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCartgoodsDao() : null;
    }
    public Boolean getIsPayMjb() {
        return this.isPayMjb;
    }
    public void setIsPayMjb(Boolean isPayMjb) {
        this.isPayMjb = isPayMjb;
    }
    public Boolean getIsBuyImmediately() {
        return this.isBuyImmediately;
    }
    public void setIsBuyImmediately(Boolean isBuyImmediately) {
        this.isBuyImmediately = isBuyImmediately;
    }


}
