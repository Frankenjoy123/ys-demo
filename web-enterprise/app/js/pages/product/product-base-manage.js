(function () {
  var app = angular.module('root');

  app.factory('productBaseManageService', ['$http', function ($http) {
    return {
      getProductBases: function (fnSuccess) {
        $http.get('/api/productbase').success(fnSuccess);
        return this;
      },
      getProductKeyCredits: function (fnSuccess) {
        $http.get('/api/productkeycredit').success(fnSuccess);
        return this;
      },
      getProDetails: function (proId, fnSuccess, fnError) {
        $http.get("/api/productbase/" + proId).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.factory('productBaseDataService', function () {
    var savedData = {
      title: '',
      currProId: '',
      proDetails: null,
      isCreateMode: true
    };

    function getDetails() {
      return savedData.proDetails;
    }

    function setDetails(data) {
      savedData.proDetails = data;
    }

    function getProId() {
      return savedData.currProId;
    }

    function setProId(data) {
      savedData.currProId = data;
    }

    function getTitle() {
      return savedData.title;
    }

    function setTitle(data) {
      savedData.title = data;
    }

    function getMode() {
      return savedData.isCreateMode;
    }

    function setMode(data) {
      savedData.isCreateMode = data;
    }

    return {
      getDetails: getDetails,
      setDetails: setDetails,
      getProId: getProId,
      setProId: setProId,
      getTitle: getTitle,
      setTitle: setTitle,
      getMode: getMode,
      setMode: setMode
    }
  });

  app.controller('ProductBaseManageCtrl', ['$scope', 'productBaseManageService', 'productBaseDataService', '$location', "$timeout", function ($scope, productBaseManageService, productBaseDataService, $location, $timeout) {
    $scope.SHELFLIFE_INTERVALS = {
      'year': '年',
      'month': '月',
      'week': '周',
      'day': '天',
      'hour': '小时'
    };

    var statusFormat = [{activated: '已激活', created: '未激活', deleted: '已删除', recalled: '已召回'}];
    $scope.formatStatusCode = function (statusCode) {
      return statusFormat[statusCode];
    }

    $scope.formatProductKeyTypes = function (productKeyTypes) {
      var result = '';
      if (productKeyTypes) {
        $.each(productKeyTypes, function (i, item) {
          result += item.name;
          if (i < productKeyTypes.length - 1) {
            result += ', ';
          }
        });
      }
      return result;
    };

    $scope.formatComments = function (comments) {
      comments || (comments = '');
      return comments.length > 30 ? comments.substring(0, 30) + '...' : comments;
    };

    $scope.dataTable = new $scope.utils.DataTable({
      pageable: {
        page: 0,
        size: 20
      },
      flush: function (callback) {
        productBaseManageService.getProductBases(function (data, status, headers) {
          if ($scope.productKeyCredits) {
            setProductKeyCredits(data, $scope.productKeyCredits);
          } else {
            getProductKeyCredits(function (productKeyCredits) {
              setProductKeyCredits(data, productKeyCredits);
            });
          }
          callback({data: data, headers: headers});

          $timeout(function () {
            $('#proBaseTable').removeClass('default breakpoint footable-loaded footable');

            $('#proBaseTable').footable().on('footable_row_expanded', function (e) {
              $('#proBaseTable tbody tr.footable-detail-show').not(e.row).each(function () {
                $('#proBaseTable').data('footable').toggleDetail(this);
              });
            });
          }, 0);
        });
      }
    });

    function getProductKeyCredits(callback) {
      productBaseManageService.getProductKeyCredits(function (data) {
        var productKeyCredits = {
          general: {
            total: 0,
            remain: 0
          },
          creditMap: {}
        };
        $.each(data, function (i, item) {
          if (item.product_base_id) {
            productKeyCredits.creditMap[item.product_base_id] = {
              total: item.total,
              remain: item.remain
            };
          } else {
            productKeyCredits.general = {
              total: item.total,
              remain: item.remain
            };
          }
        });
        $scope.productKeyCredits = productKeyCredits;
        console.log('[productKeyCredits loaded]', productKeyCredits);
        callback(productKeyCredits);
      });
    }

    function setProductKeyCredits(productBases, productKeyCredits) {
      $.each(productBases, function (i, item) {
        if (item && productKeyCredits) {
          var credit = item.credit = {total: 0, remain: 0, general: productKeyCredits.general};
          credit.total += productKeyCredits.general.total;
          credit.remain += productKeyCredits.general.remain;
          if (productKeyCredits.creditMap[item.id]) {
            credit.total += productKeyCredits.creditMap[item.id].total;
            credit.remain += productKeyCredits.creditMap[item.id].remain;
          }
          credit.percentage = (credit.remain * 100 / credit.total) | 0;
        }
      });
    }

    $scope.productBase = {
      showCreateProduct: function () {
        productBaseDataService.setProId('');
        productBaseDataService.setTitle('产品创建');
        $location.path('/emulator');
      },
      showProductBaseDetails: function (proId) {
        productBaseDataService.setProId(proId);
        $location.path('/product-view');
      },
      editProductBaseDetails: function (proId) {
        productBaseDataService.setProId(proId);
        productBaseDataService.setTitle('产品编辑');

        productBaseManageService.getProDetails(proId, function (data) {
          productBaseDataService.setDetails(data);
          $location.path('/emulator');
        }, function () {
          $scope.utils.alert('info', '获取产品信息失败');
        });
      },
      deleteProductBaseDetails: function (proId) {

      }
    };


  }]);
})();
