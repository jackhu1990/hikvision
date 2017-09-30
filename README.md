##使用方式

###安装
```
cordova plugin add https://github.com/jackhu1990/hikvision.git
```
    
###代码使用
```
               $scope.testCordova = function () {
                    // username, password, ip, port, channel
                   window.plugins.hikvision.startMonitor(function (data) {
                       alert(data);
                   }, function (data) {
                       alert(data);
                   }, "username", "68008232", "192.168.1.1", "8000", "16")
               }
```

