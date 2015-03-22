(function () {
  var app = angular.module("productBaseManage", ["interceptor"]);

  app.factory("productBaseManageService", ["$http", function ($http) {
    return {
      getProductBases: function (fnSuccess) {
        $http.get("/api/productbase").success(function (data) {
          fnSuccess(data);
        });
        return this;
      }
    };
  }]);

  app.controller("productBaseManageCtrl", ["$scope", "productBaseManageService", function ($scope, productBaseManageService) {

    $scope.getDateString = function (value) {
      return new DateTime(new Date(value)).toString('yyyy-MM-dd HH:mm:ss');
    };

    //init
    productBaseManageService.getProductBases(function (data) {
      $scope.productBases = data;
    });
  }]);
})();
