package com.thlh.baselib.db;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.CartSupplierCheck;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.LogsOrder;
import com.thlh.baselib.model.Seengood;
import com.thlh.baselib.model.Supplier;
import com.thlh.baselib.utils.BaseLog;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.TextUtils;

import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;



/**
 * 数据库管理类，采用单例模式
 * GreenDao并不保证线程安全，因此要加读写锁
 */
public class DbManager {
    private static final String DB_NAME = "thlh.db";
    private static final String LOG = "DbManager";
    private static DbManager INSTANCE = new DbManager();
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private ReentrantReadWriteLock mReentrantLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock mReadLock = mReentrantLock.readLock();
    private ReentrantReadWriteLock.WriteLock mWriteLock = mReentrantLock.writeLock();
    private DaoSession mDaoSession;
    private CartgoodsDao cartgoodsDao;
    private SeengoodDao seengoodsDao;
    private GoodsDbDao goodsDbDao;
    private SupplierDao supplierDao;
    private LogsOrderDao logsDao;
    private Thread mInitThread;

    public static DbManager getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化工作，耗时操作，不能放在主线程内
     *
     * @param context
     */
    public void initialize(final Context context) {
        if (mHelper == null) {
            mInitThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
                    mDaoMaster = new DaoMaster(mHelper.getReadableDatabase());
                    mDaoSession = mDaoMaster.newSession();
                    cartgoodsDao = mDaoSession.getCartgoodsDao();
                    seengoodsDao = mDaoSession.getSeengoodDao();
                    goodsDbDao = mDaoSession.getGoodsDbDao();
                    supplierDao = mDaoSession.getSupplierDao();
                    logsDao = mDaoSession.getLogsOrderDao();
                }
            });
            mInitThread.start();
        }
    }

    /**
     * 获取DaoMaster
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            Log.e(LOG, "initialize  not finish!");
            try {
                mInitThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mDaoMaster;
    }

    /**
     * 获取异步Session
     *
     * @return
     */
    public AsyncSession getAsyncSession() {
        DaoMaster daoMaster = getDaoMaster();
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        return asyncSession;
    }

    /**
     * =============================== Supplier =========================
     */

    /**
     *  Supplier 插入一个Goods对象
     */
    public void insertSupplier(Supplier supplier) {
        Log.e("DbManager "," insertSupplier");
        if(supplier == null) return;
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getSupplierDao().queryBuilder();
            qbcart.where(SupplierDao.Properties.Id.eq(supplier.getId()));
            Supplier tempSupplier =(Supplier) qbcart.unique();
            if(tempSupplier == null) {
                BaseLog.i("DbManger:  insertSupplier 没有supplier  插入supplier数据");
                supplierDao.insert(supplier);
            }else {
                BaseLog.i("DbManger:  insertSupplier 有supplier  ");
//                supplierDao.insertOrReplace(supplier);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 获取Supplier数据库中的id
     */
    public long getSupplierDBid(String supplier_id) {
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getSupplierDao().queryBuilder();
            qbcart.where(SupplierDao.Properties.Id.eq(supplier_id));
            Supplier supplier  =(Supplier) qbcart.unique();
            if(supplier != null ){
                return supplier.getDbid();
            }else {
                return -1;
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 获取Goods数据数量
     */
    public int getSupplierSize() {
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getSupplierDao().queryBuilder();
            return qbcart.list().size();
        } finally {
            mWriteLock.unlock();
        }
    }
    /**  =============================== Goods =========================  */

    /**
     * GoodsDB插入一个Goods对象
     */
    public void insertGoods(Goods goods) {
        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        insertGoods(goodsDb);
    }

    public void insertGoods(GoodsDb  goodsDb) {
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getGoodsDbDao().queryBuilder();
            qbcart.where(GoodsDbDao.Properties.Item_id.eq(goodsDb.getItem_id()));
            GoodsDb tempgoods =(GoodsDb) qbcart.unique();
            if(tempgoods == null){
                if(goodsDb.getDb_supplier_id() == 0 && goodsDb.getSupplier() != null){
                        BaseLog.i("DbManger:  Supplier库中没有Supplier数据， 插入Supplier数据");
                        insertSupplier(goodsDb.getSupplier());
                        long supplierDbId = getSupplierDBid(goodsDb.getSupplier().getId());
                        if (supplierDbId < 0) return;
                        goodsDb.getSupplier().setDbid(supplierDbId);
                }else {
                    BaseLog.i("DbManger:  goods里没有Supplier");
                }
                goodsDbDao.insert(goodsDb);
            }else {
                BaseLog.e("DbManger:  goods存在 ");
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 获取Goods数据数量
     */
    public int getGoodsSize() {
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getGoodsDbDao().queryBuilder();
            return qbcart.list().size();
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 获取Goods数据库中的id
     */
    public long getGoodsDBid(String item_id) {
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getGoodsDbDao().queryBuilder();
            qbcart.where(GoodsDbDao.Properties.Item_id.eq(item_id));
            GoodsDb goodsDb  =(GoodsDb) qbcart.unique();
            if(goodsDb != null ){
                return goodsDb.getDbid();
            }else {
                return -1;
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * CartGoods插入一个Goods对象
     */

    public void insertCart(Goods goods) {
        insertCart(goods,false);
    }

    public void insertCart(Goods goods,boolean isBuyImmediately) {
        GoodsDb  goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        if(goods.getIs_bundling().equals("1")){ //是套装商品 将supplierid转变为-1，
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if(goods.getBundling() == null){
                return;
            }
            GoodsBundling goodsBundling = goods.getBundling().get(0);
            if (goodsBundling == null) {
                return;
            }
            String bundinginfo = gson.toJson(goodsBundling, GoodsBundling.class);
            Supplier supplier = new Supplier();
            supplier.setId("-1");
            supplier.setName("优惠套装");
            DbManager.getInstance().insertSupplier(supplier);
            long goodsDbid = DbManager.getInstance().getGoodsDBid(goodsDb.getItem_id());
            if(goodsDbid == -1){
                goodsDbid = DbManager.getInstance().getGoodsSize() + 1;
            }
            goodsDb.setDbid(goodsDbid);
            DbManager.getInstance().insertGoods(goodsDb);
            Cartgoods cartgoods = new Cartgoods();
            cartgoods.setGoodsdb(goodsDb);
            cartgoods.setDb_goods_id(goodsDbid);
            cartgoods.setBunding_info(bundinginfo);
            cartgoods.setSupplierid("-1");
            cartgoods.setSuppliername("优惠套装");
            if(isBuyImmediately){
                cartgoods.setIsBuyImmediately(isBuyImmediately);
                cartgoods.setGoods_num(0);
            }
            DbManager.getInstance().insertCart(cartgoods);
        }else {
            insertCart( goodsDb,isBuyImmediately);
        }
    }

    public void insertCart(GoodsDb goodsDb) {
        insertCart(goodsDb,false);
    }

    public void insertCart(GoodsDb goodsDb,boolean isBuyImmediately) {
        try {
            mWriteLock.lock();
            insertGoods(goodsDb);
            long goodsDbId = getGoodsDBid(goodsDb.getItem_id());
            if(goodsDbId <0){
                return;
            }
            goodsDb.setDbid(goodsDbId);
            BaseLog.i("DbManger:  goodsDbId " +goodsDbId );
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.Db_goods_id.eq(goodsDbId));
            Cartgoods tempshopcart =(Cartgoods) qbcart.unique();
            if(tempshopcart == null){
                BaseLog.i("DbManger:  cartgoods里没有相同goods  插入数据");
                Cartgoods cartgoods = new Cartgoods();
                cartgoods.setGoodsdb(goodsDb);
                cartgoods.setSupplierid(goodsDb.getSupplier_id());
                cartgoods.setSuppliername(goodsDb.getSupplier().getName());
                if(isBuyImmediately){
                    cartgoods.setIsBuyImmediately(isBuyImmediately);
                    cartgoods.setGoods_num(1);
                }
                cartgoodsDao.insert(cartgoods);
            }else{
                int  tempint = tempshopcart.getGoods_num();
                BaseLog.i("DbManger:  cartgoods 里有相同goods  num " + tempint);
                tempshopcart.setGoods_num(tempint + 1);
                tempshopcart.setGoodsdb(goodsDb);
                tempshopcart.setSupplierid(goodsDb.getSupplier_id());
                tempshopcart.setSuppliername(goodsDb.getSupplier().getName());
                if(isBuyImmediately){
                    tempshopcart.setIsBuyImmediately(isBuyImmediately);
                    tempshopcart.setGoods_num(1);
                }
                cartgoodsDao.insertOrReplace(tempshopcart);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * Cart插入一个Cartgoods对象
     */
    public void insertCart(Cartgoods cartgoods) {
        try {
            mWriteLock.lock();
            Cartgoods tempshopcart ;
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.Db_goods_id.eq(cartgoods.getDb_goods_id()));
            tempshopcart =(Cartgoods) qbcart.unique();
            if(tempshopcart == null){
                BaseLog.e("DbManger:  cartgoods里没有相同goods  插入数据");
                cartgoods.setGoods_num(1);
                // 判断goods库是否有该数据,没有插入。
                GoodsDb tempgoods ;
                QueryBuilder qbgoods = mDaoSession.getGoodsDbDao().queryBuilder();
                qbgoods.where(GoodsDbDao.Properties.Item_id.eq(cartgoods.getGoodsdb().getItem_id()));
                tempgoods =(GoodsDb) qbgoods.unique();
                if(tempgoods == null){
                    goodsDbDao.insert(cartgoods.getGoodsdb());
                }
                cartgoodsDao.insert(cartgoods);
            }else{
                int  tempint = tempshopcart.getGoods_num();
                tempshopcart.setGoods_num(tempint + 1);
                cartgoodsDao.insertOrReplace(tempshopcart);
            }
        } finally {
            mWriteLock.unlock();
        }
    }


    /**
     * Cart删除一个Cartgoods对象
     */
    public void deleteCartGoods(Goods goods) {
        GoodsDb  goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        deleteCartGoods(goodsDb);
    }

    public void deleteCartGoods(GoodsDb goodsDb) {
        try {
            mWriteLock.lock();
            long goodsDbId = getGoodsDBid(goodsDb.getItem_id());
            if(goodsDbId <0 ){
                BaseLog.e("DbManger:  deleteCartGoods goodsDbId <0");
                return;
            }
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.Db_goods_id.eq(goodsDbId));
            Cartgoods tempshopcart =(Cartgoods) qbcart.unique();
            if(tempshopcart == null){
                BaseLog.i("DbManager deleteCartGoods tempshopcart == null ");
                return;
            }else{
                int  tempint = tempshopcart.getGoods_num();
                if(tempint ==1){
                    BaseLog.i("DbManager deleteCartGoods  num = 1");
                    cartgoodsDao.delete(tempshopcart);
//                    goodsDbDao.delete(goods);
                }else {
                    BaseLog.i("DbManager deleteCartGoods num > 1");
                    tempshopcart.setGoods_num(tempint - 1);
                    cartgoodsDao.insertOrReplace(tempshopcart);

                }
            }
        } finally {
            mWriteLock.unlock();
        }
    }
    /**
     * 获取Cartgoods数据数量
     */
    public int getCartGoodsSize() {
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            return qbcart.list().size();
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 设置为选中状态
     */
    public void setGoodsSelect(Goods goods,boolean isSelect){
        GoodsDb  goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        setGoodsSelect(goodsDb ,isSelect);
    }

    public void setGoodsSelect(GoodsDb goodsdb,boolean isSelect){
        try {
            mWriteLock.lock();
            insertGoods(goodsdb);
            long goodsDbId = getGoodsDBid(goodsdb.getItem_id());
            if(goodsDbId <0 ){
                BaseLog.e("DbManger:  setGoodsSelect goodsDbId <0");
                return;
            }
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.Db_goods_id.eq(goodsDbId));
            Cartgoods tempshopcart =(Cartgoods) qbcart.unique();
            if(tempshopcart == null){
                BaseLog.i("DbManager setGoodsSelect tempshopcart == null ");
                return;
            }else{
                tempshopcart.setIsSelect(isSelect);
                cartgoodsDao.insertOrReplace(tempshopcart);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 设置供应商商品选中状态
     */
    public void setSupplierGoodsSelect(String supplerid ,boolean isSelect){
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            for(Cartgoods cartgoods :(List<Cartgoods>)qbcart.list()){
                if(cartgoods.getGoodsdb().getSupplier_id().equals(supplerid)){
                    cartgoods.setIsSelect(isSelect);
                    cartgoodsDao.insertOrReplace(cartgoods);
                }
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 设置为全部购物车商品未选中状态
     */
    public void setAllGoodsSelect(boolean isSelect){
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            for(Cartgoods cartgoods :(List<Cartgoods>)qbcart.list()){
                cartgoods.setIsSelect(isSelect);
                cartgoodsDao.insertOrReplace(cartgoods);
            }
        } finally {
            mWriteLock.unlock();
        }
    }


    /**
     * 获取全部cartGoods对象
     */
    public List getAllCartGoods() {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getCartgoodsDao().queryBuilder();
            return qb.list();
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * 获取全部选中cartGoods对象
     */
    public List getAllSelectCartGoods(boolean isSelect) {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getCartgoodsDao().queryBuilder();
            qb.where(CartgoodsDao.Properties.IsSelect.eq(isSelect));
            return qb.list();
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * 获取立即购买的cartGoods对象
     */
    public List getImmediatelyBuyGoods() {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getCartgoodsDao().queryBuilder();
            qb.where(CartgoodsDao.Properties.IsBuyImmediately.eq(true));
            return qb.list();
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * 获取能美家币支付的cartGoods对象
     */
    public List getAllPayMjbCartGoods(boolean isCanPay,boolean isBuyImmediately) {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getCartgoodsDao().queryBuilder();
            if(isBuyImmediately){
                qb.where(CartgoodsDao.Properties.IsBuyImmediately.eq(true));
            }else {
                qb.where(CartgoodsDao.Properties.IsSelect.eq(true));
            }
            List<Cartgoods> cartgoodses = qb.list();

            if(cartgoodses ==null ||cartgoodses.size() ==0){
                BaseLog.e("getAllPayMjbCartGoods  cartgoodses ==null ||cartgoodses.size() ==0");
                return null;
            }

            if(isCanPay){ //返回可以用美家币的
                for (int i = cartgoodses.size() -1; i >-1; i--) {
                    if (cartgoodses.get(i).getGoodsdb().getIs_mjb().equals("0")) { //可用美家币的
                        cartgoodses.remove(i);
                    }
                }
            }else{//返回不可用美家币的
                for (int i = cartgoodses.size() -1; i >-1; i--) {
                    if (!cartgoodses.get(i).getGoodsdb().getIs_mjb().equals("0")) { //可用美家币的
                        cartgoodses.remove(i);
                    }
                }
            }
             return cartgoodses;
        } finally {
            mReadLock.unlock();
        }

    }

    /**
     * 设置美家币支付状态
     */
    public void setCartGoodsPayMjb(long dbGoodsId, boolean isPay) {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getCartgoodsDao().queryBuilder();
            qb.where(CartgoodsDao.Properties.Db_goods_id.eq(dbGoodsId));
            Cartgoods cartgoods = (Cartgoods)qb.unique();
            cartgoods.setIsPayMjb(isPay);
            cartgoodsDao.insertOrReplace(cartgoods);
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * 取得所有商品数量
     */
    public int getAllGoodsNum() {
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        if(cartgoodsList ==null){
            return 0;
        }else {
            int sum = 0;
            for (Cartgoods cartgood:cartgoodsList){
                // 一个套装数量为包含商品数量时的计算
//                if(cartgood.getGoods().getIs_bundling().equals("1")){
//                    GsonBuilder builder  =new GsonBuilder();;
//                    Gson gson = builder.create();;
//                    String bundinginfo = cartgood.getBunding_info();
//                    GoodsBundling goodsBundling =gson.fromJson(bundinginfo, GoodsBundling.class);
//                    sum += goodsBundling.getItem().size();
//                }else {
                    sum += cartgood.getGoods_num();
//                }
            }
            return sum;
        }
    }

    /**
     * 取得所有商品金额
     */
    public double getAllGoodsPrice() {
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        if(cartgoodsList ==null){
            return 0;
        }else {
            double totalprice = 0;
            for (Cartgoods cartgood:cartgoodsList){
                totalprice += cartgood.getGoods_num() * Double.parseDouble(cartgood.getGoodsdb().getItem_price());
            }
            return totalprice;
        }
    }

    /**
     * 取得选中商品金额
     */
    public String getSelectGoodsPrice(List<CartSupplierCheck> cartSupplierCheck) {
        BaseLog.e(" Db getSelectGoodsPrice");
        List<CartSupplier> cartSupplierList = getCartSupplierList();
        if(cartSupplierList == null|| cartSupplierCheck.size() ==0|| cartSupplierList.size()==0){
            return "0";
        }else {
            double totalprice = 0;
            double totalMjbprice = 0;
            for (int i = 0; i < cartSupplierCheck.size(); i++) {
              int checkListSize = cartSupplierCheck.get(i).getGoodsCheckStates().size();
                for (int n = 0; n < checkListSize; n++) {
                    if(cartSupplierCheck.get(i).getGoodsCheckStates().get(n)){
                        GoodsDb goodsDb = cartSupplierList.get(i).getCartgoods().get(n).getGoodsdb();
//                        if(Integer.parseInt(goodsDb.getIs_mjb()) >1){
//                            Double mjbprice = Double.parseDouble(goodsDb.getMjb_value());
//                            totalMjbprice += cartSupplierList.get(i).getCartgoods().get(n).getGoods_num() * mjbprice;
//                        }
                        Double price = Double.parseDouble(goodsDb.getItem_price());
                        totalprice += cartSupplierList.get(i).getCartgoods().get(n).getGoods_num() * price;
                    }
                }
            }
//            if(totalMjbprice == 0){
//                if(totalprice == 0){
//                    return "0";
//                }else {
//                    return TextUtils.showPrice(totalprice);
//                }
//            }else {
                if(totalprice == 0 || totalprice==totalMjbprice){
                    return "0";
                }else {
                    double  finailPrice = totalprice - totalMjbprice;
                    return TextUtils.showPrice(finailPrice) ;
                }
//            }
        }
    }

    /**
     * 取得选中商品美家币金额
     */
    public String getSelectGoodsMjbPrice(List<CartSupplierCheck> cartSupplierCheck) {
        List<CartSupplier> cartSupplierList = getCartSupplierList();
        if(cartSupplierList == null|| cartSupplierCheck.size() ==0|| cartSupplierList.size()==0){
            return "0";
        }else {
            double totalMjbprice = 0;
            for (int i = 0; i < cartSupplierCheck.size(); i++) {
                int checkListSize = cartSupplierCheck.get(i).getGoodsCheckStates().size();
                for (int n = 0; n < checkListSize; n++) {
                    if(cartSupplierCheck.get(i).getGoodsCheckStates().get(n)){
                        GoodsDb goodsDb = cartSupplierList.get(i).getCartgoods().get(n).getGoodsdb();
                        if(Integer.parseInt(goodsDb.getIs_mjb()) >1){
                            Double mjbprice = Double.parseDouble(goodsDb.getMjb_value());
                            totalMjbprice += cartSupplierList.get(i).getCartgoods().get(n).getGoods_num() * mjbprice;
                        }
                    }
                }
            }
            if(totalMjbprice ==0){
                return "0";
            }else {
                return TextUtils.showPrice(totalMjbprice);
            }
        }
    }

    public double getSelectGoodsPrice() {
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        double totalprice = 0;
        for (int i = 0; i < cartgoodsList.size(); i++) {
            if(cartgoodsList.get(i).getIsSelect()){
                totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getItem_price());
            }
        }
        return totalprice;
    }

    public double getCartGoodsPrice(boolean isImmediatelyBuy ) {
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        double totalprice = 0;
        for (int i = 0; i < cartgoodsList.size(); i++) {
            if(isImmediatelyBuy){
                if(cartgoodsList.get(i).getIsBuyImmediately()){
                    totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getItem_price());
                }
            }else {
                if(cartgoodsList.get(i).getIsSelect()){
                    totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getItem_price());
                }
            }

        }
        return totalprice;
    }


    public int getSelectGoodsNum() {
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        int num = 0;
        for (int i = 0; i < cartgoodsList.size(); i++) {
            if(cartgoodsList.get(i).getIsSelect()){
                num +=  cartgoodsList.get(i).getGoods_num();
            }
        }
        return num;
    }

    public int getCartGoodsNum(boolean isImmediatelyBuy){
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        int num = 0;
        for (int i = 0; i < cartgoodsList.size(); i++) {
            if(isImmediatelyBuy){
                if(cartgoodsList.get(i).getIsBuyImmediately()){
                    num +=  cartgoodsList.get(i).getGoods_num();
                }
            }else {
                if(cartgoodsList.get(i).getIsSelect()){
                    num +=  cartgoodsList.get(i).getGoods_num();
                }
            }

        }
        return num;
    }

    /**
     * 取得使用美家钻金额
     */
    public double getUseMjbPrice(boolean isImmediatelyBuy) {
        List<Cartgoods> cartgoodsList = getAllCartGoods();
        double totalprice = 0;
        for (int i = 0; i < cartgoodsList.size(); i++) {
            if(isImmediatelyBuy){
                if(cartgoodsList.get(i).getIsBuyImmediately()&& cartgoodsList.get(i).getIsPayMjb()){
                    if(cartgoodsList.get(i).getGoodsdb().getIs_mjb().equals("1")){
                        totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getItem_price());
                    }
                    if(cartgoodsList.get(i).getGoodsdb().getIs_mjb().equals("2")){
                        totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getMjb_value());
                    }
                }
            }else {
                if(cartgoodsList.get(i).getIsSelect()&& cartgoodsList.get(i).getIsPayMjb()){
                    if(cartgoodsList.get(i).getGoodsdb().getIs_mjb().equals("1")){
                        totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getItem_price());
                    }
                    if(cartgoodsList.get(i).getGoodsdb().getIs_mjb().equals("2")){
                        totalprice += cartgoodsList.get(i).getGoods_num() * Double.parseDouble(cartgoodsList.get(i).getGoodsdb().getMjb_value());
                    }
                }
            }


        }
        return totalprice;
    }

    public   List<CartSupplier> getCartSupplierList() { //
        return  getCartSupplierList(false,false);
    }

    /** 将购物车数据按供应商分类 */
    public   List<CartSupplier> getCartSupplierList(boolean isSelect,boolean isBuyImmediately){ //
        List<CartSupplier> cartSupplierList = new ArrayList<CartSupplier>();
        List<Cartgoods> cartgoods;
        if(isSelect )
            cartgoods = getAllSelectCartGoods(true);
        else if(isBuyImmediately)
            cartgoods = getImmediatelyBuyGoods();
        else
            cartgoods = getAllCartGoods();

        HashMap<String,List<Cartgoods>> cartgoodsMap = new HashMap<>();
        for (int i = 0; i < cartgoods.size(); i++) {
            String supplierid = cartgoods.get(i).getSupplierid();
            List<Cartgoods> templist = new ArrayList<Cartgoods>();

            if (cartgoodsMap.get(supplierid)==null){
                templist.clear();
                templist.add(cartgoods.get(i));
                cartgoodsMap.put(supplierid,templist);
            }else {
                templist.clear();
                templist.addAll(cartgoodsMap.get(supplierid));
                templist.add(cartgoods.get(i));
                cartgoodsMap.put(supplierid,templist);
            }
        }

        Iterator<Map.Entry<String,List<Cartgoods>>> iter = cartgoodsMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            List<Cartgoods> value = (List<Cartgoods>)entry.getValue();
            CartSupplier cartSupplier = new CartSupplier();
            cartSupplier.setSupplier_id(key);
            cartSupplier.setCartgoods(value);
            cartSupplierList.add(cartSupplier);
        }
        return cartSupplierList;
    }

    /** 获取调整商品格式 -配送信息用  */
    public   List<CartSupplier> getExpressMapList(){
        List<Cartgoods> cartgoods = getAllCartGoods();
        Gson gson = new Gson();
        GoodsBundling goodsBundling;
        String supplierid;
        HashMap<String,List<Cartgoods>> cartgoodsMap = new HashMap<>();
        for (int i = 0; i < cartgoods.size(); i++) {
            if(cartgoods.get(i).getBunding_info() !=null && !cartgoods.get(i).getBunding_info().equals("")){
                //为套装的情况
                goodsBundling = gson.fromJson(cartgoods.get(i).getBunding_info(), GoodsBundling.class);

                for (int n = 0; n <goodsBundling.getItem().size() ; n++) {
                    List<Cartgoods> templist = new ArrayList<Cartgoods>();
                    GoodsBundlingItem  bundingItem = goodsBundling.getItem().get(n);
                    supplierid = goodsBundling.getItem().get(n).getSupplier_id();

                    if (cartgoodsMap.get(supplierid)==null){
                        Cartgoods cartgood = new Cartgoods();
                        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(bundingItem);
                        Supplier itemSupplier = new Supplier();
                        itemSupplier.setId(bundingItem.getSupplier_id());
                        itemSupplier.setName(bundingItem.getItem_name());
                        itemSupplier.setStore_name(bundingItem.getItem_name());
                        long supplierDbid = DbManager.getInstance().getSupplierDBid(bundingItem.getSupplier_id());
                        itemSupplier.setDbid(supplierDbid);
                        goodsDb.setSupplier(itemSupplier);
                        long goodsDbid = DbManager.getInstance().getGoodsDBid(goodsBundling.getItem_id());
                        if(goodsDbid == -1){
                            goodsDbid = DbManager.getInstance().getGoodsSize()+1;
                            DbManager.getInstance().insertGoods(goodsDb);
                        }
                        goodsDb.setDbid(goodsDbid);

                        cartgood.setGoodsdb(goodsDb);
                        cartgood.setGoods_num(Integer.parseInt(bundingItem.getItem_num()));
                        cartgood.setIsSelect(true);
                        cartgood.setDb_goods_id(goodsDb.getDbid());
                        templist.add(cartgood);
                        cartgoodsMap.put(supplierid,templist);
                    }else {
                        templist.addAll(cartgoodsMap.get(supplierid));
                        Cartgoods cartgood = new Cartgoods();
                        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(bundingItem);
                        Supplier itemSupplier = new Supplier();
                        itemSupplier.setId(bundingItem.getSupplier_id());
                        itemSupplier.setName(bundingItem.getItem_name());
                        itemSupplier.setStore_name(bundingItem.getItem_name());
                        long supplierDbid = DbManager.getInstance().getSupplierDBid(bundingItem.getSupplier_id());
                        itemSupplier.setDbid(supplierDbid);
                        goodsDb.setSupplier(itemSupplier);
                        long goodsDbid = DbManager.getInstance().getGoodsDBid(goodsBundling.getItem_id());
                        if(goodsDbid == -1){
                            goodsDbid = DbManager.getInstance().getGoodsSize()+1;
                            DbManager.getInstance().insertGoods(goodsDb);
                        }
                        goodsDb.setDbid(goodsDbid);

                        cartgood.setGoodsdb(goodsDb);
                        cartgood.setGoods_num(Integer.parseInt(bundingItem.getItem_num()));
                        cartgood.setIsSelect(true);
                        cartgood.setDb_goods_id(goodsDb.getDbid());
                        templist.add(cartgood);
                        cartgoodsMap.put(supplierid,templist);
                    }
                }
            }else {
                supplierid = cartgoods.get(i).getGoodsdb().getSupplier_id();
                List<Cartgoods> templist = new ArrayList<Cartgoods>();
                if (cartgoodsMap.get(supplierid)==null){
                    templist.add(cartgoods.get(i));
                    cartgoodsMap.put(supplierid,templist);
                }else {
                    templist.addAll(cartgoodsMap.get(supplierid));
                    templist.add(cartgoods.get(i));
                    cartgoodsMap.put(supplierid,templist);
                }
            }
        }
        List<CartSupplier> cartSupplierList = new ArrayList<CartSupplier>();
        Iterator<Map.Entry<String,List<Cartgoods>>> iter = cartgoodsMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            List<Cartgoods> val = (List<Cartgoods>)entry.getValue();
            CartSupplier map = new CartSupplier();
            map.setSupplier_id(key);
            map.setCartgoods(val);
            cartSupplierList.add(map);
        }
        return cartSupplierList;
    }

    /**
     * =============================== SeenGoods =========================
     */

    /**
     * Cart插入一个Goods对象
     */
    public void insertSeenGoods(Goods goods) {
        GoodsDb  goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        insertSeenGoods(goodsDb);
    }

    public void insertSeenGoods(GoodsDb goodsDb) {
        if (goodsDb == null) {
            return;
        }
        try {
            mWriteLock.lock();
            insertGoods(goodsDb);
            long goodsDbId = getGoodsDBid(goodsDb.getItem_id());
            if(goodsDbId <0 ){
                BaseLog.e("DbManger:  insertSeenGoods goodsDbId <0");
                return;
            }
            goodsDb.setDbid(goodsDbId);
            QueryBuilder qbcart = mDaoSession.getSeengoodDao().queryBuilder();
            qbcart.where(SeengoodDao.Properties.Goods_id.eq(goodsDbId));
            Seengood tempseengood =(Seengood) qbcart.unique();
            if(tempseengood == null){
                BaseLog.i("DbManger:  seengood 里没有相同goods  插入数据");
                Seengood seengood = new Seengood();
                seengood.setGoodsdb(goodsDb);
                seengoodsDao.insert(seengood);
            }else{
                tempseengood.setGoodsdb(goodsDb);
                seengoodsDao.insertOrReplace(tempseengood);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    public void deleteSeenGoods(Goods goods) {
        GoodsDb  goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        deleteSeenGoods(goodsDb);
    }
    /**
     * SeenGoods删除一个Goods对象
     */
    public void deleteSeenGoods(GoodsDb goodsDb) {
        try {
            mWriteLock.lock();
            Seengood tempseengood ;
            QueryBuilder qbcart = mDaoSession.getSeengoodDao().queryBuilder();
            qbcart.where(SeengoodDao.Properties.Goods_id.eq(goodsDb.getDbid()));
            tempseengood =(Seengood) qbcart.unique();
            if(tempseengood == null){
                BaseLog.e("DbManager deleteSeenGoods tempseengood == null ");
                return;
            }else{
               seengoodsDao.delete(tempseengood);
            }
        } finally {
            mWriteLock.unlock();
        }
    }


    /**
     * 清除购物车中选中的商品
     */
    public void deleteSelectGoods(){
        try {
            mWriteLock.lock();
            List<Cartgoods> tempshopcartlist ;
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.IsSelect.eq(true));
            tempshopcartlist = qbcart.list();
            cartgoodsDao.deleteInTx(tempshopcartlist);
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 清除立即购买的商品
     */
    public void deleteBuyImmediatelyGoods(){
        try {
            mWriteLock.lock();
            List<Cartgoods> tempshopcartlist ;
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.IsBuyImmediately.eq(true));
            tempshopcartlist = qbcart.list();
            cartgoodsDao.deleteInTx(tempshopcartlist);
        } finally {
            mWriteLock.unlock();
        }
    }

    /**
     * 还原立即购买的商品
     */
    public void clearBuyImmediatelyGoods(){
        try {
            mWriteLock.lock();
            List<Cartgoods> tempshopcartlist ;
            QueryBuilder qbcart = mDaoSession.getCartgoodsDao().queryBuilder();
            qbcart.where(CartgoodsDao.Properties.IsBuyImmediately.eq(true));
            tempshopcartlist = qbcart.list();
            for (int i = 0; i < tempshopcartlist.size(); i++) {
                tempshopcartlist.get(i).setIsBuyImmediately(false);
            }
        } finally {
            mWriteLock.unlock();
        }
    }


    public void deleteAllSeenGoods() {
        try {
            mWriteLock.lock();
            seengoodsDao.deleteAll();
        } finally {
            mWriteLock.unlock();
        }
    }


    /**
     * 获取全部cartGoods对象
     */
    public List getAllSeenGoods() {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getSeengoodDao().queryBuilder();
//            qb.where(SeengoodDao.Properties.User_id.eq(SPUtils.getUsername())); //区分不同用户
            qb.orderDesc(SeengoodDao.Properties.Seengood_id);
            qb.limit(10);
            return qb.list();
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * ============== LogsOrder =========================
     */
    public void insertLogs(LogsOrder logsOrder) {
        if(logsOrder == null) return;
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getLogsOrderDao().queryBuilder();
            qbcart.where(LogsOrderDao.Properties.Id.eq(logsOrder.getId()));
            LogsOrder tempLogsOrder =(LogsOrder) qbcart.unique();
            if(tempLogsOrder == null){
                BaseLog.i("DbManger:  LogsOrder 里没有相同 LogsOrder  插入logs数据");
                logsDao.insert(logsOrder);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    public void insertLogs(List<LogsOrder> logsOrderList) {
        for(LogsOrder logsOrder : logsOrderList){
            insertLogs(logsOrder);
        }
    }
    /**
     * 获取最后一条
     */
    public String getlastLogsId() {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getLogsOrderDao().queryBuilder();
            LogsOrder logsOrder = (LogsOrder)qb.orderDesc(LogsOrderDao.Properties.Id).limit(1).unique();
            if(logsOrder != null ){
                return logsOrder.getId();
            }
            return "0";
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * 获取全部Logs对象
     */
    public List getAllLogs() {
        try {
            mReadLock.lock();
            QueryBuilder qb = mDaoSession.getLogsOrderDao().queryBuilder();
            return qb.list();
        } finally {
            mReadLock.unlock();
        }
    }

    /**
     * 清除消息
     */
    public void deleteLogs(String id ){
        try {
            mWriteLock.lock();
            QueryBuilder qbcart = mDaoSession.getLogsOrderDao().queryBuilder();
            qbcart.where(LogsOrderDao.Properties.Id.eq(true));
            LogsOrder logsOrder = (LogsOrder)qbcart.unique();
            if(logsOrder !=null){
                logsDao.delete(logsOrder);
            }
        } finally {
            mWriteLock.unlock();
        }
    }





    /**
     * 更新GoodsDao信息
     *
     * @param newGoods 新GoodsDao,主键必须存在
     */
    public void update(GoodsDb newGoods) {
        try {
            mWriteLock.lock();
//            GoodsDbDao goodsDbDao = getGoodsDbDao();
            goodsDbDao.update(newGoods);
        } finally {
            mWriteLock.unlock();
        }
    }

    public ReentrantReadWriteLock.ReadLock getReadLock() {
        return mReadLock;
    }

    public ReentrantReadWriteLock.WriteLock getWriteLock() {
        return mWriteLock;
    }
}
