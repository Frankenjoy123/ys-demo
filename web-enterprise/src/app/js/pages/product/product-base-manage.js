(function () {
    var app = angular.module("productBaseManage", ["interceptor"]);

    app.factory("productBaseManageService", ["$http", function ($http) {
        return {
            getProductBases: function (fnSuccess) {
                $http.get("/api/productbase").success(fnSuccess);
                return this;
            },
            getProductKeyCredits: function (fnSuccess) {
                $http.get("/api/productkeycredit").success(fnSuccess);
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
            //get product key credits
            productBaseManageService.getProductKeyCredits(function (data) {
                var productKeyCredits = {
                    general: {
                        total: 0,
                        remain: 0
                    },
                    creditMap: {}
                };
                $.each(data, function (i, item) {
                    if (item.product_base_id) {
                        productKeyCredits.creditMap[item.product_base_id] = {
                            total: item.total,
                            remain: item.remain
                        };
                    } else {
                        productKeyCredits.general = {
                            total: item.total,
                            remain: item.remain
                        };
                    }
                });
                $scope.productKeyCredits = productKeyCredits;
                console.log('[productKeyCredits loaded]: ', productKeyCredits);
                $.each($scope.productBases, function (i, item) {
                    setCredit(item, productKeyCredits);
                });
            });
        });


        function setCredit(productBase, productKeyCredits) {
            if (productBase && productKeyCredits) {
                var credit = productBase.credit = {total: 0, remain: 0, general: productKeyCredits.general};
                credit.total += productKeyCredits.general.total;
                credit.remain += productKeyCredits.general.remain;
                if (productKeyCredits.creditMap[productBase.id]) {
                    credit.total += productKeyCredits.creditMap[productBase.id].total;
                    credit.remain += productKeyCredits.creditMap[productBase.id].remain;
                }
            }
        }

    }]);
})();
