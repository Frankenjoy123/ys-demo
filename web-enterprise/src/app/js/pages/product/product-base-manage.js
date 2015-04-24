(function () {
    var app = angular.module("productBaseManage", ["interceptor"]);

    app.factory("productBaseManageService", ["$http", function ($http) {
        return {
            getProductBases: function (fnSuccess) {
                $http.get("/api/productbase").success(fnSuccess);
                return this;
            }
        };
    }]);

    app.controller("productBaseManageCtrl", ["$scope", "productBaseManageService", function ($scope, productBaseManageService) {
        $scope.shelfLifeIntervals = {
            "year": "年",
            "month": "月",
            "week": "周",
            "day": "天",
            "hour": "小时"
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
        });
    }]);
})();
