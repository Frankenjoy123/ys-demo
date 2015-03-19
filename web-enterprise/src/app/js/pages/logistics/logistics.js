(function () {
    var app = angular.module("logistics", ["interceptor"]);

    app.factory("logisticsService", ["$http", function ($http) {
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

    app.controller("logisticsCtrl", ["$scope", "logisticsService", function ($scope, logisticsService) {

        $scope.productKey = "";

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };

        $scope.productKeyClick = function () {
            logisticsService.getInfo($scope.productKey, function (data) {
                $scope.data = data;
            });
        }
    }]);
})();