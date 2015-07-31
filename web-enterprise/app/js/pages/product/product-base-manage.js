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
      },
      deleteProWithDetail: function (proId, fnSuccess, fnError) {
        $http.delete('/api/productbase/' + proId).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.factory('productBaseDataService', function () {
    var savedData = {
      title: '',
      currProId: '',
      curProStatus: '',
      proDetails: null,
      isCreateMode: true,
      proStatusShow: {wait: '待审核', active: '已激活', reject: '被拒绝', none: '未知'}
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

    function getCurProStatus() {
      return savedData.curProStatus;
    }

    function setCurProStatus(data) {
      savedData.curProStatus = data;
    }

    function getProStatusShow() {
      return savedData.proStatusShow;
    }

    return {
      getDetails: getDetails,
      setDetails: setDetails,
      getProId: getProId,
      setProId: setProId,
      getTitle: getTitle,
      setTitle: setTitle,
      getMode: getMode,
      setMode: setMode,
      getCurProStatus: getCurProStatus,
      setCurProStatus: setCurProStatus,
      getProStatusShow: getProStatusShow
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

    var proVersionStatus = {draft: '待提交', submitted: '待审核', rejected: '被拒绝', activated: '已激活', archived: '已归档'};

    var formatProStatusShow = $scope.formatProStatusShow = function (statusCode) {
      return productBaseDataService.getProStatusShow()[statusCode];
    };

    var formatProVersionStatus = $scope.formatProVersionStatus = function (statusCode) {
      return proVersionStatus[statusCode];
    };

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

          for (var item in data) {

            data[item].is_editable = false;
            data[item].status_code_show = 'none';

            if (data[item].product_base_versions) {
              if (data[item].product_base_versions[data[item].product_base_versions.length - 1].status_code == 'submitted') {
                data[item].status_code_show = 'wait';
              }
              else if (data[item].product_base_versions[data[item].product_base_versions.length - 1].status_code == 'rejected') {
                data[item].status_code_show = 'reject';
                data[item].product_base_versions[data[item].product_base_versions.length - 1].is_editable = true;
              }
              else if (data[item].product_base_versions[data[item].product_base_versions.length - 1].status_code == 'activated') {
                data[item].status_code_show = 'active';
                data[item].is_editable = true;
              }
            }
          }

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

    var productBase = $scope.productBase = {
      curDeleteProId: '',
      curDeleteProName: '',
      showCreateProduct: function () {
        productBaseDataService.setProId('');
        productBaseDataService.setTitle('产品创建');
        $location.url('/emulator?title=产品创建&proId=');
      },
      showProductBaseDetails: function (proId, status, verId) {
        productBaseDataService.setProId(proId);
        productBaseDataService.setCurProStatus(status);

        verId = verId || '';
        var url = '/product-view?proId=' + proId + '&proStatus=' + status + '&verId=' + verId;
        $location.url(url);
      },
      editProductBaseDetails: function (proId, verId) {
        productBaseDataService.setProId(proId);
        productBaseDataService.setTitle('产品编辑');

        verId = verId || '';
        var url = '/emulator?title=产品编辑&proId=' + proId + '&verId=' + verId;
        $location.url(url);
      },
      deleteProductBaseDetails: function () {
        if (productBase.curDeleteProId != '') {
          productBaseManageService.deleteProWithDetail(productBase.curDeleteProId, function (data) {

            productBase.curDeleteProId = '';
            productBase.curDeleteProName = '';

            $('#deleteConfirmDialog').modal('hide');

            $location.path('/product-base-manage');
            $scope.utils.alert('info', '删除产品成功');

          }, function () {
            $scope.utils.alert('danger', '删除产品失败', '#deleteConfirmDialog .modal-dialog', false);
          });
        }
      },
      showDeleteProModal: function (id, name) {
        productBase.curDeleteProId = id;
        productBase.curDeleteProName = name;

        $('#deleteConfirmDialog').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
          $(this).removeData("bs.modal");
        });
      },
      deleteProductBaseDetailsCancel: function () {
        productBase.curDeleteProId = '';
        productBase.curDeleteProName = '';

        $('#deleteConfirmDialog').modal('hide');
      }
    };


  }]);
})();
