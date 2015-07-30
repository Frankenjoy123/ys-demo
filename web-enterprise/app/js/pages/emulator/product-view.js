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
      img800400: '',
      img400400: '',
      formatStatusCode: function () {
        return productBaseDataService.getProStatusShow()[$location.search()['proStatus']];
      }
    };

    if ($location.search()['proId'] != '') {
      productViewService.getProDetails($location.search()['proId'], function (data) {

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

        product.img800400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-800x400?access_token=" + $scope.utils.auth.getAccessToken();
        product.img400400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-400x400?access_token=" + $scope.utils.auth.getAccessToken();

        var dataPreview = {};
        dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo/image-128x128?access_token=" + $scope.utils.auth.getAccessToken();
        dataPreview.proImgUrl = "/api/productbase/" + $location.search()['proId'] + "/image/image-800x400?access_token=" + $scope.utils.auth.getAccessToken();

        dataPreview.barcode = product.barCode;
        dataPreview.name = product.productName;

        if (product.productInfos) {
          dataPreview.details = product.productInfos.slice(0);
        }

        dataPreview.isReadOnlyMode = true;

        $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);

      }, function () {
      });
    }
    $scope.return = function () {
      $location.path('/product-base-manage');
    }
  }]);
})();