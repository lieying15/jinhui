package com.thlh.baselib.utils;

import android.util.Log;

import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.GoodsDetail;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Supplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class GoodsChangeUtils {

    public static Goods changeGoods(GoodsOrder tempgoods){
        Goods goods = new Goods();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setBar_code(tempgoods.getBar_code());
        goods.setIs_div_delivery(tempgoods.getIs_div_delivery());
        goods.setIs_area(tempgoods.getIs_area());
        goods.setEnd_date(tempgoods.getEnd_date());
        goods.setIs_g(tempgoods.getIs_g());
        goods.setIs_mjb(tempgoods.getIs_mjb());
        goods.setIs_bundling(tempgoods.getIs_bundling());
        goods.setIs_promotion(tempgoods.getIs_promotion());
        goods.setIs_shelves(tempgoods.getIs_shelves());
        goods.setItem_no(tempgoods.getItem_no());
        goods.setItem_subtitle(tempgoods.getItem_subtitle());
        goods.setWeight(tempgoods.getWeight());
//        goods.setSupplier_price(tempgoods.getSupplier_price());
//        goods.setSupplier_name(tempgoods.getSupplier_name());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        goods.setStorage(tempgoods.getStorage());
        goods.setStart_date(tempgoods.getStart_date());
        goods.setItem_price(tempgoods.getItem_price());
        goods.setPromotion_price(tempgoods.getPromotion_price());
        goods.setMember_price(tempgoods.getMember_price());
        goods.setBundling_price(tempgoods.getBundling_price());
        goods.setPart_of_id(tempgoods.getPart_of_id());
        goods.setIs_part(tempgoods.getIs_part());
        goods.setPart_is_bundling(tempgoods.getPart_is_bundling());
        goods.setIs_prepare(tempgoods.getIs_prepare());
        goods.setSpecial(tempgoods.getSpecial());
        goods.setLimit_icon(tempgoods.getLimit_icon());
        goods.setLimit_num(tempgoods.getLimit_num());
        goods.setIs_limit(tempgoods.getIs_limit());
        goods.setIs_pack(tempgoods.getIs_pack());
        goods.setPack_num(tempgoods.getPack_num());
        goods.setMjb_value(tempgoods.getMjb_value());
        if(tempgoods.getSupplier()!=null){
            goods.setSupplier(tempgoods.getSupplier());
        }
        return  goods;
    }

    public static Goods changeGoods(GoodsDb tempgoods){
        Goods goods = new Goods();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setBar_code(tempgoods.getBar_code());
        goods.setIs_div_delivery(tempgoods.getIs_div_delivery());
        goods.setIs_area(tempgoods.getIs_area());
        goods.setEnd_date(tempgoods.getEnd_date());
        goods.setIs_g(tempgoods.getIs_g());
        goods.setIs_mjb(tempgoods.getIs_mjb());
        goods.setIs_bundling(tempgoods.getIs_bundling());
        goods.setIs_promotion(tempgoods.getIs_promotion());
        goods.setIs_shelves(tempgoods.getIs_shelves());
        goods.setItem_no(tempgoods.getItem_no());
        goods.setItem_subtitle(tempgoods.getItem_subtitle());
        goods.setWeight(tempgoods.getWeight());
//        goods.setSupplier_price(tempgoods.getSupplier_price());
//        goods.setSupplier_name(tempgoods.getSupplier_name());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        goods.setStorage(tempgoods.getStorage());
        goods.setStart_date(tempgoods.getStart_date());
        goods.setItem_price(tempgoods.getItem_price());
        goods.setPromotion_price(tempgoods.getPromotion_price());
        goods.setMember_price(tempgoods.getMember_price());
        goods.setBundling_price(tempgoods.getBundling_price());
        goods.setPart_of_id(tempgoods.getPart_of_id());
        goods.setIs_part(tempgoods.getIs_part());
        goods.setPart_is_bundling(tempgoods.getPart_is_bundling());
        goods.setSpecial(tempgoods.getSpecial());
        goods.setIs_prepare(tempgoods.getIs_prepare());
        goods.setLimit_icon(tempgoods.getLimit_icon());
        goods.setLimit_num(tempgoods.getLimit_num());
        goods.setIs_limit(tempgoods.getIs_limit());
        goods.setIs_pack(tempgoods.getIs_pack());
        goods.setPack_num(tempgoods.getPack_num());
        goods.setMjb_value(tempgoods.getMjb_value());
        if(tempgoods.getSupplier()!=null){
            goods.setSupplier(tempgoods.getSupplier());
        }
        return  goods;
    }


    public static Goods changeGoods(GoodsDetail tempgoods){
        Goods goods = new Goods();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setBar_code(tempgoods.getBar_code());
        goods.setIs_div_delivery(tempgoods.getIs_div_delivery());
        goods.setIs_area(tempgoods.getIs_area());
        goods.setEnd_date(tempgoods.getEnd_date());
        goods.setIs_g(tempgoods.getIs_g());
        goods.setIs_mjb(tempgoods.getIs_mjb());
        goods.setIs_bundling(tempgoods.getIs_bundling());
        goods.setIs_promotion(tempgoods.getIs_promotion());
        goods.setIs_shelves(tempgoods.getIs_shelves());
        goods.setItem_no(tempgoods.getItem_no());
        goods.setItem_subtitle(tempgoods.getItem_subtitle());
        goods.setWeight(tempgoods.getWeight());
//        goods.setSupplier_price(tempgoods.getSupplier_price());
//        goods.setSupplier_name(tempgoods.getSupplier_name());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        goods.setStorage(tempgoods.getStorage());
        goods.setStart_date(tempgoods.getStart_date());
        goods.setItem_price(tempgoods.getItem_price());
        goods.setPromotion_price(tempgoods.getPromotion_price());
        goods.setMember_price(tempgoods.getMember_price());
        goods.setBundling_price(tempgoods.getBundling_price());
        goods.setSpecial(tempgoods.getSpecial());
        goods.setPart_of_id(tempgoods.getPart_of_id());
        goods.setIs_part(tempgoods.getIs_part());
        goods.setIs_prepare(tempgoods.getIs_prepare());
        goods.setLimit_icon(tempgoods.getLimit_icon());
        goods.setLimit_num(tempgoods.getLimit_num());
        goods.setIs_limit(tempgoods.getIs_limit());
        goods.setIs_pack(tempgoods.getIs_pack());
        goods.setPack_num(tempgoods.getPack_num());
        goods.setMjb_value(tempgoods.getMjb_value());
        goods.setPart_is_bundling(tempgoods.getPart_is_bundling());
        goods.setSupplier(tempgoods.getSupplier());
        if(tempgoods.getSupplier()!=null){
            goods.setSupplier(tempgoods.getSupplier());
        }
        return  goods;
    }

    public static Goods changeGoods(GoodsBundlingItem tempgoods){
        Goods goods = new Goods();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_price(tempgoods.getItem_price());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        return  goods;
    }


    public static Goods changeGoods(GoodsBundling tempgoods){
        Goods goods = new Goods();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_img_thumb("");
        return  goods;
    }


    public static GoodsDb changeGoodsDb(Goods tempgoods){
        GoodsDb goods = new GoodsDb();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setBar_code(tempgoods.getBar_code());
        goods.setIs_div_delivery(tempgoods.getIs_div_delivery());
        goods.setIs_area(tempgoods.getIs_area());
        goods.setEnd_date(tempgoods.getEnd_date());
        goods.setIs_g(tempgoods.getIs_g());
        goods.setIs_mjb(tempgoods.getIs_mjb());
        goods.setIs_bundling(tempgoods.getIs_bundling());
        goods.setIs_promotion(tempgoods.getIs_promotion());
        goods.setIs_shelves(tempgoods.getIs_shelves());
        goods.setItem_no(tempgoods.getItem_no());
        goods.setItem_subtitle(tempgoods.getItem_subtitle());
        goods.setWeight(tempgoods.getWeight());
//        goods.setSupplier(tempgoods.getSupplier());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        goods.setStorage(tempgoods.getStorage());
        goods.setStart_date(tempgoods.getStart_date());
        goods.setItem_price(tempgoods.getItem_price());
        goods.setPromotion_price(tempgoods.getPromotion_price());
        goods.setMember_price(tempgoods.getMember_price());
        goods.setBundling_price(tempgoods.getBundling_price());
        goods.setPart_of_id(tempgoods.getPart_of_id());
        goods.setIs_part(tempgoods.getIs_part());
        goods.setPart_is_bundling(tempgoods.getPart_is_bundling());
        goods.setSpecial(tempgoods.getSpecial());
        goods.setIs_prepare(tempgoods.getIs_prepare());
        goods.setLimit_icon(tempgoods.getLimit_icon());
        goods.setLimit_num(tempgoods.getLimit_num());
        goods.setIs_limit(tempgoods.getIs_limit());
        goods.setIs_pack(tempgoods.getIs_pack());
        goods.setPack_num(tempgoods.getPack_num());
        goods.setMjb_value(tempgoods.getMjb_value());

        if(tempgoods.getSupplier() != null){
            DbManager.getInstance().insertSupplier(tempgoods.getSupplier());
            long supplierDbId = DbManager.getInstance().getSupplierDBid(tempgoods.getSupplier().getId());
            if(supplierDbId < 0 ){
                BaseLog.e("GoodsChangeUtils  changeGoodsDb  Goods=>GoodsDb supplierDbId < 0！");
                return  goods;
            }
            tempgoods.getSupplier().setDbid(supplierDbId);
            goods.setSupplier(tempgoods.getSupplier());
        }else {

        }
        return  goods;
    }

    public static GoodsDb changeGoodsDb(GoodsOrder tempgoods){
        GoodsDb goods = new GoodsDb();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setBar_code(tempgoods.getBar_code());
        goods.setIs_div_delivery(tempgoods.getIs_div_delivery());
        goods.setIs_area(tempgoods.getIs_area());
        goods.setEnd_date(tempgoods.getEnd_date());
        goods.setIs_g(tempgoods.getIs_g());
        goods.setIs_mjb(tempgoods.getIs_mjb());
        goods.setIs_bundling(tempgoods.getIs_bundling());
        goods.setIs_promotion(tempgoods.getIs_promotion());
        goods.setIs_shelves(tempgoods.getIs_shelves());
        goods.setItem_no(tempgoods.getItem_no());
        goods.setItem_subtitle(tempgoods.getItem_subtitle());
        goods.setWeight(tempgoods.getWeight());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        goods.setStorage(tempgoods.getStorage());
        goods.setStart_date(tempgoods.getStart_date());
        goods.setItem_price(tempgoods.getItem_price());
        goods.setPromotion_price(tempgoods.getPromotion_price());
        goods.setMember_price(tempgoods.getMember_price());
        goods.setBundling_price(tempgoods.getBundling_price());
        goods.setPart_of_id(tempgoods.getPart_of_id());
        goods.setIs_part(tempgoods.getIs_part());
        goods.setPart_is_bundling(tempgoods.getPart_is_bundling());
        goods.setSpecial(tempgoods.getSpecial());
        goods.setIs_prepare(tempgoods.getIs_prepare());
        goods.setLimit_icon(tempgoods.getLimit_icon());
        goods.setLimit_num(tempgoods.getLimit_num());
        goods.setIs_limit(tempgoods.getIs_limit());
        goods.setIs_pack(tempgoods.getIs_pack());
        goods.setPack_num(tempgoods.getPack_num());
        goods.setMjb_value(tempgoods.getMjb_value());
        if(tempgoods.getSupplier() != null){
            Supplier supplier = new Supplier();
            supplier.setContact(tempgoods.getSupplier().getContact());
            supplier.setEmail(tempgoods.getSupplier().getEmail());
            supplier.setId(tempgoods.getSupplier().getId());
            supplier.setMobile(tempgoods.getSupplier().getMobile());
            supplier.setName(tempgoods.getSupplier().getName());
            supplier.setPhone(tempgoods.getSupplier().getPhone());
            supplier.setStore_name(tempgoods.getSupplier().getStore_name());
            DbManager.getInstance().insertSupplier(supplier);
            long supplierDbId = DbManager.getInstance().getSupplierDBid(tempgoods.getSupplier().getId());
            if(supplierDbId < 0 ){
                BaseLog.e("GoodsChangeUtils  changeGoodsDb  Goods=>GoodsDb supplierDbId < 0！");
                return  goods;
            }
            supplier.setDbid(supplierDbId);
            goods.setSupplier(supplier);
        }
        return  goods;
    }

    public static GoodsDb changeGoodsDb(GoodsBundlingItem tempgoods){
        GoodsDb goods = new GoodsDb();
        goods.setItem_id(tempgoods.getItem_id());
        goods.setItem_name(tempgoods.getItem_name());
        goods.setItem_img(tempgoods.getItem_img());
        goods.setItem_img_thumb(tempgoods.getItem_img_thumb());
        goods.setBar_code(tempgoods.getBar_code());
        goods.setIs_area(tempgoods.getIs_area());
        goods.setIs_g(tempgoods.getIs_g());
        goods.setIs_mjb(tempgoods.getIs_mjb());
        goods.setIs_shelves(tempgoods.getIs_shelves());
        goods.setItem_subtitle(tempgoods.getItem_subtitle());
        goods.setWeight(tempgoods.getWeight());
        goods.setSupplier_id(tempgoods.getSupplier_id());
        goods.setItem_price(tempgoods.getItem_price());
        return  goods;
    }




    public static List<Goods> changeGoodsList(List<GoodsDb> goodsLIst){
        List<Goods> goodsList = new ArrayList<>();
        for (GoodsDb goodsDb : goodsLIst){
            Goods goods = changeGoods(goodsDb);
            goodsList.add(goods);
        }
        return  goodsList;
    }
}
