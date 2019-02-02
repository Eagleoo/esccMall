package com.yda.esccmall.util;

import android.content.Context;
import android.hardware.Camera;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * @className: CommonUtil
 * @classDescription: 通用工具类
 * @author: miao
 * @createTime: 2017年2月10日
 */
public class CommonUtil {

    /**
     * @author miao
     * @createTime 2017年2月10日
     * @lastModify 2017年2月10日
     * @param
     * @return
     */
    public static boolean isCameraCanUse() {
            boolean canUse = true;
            Camera mCamera = null;
            try {
                mCamera = Camera.open();
            } catch (Exception e) {
                canUse = false;
            }
            if (canUse) {
                if (mCamera != null)
                    mCamera.release();
                mCamera = null;
            }
            return canUse;
    }

    public static String getCurTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getMac(Context context){
        WifiManager wm = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.e("******MAC*****",wm.getConnectionInfo().getMacAddress()+" ********");
        return wm.getConnectionInfo().getMacAddress();
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

}
