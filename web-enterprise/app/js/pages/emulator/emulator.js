(function () {
  var app = angular.module('root');

  app.value('ProductDetailsVersion', '1.0');

  app.factory("emulatorService", ["$http", function ($http) {
    return {
      createProWithDetail: function (proDetail, fnSuccess, fnError) {
        $http.post("/api/productbase/withdetail", proDetail).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "FileUploader", "$location", "ProductDetailsVersion", function ($scope, emulatorService, $timeout, FileUploader, $location, ProductDetailsVersion) {

    var uploader = $scope.uploader = new FileUploader({
      url: ''
    });

    //set AccessToken http header
    var accessToken = $scope.utils.auth.getAccessToken();
    accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

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
      //if (product.barCode == '') {
      //  $scope.utils.alert('info', '产品BarCode不能为空');
      //  return;
      //}
      //
      //if (product.productName == '') {
      //  $scope.utils.alert('info', '产品名不能为空');
      //  return;
      //}

      if (!isValid) {
        $scope.utils.alert('info', '页面验证有错误，请返回检查');
        return;
      }

      if (!product.keyTypePubInput && !product.keyTypePriInput && !product.keyTypeRFIDInput) {
        $scope.utils.alert('info', '产品码类型至少要选择一种');
        return;
      }

      if (product.expireDateUnit == '') {
        $scope.utils.alert('info', '请选择产品过期单位');
        return;
      }
      //
      //if (!(/(^[1-9]\d*$)/.test(product.expireDate))) {
      //  $scope.utils.alert('info', '产品过期时间应为正整数');
      //  return;
      //}

      if (uploader.queue.length == 0) {
        $scope.utils.alert('info', '产品图片不能为空');
        return;
      }

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
        emulatorService.createProWithDetail(proWithDetails, function (data) {
              if (data.id != null && data.id != '') {
                uploader.queue[uploader.queue.length - 1].url = '/api/productbase/withdetailfile/' + data.id + "/full-mobile";
                uploader.queue[uploader.queue.length - 1].headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken;

                uploader.uploadAll();

                $scope.utils.alert('success', '创建产品成功');

                $location.path('/product-base-manage');
              }
            },
            function (data, state) {
              $scope.utils.alert('info', '创建产品失败');
            });
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

      //$('#createProduct').bootstrapValidator({
      //  message: '该字段不能为空',
      //  feedbackIcons: {
      //    valid: 'fa fa-check-circle fa-lg text-success',
      //    invalid: 'fa fa-times-circle fa-lg',
      //    validating: 'fa fa-refresh'
      //  },
      //  fields: {
      //    barCode: {
      //      validators: {
      //        notEmpty: {
      //          message: '请输入产品BarCode'
      //        }
      //      }
      //    },
      //    productName: {
      //      validators: {
      //        notEmpty: {
      //          message: '请输入产品名'
      //        }
      //      }
      //    },
      //    greaterthan: {
      //      validators: {
      //        notEmpty: {
      //          message: '请输入产品过期时间'
      //        },
      //        greaterThan: {
      //          inclusive: false,
      //          //If true, the input value must be greater than or equal to the comparison one.
      //          //If false, the input value must be greater than the comparison one
      //          value: 0,
      //          message: '请输入大于1的正整数'
      //        }
      //      }
      //    }
      //  }
      //}).on('success.field.bv', function (e, data) {
      //  // $(e.target)  --> The field element
      //  // data.bv      --> The BootstrapValidator instance
      //  // data.field   --> The field name
      //  // data.element --> The field element
      //
      //  var $parent = data.element.parents('.form-group');
      //
      //  // Remove the has-success class
      //  $parent.removeClass('has-success');
      //});
    }, 0);

  }]);
})();