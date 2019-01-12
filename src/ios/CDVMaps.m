//
//  CDVMaps.m
//
//  Created by chenlounai on 2018/11/28.
//

#import "CDVMaps.h"
@implementation CDVMaps
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
