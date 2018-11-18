package com.mzw.appwidgetdemof.tools;

import java.text.SimpleDateFormat;

/**
 * 常量参数
 * Created by think on 2018/11/10.
 */

public class ConstantParameter {

    //mob api
    public static final String MOB_APP_KEY = "28b14e6fa499a";
    public static final String MOB_APP_SECRET = "4fb9cc73c4cbe5a17abe73da57ea9d66";
    //存储接口
    public static final String MOB_URL_PUT = "http://apicloud.mob.com/ucache/put";
    //查询接口
    public static final String MOB_URL_GET = "http://apicloud.mob.com/ucache/get";
    //集合名称，一个用户只能拥有5个自定义的table   特殊节日
    public static final String MOB_TABLE = "mzw_festival";
    public static final String birthday_k = "birthday";


    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    public static SimpleDateFormat sdf_a = new SimpleDateFormat("yyyy-MM-dd");

    // 做 比较使用  指定时间是否为今天   今月   今年
    public static SimpleDateFormat sdf_toyear = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat sdf_tomonth = new SimpleDateFormat("yyyyMM");
    public static SimpleDateFormat sdf_today = new SimpleDateFormat("yyyyMMdd");

    public static SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat sdf_month = new SimpleDateFormat("MM");
    public static SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
    public static SimpleDateFormat sdf_hhmm = new SimpleDateFormat("HHmm");//做通知 id 用


    public static final String itemLayout = "com.mzw.appwidgetdemob.itemLayout";
    public static final String itemLayout_a = "com.mzw.appwidgetdemob.itemLayout_a";
    public static final String itemLayout_b = "com.mzw.appwidgetdemob.itemLayout_b";
    public static final String itemLayout_c = "com.mzw.appwidgetdemob.itemLayout_c";
    public static final String itemLayout_d = "com.mzw.appwidgetdemob.itemLayout_d";
    public static final String itemLayout_e = "com.mzw.appwidgetdemob.itemLayout_e";
    public static final String itemLayout_f = "com.mzw.appwidgetdemob.itemLayout_f";
    public static final String itemLayout_g = "com.mzw.appwidgetdemob.itemLayout_g";
    public static final String itemLayout_h = "com.mzw.appwidgetdemob.itemLayout_h";
    public static final String itemLayout_i = "com.mzw.appwidgetdemob.itemLayout_i";
    public static final String itemLayout_j = "com.mzw.appwidgetdemob.itemLayout_j";

    public static final String itemLayout_k = "com.mzw.appwidgetdemob.itemLayout_k";
    public static final String itemLayout_l = "com.mzw.appwidgetdemob.itemLayout_l";
    public static final String itemLayout_m = "com.mzw.appwidgetdemob.itemLayout_m";
    public static final String itemLayout_n = "com.mzw.appwidgetdemob.itemLayout_n";
    public static final String itemLayout_o = "com.mzw.appwidgetdemob.itemLayout_o";
    public static final String itemLayout_p = "com.mzw.appwidgetdemob.itemLayout_p";
    public static final String itemLayout_q = "com.mzw.appwidgetdemob.itemLayout_q";
    public static final String itemLayout_r = "com.mzw.appwidgetdemob.itemLayout_r";
    public static final String itemLayout_s = "com.mzw.appwidgetdemob.itemLayout_s";
    public static final String itemLayout_t = "com.mzw.appwidgetdemob.itemLayout_t";

    public static final String itemLayout_u = "com.mzw.appwidgetdemob.itemLayout_u";
    public static final String itemLayout_v = "com.mzw.appwidgetdemob.itemLayout_v";
    public static final String itemLayout_w = "com.mzw.appwidgetdemob.itemLayout_w";
    public static final String itemLayout_x = "com.mzw.appwidgetdemob.itemLayout_x";
    public static final String itemLayout_y = "com.mzw.appwidgetdemob.itemLayout_y";
    public static final String itemLayout_z = "com.mzw.appwidgetdemob.itemLayout_z";
    public static final String itemLayout_aa = "com.mzw.appwidgetdemob.itemLayout_aa";
    public static final String itemLayout_ab = "com.mzw.appwidgetdemob.itemLayout_ab";
    public static final String itemLayout_ac = "com.mzw.appwidgetdemob.itemLayout_ac";
    public static final String itemLayout_ad = "com.mzw.appwidgetdemob.itemLayout_ad";

    public static final String itemLayout_ae = "com.mzw.appwidgetdemob.itemLayout_ae";
    public static final String itemLayout_af = "com.mzw.appwidgetdemob.itemLayout_af";
    public static final String itemLayout_ag = "com.mzw.appwidgetdemob.itemLayout_ag";
    public static final String itemLayout_ah = "com.mzw.appwidgetdemob.itemLayout_ah";
    public static final String itemLayout_ai = "com.mzw.appwidgetdemob.itemLayout_ai";

    public static final String month_previous = "com.mzw.appwidgetdemob.month_previous";
    public static final String month_next = "com.mzw.appwidgetdemob.month_next";

    //返回今天
    public static final String back_today = "com.mzw.appwidgetdemob.back_today";
    
}
