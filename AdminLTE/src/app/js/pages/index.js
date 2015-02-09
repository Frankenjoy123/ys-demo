(function () {
    var app = angular.module("root", [
        "ngRoute", 
        "interceptor",
        "head", 
        "nav",
        "a", 
        "b",
        "msg"
    ]);

    app.config(["$routeProvider", function ($routeProvider) {
            $routeProvider
                    .when('/a', {
                        templateUrl: "pages/a/a.html",
                        controller: "aCtrl"
                    })
                    .when('/b', {
                        templateUrl: "pages/b/b.html",
                        controller: "bCtrl"
                    })
                    .when('/msg', {
                        templateUrl: "pages/msg/msg.html",
                        controller: "msgCtrl"
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