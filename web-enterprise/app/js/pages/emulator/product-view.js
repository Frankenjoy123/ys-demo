(function () {
  var app = angular.module('root');

  app.factory("productViewService", ["$http", function ($http) {
    return {
      getProDetails: function (proId, fnSuccess, fnError) {
        $http.post("/api/productbase/withdetail", proDetail).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("productViewCtrl", ["$scope", "productViewService", "$location", function ($scope, productViewService, $location) {

    $scope.fileInput = '';

    var product = $scope.product = {
      productInfos: [{key: '', value: ''}],
      productAddress: [{address: '', tel: ''}],
      productCommerce: [{title: '', url: ''}],
      barCode: '',
      productName: '',
      expireDate: 1,
      expireDateUnitValue: [{key: 'year', value: '年'}, {key: 'month', value: '月'}, {key: 'week', value: '周'}, {
        key: 'day',
        value: '天'
      }, {key: 'hour', value: '时'}],
      expireDateUnit: '',
      comment: '',
      keyTypePubInput: '',
      keyTypePriInput: '',
      keyTypeRFIDInput: '',
      hotline: '',
      support: '',
      proPicPreview: false,
      addProductInfo: function () {
        this.productInfos.push({key: '', value: ''});
      },
      subProductInfo: function () {
        this.productInfos.pop();
      },
      addProAddress: function () {
        this.productAddress.push({address: '', tel: ''});
      },
      subProAddress: function () {
        this.productAddress.pop();
      },
      addProductCommerce: function () {
        this.productCommerce.push({title: '', url: ''});
      },
      subProductCommerce: function () {
        this.productCommerce.pop();
      }
    };

    $scope.preview = function () {

      var dataPreview = {};
      dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo-mobile?access_token=" + $scope.utils.auth.getAccessToken();
      dataPreview.proImgUrl = $scope.fileInput;
      dataPreview.barcode = product.barCode;
      dataPreview.name = product.productName;
      dataPreview.details = product.productInfos;

      $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);
    };

    $scope.submit = function (isValid) {

      var proWithDetails = {};

      proWithDetails.category_id = 0;
      proWithDetails.barcode = product.barCode;
      proWithDetails.name = product.productName;
      proWithDetails.comments = product.comment;
      proWithDetails.product_key_type_codes = [];
      if (product.keyTypePubInput)
        proWithDetails.product_key_type_codes.push("qr_public");
      if (product.keyTypePriInput)
        proWithDetails.product_key_type_codes.push("qr_secure");
      if (product.keyTypeRFIDInput)
        proWithDetails.product_key_type_codes.push("rfid");

      proWithDetails.shelf_life = product.expireDate - 0;
      proWithDetails.shelf_life_interval = product.expireDateUnit;

      proWithDetails.status_code = '待审核';

      var proDetails = {};
      proDetails.version = ProductDetailsVersion;

      proDetails.details = [];
      for (var proInfo in product.productInfos) {
        if (product.productInfos[proInfo].key != '' && product.productInfos[proInfo].value != '')
          proDetails.details.push({
            name: product.productInfos[proInfo].key,
            value: product.productInfos[proInfo].value
          });
      }

      proDetails.contact = {hotline: product.hotline, support: product.support};

      proDetails['e_commerce'] = [];
      for (var proCommerce in product.productCommerce) {
        if (product.productCommerce[proCommerce].title != '' && product.productCommerce[proCommerce].url != '') {
          proDetails['e_commerce'].push({
            title: product.productCommerce[proCommerce].title,
            url: product.productCommerce[proCommerce].url
          });
        }
      }

      proDetails['t_commerce'] = [];
      for (var proAddress in product.productAddress) {
        if (product.productAddress[proAddress].address != '' && product.productAddress[proAddress].tel != '') {
          proDetails['t_commerce'].push({
            address: product.productAddress[proAddress].address,
            tel: product.productAddress[proAddress].tel
          });
        }
      }

      proWithDetails.details = JSON.stringify(proDetails);

      try {

      }
      catch (ex) {

      }
    };

    $timeout(function () {
      var divImgWrap = $("#divImgWrap");
      var fileInput = $("#fileInput");
      var imgProductbase = $("#imgProductbase");

      if (typeof FileReader === 'undefined') {
        divImgWrap.html("您的浏览器不支持图片预览");
      } else {
        fileInput.change(function () {

              var file = uploader.queue[uploader.queue.length - 1]._file;
              var reader = new FileReader();
              reader.readAsDataURL(file);
              reader.onload = function (e) {
                product.proPicPreview = true;
                $scope.$apply();

                imgProductbase.attr('src', this.result);
                $scope.fileInput = this.result;
              };

              uploader.queue = uploader.queue.slice(uploader.queue.length - 1, uploader.queue.length);
            }
        );
      }
    }, 0);

  }]);
})();