(function () {
  var app = angular.module("productKeyManage", ["interceptor"]);

  app.factory("productKeyManageService", ["$http", function ($http) {
    return {
      getProductKeyBatches: function (fnSuccess) {
        $http.get("/api/productkeybatch").success(function (data) {
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

      }
    };
  }]);

  app.controller("productKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {
    $scope.newData = {
      productBaseId: 0,
      quantity: 0
    };

    $scope.getDateString = function (value) {
      return new DateTime(new Date(value)).toString('yyyy-MM-dd HH:mm:ss');
    };

    $scope.creationPanel = {
      create: function () {
        var newData = $scope.newData;
        console.log(newData);
        var requestData = {
          quantity: newData.quantity,
          productBaseId: newData.productBaseId
        };
        productKeyManageService.createProductKeyBatch(requestData, function (data) {

        }, function (data, error) {

        });
      }
    };

    //init
    productKeyManageService
      .getProductBases(function (data) {
        $scope.creationPanel.productBases = data;
      })
      .getProductKeyTypes(function (data) {
        $scope.creationPanel.keyTypes = data;
      })
      .getProductKeyBatches(function (data) {
        $scope.productKeyBatches = data;
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
