package com.thlh.baselib.config;

public class Constants {
	public static final String API_HEADER = "x-mjmw365-token";
	//支付宝支付参数
	public static final String ALI_RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAO2S1EWP2agXyjOvWpVMHFar6Suu8E6Rfr1wR70wSO5ZOmx6iYx/WWxgZwIe2Yratp2CCW1OwlnwAq6W+V6KSd0dYoB4lv5bYshLY85m+GpaVqc1MUTyonboFWvBGhE761yMXrSbuje8ZQahmW16nVgEWBPaf4xMgAx2j6FAIRKpAgMBAAECgYEApv2YO7Ubdy4RsTbG4z52hO2dnlj/DRr0DVCI/JCeEvuhN3MCkEFLqAQzI6hvnUyTJC+tVKoRroooOHRioHNv6qO9WMFNjkxrg9a2krEQidvkUjQ8opiZMD6mUjprY4cXe+BBe0oNoCgGrJ1vSz4rreIZybs4/vtgwalluvzA58kCQQD/emeo1bAUxua5n5qFdNIC8+8eKoV0/+dxQwqe/nbCwuvBYLjV4Fv/E0kvZRZVH5ZlG62QFYgxYfUjw3NRALPrAkEA7g8Pwr/aG3mAqTGHL56tC5V0yeMcPtQGvA/ZSbBLF0c2E5h0hfLgO1HTS+055WZktMQ7DLmETzlFeE0d0L1yuwJACIt/jBYZnZhJf0vnTy9pFtAx30Bs1kz44MDtSgQY1MrQN9eUVxL6AL4UZJFs/cvgq+ERHRCbTYuJN3kCEmODoQJAYy/vVytsisoDK3uxYcvxOcRZ1sxDtoHNFIpd9G/5uYc4Js38rtrVRqH/McFzv/r7tr2UBOc/E2s/YLJw28ozhQJAB9h5QvYTJukR/Ohl1U+CqEyE1vb/kGEcneCQYXWGmnzX/x6Ew7go+J9abfiB60aD15ILrw803wYfVwMYCV7jag==";
	public static final String ALI_PARTNER = "2088421396417380";  //合作者id
	public static final String ALI_SELLER = "tech@mail.thlhchina.com";//卖家支付宝ID
	public static final String ALI_NOTIFY_URL = "Notify/alipay";
	public static final String ALI_CLOSETIME = "60m";  //超时关闭时间
	//微信支付参数
	public static final String WECHAT_APP_ID = "wxa1a34eeee13e9a53";
	public static final String WECHAT_MCH_ID = "1370241002";
	public static final String WECHAT_API_KEY ="E71D2BC53942E47BBF1CB87AD78ACECB";
	public static final String WECHAT_NOTIFY_URL = "Notify/wechat";

	//支付目的 1下单 2钱包充值 3美家币充值
	public static final String PAY_PURPOSE_ORDER = "1";
	public static final String PAY_PURPOSE_BALANCE = "2";
	public static final String PAY_PURPOSE_MJB = "3";

	// 支付方式 1:美家币支付 2:余额支付 3:微信 4:支付宝 5:银联 6:货到付款 7:积分换购
	public static final String PAY_TYPE_MJB = "1";
	public static final String PAY_TYPE_BALANCE = "2";
	public static final String PAY_TYPE_WECHAT = "3";
	public static final String PAY_TYPE_ALIPAY = "4";
	public static final String PAY_TYPE_BANK = "5";
	public static final String PAY_TYPE_OFFLINE = "6";
	public static final String PAY_TYPE_EXCHANGE= "7";

	//列表页信息
	public static final int PageDataCount = 20;
	public static final int LogsDataCount = 50;
	public static final int IceBoxScreenPageCount = 50;
	public static final String SimilarDataCount = "3";
	public static final String LOCAL_IMAGE_PATH = "content://media/external/images/media/";


	//北京及天津的充值送冰箱价格
	public static final int ICEBOX_PRICE = 4500;
	//其他城市充值送冰箱价格----6999
	public static final int RECHARGE_SENDICEBOX_PRICE = 4500;
	//深圳代理商充值价格----7999
	public static final int RECHARGE_SENDICEBOX_PRICE_SZ = 4500;

	//登录类型
	public  static final int LOGIN_TYPE_NORMAL = 0;
	public  static final int LOGIN_TYPE_WECHAT = 1;
	//绑定手机类型  0普通  1 通过手机修改绑定  2通过密码修改绑定
	public  static final int BINDPHONE_TYPE_NORMAL = 0;
	public  static final int BINDPHONE_TYPE_BYPHONE = 1;
	public  static final int BINDPHONE_TYPE_BYPW = 2;



	//充值记录
	public  static final String RECORD_FROM_ALL = "0";//0:全部=default 1:美家钻 2:余额
	public  static final String RECORD_FROM_MJB = "1";
	public  static final String RECORD_FROM_BAlANCE = "2";
	public  static final String RECORD_TYPE_All = "A"; //A:全部=default R:收入, E:支出
	public  static final String RECORD_TYPE_RECHARGE = "R";
	public  static final String RECORD_TYPE_CONSUME = "E";

