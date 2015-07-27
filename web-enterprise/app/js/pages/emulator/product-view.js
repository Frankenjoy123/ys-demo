(function () {
  var app = angular.module('root');

  app.factory("productViewService", ["$http", "productBaseDataService", function ($http, productBaseDataService) {
    return {
      getProDetails: function (fnSuccess, fnError) {
        $http.get("/api/productbase/" + productBaseDataService.getProId()).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("productViewCtrl", ["$scope", "productViewService", "$location", function ($scope, productViewService, $location) {

    var product = $scope.product = {
      productInfos: [],//{name: '', value: ''}
      productAddress: [],//{address: '', tel: ''}
      productCommerce: [],//{title: '', url: ''}
      barCode: '',
      statusCode: '',
      productName: '',
      expireDate: '',
      expireDateUnit: '',
      comment: '',
      productKeyTypeCodes: [],
      hotline: '',
      support: '',
      statusFormat: [{activated: '已激活', created: '未激活', deleted: '已删除', recalled: '已召回'}],
      formatStatusCode: function () {
        return this.statusFormat[this.statusCode];
      }
    };

    productViewService.getProDetails(function (data) {

      product.productName = data.name;
      product.barCode = data.barcode;
      product.productKeyTypeCodes = data.product_key_type_codes;
      product.expireDate = data.shelf_life;
      product.expireDateUnit = data.shelf_life_interval;
      product.statusCode = data.status_code;
      product.comments = data.comment;

      if (data.product_base_details) {
        var details = data.product_base_details;
        for (var proInfo in details.item) {
          product.productInfos.push({name: details.item[proInfo].name, value: details.item[proInfo].value});
        }

        product.hotline = details.contact.hotline;
        product.support = details.contact.support;

        for (var proCommerce in details.e_commerce) {
          product.productCommerce.push({
            title: details.e_commerce[proCommerce].title,
            url: details.e_commerce[proCommerce].url
          });
        }

        for (var proAddress in details.t_commerce) {
          product.productAddress.push({
            address: details.t_commerce[proAddress].address,
            tel: details.t_commerce[proAddress].tel
          });
        }
      }

      var dataPreview = {};
      dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo-mobile?access_token=" + $scope.utils.auth.getAccessToken();

      //if ($scope.fileInput == '') {
      dataPreview.proImgUrl = 'ysdefault.jpg';
      //}
      //else {
      //  dataPreview.proImgUrl = $scope.fileInput;
      //}

      dataPreview.barcode = product.barCode;
      dataPreview.name = product.productName;
      dataPreview.details = product.productInfos;

      $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);

    }, function () {
    });

    $scope.return = function () {
      $location.path('/product-base-manage');
    }
  }]);
})();