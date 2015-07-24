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

  app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "FileUploader", "$location", "ProductDetailsVersion", "productBaseDataService", function ($scope, emulatorService, $timeout, FileUploader, $location, ProductDetailsVersion, productBaseDataService) {

    var jcropObj = null;
    var bounds, boundx, boundy;

    var bounds800400, coords800400;
    var coords800400Result = {x: 0, y: 0, w: 0, h: 0};
    var coords400Result = {x: 0, y: 0, w: 0, h: 0};

    var uploader = $scope.uploader = new FileUploader({
      url: ''
    });

    //set AccessToken http header
    var accessToken = $scope.utils.auth.getAccessToken();
    accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

    $scope.fileInput = '';

    var product = $scope.product = {
      productTitle: '产品创建',
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
      proPicPreviewUpload: false,
      showButton800400: true,
      showButton400: false,
      imageWord: '选择图片',
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
      },
      initJcrop400: function () {
        if (jcropObj != null) {
          jcropObj.destroy();
        }

        $("#imgProductbase").Jcrop({
              allowSelect: false,
              aspectRatio: 1,
              onChange: this.showPreview400,
              onSelect: this.showPreview400,
              setSelect: [0, 0, 130, 130]
            },
            function () {
              jcropObj = this;

              bounds = jcropObj.getBounds();
              boundx = bounds[0];
              boundy = bounds[1];
            });
      },
      showPreview400: function (coords) {
        $("#imgProductbase400").css('visibility', 'visible');

        coords400Result.x = parseInt(($('#imgProductbase400')[0].naturalWidth / 422) * coords.x);
        coords400Result.w = parseInt(($('#imgProductbase400')[0].naturalWidth / 422) * coords.w);
        coords400Result.y = parseInt(($('#imgProductbase400')[0].naturalHeight / 211) * coords.y);
        coords400Result.h = parseInt(($('#imgProductbase400')[0].naturalHeight / 211) * coords.h);

        $('#showRealPix').html('W:' + coords400Result.w + ' H:' + coords400Result.h);

        if (parseInt(coords.w) > 0) {
          var rx = 130 / coords.w;
          var ry = 130 / coords.h;

          $('#imgProductbase400').css({
            width: Math.round(rx * boundx) + 'px',
            height: Math.round(ry * boundy) + 'px',
            marginLeft: '-' + Math.round(rx * coords.x) + 'px',
            marginTop: '-' + Math.round(ry * coords.y) + 'px'
          });
        }
      },
      initJcrop800400: function () {
        if (jcropObj != null) {
          jcropObj.destroy();
        }

        $("#imgProductbase").Jcrop({
              allowSelect: false,
              aspectRatio: 2,
              onChange: this.showPreview800400,
              onSelect: this.showPreview800400,
              setSelect: [0, 0, 260, 130]
            },
            function () {
              jcropObj = this;

              bounds = jcropObj.getBounds();
              bounds800400 = bounds;
              boundx = bounds[0];
              boundy = bounds[1];
            });
      },
      showPreview800400: function (coords) {
        $("#imgProductbase800400").css('visibility', 'visible');

        coords800400 = coords;

        coords800400Result.x = parseInt(($('#imgProductbase800400')[0].naturalWidth / 422) * coords.x);
        coords800400Result.w = parseInt(($('#imgProductbase800400')[0].naturalWidth / 422) * coords.w);
        coords800400Result.y = parseInt(($('#imgProductbase800400')[0].naturalHeight / 211) * coords.y);
        coords800400Result.h = parseInt(($('#imgProductbase800400')[0].naturalHeight / 211) * coords.h);

        $('#showRealPix').html('W:' + coords800400Result.w + ' H:' + coords800400Result.h);

        if (parseInt(coords.w) > 0) {
          var rx = 260 / coords.w;
          var ry = 130 / coords.h;

          $('#imgProductbase800400').css({
            width: Math.round(rx * boundx) + 'px',
            height: Math.round(ry * boundy) + 'px',
            marginLeft: '-' + Math.round(rx * coords.x) + 'px',
            marginTop: '-' + Math.round(ry * coords.y) + 'px'
          });
        }
      },
      comfirmSelect800400: function () {
        this.showButton800400 = false;
        this.showButton400 = true;

        this.initJcrop400();
      },
      comfirmSelect400: function () {
        this.showButton800400 = false;
        this.showButton400 = false;

        if (jcropObj != null) {
          jcropObj.destroy();
        }

        this.proPicPreviewUpload = false;
        this.imageWord = "重新选择";
      }
    };

    if (productBaseDataService.getTitle() != '') {
      product.productTitle = productBaseDataService.getTitle();
    }

    if (productBaseDataService.getDetails() != null) {
      var data = productBaseDataService.getDetails();

      product.productName = data.name;
      product.barcode = data.barCode;
      product.productKeyTypeCodes = data.product_key_type_codes;
      product.expireDate = data.shelf_life;
      product.expireDateUnit = data.shelf_life_interval;
      product.comments = data.comment;

      var details = data.product_base_details;
      product.productInfos = [];
      for (var proInfo in details.item) {
        product.productInfos.push({name: details.item[proInfo].name, value: details.item[proInfo].value});
      }

      product.hotline = details.contact.hotline;
      product.support = details.contact.support;

      product.productCommerce = [];
      for (var proCommerce in details.e_commerce) {
        product.productCommerce.push({
          title: details.e_commerce[proCommerce].title,
          url: details.e_commerce[proCommerce].url
        });
      }

      product.productAddress = [];
      for (var proAddress in details.t_commerce) {
        product.productAddress.push({
          address: details.t_commerce[proAddress].address,
          tel: details.t_commerce[proAddress].tel
        });
      }
    }

    $scope.preview = function () {

      var dataPreview = {};
      dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo-mobile?access_token=" + $scope.utils.auth.getAccessToken();

      if ($scope.fileInput == '') {
        dataPreview.proImgUrl = 'ysdefault.jpg';
      }
      else {
        dataPreview.proImgUrl = $scope.fileInput;
      }

      dataPreview.bounds800400 = bounds800400;
      dataPreview.coords800400 = coords800400;
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
      var imgProductbase800400 = $("#imgProductbase800400");
      var imgProductbase400 = $("#imgProductbase400");

      if (typeof FileReader === 'undefined') {
        divImgWrap.html("您的浏览器不支持图片预览");
      } else {
        fileInput.change(function () {

              var file = uploader.queue[uploader.queue.length - 1]._file;
              var reader = new FileReader();
              reader.readAsDataURL(file);
              reader.onload = function (e) {
                product.proPicPreview = true;
                product.proPicPreviewUpload = true;
                product.showButton800400 = true;
                product.showButton400 = false;

                $scope.$apply();

                var oriImg = this.result;
                imgProductbase.attr('src', oriImg);
                imgProductbase800400.attr('src', oriImg);
                imgProductbase400.attr('src', oriImg);

                imgProductbase800400.css('visibility', 'hidden');
                imgProductbase400.css('visibility', 'hidden');

                product.initJcrop800400();

                $scope.fileInput = this.result;
              };

              uploader.queue = uploader.queue.slice(uploader.queue.length - 1, uploader.queue.length);
            }
        );
      }
    }, 0);

  }]);
})();