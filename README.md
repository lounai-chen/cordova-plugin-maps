### 前言
这是一个可以打开高德地图和百度地图的cordova插件。
支持的功能有GPS驾车导航、获取我当前的位置。
### 参考
高德地图开放平台的地址：https://lbs.amap.com/api/amap-mobile/guide/android/navigation

百度地图开放平台的地址：http://lbsyun.baidu.com/index.php?title=uri/api/android

### 支持平台   
ios  
android

### 安装
在线npm安装  

   cordova plugin add  cordova-plugin-maps  
   
在线url安装:
cordova plugin add
https://gitlab.com/zzl_public/cordova-plugin-maps.git
   



### 使用例子

  // 导航
  
  
```
Maps.gps(function(t){alert(t)},function(r){alert(r)}, "116.31088","39.99281");
```

  
  // 我的位置
  

```
Maps.open(function(t){alert(t)},function(r){alert(r)})
```

  
 
 


   
