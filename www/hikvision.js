function hikvision() {
}

/* 函数调用成功后返回登录drv句柄handler, 播放视频句柄player*/
hikvision.prototype.startMonitor = function (successCallback, errorCallback, username, password, ip, port, channel) {
    cordova.exec(successCallback, errorCallback, "hikvision", "startMonitor", [username, password, ip, port, channel]);
};
/* 使用登录句柄handler, 播放句柄player进行停止播放与退出dvr操作 */
hikvision.prototype.stopMonitor = function (successCallback, errorCallback, handler, player) {
    cordova.exec(successCallback, errorCallback, "hikvision", "stopMonitor", [handler, player]);
};

hikvision.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.hikvision = new hikvision();

    return window.plugins.hikvision;
};

cordova.addConstructor(hikvision.install);