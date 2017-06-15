package com.thlh.baselib.model;


import java.util.List;

public class Goods  {

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
    private String  limit_icon ;//0:不显示限购角标 1:显示限购角标
    private String  limit_num;//在商品详情页中，显示限购的商品数量
    private String  is_pack; //0:正常商品 1:只能整箱购买商品
    private String  pack_num ;//整箱购买数量
    private String  mjb_value ;//美家币可用值
    private List<GoodsBundling> bundlings;

    private Supplier supplier;

    public List<GoodsBundling> getBundling() {
        return bundlings;
    }

    public String getMjb_value() {
        return mjb_value;
    }

    public void setMjb_value(String mjb_value) {
        this.mjb_value = mjb_value;
    }

    public List<GoodsBundling> getBundlings() {
        return bundlings;
    }

    public void setBundlings(List<GoodsBundling> bundlings) {
        this.bundlings = bundlings;
    }

    public void setBundling(List<GoodsBundling> bundlings) {
        this.bundlings = bundlings;
    }

    public String getIs_limit() {
        return is_limit;
    }

    public void setIs_limit(String is_limit) {
        this.is_limit = is_limit;
    }

    public String getIs_pack() {
        return is_pack;
    }

    public void setIs_pack(String is_pack) {
        this.is_pack = is_pack;
    }

    public String getPack_num() {
        return pack_num;
    }

    public void setPack_num(String pack_num) {
        this.pack_num = pack_num;
    }

    public String getLimit_icon() {
        return limit_icon;
    }

    public void setLimit_icon(String limit_icon) {
        this.limit_icon = limit_icon;
    }

    public String getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(String limit_num) {
        this.limit_num = limit_num;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getMember_price() {
        return member_price;
    }

    public void setMember_price(String member_price) {
        this.member_price = member_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getIs_promotion() {
        return is_promotion;
    }

    public void setIs_promotion(String is_promotion) {
        this.is_promotion = is_promotion;
    }

    public String getPromotion_price() {
        return promotion_price;
    }

    public void setPromotion_price(String promotion_price) {
        this.promotion_price = promotion_price;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getItem_img_thumb() {
        return item_img_thumb;
    }

    public void setItem_img_thumb(String item_img_thumb) {
        this.item_img_thumb = item_img_thumb;
    }

    public String getItem_subtitle() {
        return item_subtitle;
    }

    public void setItem_subtitle(String item_subtitle) {
        this.item_subtitle = item_subtitle;
    }

    public String getIs_shelves() {
        return is_shelves;
    }

    public void setIs_shelves(String is_shelves) {
        this.is_shelves = is_shelves;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBundling_price() {
        return bundling_price;
    }

    public void setBundling_price(String bundling_price) {
        this.bundling_price = bundling_price;
    }

    public String getIs_bundling() {
        return is_bundling;
    }

    public void setIs_bundling(String is_bundling) {
        this.is_bundling = is_bundling;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getIs_g() {
        return is_g;
    }

    public void setIs_g(String is_g) {
        this.is_g = is_g;
    }

    public String getIs_area() {
        return is_area;
    }

    public void setIs_area(String is_area) {
        this.is_area = is_area;
    }

    public String getIs_div_delivery() {
        return is_div_delivery;
    }

    public void setIs_div_delivery(String is_div_delivery) {
        this.is_div_delivery = is_div_delivery;
    }

    public String getIs_mjb() {
        return is_mjb;
    }

    public void setIs_mjb(String is_mjb) {
        this.is_mjb = is_mjb;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getIs_part() {
        return is_part;
    }

    public void setIs_part(String is_part) {
        this.is_part = is_part;
    }

    public String getPart_of_id() {
        return part_of_id;
    }

    public void setPart_of_id(String part_of_id) {
        this.part_of_id = part_of_id;
    }

    public String getPart_is_bundling() {
        return part_is_bundling;
    }

    public void setPart_is_bundling(String part_is_bundling) {
        this.part_is_bundling = part_is_bundling;
    }

    public String getIs_prepare() {
        return is_prepare;
    }

    public void setIs_prepare(String is_prepare) {
        this.is_prepare = is_prepare;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


}
