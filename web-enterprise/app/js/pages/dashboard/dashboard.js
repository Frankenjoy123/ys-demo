(function () {
    var app = angular.module('root');

    app.factory("dashboardService", ["$http", function ($http) {
        return {
            getProductKeyQuantitySum: function (fnSuccess) {
                $http.get('/api/productkeybatch/sum/quantity').success(fnSuccess);
            }
        };
    }]);

    app.controller("DashboardCtrl", ["$scope", "dashboardService", "$timeout", function ($scope, dashboardService, $timeout) {
        $scope.productKeyQuantitySum = 0;
        //get quantity sum of product key batches
        dashboardService.getProductKeyQuantitySum(function (data) {
            $scope.productKeyQuantitySum = data;
        })

        $timeout(function () {
            $('#maChart01').circliful();
            $('#maChart02').circliful();
            $('#maChart03').circliful();

        }, 0);

    }]);
})();