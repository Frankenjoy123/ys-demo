(function () {
  var app = angular.module('root');

  app.factory("productViewService", ["$http", "productBaseDataService", function ($http, productBaseDataService) {

    return {
      getProDetails: function (fnSuccess, fnError) {
        $http.get("/api/productbase/" + productBaseDataService.get()).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("productViewCtrl", ["$scope", "productViewService", "$location", function ($scope, productViewService, $location) {
    productViewService.getProDetails(function (data) {

    }, function () {
    });

    $scope.return = function () {
      $location.path('/product-base-manage');
    }
  }]);
})();