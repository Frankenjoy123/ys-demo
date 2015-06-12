(function () {
    var app = angular.module('root');

    app.factory("dashboardService", ["$http", function ($http) {
        return {
            getProductKeyQuantitySum: function (fnSuccess) {
                $http.get('/api/productkeybatch/sum/quantity').success(fnSuccess);
            }
        };
    }]);

    app.controller("DashboardCtrl", ["$scope", "dashboardService", function ($scope, dashboardService) {
        $scope.productKeyQuantitySum = 0;
        //get quantity sum of product key batches
        dashboardService.getProductKeyQuantitySum(function (data) {
            $scope.productKeyQuantitySum = data;
        })
    }]);
})();