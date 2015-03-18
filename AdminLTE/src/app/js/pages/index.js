(function () {
    var app = angular.module("root", [
        "ngRoute",
        "interceptor",
        "head",
        "nav",
        "accountManage",
        "productKeyManage",
        "msg",
        "logistics"
    ]);

    app.config(["$routeProvider", function ($routeProvider) {
        $routeProvider
            .when('/account', {
                templateUrl: "pages/account/manage.html",
                controller: "accountManageCtrl"
            })
            .when('/productKeyManage', {
                templateUrl: "pages/product/keyManage.html",
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
                templateUrl: "pages/package/packageManage.html"

            })
            .when('/logistics', {
                templateUrl: "pages/logistics/logistics.html",
                controller: "logisticsCtrl"
            })
            .otherwise({
                redirectTo: "/"
            });
    }]);

    app.controller("rootCtrl", ["$scope", function ($scope) {
        $scope.user = {
            name: "Jane Doe",
            pic: "img/avatar3.png",
            status: "online",
            title: "Web Developer",
            since: "Nov. 2012"
        };
    }]);
})();