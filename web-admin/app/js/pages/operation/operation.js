(function () {
    var app = angular.module('root');

    app.factory("operationService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/logistics/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }
        };
    }]);

    app.controller("OperationCtrl", ["$scope", "operationService", function ($scope, operationService) {

        $scope.productKey = "";

        $scope.bodyShow = 0;

        $scope.packageClick = function () {

            if ($scope.productKey == null || $scope.productKey == "") {
                $scope.bodyShow = 0;
                return;
            }

            logisticsService.getInfo($scope.productKey, function (data) {
                $scope.bodyShow = 1;
                $scope.data = data;
            });
        }
    }]);
})();