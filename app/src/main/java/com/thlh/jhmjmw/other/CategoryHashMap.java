package com.thlh.jhmjmw.other;


import com.thlh.jhmjmw.R;

import java.util.HashMap;
import java.util.Map;

import static com.thlh.jhmjmw.R.drawable.icon_hp_daily_select;
import static com.thlh.jhmjmw.R.drawable.icon_hp_drink;
import static com.thlh.jhmjmw.R.drawable.icon_hp_drink_select;
import static com.thlh.jhmjmw.R.drawable.icon_hp_kitchen;
import static com.thlh.jhmjmw.R.drawable.icon_hp_kitchen_select;


/**
 * Created by DE on 2015/7/2.
 */
public class CategoryHashMap {
    //首页服务根据categroy对应的图标
    public static Map<String, Integer> iconWhiteMap = new HashMap<String, Integer>(){{
            put("美家精品", R.drawable.icon_boutique);
            put("美家特惠", R.drawable.icon_privilege);
            put("美家会员", R.drawable.icon_member);
            put("数码电器", R.drawable.icon_topcate_digital);
            put("生活百货", R.drawable.icon_topcate_daily);
            put("食品生鲜", R.drawable.icon_topcate_food);
            put("生活保健", R.drawable.icon_topcate_health);
            put("保健品", R.drawable.icon_topcate_health);
            put("家居家纺", R.drawable.icon_topcate_home);
            put("母婴儿童", R.drawable.icon_topcate_baby);
            put("时尚美妆", R.drawable.icon_topcate_beauty);
            put("酒水饮料", R.drawable.icon_ctopcate_drink);
            put("酒　　类", R.drawable.icon_ctopcate_drink);
            put("其他", R.drawable.icon_topcate_other);
            put("其　　他", R.drawable.icon_topcate_other);
        }
    };

    //首页服务根据categroy对应的cat类型
    public static Map<String, Integer> iconBlueMap = new HashMap<String, Integer>(){{
            put("美家精品", R.drawable.icon_boutique_blue);
            put("美家特惠", R.drawable.icon_privilege_blue);
            put("美家会员", R.drawable.icon_member_blue);
            put("数码电器", R.drawable.icon_topcate_digital_blue);
            put("生活百货", R.drawable.icon_topcate_daily_blue);
            put("食品生鲜", R.drawable.icon_topcate_food_blue);
            put("生活保健", R.drawable.icon_topcate_health_blue);
            put("保健品", R.drawable.icon_topcate_health_blue);
            put("家居家纺", R.drawable.icon_topcate_home_blue);
            put("母婴儿童", R.drawable.icon_topcate_baby_blue);
            put("时尚美妆", R.drawable.icon_topcate_beauty_blue);
            put("酒水饮料", R.drawable.icon_topcate_drink_blue);
            put("酒　　类", R.drawable.icon_topcate_drink_blue);
            put("其他", R.drawable.icon_topcate_other_blue);
            put("其　　他", R.drawable.icon_topcate_other_blue);
        }
    };

    public static Map<String, Integer> iconWineMap = new HashMap<String, Integer>(){{
        put("美家精品", R.drawable.icon_boutique_wine);
        put("美家特惠", R.drawable.icon_privilege_wine);
        put("美家会员", R.drawable.icon_member_wine);
        put("数码电器", R.drawable.icon_topcate_digital_wine);
        put("生活百货", R.drawable.icon_topcate_daily_wine);
        put("食品生鲜", R.drawable.icon_topcate_food_wine);
        put("生活保健", R.drawable.icon_topcate_health_wine);
        put("保健品", R.drawable.icon_topcate_health_wine);
        put("家居家纺", R.drawable.icon_topcate_home_wine);
        put("母婴儿童", R.drawable.icon_topcate_baby_wine);
        put("时尚美妆", R.drawable.icon_topcate_beauty_wine);
        put("酒水饮料", R.drawable.icon_topcate_drink_wine);
        put("酒　　类", R.drawable.icon_topcate_drink_wine);
        put("其他", R.drawable.icon_topcate_other_wine);
        put("其　　他", R.drawable.icon_topcate_other_wine);
        }
    };

