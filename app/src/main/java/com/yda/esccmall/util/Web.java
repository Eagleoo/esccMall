package com.yda.esccmall.util;

/**
 * Created by Administrator on 2018/3/29.
 */

public class Web {

//    public static String token = "-1";

    //public static String url = "http://apptest.esccclub.com";//测试
    public static String url = "http://m.web20181211.demo.dnsrw.com";//

    public static String login = "/tools/submit_ajax.ashx?action=user_login&site_id=2";
    public static String autologin = "/autologin.aspx?site_id=2";
    //加上随机数
    public static String version = "/app/app_update_log.xml?i="+System.currentTimeMillis();



    public static String getIsProduction() {
        String IsProduction;
        if (url.equals("http://m.web20181211.demo.dnsrw.com")){
            IsProduction ="false";
        }
        else {
            IsProduction ="true";
        }
        return IsProduction;
    }


}



