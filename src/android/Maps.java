package org.apache.cordova.maps;

import android.widget.Toast;
import android.util.Log;
import android.widget.Toast;

import android.content.Intent;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import java.io.File;
import java.net.URISyntaxException;

import android.app.Activity;

//import android.support.v7.app.AppCompatActivity;

//import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * maps
 * 
 */
public class Maps extends CordovaPlugin {
	double mEndLng = 116.31088; // 经纬度 - 北京大学
    double mEndLat = 39.99281;
    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        // 打开我的位置
		if ("open".equals(action)){
		   if (isInstallByread("com.autonavi.minimap")) {
			   // 高德
			     Intent intent = new Intent("android.intent.action.VIEW",
                       android.net.Uri.parse("androidamap://myLocation?sourceApplication=softname"));
			   cordova.startActivityForResult(null,intent,1); // 启动调用
		   }
		   else  if (isInstallByread("com.baidu.BaiduMap")) {
			   //百度
               Intent intent = new Intent("android.intent.action.VIEW",
                       android.net.Uri.parse("baidumap://map?src=andr.baidu.openAPIdemo"));
               cordova.startActivityForResult(null,intent,1); // 启动调用
		   }
		   return true;
		}
		// GPS 导航
		else if ("gps".equals(action)){
            // 获取activity和context --> cordova.getActivity()和cordova.getContext()
			mEndLng = Double.parseDouble(args.getString(0));
			mEndLat = Double.parseDouble(args.getString(1));
			if (mEndLng != 0.0 && mEndLat !=0.0) {
                //移动APP调起Android高德地图方式
                Intent intent = new Intent("android.intent.action.VIEW",
                        android.net.Uri.parse("androidamap://navi?sourceApplication=ZZl地图&lat=" + mEndLat + "&lon=" + mEndLng + "&dev=0&style=2"));
                intent.setPackage("com.autonavi.minimap");
                if (isInstallByread("com.autonavi.minimap")) {
                    cordova.startActivityForResult(null,intent,1); // 启动调用
                } else {
                    startBaiduMap();
                }
            } else{ 
				Toast.makeText(cordova.getContext(), "终点坐标不明确，请确认", Toast.LENGTH_SHORT).show();
            } 
            //Toast.makeText(cordova.getContext(),"成功",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
	
	
	//移动APP调起Android百度地图方式
    private void startBaiduMap() {
        Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse("intent://map/navi?location="+mEndLat+","+mEndLng +
                            "&type=TIME&src=thirdapp.navi.hndist.sydt#Intent;scheme=bdapp;" +
                            "package=com.baidu.BaiduMap;end"));
        if (isInstallByread("com.baidu.BaiduMap")) {
            cordova.startActivityForResult(null,intent,1); // 启动调用
        } else { 
			Toast.makeText(cordova.getContext(), "没有安装高德/百度地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    //判断是否安装目标应用
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

}
