(function () {
  var app = angular.module("productKeyManage", ["interceptor"]);

  app.factory("productKeyManageService", ["$http", function ($http) {
    return {
      getProductKeyBatch: function (fnSuccess) {
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

      },
      activeProductKeyBatch: function (request, fnSuccess) {

      }
    };
  }]);

  app.controller("productKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {
    $scope.creationPanel = {
      create: function () {
        var newData = $scope.newData;
        console.log(newData);
        var requestData = {
          quantity: newData.quantity,
          productTypeId: newData.productTypeId,
          //"manufacturingDate": "2014-11-28",
          productKeyTypeIds: newData.keyTypeIds,
          createClientId: 100,
          createAccountId: 1000
        };
        productKeyManageService.createProductKeyBatch(requestData, function (data) {

        });
      }
    };

    productKeyManageService
      .getProductBases(function (data) {
        $scope.creationPanel.productBases = data;
      })
      .getProductKeyTypes(function (data) {
        $scope.creationPanel.keyTypes = data;
      });

    $scope.results = [];

    $scope.getTypeName = function (value) {
      var output = '';
      $.each($scope.keyTypeOptions, function (i, k) {
        if (k.value == value) {
          output = k.name;
        }
      });
      return output;
    };

    $scope.getDateString = function (value) {
      var date = new Date(value);
      return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
    };

    $scope.newData = {
      productTypeId: 1,
      quantity: 5,
      keyTypeIds: [1, 2]
    };
    $scope.setChecked = function (type, value) {
      if ($.inArray(value, type) < 0) {
        return "";
      }
      return "checked";
    };
    $scope.keyTypeOptions = [{
      id: 1,
      name: '私有二维码',
      value: 1
    }, {
      id: 2,
      name: '二维码',
      value: 2
    }, {
      id: 3,
      name: '私有RFID',
      value: 11
    }, {
      id: 4,
      name: 'RFID',
      value: 12
    }];
    $scope.toggleKeyTypeOption = function (value) {
      var idx = $scope.newData.keyTypeIds.indexOf(value);
      if (idx > -1) {
        $scope.newData.keyTypeIds = $scope.newData.keyTypeIds.slice(idx);
      } else {
        $scope.newData.keyTypeIds.push(value);
      }
    };

    $scope.active = function (index) {
      var data;
      $.each($scope.results, function (i, d) {
        if (d.index == index) {
          data = d;
        }
      });
      //console.log(data);
      var requestData = {manufacturingDate: new DateTime(new Date()).toString('yyyy-MM-dd')};
      console.log(requestData);
      $.each(data.products, function (i, p) {
        $.ajax({
          //url: 'http://admin.page/api/products/' + p.keys[0] + '/active',
          url: '/api/products/' + p.keys[0] + '/active',
          type: 'POST',
          dataType: 'json',
          data: requestData
        }).done(function (r) {
          data.active = true;
          $scope.$apply();
        }).fail(function (err) {
          console.log(err);
        });
      });
    };
    //productKeyManageService.getInfo(function (data) {
    //  $scope.data.accounts = data;
    //});
  }]);


})();
