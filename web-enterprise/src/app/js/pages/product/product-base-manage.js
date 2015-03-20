(function () {
  var app = angular.module("productBaseManage", ["interceptor"]);

  app.factory("productBaseManageService", ["$http", function ($http) {
    return {
      getProductBases: function (fnSuccess) {
        $http.get("/api/product/base").success(function (data) {
          fnSuccess(data);
        });
        return this;
      }
    };
  }]);

  app.controller("productBaseManageCtrl", ["$scope", "productBaseManageService", function ($scope, productBaseManageService) {

  }]);
})();
