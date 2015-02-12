(function () {
  var app = angular.module("root", [
    "ngRoute",
    "interceptor",
    "head",
    "nav",
    "accountManage",
    "productKeyManage",
    "msg"
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