(function () {
  var app = angular.module("productKeyManage", ["interceptor"]);

  app.factory("productKeyManageService", ["$http", function ($http) {
    return {
      getInfo: function (fnSuccess, fnError) {
        $http.get("mock/productKeyBatch.json")
          .success(function (data) {
            fnSuccess(data);
          });
      }
    };
  }]);

  app.controller("productKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {
    $scope.productTypeOptions = [
      { value: 1, name: "王老吉" },
      { value: 2, name: "茅台" },
      { value: 3, name: "无限极牌钙片" },
      { value: 4, name: "维生素C泡腾片（甜橙）" },
      { value: 5, name: "椰树牌椰汁" },
      { value: 6, name: "海洋之娇" }
    ];
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
    $scope.create = function () {
      resultsCount++;
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

      $.ajax({
        //url: 'http://admin.page/api/products',
        url: 'http://wweb.chinacloudapp.cn/api/products',
        type: 'PUT',
        dataType: 'json',
        data: requestData
      }).done(function (data) {
        for (var i = 0; i < $scope.productTypeOptions.length; i++) {
          if ($scope.productTypeOptions[i].value == newData.productTypeId) {
            data.productTypeName = $scope.productTypeOptions[i].name;
            break;
          }
        }
        data.index = $scope.results.length;
        //console.log(data);
        $scope.results.splice(0, 0, data);
        $scope.$apply();
      }).fail(function (err) {
        console.log(err);
      });
    };

    $scope.active = function (index) {
      var data;
      $.each($scope.results, function (i, d) {
        if (d.index == index) {
          data = d;
        }
      });
      //console.log(data);
      var requestData = { manufacturingDate: new DateTime(new Date()).toString('yyyy-MM-dd') };
      console.log(requestData);
      $.each(data.products, function (i, p) {
        $.ajax({
          //url: 'http://admin.page/api/products/' + p.keys[0] + '/active',
          url: 'http://wweb.chinacloudapp.cn/api/products/' + p.keys[0] + '/active',
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
