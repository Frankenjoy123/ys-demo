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
        "ngAnimate"
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
            .otherwise({
                redirectTo: "/"
            });
    }]);

    app.controller("rootCtrl", ["$scope", "$timeout", function ($scope, $timeout) {
        if (!$.cookie(window.YUNSOO_CONFIG.AUTH_COOKIE_NAME)) {
            //todo
            window.location.href = "login.html";
        }
        $scope.user = {
            name: "Jane Doe",
            pic: "img/avatar3.png",
            status: "online",
            title: "支持工程师",
            since: "2015-03-01"
        };
        $scope.logout = function () {
            console.log('[logout]');
            $.removeCookie(window.YUNSOO_CONFIG.AUTH_COOKIE_NAME, {path: '/'});
            window.location.href = "login.html";
        };

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
        }
    }]);
})();
