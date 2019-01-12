//
//  CDVMaps.m
//
//  Created by chenlounai on 2018/11/28.
//

#import "CDVMaps.h"
#import <CoreLocation/CoreLocation.h>
@interface CDVMaps ()<CLLocationManagerDelegate>

@property (nonatomic , strong) CLLocationManager *locationManager;

@end
@implementation CDVMaps
    

    #pragma mark CoreLocation deleagte (定位失败)
/*定位失败则执行此代理方法*/
/*定位失败弹出提示窗，点击打开定位按钮 按钮，会打开系统设置，提示打开定位服务*/
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    /*设置提示提示用户打开定位服务*/
    UIAlertController * alert = [UIAlertController alertControllerWithTitle:@"允许\"定位\"提示" message:@"请在设置中打开定位" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction * ok =[UIAlertAction actionWithTitle:@"打开定位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        /*打开定位设置*/
        NSURL * settingsURL = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
        [[UIApplication sharedApplication]openURL:settingsURL];
    }];
    UIAlertAction * cacel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:ok];
    [alert addAction:cacel];
    [self presentViewController:alert animated:YES completion:nil];
}
/*定位成功后则执行此代理方法*/
#pragma mark 定位成功
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    [_locationManager stopUpdatingLocation];
    /*旧值*/
    CLLocation * currentLocation = [locations lastObject];
    CLGeocoder * geoCoder = [[CLGeocoder alloc]init];
    /*打印当前经纬度*/
    NSLog(@"当前经纬度：%f,%f",currentLocation.coordinate.latitude,currentLocation.coordinate.longitude);
    /*地理反编码 -- 可以根据地理位置（经纬度）确认位置信息 （街道、门牌）*/
    [geoCoder reverseGeocodeLocation:currentLocation completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        if (placemarks.count >0) {
            CLPlacemark * placeMark = placemarks[0];
            NSString  *currentCity = placeMark.locality;
            if (!currentCity) {
                currentCity = @"无法定位当前城市";
            }
            /*看需求定义一个全局变量来接受赋值*/
            NSLog(@"%@",placeMark.country);/*当前国家*/
            NSLog(@"当前城市%@",currentCity);/*当前城市*/
            NSLog(@"%@",placeMark.subLocality);/*当前位置*/
            NSLog(@"%@",placeMark.thoroughfare);/*当前街道*/
            NSLog(@"%@",placeMark.name);/*具体地址 ** 市 ** 区** 街道*/
            /*根据经纬度判断当前距离*/
            /*这个地方需要double转字符串赋值到label上面*/
//            self.headView.Distance.text =HZString(@"距您%.1fkm",[self getDistance:currentLocation.coordinate.latitude lng1:currentLocation.coordinate.longitude lat2:weiDouble lng2:jingDouble]);
        }
        else if (error == nil&&placemarks.count == 0){
            NSLog(@"没有地址返回");
        }
        else if (error){
            NSLog(@"location error:%@",error);
        }
    }];
}

// 获取我的位置
- (void) getMyLocation:(CDVInvokedUrlCommand*)command
{
        //创建位置管理器（定位用户的位置）
    if([CLLocationManager locationServicesEnabled]){
        self.locationManager=[[CLLocationManager alloc]init];
        //2.设置代理
        self.locationManager.delegate=self;
        [self.locationManager requestAlwaysAuthorization];
        NSString *currentCity = [NSString new];
        [self.locationManager requestWhenInUseAuthorization];
        
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
        self.locationManager.distanceFilter = 5.0;
        [self.locationManager startUpdatingLocation];
    }

}

    
// 打开地图 -- 我的位置
- (void)open:(CDVInvokedUrlCommand*)command
{
    NSLog(@"开始。。");
    // 高德地图
    NSURL *myLocationScheme = [NSURL URLWithString:@"iosamap://myLocation?sourceApplication=applicationName"]; 
    if ([[UIDevice currentDevice].systemVersion integerValue] >= 10) { //iOS10以后,使用新API 
        [[UIApplication sharedApplication] openURL:myLocationScheme options:@{} completionHandler:^(BOOL success) { NSLog(@"scheme调用结束"); }]; 
    } else { //iOS10以前,使用旧API 
        [[UIApplication sharedApplication] openURL:myLocationScheme]; 
    }
}
 

// 导航
- (void)gps:(CDVInvokedUrlCommand*)command
{
    // 获取传来的参数
    NSString *urlsting = [command.arguments objectAtIndex:0];
    NSLog(@"开始导航。。");
    NSLog(urlsting);
 
   @try{
        if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"iosamap://"]]) {
            // 高德地图
            //NSString *tep = @"iosamap://navi?sourceApplication=applicationName&poiname=fangheng&poiid=BGVIS&lat=22.537422&lon=113.975095&dev=0&style=2";
            //NSString *urlsting =[[NSString stringWithFormat:@"iosamap://navi?sourceApplication=applicationName&poiname=fangheng&poiid=BGVIS&lat=%.6f&lon=%.6f&dev=0&style=2",d_latitude,d_longitude]stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            //NSLog(urlsting);
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlsting] options:nil completionHandler:^(BOOL success) {
                    NSLog(@"scheme调用高德地图结束");    
            }];
        } 
//        else if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"baidumap://"]]) {
//            // 百度地图
//            NSString *urlString = [[NSString stringWithFormat:@"baidumap://map/direction?region=beijing&origin=%@,%@&destination=西直门&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo", latitude, longitude] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//            NSURL *url = [NSURL URLWithString:urlString];
//            [[UIApplication sharedApplication] openURL:url options:nil completionHandler:^(BOOL success) {
//                NSLog(@"scheme调用百度地图结束");
//            }];
    //    }
        else{
            //没有安装地图APP
            
            CDVPluginResult*pluginResult =[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"建议安装高德地图APP"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

        }

    } 
    @catch(NSException *ex){
      NSLog(@"异常");
      NSLog(@"%s\n%@",__FUNCTION__,ex);
    }
    @finally {
        //最新版IOS12.1的canOpenURL判断有误， 再试一次高德地图，却可以打开
//        NSString *urlsting =[[NSString stringWithFormat:@"iosamap://navi?sourceApplication=applicationName&poiname=fangheng&poiid=BGVIS&lat=%@&lon=%@&dev=1&style=2",latitude,longitude]stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlsting] options:nil completionHandler:^(BOOL success) {
             NSLog(@"scheme调用高德地图结束");    
        }];
    }


     
} 

@end
