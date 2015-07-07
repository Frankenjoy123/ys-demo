(function () {
    var app = angular.module('root');

    app.factory('productBaseManageService', ['$http', function ($http) {
        return {
            getProductBases: function (fnSuccess) {
                $http.get('/api/productbase').success(fnSuccess);
                return this;
            },
            getProductKeyCredits: function (fnSuccess) {
                $http.get('/api/productkeycredit').success(fnSuccess);
                return this;
            }
        };
    }]);

    app.controller('ProductBaseManageCtrl', ['$scope', 'productBaseManageService', function ($scope, productBaseManageService) {
        $scope.SHELFLIFE_INTERVALS = {
            'year': '年',
            'month': '月',
            'week': '周',
            'day': '天',
            'hour': '小时'
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

        $scope.formatComments = function (comments) {
            comments || (comments = '');
            return comments.length > 30 ? comments.substring(0, 30) + '...' : comments;
        };

        $scope.dataTable = new $scope.utils.DataTable({
            pageable: {
                page: 0,
                size: 20
            },
            flush: function (callback) {
                productBaseManageService.getProductBases(function (data, status, headers) {
                    if ($scope.productKeyCredits) {
                        setProductKeyCredits(data, $scope.productKeyCredits);
                    } else {
                        getProductKeyCredits(function (productKeyCredits) {
                            setProductKeyCredits(data, productKeyCredits);
                        });
                    }
                    callback({data: data, headers: headers});
                });
            }
        });

        function getProductKeyCredits(callback) {
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
                console.log('[productKeyCredits loaded]', productKeyCredits);
                callback(productKeyCredits);
            });
        }

        function setProductKeyCredits(productBases, productKeyCredits) {
            $.each(productBases, function (i, item) {
                if (item && productKeyCredits) {
                    var credit = item.credit = {total: 0, remain: 0, general: productKeyCredits.general};
                    credit.total += productKeyCredits.general.total;
                    credit.remain += productKeyCredits.general.remain;
                    if (productKeyCredits.creditMap[item.id]) {
                        credit.total += productKeyCredits.creditMap[item.id].total;
                        credit.remain += productKeyCredits.creditMap[item.id].remain;
                    }
                    credit.percentage = (credit.remain * 100 / credit.total) | 0;
                }
            });
        }

    }]);
})();
