(function () {
  var app = angular.module('root');

  app.factory("productViewService", ["$http", function ($http) {
    return {
      getProDetails: function (proId, fnSuccess, fnError) {
        $http.get("/api/productbase/" + proId).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("productViewCtrl", ["$scope", "productViewService", "$location", "productBaseDataService", function ($scope, productViewService, $location, productBaseDataService) {

    var product = $scope.product = {
      productInfos: [],//{name: '', value: ''}
      productAddress: [],//{address: '', tel: ''}
      productCommerce: [],//{title: '', url: ''}
      barCode: '',
      statusCode: productBaseDataService.getCurProStatus(),
      productName: '',
      expireDate: '',
      expireDateUnit: '',
      comments: '',
      productKeyTypeCodes: [],
      hotline: '',
      support: '',
      formatStatusCode: function () {
        return productBaseDataService.getProStatusShow()[productBaseDataService.getCurProStatus()];
      }
    };

    if (productBaseDataService.getProId() != '') {
      productViewService.getProDetails(productBaseDataService.getProId(), function (data) {

        product.productName = data.name;
        product.barCode = data.barcode;

        product.productKeyTypeCodes = data.product_key_types.slice(0);

        product.expireDate = data.shelf_life;
        product.expireDateUnit = data.shelf_life_interval;
        product.comments = data.comments;

        if (data.product_base_details) {
          var details = data.product_base_details;
          for (var proInfo in details.details) {
            product.productInfos.push({name: details.details[proInfo].name, value: details.details[proInfo].value});
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
        dataPreview.details = product.productInfos.slice(0);

        $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);

      }, function () {
      });
    }
    $scope.return = function () {
      $location.path('/product-base-manage');
    }
  }]);
})();