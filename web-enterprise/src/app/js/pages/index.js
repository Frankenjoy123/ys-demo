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
                templateUrl: "pages/account/manage.html",
                controller: "accountManageCtrl"
            })
            .when('/product-base-manage', {
                templateUrl: "pages/product/product-base-manage.html",
                controller: "productBaseManageCtrl"
            })
            .when('/product-key-manage', {
                templateUrl: "pages/product/product-key-manage.html",
                controller: "productKeyManageCtrl"
            })
            .when('/msg', {
                templateUrl: "pages/msg/msg.html",
                controller: "msgCtrl"
            })
            .when('/test', {
                templateUrl: "pages/empty.html"
            })
            .when('/packageManage', {
                templateUrl: "pages/package/packageManage.html",
                controller: "packageCtrl"
            })
            .when('/packageSearch', {
                templateUrl: "pages/package/packageSearch.html",
                controller: "packageSearchCtrl"
            })
            .when('/logistics', {
                templateUrl: "pages/logistics/logistics.html",
                controller: "logisticsManageCtrl"
            })
            .when('/search', {
                templateUrl: "pages/search/search.html"
            })
            .when('/config', {
                templateUrl: "pages/config/config.html",
                controller: "configCtrl"
            })
            .otherwise({
                redirectTo: "/"
            });
    }]);

    app.controller("rootCtrl", ["$scope", "$timeout", "$http", function ($scope, $timeout, $http) {
        if (!$.cookie(YUNSOO_CONFIG.AUTH_COOKIE_NAME)) {
            //todo
            window.location.href = "login.html";
        }

        $scope.logout = function () {
            console.log('[logout]');
            $.removeCookie(YUNSOO_CONFIG.AUTH_COOKIE_NAME, {path: '/'});
            window.location.href = "login.html";
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

        $scope.formatDateString = function (value) {
            return new DateTime(new Date(value)).toString('yyyy-MM-dd HH:mm:ss');
        };

        $scope.currentContext = {};
    }]);
})();
