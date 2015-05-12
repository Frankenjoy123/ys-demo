(function () {
    var app = angular.module("root", [
        "ngRoute",
        "interceptor",
        "head",
        "nav",
        "accountManage",
        "productBaseManage",
        "productKeyManage",
        "msg",
        "logistics",
        "logisticsManage",
        "package",
        "packageSearch",
        "ngAnimate",
        "config"
    ]);

    app.config(["$routeProvider", function ($routeProvider) {
        $routeProvider
            .when('/account', {
                templateUrl: "obsolete/pages/account/manage.html",
                controller: "accountManageCtrl"
            })
            .when('/product-base-manage', {
                templateUrl: "obsolete/pages/product/product-base-manage.html",
                controller: "productBaseManageCtrl"
            })
            .when('/product-key-manage', {
                templateUrl: "obsolete/pages/product/product-key-manage.html",
                controller: "productKeyManageCtrl"
            })
            .when('/msg', {
                templateUrl: "obsolete/pages/msg/msg.html",
                controller: "msgCtrl"
            })
            .when('/test', {
                templateUrl: "obsolete/pages/empty.html"
            })
            .when('/packageManage', {
                templateUrl: "obsolete/pages/package/packageManage.html",
                controller: "packageCtrl"
            })
            .when('/packageSearch', {
                templateUrl: "obsolete/pages/package/packageSearch.html",
                controller: "packageSearchCtrl"
            })
            .when('/logistics', {
                templateUrl: "obsolete/pages/logistics/logistics.html",
                controller: "logisticsManageCtrl"
            })
            .when('/search', {
                templateUrl: "obsolete/pages/search/search.html"
            })
            .when('/config', {
                templateUrl: "obsolete/pages/config/config.html",
                controller: "configCtrl"
            })
            .otherwise({
                templateUrl: "obsolete/pages/product/product-key-manage.html",
                controller: "productKeyManageCtrl"
            });
    }]);

    app.controller("rootCtrl", ["$scope", "$timeout", "$http", function ($scope, $timeout, $http) {
        if (!$.cookie(YUNSOO_CONFIG.AUTH_COOKIE_NAME)) {
            //todo
            window.location.href = "obsolete/login.html";
        }

        $scope.logout = function () {
            console.log('[logout]');
            $.removeCookie(YUNSOO_CONFIG.AUTH_COOKIE_NAME, {path: '/'});
            window.location.href = "obsolete/login.html";
        };

        $http.get("/api/account/current")
            .success(function (data) {
                $scope.account = data;
            }).error(function (data, state) {
            });

        $scope.alertMsgs = [];
        function getMsgIndex(msgs, msg, level) {
            var index = -1;
            for (var i = 0; i < msgs.length; i++) {
                var item = msgs[i];
                if (item.level == level && item.message == msg) {
                    index = i;
                    break;
                }
            }
            return index;
        }

        $scope.addAlertMsg = function (msg, level, autoHide) {
            $scope.alertMsgs.push({
                level: level,
                message: msg
            });
            if (autoHide) {
                var index = getMsgIndex($scope.alertMsgs, msg, level);
                $timeout((function (i) {
                    return function (i) {
                        $scope.alertMsgs.splice(i, 1);
                    };
                })(index), 3 * 1000);
            }
        };

        //This datetime format method is global, other pages will also use this method
        $scope.formatDateString = function (value) {
            return new DateTime(new Date(value)).toString('yyyy-MM-dd HH:mm:ss');
        };

        $scope.currentContext = {};
    }]);
})();
