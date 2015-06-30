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
      getCurrentAccountPolicies: function (fnSuccess, fnError) {
        var url = '/api/account/current/permission';
        $http.get(url).success(fnSuccess).error(fnError);
      }
    };
  }]);

  app.controller('rootCtrl', ['$scope', '$timeout', '$http', 'YUNSOO_CONFIG', 'utils', 'rootService', 'productBaseManageService',
    function ($scope, $timeout, $http, YUNSOO_CONFIG, utils, rootService, productBaseManageService) {
      console.log('[root controller start]');

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
              setTimeout(refresh, (data.expires_in - 30/*seconds*/) * 1000);
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

      rootService.getCurrentAccountPolicies(function (data) {
        $scope.currAccountPolicies = data;
        $scope.$broadcast('accountPolicies-ready', data);
      });

      var hasAccess = $scope.hasAccess = function (data, path, type) {
        return true;
      };

      $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
        var path = current.$$route ? current.$$route.originalPath : '/dashboard';

        if ($scope.currAccountPolicies) {
          if (!hasAccess($scope.currAccountPolicies, '/dashboard', 'read')) {
            $('#liDashBoard').attr('style', "display:none");
            if (path == '/dashboard') {
              window.location.href = '403.html';
            }
          }

          //$('#liProductKey').attr('style', "display:none");
          //$('#liPackageManage').attr('style', "display:none");
          //$('#liLogistics').attr('style', "display:none");
          //$('#liAllCharts').attr('style', "display:none");
          //$('#liAllSettings').attr('style', "display:none");
          //$('#liProductBase').attr('style', "display:none");
          //$('#liMessage').attr('style', "display:none");
          //$('#liDevice').attr('style', "display:none");
          //$('#liAccount').attr('style', "display:none");
          //$('#liGroup').attr('style', "display:none");
          //$('#liPassword').attr('style', "display:none");
        }
        else {
          $scope.$on('accountPolicies-ready', function (data) {


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
