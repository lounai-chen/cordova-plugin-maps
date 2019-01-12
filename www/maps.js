var exec = require('cordova/exec');

var maps = {
    //打开第三方地图：我的位置
    open:function(success,error){
        exec(success, error, 'Maps', 'open', []);
    },
    //GPS导航  arg0：经度  arg1：纬度
    gps:function(success,error,arg0,arg1){
        exec(success, error, 'Maps', 'gps', [arg0,arg1]);
    }
    // 获取我当前的位置 IOS，返回GPS
    getMyLocation:function(success,error){
        exec(success, error, 'Maps', 'gps', []);
    }
}

module.exports = maps
