(function () {
    var app = angular.module("productBaseManage", ["interceptor"]);

    app.factory("productBaseManageService", ["$http", function ($http) {
        return {
            getProductBases: function (fnSuccess) {
                $http.get("/api/productbase").success(fnSuccess);
                return this;
            },
            getShelfLifeInterval: function (fnSuccess) {
                $http.get("mock/shelf_life_interval.json").success(fnSuccess);
                return this;
            }
        };
    }]);

    app.controller("productBaseManageCtrl", ["$scope", "productBaseManageService", function ($scope, productBaseManageService) {

        $scope.getDateString = function (value) {
            return new DateTime(new Date(value)).toString('yyyy-MM-dd HH:mm:ss');
        };

        $scope.formatProductKeyTypes = function (productKeyTypes) {
            var result = '';
            if (productKeyTypes) {
                $.each(productKeyTypes, function (i, item) {
                    result += item.name;
                    if (i < productKeyTypes.length - 1) {
                        result += ', ';
                    }
                });
            }
            return result;
        };

        //init
        productBaseManageService.getProductBases(function (data) {
            $scope.productBases = data;
        }).getShelfLifeInterval(function (data) {
            $scope.shelfLifeIntervals = data;
        });
    }]);
})();
