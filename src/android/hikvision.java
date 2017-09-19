package com.nodehope.cordova.plugins.hikvision;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.test.demo.*;


public class hikvision extends CordovaPlugin {
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        Context context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("loginDvr")) {
            String ssid = args.getString(0);
            String password = args.getString(1);
            //设置要配置的ssid 和pswd
            try {
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        } else if (action.equals("startMonitor")) {
            //通过Intent绑定将要调用的Activity
            Intent intent = new Intent(this.cordova.getActivity(), DemoActivity.class);
            //加入将要传输到activity中的参数
            intent.putExtra("province", args.getString(0));
            //启动activity
            this.cordova.startActivityForResult(this, intent, 0);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 根据resultCode判断处理结果
        Context context = this.cordova.getActivity().getApplicationContext();
        if (resultCode == Activity.RESULT_OK) {
            String spot = intent.getStringExtra("spot");
            context.success(spot);
        }
    }
}