(function () {
  var app = angular.module('root');

  app.factory("productViewService", ["$http", function ($http) {
    return {
      getProDetails: function (proId, verId, fnSuccess, fnError) {

        var url = '';
        if (verId == '') {
          url = "/api/productbase/" + proId;
        }
        else {
          url = "/api/productbase/" + proId + '?version=' + verId;
        }

        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      },
      updateProStatus: function (proId, status, comments, fnSuccess, fnError) {
        $http.patch("/api/productbase/" + proId + '/approval?approval_status=' + status + '&review_comments=' + comments).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("productViewCtrl", ["$scope", "productViewService", "$location", "productBaseDataService", function ($scope, productViewService, $location, productBaseDataService) {

    var product = $scope.product = {
      rejectComment: '',
      productInfos: [],//{name: '', value: ''}
      productAddress: [],//{address: '', tel: ''}
      productCommerce: [],//{title: '', url: ''}
      barCode: '',
      statusCode: $location.search()['proStatus'],
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
      },
      showModal: function () {
        $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
          $(this).removeData("bs.modal");
        });
      },
      hideModal: function () {
        $('#myModal').modal('hide');
      },
      returnTo: function () {
        $location.path('/product-base-manage');
      },
      approve: function () {
        productViewService.updateProStatus($location.search()['proId'], 'approved', '审核通过', function () {
          $location.path('/product-base-manage');
          $scope.utils.alert('info', '审核产品成功');
        }, function () {
          $scope.utils.alert('danger', '审核产品失败');
        });
      },
      reject: function () {
        productViewService.updateProStatus($location.search()['proId'], 'rejected', product.rejectComment, function () {

          $location.path('/product-base-manage');
          $scope.utils.alert('info', '拒绝产品成功');
        }, function () {
          $scope.utils.alert('danger', '拒绝产品成功', '#myModal .modal-dialog', false);
        });
      }
    };

    if ($location.search()['proId'] != '') {
      productViewService.getProDetails($location.search()['proId'], $location.search()['verId'], function (data) {

        product.productName = data.name;
        product.barCode = data.barcode;

        product.productKeyTypeCodes = data.product_key_types.slice(0);

        product.expireDate = data.shelf_life;
        product.expireDateUnit = productBaseDataService.getProShelfLife()[data.shelf_life_interval];
        product.comments = data.comments;

        if (data.details) {
          var details = data.details;
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

        if ($location.search()['verId'] == '') {
          product.img800400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-800x400?access_token=" + $scope.utils.auth.getAccessToken();
          product.img400400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-400x400?access_token=" + $scope.utils.auth.getAccessToken();
        }
        else {
          product.img800400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-800x400?version=" + $location.search()['verId'] + "&access_token=" + $scope.utils.auth.getAccessToken();
          product.img400400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-400x400?version=" + $location.search()['verId'] + "&access_token=" + $scope.utils.auth.getAccessToken();
        }

        var dataPreview = {};

        dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo/image-128x128?access_token=" + $scope.utils.auth.getAccessToken();
        dataPreview.proImgUrl = product.img800400;

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
  }]);
})();