package org.apache.cordova.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.util.Log;
import android.widget.Toast;

import android.content.Intent;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import java.io.File;
import android.os.Build;
import java.net.URISyntaxException;

import android.app.Activity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

//import android.support.v7.app.AppCompatActivity;

//import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * maps
 * 
 */
public class Maps extends CordovaPlugin {
	double mEndLng = 116.31088; // 经 度
    double mEndLat = 39.99281;  // 纬 度
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION };
    private CallbackContext BleButtonCallbackContext = null;
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
//		   else  if (isInstallByread("com.baidu.BaiduMap")) {
//			   //百度
//               Intent intent = new Intent("android.intent.action.VIEW",
//                       android.net.Uri.parse("baidumap://map?src=andr.baidu.openAPIdemo"));
//               cordova.startActivityForResult(null,intent,1); // 启动调用
//		   }
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
                        android.net.Uri.parse("androidamap://navi?sourceApplication=SmartVoice&lat=" + mEndLat + "&lon=" + mEndLng + "&dev=0&style=2"));
                intent.setPackage("com.autonavi.minimap");
                if (isInstallByread("com.autonavi.minimap")) {
                    cordova.startActivityForResult(null,intent,1); // 启动调用
                }
//                else {
//                    startBaiduMap();
//                }
            } else{ 
				Toast.makeText(cordova.getContext(), "终点坐标不明确，请确认", Toast.LENGTH_SHORT).show();
            } 
            //Toast.makeText(cordova.getContext(),"成功",Toast.LENGTH_SHORT).show();
            return true;
        }
        // 获取GPS
        else if ("getMyLocation".equals(action)) {
            this.BleButtonCallbackContext = callbackContext;
            showGPSContacts();
            getLocation();
        }
        return false;
    }

    /**
     * 检测GPS、位置权限是否开启
     */
    public void showGPSContacts() {
        LocationManager    lm = (LocationManager) cordova.getContext().getSystemService(cordova.getContext().LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(cordova.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(cordova.getActivity(), LOCATIONGPS,
                            BAIDU_READ_PHONE_STATE);
                } else {
                    getLocation();//getLocation为定位方法
                }
            } else {
                getLocation();//getLocation为定位方法
            }
        } else {
            Toast.makeText(cordova.getContext(), "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(intent, PRIVATE_CODE);
        }
    }

    /**
     * 获取具体位置的经纬度
     */
    private void getLocation() {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = cordova.getContext().LOCATION_SERVICE;
        locationManager = (LocationManager) cordova.getContext().getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        /**这段代码不需要深究，是locationManager.getLastKnownLocation(provider)自动生成的，不加会出错**/
        if (ActivityCompat.checkSelfPermission(cordova.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(cordova.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        updateLocation(location);
    }

    /**
     * 获取到当前位置的经纬度
     * @param location
     */
    private void updateLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.e(null,"维度：" + latitude + "\n经度" + longitude);
            sendUpdate(longitude+","+ latitude ,true);
        } else {
            Log.e(null,"无法获取到位置信息");
        }
    }

    private void sendUpdate(String message, boolean keepCallback) {
        if (this.BleButtonCallbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, message);
            result.setKeepCallback(keepCallback);
            this.BleButtonCallbackContext.sendPluginResult(result);
        }
    }
    /**
     * Android6.0申请权限的回调方法
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
//            case BAIDU_READ_PHONE_STATE:
//                //如果用户取消，permissions可能为null.
//                if (grantResults[0] == PERMISSION_GRANTED && grantResults.length > 0) {  //有权限
//                    // 获取到权限，作相应处理
//                    getLocation();
//                } else {
//                    showGPSContacts();
//                }
//                break;
//            default:
//                break;
//        }
//    }

	
	//移动APP调起Android百度地图方式
    private void startBaiduMap() {
        Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse("intent://map/navi?location="+mEndLat+","+mEndLng +
                            "&type=TIME&src=thirdapp.navi.hndist.sydt#Intent;scheme=bdapp;" +
                            "package=com.baidu.BaiduMap;end"));
        if (isInstallByread("com.baidu.BaiduMap")) {
            cordova.startActivityForResult(null,intent,1); // 启动调用
        } else { 
			Toast.makeText(cordova.getContext(), "没有安装高德客户端", Toast.LENGTH_SHORT).show();
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
