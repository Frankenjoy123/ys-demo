(function () {
    var app = angular.module("productKeyManage", ["interceptor"]);

    app.factory("productKeyManageService", ["$http", function ($http) {
        return {
            getProductKeyBatches: function (productBaseId, fnSuccess) {
                $http.get("/api/productkeybatch?productBaseId=" + productBaseId).success(function (data) {
                    fnSuccess(data);
                });
                return this;
            },
            getProductBases: function (fnSuccess) {
                $http.get("/api/productbase").success(fnSuccess);
                return this;
            },
            getProductKeyTypes: function (fnSuccess) {
                $http.get("/api/productkeytype").success(fnSuccess);
                return this;
            },
            createProductKeyBatch: function (request, fnSuccess, fnFail) {
                $http.post("/api/productkeybatch", request).success(fnSuccess).error(fnFail);
                return this;
            },
            downloadProductKeys: function (listPanel, batchId) {
                listPanel.downloadFrameSrc = '/api/productkeybatch/' + batchId + '/keys';
            }
        };
    }]);

    app.controller("productKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {

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

        $scope.creationPanel = {
            model: {
                productBaseId: 0,
                quantity: 0
            },
            create: function () {
                var model = this.model;
                console.log('[before productKeyBatch create]: ', model);
                var requestData = {
                    quantity: model.quantity,
                    productBaseId: model.productBaseId
                };
                var selectedProductBase = this.selectedProductBase;
                productKeyManageService.createProductKeyBatch(requestData, function (data) {
                    console.log('[newProductKeyBatch created]:', data);
                    $scope.addAlertMsg('创建成功', 'success', true);
                    if (!data.productBase) {
                        data.productBase = selectedProductBase;
                    }
                    $scope.listPanel.newProductKeyBatches.push(data);
                    //$scope.$apply();
                }, function (error, data) {
                    console.log(error, data);
                    $scope.addAlertMsg(error.message, 'danger', true);
                });
            },
            productBaseIdChanged: function (productBaseId) {
                console.log('[productBaseId changed]:', productBaseId);
                var selectedProductBase;
                $.each(this.productBases, function (i, item) {
                    if (item && item.id === productBaseId) {
                        selectedProductBase = item;
                        console.log('[selectedProductBase]:', selectedProductBase);
                    }
                });
                this.selectedProductBase = selectedProductBase;
            }
        };

        $scope.listPanel = {
            productKeyBatches: [],
            newProductKeyBatches: [],
            download: function (batchId) {
                batchId && productKeyManageService.downloadProductKeys(this, batchId);
            },
            downloadFrameSrc: ''
        };

        //init
        productKeyManageService
            .getProductBases(function (data) {
                $scope.productBases = $scope.creationPanel.productBases = data;
                if ($scope.productBases) {
                    $.each($scope.productBases, function (i, item) {
                        productKeyManageService.getProductKeyBatches(item.id, function (data) {
                            if (data && data.length) {
                                $scope.listPanel.productKeyBatches.push({
                                    productBase: item,
                                    batches: data
                                });
                            }
                        });
                    });
                }
            });

    }]);

})();
