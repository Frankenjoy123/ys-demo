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
                $http.get("/api/productbase").success(function (data) {
                    fnSuccess(data);
                });
                return this;
            },
            getProductKeyTypes: function (fnSuccess) {
                $http.get("/api/productkeytype").success(function (data) {
                    fnSuccess(data);
                });
                return this;
            },
            createProductKeyBatch: function (request, fnSuccess, fnFail) {
                $http.post("/api/productkeybatch", request).success(function (data) {
                    fnSuccess(data);
                });
                return this;
            },
            downloadProductKeys: function (listPanel, batchId) {
                listPanel.downloadFrameSrc = '/api/productkeybatch/' + batchId + '/keys';
            }
        };
    }]);

    app.controller("productKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {
        $scope.creationModel = {
            productBaseId: 0,
            quantity: 0
        };

        $scope.getDateString = function (value) {
            return new DateTime(new Date(value)).toString('yyyy-MM-dd HH:mm:ss');
        };

        $scope.$watch($scope.creationModel.productBaseId, function (oldValue, newValue) {
            console.log(oldValue, newValue);
        });

        $scope.creationPanel = {
            create: function () {
                var model = $scope.creationModel;
                console.log(model);
                var requestData = {
                    quantity: model.quantity,
                    productBaseId: model.productBaseId
                };
                productKeyManageService.createProductKeyBatch(requestData, function (data) {
                    $scope.$apply();
                }, function (data, error) {

                });
            }
        };

        $scope.listPanel = {
            download: function (batchId) {
                batchId && productKeyManageService.downloadProductKeys(this, batchId);
            },
            downloadFrameSrc: ''
        };

        //init
        $scope.productKeyBatches = [];
        productKeyManageService
            .getProductBases(function (data) {
                $scope.productBases = $scope.creationPanel.productBases = data;
                if ($scope.productBases) {
                    $.each($scope.productBases, function (i, item) {
                        productKeyManageService.getProductKeyBatches(item.id, function (data) {
                            if (data && data.length) {
                                $scope.productKeyBatches.push({
                                    productBase: item,
                                    batches: data
                                });
                            }
                        });
                    });
                }
            })
            .getProductKeyTypes(function (data) {
                $scope.creationPanel.keyTypes = data;
            });


        //$scope.getTypeName = function (value) {
        //  var output = '';
        //  $.each($scope.keyTypeOptions, function (i, k) {
        //    if (k.value == value) {
        //      output = k.name;
        //    }
        //  });
        //  return output;
        //};

        //$scope.active = function (index) {
        //  var data;
        //  $.each($scope.results, function (i, d) {
        //    if (d.index == index) {
        //      data = d;
        //    }
        //  });
        //  //console.log(data);
        //  var requestData = {manufacturingDate: new DateTime(new Date()).toString('yyyy-MM-dd')};
        //  console.log(requestData);
        //  $.each(data.products, function (i, p) {
        //    $.ajax({
        //      //url: 'http://admin.page/api/products/' + p.keys[0] + '/active',
        //      url: '/api/products/' + p.keys[0] + '/active',
        //      type: 'POST',
        //      dataType: 'json',
        //      data: requestData
        //    }).done(function (r) {
        //      data.active = true;
        //      $scope.$apply();
        //    }).fail(function (err) {
        //      console.log(err);
        //    });
        //  });
        //};
        //productKeyManageService.getInfo(function (data) {
        //  $scope.data.accounts = data;
        //});
    }]);


})();
