function hikvision() {
}

hikvision.prototype.loginDvr = function (successCallback, errorCallback, wifi, password) {
    cordova.exec(successCallback, errorCallback, "hikvision", "loginDvr", [wifi, password]);
};
hikvision.prototype.startMonitor = function (successCallback, errorCallback, wifi, password, ip) {
    cordova.exec(successCallback, errorCallback, "hikvision", "startMonitor", [wifi, password, ip]);
};

hikvision.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.hikvision = new hikvision();

    return window.plugins.hikvision;
};

cordova.addConstructor(hikvision.install);