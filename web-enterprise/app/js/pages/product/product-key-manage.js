(function () {
    var app = angular.module('root');

    app.factory("productKeyManageService", ["$http", function ($http) {
        return {
            getProductKeyBatches: function (productBaseId, fnSuccess) {
                $http.get("/api/productkeybatch?product_base_id=" + productBaseId).success(fnSuccess);
                return this;
            },
            getProductBases: function (fnSuccess) {
                $http.get("/api/productbase").success(fnSuccess);
                return this;
            },
            getProductKeyCredits: function (fnSuccess) {
                $http.get("/api/productkeycredit", {test: "hello"}).success(fnSuccess);
                return this;
            },
            createProductKeyBatch: function (request, fnSuccess, fnFail) {
                $http.post("/api/productkeybatch", request).success(fnSuccess).error(fnFail);
                return this;
            },
            downloadProductKeys: function (listPanel, batchId, auth) {
                listPanel.downloadFrameSrc = '/api/productkeybatch/' + batchId + '/keys?' + auth;
            }
        };
    }]);

    app.controller("productKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {

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
                    data.productBase = selectedProductBase;
                    selectedProductBase.credit.remain -= requestData.quantity;
                    $scope.listPanel.newProductKeyBatches.push(data);

                    $scope.utils.alert('success', '产品码创建成功');
                }, function (error, data) {
                    console.log(error, data);
                    var message = (error.message || '').substring(0, 100);
                    $scope.utils.alert('danger', message);
                });
            },
            productBaseIdChanged: function (productBaseId) {
                console.log('[productBaseId changed]:', productBaseId);
                var selectedProductBase = null;
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
                if (batchId) {
                    var accessToken = $scope.context.getAccessToken();
                    var auth = accessToken ? $scope.YUNSOO_CONFIG.PARAMETER_ACCESS_TOKEN + '=' + accessToken : '';
                    productKeyManageService.downloadProductKeys(this, batchId, auth);
                }
            },
            downloadFrameSrc: ''
        };

        //init
        //get product bases
        productKeyManageService.getProductBases(function (data) {
            $scope.productBases = $scope.creationPanel.productBases = data;
            if ($scope.productBases) {

                //get product key credits
                productKeyManageService.getProductKeyCredits(function (data) {
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
                            }
                            else {
                                productKeyCredits.general = {
                                    total: item.total,
                                    remain: item.remain
                                };
                            }
                        });
                        $scope.productKeyCredits = productKeyCredits;
                        console.log('[productKeyCredits loaded]: ', productKeyCredits);

                        //get product key batches
                        $.each($scope.productBases, function (i, item) {
                            setCredit(item, productKeyCredits);
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
                )
                ;

            }
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
                credit.percentage = (credit.remain * 100 / credit.total) | 0;
            }
        }

    }
    ])
    ;
//end of controller

})
();
