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
    private CallbackContext callbackContext;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        this.callbackContext = callbackContext;
        Context context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("startMonitor")) {
            try {
                //通过Intent绑定将要调用的Activity
                Intent intent = new Intent(this.cordova.getActivity(), DemoActivity.class);
                //加入将要传输到activity中的参数
                intent.putExtra("username", args.getString(0));
                intent.putExtra("password", args.getString(1));
                intent.putExtra("ip", args.getString(2));
                intent.putExtra("channel", args.getString(3));
                //启动activity
                this.cordova.startActivityForResult(this, intent, 0);
                //this.cordova.getActivity().startActivity(intent); // 启动Activity
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
        } else if (action.equals("stopMonitor")) {
            try {
                intent.putExtra("handler", args.getString(0));
                intent.putExtra("player", args.getString(1));
                //启动activity
                this.cordova.startActivityForResult(this, intent, 0);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
        } else if (action.equals("test")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);// 设置Intent Action属性
            intent.setType("vnd.android.cursor.item/phone");// 设置Intent Type 属性
            //主要是获取通讯录的内容
            this.cordova.getActivity().startActivity(intent); // 启动Activity
            Toast.makeText(context, "默认Toast样式",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 根据resultCode判断处理结果
        if (resultCode == Activity.RESULT_OK) {
            String spot = intent.getStringExtra("spot");
            callbackContext.success(spot);
        }
    }
}