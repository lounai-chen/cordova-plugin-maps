var exec = require('cordova/exec');

var maps = {
    //我的位置
    open:function(success,error){
        exec(success, error, 'Maps', 'open', []);
    },
    //GPS导航  arg0：经度  arg1：纬度
    gps:function(success,error,arg0,arg1){
        exec(success, error, 'Maps', 'gps', [arg0,arg1]);
    }
}

module.exports = maps