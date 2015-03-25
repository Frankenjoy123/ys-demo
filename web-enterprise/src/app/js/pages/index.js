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
        "packageSearch"
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
        $scope.user = {
            name: "Jane Doe",
            pic: "img/avatar3.png",
            status: "online",
            title: "Web Developer",
            since: "Nov. 2012"
        };
        $scope.alertMsgs = [];
        $scope.addAlertMsg = function (msg, level, autoHide) {
            $scope.alertMsgs.push({
                level: level,
                message: msg
            });
            var index = $scope.alertMsgs.length - 1;
            $timeout(function () {
                $scope.alertMsgs.slice(index, 1);
            }, 5000);
        }
    }]);
})();