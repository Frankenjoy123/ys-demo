(function () {
  var app = angular.module('root');

  app.value('ProductDetailsVersion', '1.0');
  app.value('ProductBaseImagePreviewRec', {w: 422, h: 211, r: 1.8957, ow: 800, oh: 400});

  app.factory("emulatorService", ["$http", function ($http) {
    return {
      createProWithDetail: function (proDetail, fnSuccess, fnError) {
        $http.post("/api/productbase", proDetail).success(fnSuccess).error(fnError);

        return this;
      },
      updateProWithDetail: function (proDetail, fnSuccess, fnError) {
        $http.patch("/api/productbase/" + proDetail.id, proDetail).success(fnSuccess).error(fnError);

        return this;
      },
      putProWithDetailImage: function (proId, proDetailImage, fnSuccess, fnError) {
        $http.put("/api/productbase/" + proId + '/image', proDetailImage).success(fnSuccess).error(fnError);

        return this;
      },
      getProDetails: function (proId, fnSuccess, fnError) {
        $http.get("/api/productbase/" + proId).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "$location", "ProductDetailsVersion", "productBaseDataService", "ProductBaseImagePreviewRec", function ($scope, emulatorService, $timeout, $location, ProductDetailsVersion, productBaseDataService, ProductBaseImagePreviewRec) {

    var jcropObj = null;
    var bounds, boundx, boundy;

    var bounds800400, coords800400;
    var coords800400Result = {x: 0, y: 0, w: 0, h: 0};
    var coords400Result = {x: 0, y: 0, w: 0, h: 0};
    var fileInputContent = '';

    var imgProductbaseWidth = ProductBaseImagePreviewRec.w;
    var imgProductbaseHeight = ProductBaseImagePreviewRec.h;

    var product = $scope.product = {
      spinnerShow: false,
      productTitle: '产品创建',
      productInfos: [{name: '', value: ''}],
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
      comments: '',
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
      imgSrc800400: '',
      imgSrc400400: '',
      addProductInfo: function () {
        this.productInfos.push({name: '', value: ''});
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
              setSelect: [0, 0, 30, 30]
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

        coords400Result.x = parseInt(($('#imgProductbase400')[0].naturalWidth / imgProductbaseWidth) * coords.x);
        coords400Result.w = parseInt(($('#imgProductbase400')[0].naturalWidth / imgProductbaseWidth) * coords.w);
        coords400Result.y = parseInt(($('#imgProductbase400')[0].naturalHeight / imgProductbaseHeight) * coords.y);
        coords400Result.h = parseInt(($('#imgProductbase400')[0].naturalHeight / imgProductbaseHeight) * coords.h);

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
              setSelect: [0, 0, 60, 30]
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

        coords800400Result.x = parseInt(($('#imgProductbase800400')[0].naturalWidth / imgProductbaseWidth) * coords.x);
        coords800400Result.w = parseInt(($('#imgProductbase800400')[0].naturalWidth / imgProductbaseWidth) * coords.w);
        coords800400Result.y = parseInt(($('#imgProductbase800400')[0].naturalHeight / imgProductbaseHeight) * coords.y);
        coords800400Result.h = parseInt(($('#imgProductbase800400')[0].naturalHeight / imgProductbaseHeight) * coords.h);

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

    if ($location.search()['title'] != '') {
      product.productTitle = $location.search()['title'];
    }

    if ($location.search()['proId'] != '') {
      emulatorService.getProDetails($location.search()['proId'], function (data) {
        product.productName = data.name;
        product.barCode = data.barcode;
        for (var type in data.product_key_types) {
          if (data.product_key_types[type].code == 'qr_public') {
            product.keyTypePubInput = 'true';
          }
          else if (data.product_key_types[type].code == 'qr_secure') {
            product.keyTypePriInput = 'true';
          }
          else if (data.product_key_types[type].code == 'rfid') {
            product.keyTypeRFIDInput = 'true';
          }
        }
        product.expireDate = data.shelf_life - 0;
        product.expireDateUnit = data.shelf_life_interval;
        product.comments = data.comments;

        if (data.product_base_details) {
          var details = data.product_base_details;
          product.productInfos = [];
          for (var proInfo in details.details) {
            product.productInfos.push({name: details.details[proInfo].name, value: details.details[proInfo].value});
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

        product.imgSrc800400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-800x400?access_token=" + $scope.utils.auth.getAccessToken();
        product.imgSrc400400 = "/api/productbase/" + $location.search()['proId'] + "/image/image-400x400?access_token=" + $scope.utils.auth.getAccessToken();

        product.proPicPreview = true;
        product.proPicPreviewUpload = false;
        product.showButton800400 = false;

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
        $scope.utils.alert('info', '获取产品信息失败');
      });
    }

    $scope.preview = function () {

      var dataPreview = {};
      dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo/image-128x128?access_token=" + $scope.utils.auth.getAccessToken();

      dataPreview.isReadOnlyMode = false;

      if (fileInputContent == '') {

        dataPreview.isReadOnlyMode = true;

        if ($('#imgProductbase800400').attr('src') == '') {
          dataPreview.proImgUrl = 'ysdefault.jpg';
        }
        else {
          dataPreview.proImgUrl = $('#imgProductbase800400').attr('src');
        }
      }
      else {
        dataPreview.proImgUrl = fileInputContent;
      }

      dataPreview.bounds800400 = bounds800400;
      dataPreview.coords800400 = coords800400;
      dataPreview.barcode = product.barCode;
      dataPreview.name = product.productName;
      dataPreview.details = product.productInfos;

      $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);
    };

    $scope.submit = function (isValid) {

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

      if ($('#imgProductbase800400').attr('src') == '') {
        $scope.utils.alert('info', '图像800*400像素不能为空');
        return;
      }

      if ($('#imgProductbase400').attr('src') == '') {
        $scope.utils.alert('info', '图像400*400像素不能为空');
        return;
      }

      product.spinnerShow = true;

      var proWithDetails = {};

      proWithDetails.category_id = 0;
      proWithDetails.barcode = product.barCode;
      proWithDetails.name = product.productName;
      proWithDetails.comments = product.comments;
      proWithDetails.product_key_type_codes = [];
      if (product.keyTypePubInput)
        proWithDetails.product_key_type_codes.push("qr_public");
      if (product.keyTypePriInput)
        proWithDetails.product_key_type_codes.push("qr_secure");
      if (product.keyTypeRFIDInput)
        proWithDetails.product_key_type_codes.push("rfid");

      proWithDetails.shelf_life = product.expireDate - 0;
      proWithDetails.shelf_life_interval = product.expireDateUnit;

      var proDetails = proWithDetails.product_base_details = {};
      proDetails.version = ProductDetailsVersion;

      proDetails.details = [];
      for (var proInfo in product.productInfos) {
        if (product.productInfos[proInfo].name != '' && product.productInfos[proInfo].value != '')
          proDetails.details.push({
            name: product.productInfos[proInfo].name,
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

      if ($location.search()['proId'] != '') {
        proWithDetails.id = $location.search()['proId'];
        emulatorService.updateProWithDetail(proWithDetails, function () {
              var detailImg = {};
              detailImg.product_base_id = $location.search()['proId'];
              detailImg.image_content = fileInputContent;
              detailImg.image_rect = {};
              detailImg.image_rect.initX = coords800400Result.x;
              detailImg.image_rect.initY = coords800400Result.y;
              detailImg.image_rect.width = coords800400Result.w;
              detailImg.image_rect.height = coords800400Result.h;

              detailImg.image_square = {};
              detailImg.image_square.initX = coords400Result.x;
              detailImg.image_square.initY = coords400Result.y;
              detailImg.image_square.width = coords400Result.w;
              detailImg.image_square.height = coords400Result.h;

              emulatorService.putProWithDetailImage($location.search()['proId'], detailImg, function () {
                $scope.utils.alert('success', '更新产品信息成功');
                product.spinnerShow = false;
                $location.path('/product-base-manage');
              }, function () {
                $scope.utils.alert('info', '更新产品信息成功，但更新产品图片失败');
                product.spinnerShow = false;
                $location.path('/product-base-manage');
              });
            },
            function () {
              $scope.utils.alert('info', '更新产品信息失败');
              product.spinnerShow = false;
            });
      }
      else {
        emulatorService.createProWithDetail(proWithDetails, function (data) {
              if (data != null && data != '') {

                var detailImg = {};
                detailImg.product_base_id = data.id;
                detailImg.image_content = fileInputContent;
                detailImg.image_rect = {};
                detailImg.image_rect.initX = coords800400Result.x;
                detailImg.image_rect.initY = coords800400Result.y;
                detailImg.image_rect.width = coords800400Result.w;
                detailImg.image_rect.height = coords800400Result.h;

                detailImg.image_square = {};
                detailImg.image_square.initX = coords400Result.x;
                detailImg.image_square.initY = coords400Result.y;
                detailImg.image_square.width = coords400Result.w;
                detailImg.image_square.height = coords400Result.h;

                emulatorService.putProWithDetailImage(data.id, detailImg, function () {
                  $scope.utils.alert('success', '创建产品信息成功');
                  product.spinnerShow = false;
                  $location.path('/product-base-manage');
                }, function () {
                  $scope.utils.alert('info', '创建产品信息成功，但上传产品图片失败');
                  product.spinnerShow = false;
                  $location.path('/product-base-manage');
                });
              }
            },
            function () {
              $scope.utils.alert('info', '创建产品信息失败');
              product.spinnerShow = false;
            });
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

              var reader = new FileReader();
              reader.readAsDataURL(fileInput[0].files[0]);
              reader.onload = function (e) {
                product.proPicPreview = true;
                product.proPicPreviewUpload = true;
                product.showButton800400 = true;
                product.showButton400 = false;

                $scope.$apply();

                if (jcropObj != null) {
                  jcropObj.destroy();
                }

                imgProductbaseWidth = ProductBaseImagePreviewRec.w;
                imgProductbaseHeight = ProductBaseImagePreviewRec.h;

                imgProductbase.css('width', imgProductbaseWidth + 'px');
                imgProductbase.css('height', imgProductbaseHeight + 'px');

                var oriImg = this.result;

                imgProductbase.attr('src', oriImg);

                $timeout(function () {
                  if (imgProductbase[0].naturalWidth < ProductBaseImagePreviewRec.ow && imgProductbase[0].naturalHeight < ProductBaseImagePreviewRec.oh) {
                    imgProductbaseWidth = imgProductbase[0].naturalWidth / ProductBaseImagePreviewRec.r;
                    imgProductbaseHeight = imgProductbase[0].naturalHeight / ProductBaseImagePreviewRec.r;
                  }
                  else {
                    if ((imgProductbase[0].naturalWidth / ProductBaseImagePreviewRec.w) >= (imgProductbase[0].naturalHeight / ProductBaseImagePreviewRec.h)) {
                      var rate = imgProductbase[0].naturalWidth / ProductBaseImagePreviewRec.w;
                      imgProductbaseHeight = imgProductbase[0].naturalHeight / rate;
                    }
                    else if ((imgProductbase[0].naturalWidth / ProductBaseImagePreviewRec.w) < (imgProductbase[0].naturalHeight / ProductBaseImagePreviewRec.h)) {
                      var rate = imgProductbase[0].naturalHeight / ProductBaseImagePreviewRec.h;
                      imgProductbaseWidth = imgProductbase[0].naturalWidth / rate;
                    }
                  }

                  imgProductbase.css('width', imgProductbaseWidth + 'px');
                  imgProductbase.css('height', imgProductbaseHeight + 'px');

                  imgProductbase800400.attr('src', oriImg);
                  imgProductbase400.attr('src', oriImg);

                  imgProductbase800400.css('visibility', 'hidden');
                  imgProductbase400.css('visibility', 'hidden');

                  product.initJcrop800400();

                  fileInputContent = oriImg;

                }, 50);

              };
            }
        );
      }
    }, 0);

  }]);
})();