    public static Map<String, Integer> iconBlackMap = new HashMap<String, Integer>(){{
        put("美家精品", R.drawable.icon_boutique_black);
        put("美家特惠", R.drawable.icon_privilege_black);
        put("美家会员", R.drawable.icon_member_black);
        put("数码电器", R.drawable.icon_topcate_digital_black);
        put("生活百货", R.drawable.icon_topcate_daily_black);
        put("食品生鲜", R.drawable.icon_topcate_food_black);
        put("生活保健", R.drawable.icon_topcate_health_black);
        put("保健品", R.drawable.icon_topcate_health_black);
        put("家居家纺", R.drawable.icon_topcate_home_black);
        put("添置新家", R.drawable.icon_topcate_home_black);
        put("母婴儿童", R.drawable.icon_topcate_baby_black);
        put("时尚美妆", R.drawable.icon_topcate_beauty_black);
        put("酒水饮料", R.drawable.icon_topcate_drink_black);
        put("酒　　类", R.drawable.icon_topcate_drink_black);
        put("其他", R.drawable.icon_topcate_other_black);
        put("其　　他", R.drawable.icon_topcate_other_black);
    }
    };

    public static Map<String, Integer> hpSelectTab = new HashMap<String, Integer>(){
        {
            put("0", R.drawable.icon_hp_home_select );
            put("1", R.drawable.icon_hp_mjmw_select );
            put("2", R.drawable.icon_hp_drink_select );
            put("3", R.drawable.icon_hp_kitchen_select);
            put("4", R.drawable.icon_hp_daily_select);
            put("5", R.drawable.icon_hp_food_select);
            put("6", R.drawable.icon_hp_luxury_select);
            put("7", R.drawable.icon_hp_beauty_select);


            put("酒水饮料", icon_hp_drink_select);
            put("厨房用具", icon_hp_kitchen_select);
            put("生活百货", icon_hp_daily_select);
            put("食品保健", R.drawable.icon_hp_food_select);
            put("轻奢精选", R.drawable.icon_hp_luxury_select);
            put("美容美妆", R.drawable.icon_hp_beauty_select);
            put("家居家纺", R.drawable.icon_hp_home_select);
            put("添置新家", R.drawable.icon_hp_home_select);
            put("母婴儿童", R.drawable.icon_hp_baby_select);
            put("每家必备", R.drawable.icon_hp_mjmw_select);
            put("其他", R.drawable.bg_null);

        }
    };

    public static Map<String, Integer> hpSelectTextTab = new HashMap<String, Integer>(){
        {
            put("0", R.drawable.select_first);
            put("1", R.drawable.select_second );
            put("2", R.drawable.select_three);
            put("3", R.drawable.select_four);
            put("4", R.drawable.select_five);
            put("5", R.drawable.select_six);
            put("6", R.drawable.select_seven);
            put("7", R.drawable.select_eight);
            put("其他", R.drawable.bg_null);

        }
    };

    public static Map<String, Integer> hpSelectedTextTab = new HashMap<String, Integer>(){
        {
            put("0", R.drawable.selected_first);
            put("1", R.drawable.selected_two );
            put("2", R.drawable.selected_three);
            put("3", R.drawable.selected_four);
            put("4", R.drawable.selected_five);
            put("5", R.drawable.selected_six);
            put("6", R.drawable.selected_seven);
            put("7", R.drawable.selected_eight);
            put("其他", R.drawable.bg_null);

        }
    };

    public static Map<String, Integer> hpUnSelectTab = new HashMap<String, Integer>(){
        {
            put("0", R.drawable.icon_hp_home );
            put("1", R.drawable.icon_hp_mjmw );
            put("2", R.drawable.icon_hp_drink);
            put("3", R.drawable.icon_hp_kitchen);
            put("4", R.drawable.icon_hp_daily);
            put("5", R.drawable.icon_hp_food);
            put("6", R.drawable.icon_hp_luxury);
            put("7", R.drawable.icon_hp_beauty);

            put("酒水饮料", icon_hp_drink);
            put("厨房用具", icon_hp_kitchen);
            put("生活百货", R.drawable.icon_hp_daily);
            put("食品保健", R.drawable.icon_hp_food);
            put("轻奢精选", R.drawable.icon_hp_luxury);
            put("美容美妆", R.drawable.icon_hp_beauty);
            put("家居家纺", R.drawable.icon_hp_home);
            put("添置新家", R.drawable.icon_hp_home);
            put("母婴儿童", R.drawable.icon_hp_baby);
            put("其他", R.drawable.bg_null);
        }
    };


}
