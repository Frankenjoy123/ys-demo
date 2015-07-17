(function ($) {
  var app = angular.module('root', [
    'ngRoute',
    //'ngAnimate',
    'YUNSOO_CONFIG',
    'utils',
    'interceptor',
    'angularFileUpload',
    'dataFilterService'
  ]);

  //config root
  app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/dashboard', {
          templateUrl: 'pages/dashboard/dashboard.html',
          controller: 'DashboardCtrl'
        })
        .when('/account', {
          templateUrl: 'pages/account/account-manage.html',
          controller: 'AccountManageCtrl'
        })
        .when('/product-base-manage', {
          templateUrl: 'pages/product/product-base-manage.html',
          controller: 'ProductBaseManageCtrl'
        })
        .when('/product-key-manage', {
          templateUrl: 'pages/product/product-key-manage.html',
          controller: 'ProductKeyManageCtrl'
        })
        .when('/message', {
          templateUrl: 'pages/message/message.html',
          controller: 'MessageCtrl'
        })
        .when('/package-manage', {
          templateUrl: 'pages/package/packageManage.html',
          controller: 'PackageCtrl'
        })
        .when('/package-search', {
          templateUrl: 'pages/package/packageSearch.html',
          controller: 'PackageSearchCtrl'
        })
        .when('/logistics', {
          templateUrl: 'pages/logistics/logistics.html',
          controller: 'LogisticsManageCtrl'
        })
        .when('/search', {
          templateUrl: 'pages/search/search.html',
          controller: 'SearchCtrl'
        })
        .when('/setting', {
          templateUrl: 'pages/setting/setting.html',
          controller: 'SettingCtrl'
        })
        .when('/emulator', {
          templateUrl: 'pages/emulator/emulator.html',
          controller: 'emulatorCtrl'
        })
        .when('/device', {
          templateUrl: 'pages/device/device.html',
          controller: 'deviceCtrl'
        })
        .when('/echarts-bar', {
          templateUrl: 'pages/echarts/echarts-bar.html',
          controller: 'echartsBarCtrl'
        })
        .when('/stacked-bar', {
          templateUrl: 'pages/echarts/stacked-bar.html',
          controller: 'stackedBarCtrl'
        })
        .when('/mix-map', {
          templateUrl: 'pages/echarts/mix-map.html',
          controller: 'mixMapCtrl'
        })
        .when('/pie-map', {
          templateUrl: 'pages/echarts/pie-map.html',
          controller: 'pieMapCtrl'
        })
        .when('/group', {
          templateUrl: 'pages/group/group.html',
          controller: 'groupCtrl'
        })
        .when('/no_access', {
          templateUrl: '403.html'
        })
        .otherwise({
          templateUrl: 'pages/dashboard/dashboard.html',
          controller: 'DashboardCtrl'
        });
  }]);

  app.factory('rootService', ['$http', '$rootScope', function ($http, $rootScope) {
    return {
      initContext: function () {
        var context = this;
        //load current account info
        $http.get('/api/account/current').success(function (data) {
          context.account = data;
          console.log('[get current account]', data);
        }).error(function (data, code) {
          console.log('[get current account]', 'failed', code);
        });

        //load current organization info
        $http.get('/api/organization/current').success(function (data) {
          context.organization = data;
          console.log('[get current organization]', data);

          $rootScope.$broadcast('context-organization-ready');
        }).error(function (data, code) {
          console.log('[get current organization]', 'failed', code);
        });
      },
      refreshAccessToken: function (permanentToken, fnSuccess) {
        //refresh access token
        permanentToken && $http.get('/api/auth/accesstoken?permanent_token=' + permanentToken).success(fnSuccess);
      },
      getAccountAllPermissions: function (fnSuccess, fnError) {
        var url = '/api/account/current/permission';
        $http.get(url).success(fnSuccess).error(fnError);
      }
    };
  }]);

  app.controller('rootCtrl', ['$scope', '$timeout', '$http', 'YUNSOO_CONFIG', 'utils', 'rootService', 'productBaseManageService', '$location',
    function ($scope, $timeout, $http, YUNSOO_CONFIG, utils, rootService, productBaseManageService, $location) {
      console.log('[root controller start]');

      var RESOURCE = YUNSOO_CONFIG.PAGE_ACCESS.RESOURCE;
      var ACTION = YUNSOO_CONFIG.PAGE_ACCESS.ACTION;

      //YUNSOO_CONFIG
      $scope.YUNSOO_CONFIG = YUNSOO_CONFIG;

      //utils
      $scope.utils = utils;

      //context
      $scope.context = {};
      window._debug && (window._debug.context = $scope.context); //debug only

      //check auth
      var auth = utils.auth.getAuth();
      if (auth && utils.auth.getPermanentToken()) {
        //refresh access token before expires
        setTimeout(function refresh() {
          rootService.refreshAccessToken(utils.auth.getPermanentToken(), function (data) {
            console.log('[access token]', 'refreshed successfully');
            utils.auth.setAccessToken(data.token, DateTime.now().addSeconds(data.expires_in).valueOf());
            if (data.expires_in > 60) {
              setTimeout(refresh, (data.expires_in / 1.2 /*seconds*/) * 1000);
            }
          });
        }, new DateTime(auth.accessToken.expires).addSeconds(-30) - DateTime.now());
      } else {
        //redirect back to login page
        window.location.href = 'login.html';
      }

      $scope.productKeyCreditSum = {
        total: 0,
        remain: 0,
        percentage: 0
      };

      //init current context
      rootService.initContext.apply($scope.context);


      //load product key credit for product key widget on the main menu
      productBaseManageService.getProductKeyCredits(function (data) {
        var sum = $scope.productKeyCreditSum;
        $.each(data, function (i, item) {
          sum.total += item.total;
          sum.remain += item.remain;
        });
        sum.percentage = (sum.remain * 100 / sum.total) | 0;
        console.log('[get productKeyCreditSum]: ', sum);
        sum.ready = true;
        $scope.$broadcast('productKeyCreditSum-ready', sum);
      });

      rootService.getAccountAllPermissions(function (data) {
        $scope.currAccountAllPermissions = data;
        $scope.$broadcast('accountPermissions-ready', data);
      });

      var hasAccess = $scope.hasAccess = function (resource, action) {

        var data = $scope.currAccountAllPermissions;

        if (data) {
          for (var i = 0; i < data.length; i++) {
            if (data[i].resource_code == '*' && (data[i].action_code == '*')) {
              return true;
            }
            else {
              if (data[i].resource_code == resource && (data[i].action_code == action)) {
                return true;
              }
            }
          }
        }

        return false;
      };

      function ApplyAccess(path) {

        var isShowSetting = false;

        if (!hasAccess(RESOURCE.DASHBOARD, ACTION.READ)) {
          $('#liDashBoard').attr('style', "display:none");
          if (path == '/dashboard') {
            $location.path('/no_access');
          }
        }

        if (!hasAccess(RESOURCE.PRODUCTKEY, ACTION.READ) && !hasAccess(RESOURCE.PRODUCTKEY, ACTION.MANAGE)) {
          $('#liProductKey').attr('style', "display:none");
          if (path == '/product-key-manage') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.PRODUCTKEY, ACTION.READ) && !hasAccess(RESOURCE.PRODUCTKEY, ACTION.MANAGE)) {
          $scope.productKeyButtonShow = false;
        }
        else if (hasAccess(RESOURCE.PRODUCTKEY, ACTION.MANAGE)) {
          $scope.productKeyButtonShow = true;
        }

        if (!hasAccess(RESOURCE.PACKAGE, ACTION.READ) && !hasAccess(RESOURCE.PACKAGE, ACTION.MANAGE)) {
          $('#liPackageManage').attr('style', "display:none");
          if (path == '/package-manage') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.PACKAGE, ACTION.READ) && !hasAccess(RESOURCE.PACKAGE, ACTION.MANAGE)) {
          $scope.packageButtonShow = false;
        }
        else if (hasAccess(RESOURCE.PACKAGE, ACTION.MANAGE)) {
          $scope.packageButtonShow = true;
        }

        if (!hasAccess(RESOURCE.LOGISTICS, ACTION.READ) && !hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $('#liLogistics').attr('style', "display:none");
          if (path == '/logistics') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.READ) && !hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.logisticsButtonShow = false;
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.logisticsButtonShow = true;
        }

        if (!hasAccess(RESOURCE.REPORT, ACTION.READ)) {
          $('#liAllCharts').attr('style', "display:none");
          if (path == '/echarts-bar' || path == '/stacked-bar' || path == '/mix-map' || path == '/pie-map') {
            $location.path('/no_access');
          }
        }

        if (!hasAccess(RESOURCE.PRODUCTBASE, ACTION.READ) && !hasAccess(RESOURCE.PRODUCTBASE, ACTION.MANAGE)) {
          $('#liProductBase').attr('style', "display:none");
          if (path == '/product-base-manage' || path == '/emulator') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.READ) && !hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.productBaseButtonShow = false;
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.productBaseButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.MESSAGE, ACTION.READ) && !hasAccess(RESOURCE.MESSAGE, ACTION.MANAGE)) {
          $('#liMessage').attr('style', "display:none");
          if (path == '/message') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.MESSAGE, ACTION.READ) && !hasAccess(RESOURCE.MESSAGE, ACTION.MANAGE)) {
          $scope.messageButtonShow = false;
        }
        else if (hasAccess(RESOURCE.MESSAGE, ACTION.MANAGE)) {
          $scope.messageButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.DEVICE, ACTION.READ) && !hasAccess(RESOURCE.DEVICE, ACTION.MANAGE)) {
          $('#liDevice').attr('style', "display:none");
          if (path == '/device') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.DEVICE, ACTION.READ) && !hasAccess(RESOURCE.DEVICE, ACTION.MANAGE)) {
          $scope.deviceButtonShow = false;
        }
        else if (hasAccess(RESOURCE.DEVICE, ACTION.MANAGE)) {
          $scope.deviceButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.ACCOUNT, ACTION.READ) && !hasAccess(RESOURCE.ACCOUNT, ACTION.MANAGE)) {
          $('#liAccount').attr('style', "display:none");
          if (path == '/account') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.ACCOUNT, ACTION.READ) && !hasAccess(RESOURCE.ACCOUNT, ACTION.MANAGE)) {
          $scope.accountButtonShow = false;
        }
        else if (hasAccess(RESOURCE.ACCOUNT, ACTION.MANAGE)) {
          $scope.accountButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.GROUP, ACTION.READ) && !hasAccess(RESOURCE.GROUP, ACTION.MANAGE)) {
          $('#liGroup').attr('style', "display:none");
          if (path == '/group') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.GROUP, ACTION.READ) && !hasAccess(RESOURCE.GROUP, ACTION.MANAGE)) {
          $scope.groupButtonShow = false;
        }
        else if (hasAccess(RESOURCE.GROUP, ACTION.MANAGE)) {
          $scope.groupButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.PROFILE, ACTION.READ)) {
          $('#liPassword').attr('style', "display:none");
          if (path == '/setting') {
            $location.path('/no_access');
          }
        }
        else {
          isShowSetting = true;
        }

        if (!isShowSetting) {
          $('#liAllSettings').attr('style', "display:none");
        }
      }

      $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
        var path = current.$$route ? current.$$route.originalPath : '/dashboard';

        if ($scope.currAccountAllPermissions) {
          ApplyAccess(path);
        }
        else {
          $scope.$on('accountPermissions-ready', function (data) {
            ApplyAccess(path);
          });
        }
      });

      //show welcome message
      $timeout(function () {
        $scope.utils.notification('info', '欢迎登陆云溯管理平台');
      }, 3000);


      console.log('[root controller end]');
    }
  ])
  ;//end of controller

  app.directive('scrollTop', function () {
    return {
      restrict: 'A',
      scope: true,
      link: function (scope, $element, attrs) {
        if (!nifty.isMobile) {
          var isVisible = false;
          var offsetTop = 250;
          var $window = $(window);
          var $bodyHtml = $('body, html');

          $window.scroll(function () {
            if ($window.scrollTop() > offsetTop && !isVisible) {
              //nifty.navbar.addClass('shadow');
              $element.addClass('in');
              isVisible = true;
            } else if ($window.scrollTop() < offsetTop && isVisible) {
              //nifty.navbar.removeClass('shadow');
              $element.removeClass('in');
              isVisible = false;
            }
          });

          $element.on('click', function (e) {
            e.preventDefault();

            $bodyHtml.animate({
              scrollTop: 0
            }, 500);
          });
        }
      }
    };
  });

})(jQuery);
