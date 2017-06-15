package com.thlh.baselib.model;

import com.thlh.baselib.db.DaoSession;
import com.thlh.baselib.db.GoodsDbDao;
import com.thlh.baselib.db.SupplierDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class GoodsDb {
    @Id
    private Long dbid;

    private String item_id;
    private String item_name;
    private String bar_code;
    private String item_price;
    private String member_price;
    private String market_price;
    private String is_promotion;
    private String promotion_price;
    private String start_date;
    private String end_date;
    private String item_img;
    private String item_img_thumb;
    private String item_subtitle;
    private String is_shelves;
    private String weight;
    private String bundling_price;
    private String is_bundling;
    private String supplier_id;
    private String is_g;
    private String is_area;
    private String is_div_delivery;
    private String is_mjb;
    private int storage;
    private String special;
    private String item_no;
    private String is_part;
    private String part_of_id;
    private String part_is_bundling;
    private String  is_prepare;
    private String  is_limit; //0:非限购商品 1：限购商品
    private String  is_pack; //0:正常商品 1:只能整箱购买商品
    private String  pack_num ;//整箱购买数量
    private String  limit_icon ;//0:不显示限购角标 1:显示限购角标
    private String  limit_num;//在商品详情页中，显示限购的商品数量
    private String  mjb_value;//美家币可用值
    @ToOne(joinProperty = "db_supplier_id")
    private Supplier supplier;
    private long db_supplier_id;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 578804442)
    private transient GoodsDbDao myDao;
    @Generated(hash = 1736736838)
    public GoodsDb(Long dbid, String item_id, String item_name, String bar_code, String item_price,
            String member_price, String market_price, String is_promotion, String promotion_price,
            String start_date, String end_date, String item_img, String item_img_thumb, String item_subtitle,
            String is_shelves, String weight, String bundling_price, String is_bundling, String supplier_id,
            String is_g, String is_area, String is_div_delivery, String is_mjb, int storage, String special,
            String item_no, String is_part, String part_of_id, String part_is_bundling, String is_prepare,
            String is_limit, String is_pack, String pack_num, String limit_icon, String limit_num,
            String mjb_value, long db_supplier_id) {
        this.dbid = dbid;
        this.item_id = item_id;
        this.item_name = item_name;
        this.bar_code = bar_code;
        this.item_price = item_price;
        this.member_price = member_price;
        this.market_price = market_price;
        this.is_promotion = is_promotion;
        this.promotion_price = promotion_price;
        this.start_date = start_date;
        this.end_date = end_date;
        this.item_img = item_img;
        this.item_img_thumb = item_img_thumb;
        this.item_subtitle = item_subtitle;
        this.is_shelves = is_shelves;
        this.weight = weight;
        this.bundling_price = bundling_price;
        this.is_bundling = is_bundling;
        this.supplier_id = supplier_id;
        this.is_g = is_g;
        this.is_area = is_area;
        this.is_div_delivery = is_div_delivery;
        this.is_mjb = is_mjb;
        this.storage = storage;
        this.special = special;
        this.item_no = item_no;
        this.is_part = is_part;
        this.part_of_id = part_of_id;
        this.part_is_bundling = part_is_bundling;
        this.is_prepare = is_prepare;
        this.is_limit = is_limit;
        this.is_pack = is_pack;
        this.pack_num = pack_num;
        this.limit_icon = limit_icon;
        this.limit_num = limit_num;
        this.mjb_value = mjb_value;
        this.db_supplier_id = db_supplier_id;
    }
    @Generated(hash = 1678238698)
    public GoodsDb() {
    }
    public Long getDbid() {
        return this.dbid;
    }
    public void setDbid(Long dbid) {
        this.dbid = dbid;
    }
    public String getItem_id() {
        return this.item_id;
    }
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
    public String getItem_name() {
        return this.item_name;
    }
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    public String getBar_code() {
        return this.bar_code;
    }
    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }
    public String getItem_price() {
        return this.item_price;
    }
    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }
    public String getMember_price() {
        return this.member_price;
    }
    public void setMember_price(String member_price) {
        this.member_price = member_price;
    }
    public String getMarket_price() {
        return this.market_price;
    }
    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }
    public String getIs_promotion() {
        return this.is_promotion;
    }
    public void setIs_promotion(String is_promotion) {
        this.is_promotion = is_promotion;
    }
    public String getPromotion_price() {
        return this.promotion_price;
    }
    public void setPromotion_price(String promotion_price) {
        this.promotion_price = promotion_price;
    }
    public String getStart_date() {
        return this.start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getEnd_date() {
        return this.end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public String getItem_img() {
        return this.item_img;
    }
    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }
    public String getItem_img_thumb() {
        return this.item_img_thumb;
    }
    public void setItem_img_thumb(String item_img_thumb) {
        this.item_img_thumb = item_img_thumb;
    }
    public String getItem_subtitle() {
        return this.item_subtitle;
    }
    public void setItem_subtitle(String item_subtitle) {
        this.item_subtitle = item_subtitle;
    }
    public String getIs_shelves() {
        return this.is_shelves;
    }
    public void setIs_shelves(String is_shelves) {
        this.is_shelves = is_shelves;
    }
    public String getWeight() {
        return this.weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getBundling_price() {
        return this.bundling_price;
    }
    public void setBundling_price(String bundling_price) {
        this.bundling_price = bundling_price;
    }
    public String getIs_bundling() {
        return this.is_bundling;
    }
    public void setIs_bundling(String is_bundling) {
        this.is_bundling = is_bundling;
    }
    public String getSupplier_id() {
        return this.supplier_id;
    }
    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }
    public String getIs_g() {
        return this.is_g;
    }
    public void setIs_g(String is_g) {
        this.is_g = is_g;
    }
    public String getIs_area() {
        return this.is_area;
    }
    public void setIs_area(String is_area) {
        this.is_area = is_area;
    }
    public String getIs_div_delivery() {
        return this.is_div_delivery;
    }
    public void setIs_div_delivery(String is_div_delivery) {
        this.is_div_delivery = is_div_delivery;
    }
    public String getIs_mjb() {
        return this.is_mjb;
    }
    public void setIs_mjb(String is_mjb) {
        this.is_mjb = is_mjb;
    }
    public int getStorage() {
        return this.storage;
    }
    public void setStorage(int storage) {
        this.storage = storage;
    }
    public String getSpecial() {
        return this.special;
    }
    public void setSpecial(String special) {
        this.special = special;
    }
    public String getItem_no() {
        return this.item_no;
    }
    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }
    public long getDb_supplier_id() {
        return this.db_supplier_id;
    }
    public void setDb_supplier_id(long db_supplier_id) {
        this.db_supplier_id = db_supplier_id;
    }
    @Generated(hash = 138112684)
    private transient Long supplier__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1008172333)
    public Supplier getSupplier() {
        long __key = this.db_supplier_id;
        if (supplier__resolvedKey == null || !supplier__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SupplierDao targetDao = daoSession.getSupplierDao();
            Supplier supplierNew = targetDao.load(__key);
            synchronized (this) {
                supplier = supplierNew;
                supplier__resolvedKey = __key;
            }
        }
        return supplier;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2128323067)
    public void setSupplier(@NotNull Supplier supplier) {
        if (supplier == null) {
            throw new DaoException(
                    "To-one property 'db_supplier_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.supplier = supplier;
            db_supplier_id = supplier.getDbid();
            supplier__resolvedKey = db_supplier_id;
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
    @Generated(hash = 492298317)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGoodsDbDao() : null;
    }
    public String getIs_part() {
        return this.is_part;
    }
    public void setIs_part(String is_part) {
        this.is_part = is_part;
    }
    public String getPart_of_id() {
        return this.part_of_id;
    }
    public void setPart_of_id(String part_of_id) {
        this.part_of_id = part_of_id;
    }
    public String getPart_is_bundling() {
        return this.part_is_bundling;
    }
    public void setPart_is_bundling(String part_is_bundling) {
        this.part_is_bundling = part_is_bundling;
    }
    public String getIs_prepare() {
        return this.is_prepare;
    }
    public void setIs_prepare(String is_prepare) {
        this.is_prepare = is_prepare;
    }
    public String getIs_limit() {
        return this.is_limit;
    }
    public void setIs_limit(String is_limit) {
        this.is_limit = is_limit;
    }
    public String getIs_pack() {
        return this.is_pack;
    }
    public void setIs_pack(String is_pack) {
        this.is_pack = is_pack;
    }
    public String getPack_num() {
        return this.pack_num;
    }
    public void setPack_num(String pack_num) {
        this.pack_num = pack_num;
    }
    public String getLimit_icon() {
        return this.limit_icon;
    }
    public void setLimit_icon(String limit_icon) {
        this.limit_icon = limit_icon;
    }
    public String getLimit_num() {
        return this.limit_num;
    }
    public void setLimit_num(String limit_num) {
        this.limit_num = limit_num;
    }
    public String getMjb_value() {
        return this.mjb_value;
    }
    public void setMjb_value(String mjb_value) {
        this.mjb_value = mjb_value;
    }



}