	//优惠券 列表 FLAG
	public static final String COUPON_FLAG_UNUSE = "0";
	public static final String COUPON_FLAG_USED = "1";
	public static final String COUPON_FLAG_NEARTIME = "2";

	//优惠券  FLAG
	public static final String COUPON_UNUSE = "0";
	public static final String COUPON_USED = "1";

	public static final int ORDER_TYPE_ALL = 0; // 0:全部订单 1:待付款，2：待发货 3：待收货 5:待评价 6:已评价 7:已完成 8:已取消 9未完成
	public static final int ORDER_TYPE_WAIT_PAY = 1;
	public static final int ORDER_TYPE_WAIT_SENDOUT = 2;
	public static final int ORDER_TYPE_WAIT_GAIN = 3;
	public static final int ORDER_TYPE_WAIT_COMMENT = 5;
	public static final int ORDER_TYPE_HAS_COMMENT = 6;
	public static final int ORDER_TYPE_COMPLETE = 7;
	public static final int ORDER_TYPE_CANCEL = 8;
	public static final int ORDER_TYPE_UNCOMPLETE = 9;


	public static final String ORDER_TEXT_ALL = "全部订单"; // 0:全部订单 1:待付款，2：待发货 3：待收货 5:待评价 6:已评价 7:已完成 8:已取消 9:未完成
	public static final String ORDER_TEXT_WAIT_PAY = "待付款";
	public static final String ORDER_TEXT_WAIT_SENDOUT = "待发货";
	public static final String ORDER_TEXT_WAIT_GAIN = "待收货";
	public static final String ORDER_TEXT_WAIT_COMMENT = "待评价";
	public static final String ORDER_TEXT_HAS_COMMENT = "已评价";
	public static final String ORDER_TEXT_COMPLETE = "已完成";
	public static final String ORDER_TEXT_CANCEL = "已取消";
	public static final String ORDER_TEXT_UNCOMPLETE = "未完成";



	//冰箱功能  0节能 1 自动 2假日 3锁定   预定义 0:未设置 1:速冷 2:速冻 3:智能 4:锁定
	public static final String ICEBOX_FUNCTION_NONE = "0";
	public static final String ICEBOX_FUNCTION_COLD = "1";
	public static final String ICEBOX_FUNCTION_FREEZE = "2";
	public static final String ICEBOX_FUNCTION_AUTO = "3";
	public static final String ICEBOX_FUNCTION_LOCK = "4";

	//评论界级别 grade0:全部 1:好评 2:中评 3:差评 4图片
	public static final int COMMENT_GRADE_ALL = 0;
	public static final int COMMENT_GRADE_GOOD = 1;
	public static final int COMMENT_GRADE_NORMAL = 2;
	public static final int COMMENT_GRADE_BAD = 3;
	public static final int COMMENT_GRADE_PHOTO = 4;

	//筛选类别 1品牌 0店铺
	public static final int FILTER_TYPE_SUPPLIER = 0;
	public static final int FILTER_TYPE_BRAND = 1;

	//搜索类别  1 用catid搜索  0 用keyword搜索
	public static final int SEARCH_TYPE_CAT = 1;
	public static final int SEARCH_TYPE_KEYWORD = 0;

	//相册选择类型  0 单选 1 多选
	public static final int ALBUM_SELECT_SINGLE = 0;
	public static final int ALBUM_SELECT_MUTIPLE= 1;

	//回调页的返回
	public static final String RESPONSE_BACK_TYPE_HOME = "0";
	public static final String RESPONSE_BACK_TYPE_PAYPW = "1"; //回到支付

	//密码页相关
	public static final int PAYPW_TYPE_NEW = 2;//设置支付密码
	public static final int PAYPW_TYPE_ORDERCONFIRM_PAY = 4;//支付密码 订单确认页支付
	public static final int PAYPW_TYPE_ORDER_PAY = 5;//支付密码 代付款页支付
	//手机验证码相关
	public static final int PHONECODE_TYPE_PWRESET = 0; //重置密码
	public static final int PHONECODE_TYPE_BINDPHONE = 1;//绑定手机
	public static final int PHONECODE_TYPE_RESET = 3;//重置支付密码
	public static final int PHONECODE_TYPE_CLOSE_PAYPW = 6;//重置支付密码



	//选择商品支付美家币相关
	public static final int MJBSELECT_TYPE_ORDER_CONFIRM = 1; //订单确认页
	public static final int MJBSELECT_TYPE_ORDER_PAY = 2;//待支付页



	//界面编号
	public static final int ACTIVITYCODE_GUIDE = 1; //引导
	public static final int ACTIVITYCODE_LOGIN = 2;//登录
	public static final int ACTIVITYCODE_INDEX = 3; //主页
	public static final int ACTIVITYCODE_SCANNQR = 4; //扫描二维码




}
