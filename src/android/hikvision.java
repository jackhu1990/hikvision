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
import android.widget.Toast;


public class hikvision extends CordovaPlugin {
    private CallbackContext context;
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        this.context = callbackContext;

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
            Context context = this.cordova.getActivity().getApplicationContext();

            //通过Intent绑定将要调用的Activity
            Intent intent = new Intent(this.cordova.getActivity(), DemoActivity.class);
            //加入将要传输到activity中的参数
            intent.putExtra("province", args.getString(0));
            //启动activity
            //this.cordova.startActivityForResult(this, intent, 0);
            this.cordova.getActivity().startActivity(intent); // 启动Activity

            /*
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);// 设置Intent Action属性
            intent.setType("vnd.android.cursor.item/phone");// 设置Intent Type 属性
            //主要是获取通讯录的内容
            this.cordova.getActivity().startActivity(intent); // 启动Activity
            Toast.makeText(context, "默认Toast样式",
                    Toast.LENGTH_SHORT).show();
            */
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 根据resultCode判断处理结果
        if (resultCode == Activity.RESULT_OK) {
            String spot = intent.getStringExtra("spot");
            context.success(spot);
        }
    }
}