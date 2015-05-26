(function () {
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
            .otherwise({
                templateUrl: 'pages/dashboard/dashboard.html',
                controller: 'DashboardCtrl'
            });
    }]);

    app.factory('rootService', ['$http', '$rootScope', function ($http) {
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
                }).error(function (data, code) {
                    console.log('[get current organization]', 'failed', code);
                });
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
            $scope.context = {
                getAccessToken: function () {
                    var accessToken = $.cookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN);
                    return typeof accessToken === 'string' ? accessToken : null;
                }
            };

            //check authentication
            if (!$scope.context.getAccessToken()) {
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
            });

            //init menu
            var menu = {};
            $('#mainnav').find('#mainnav-menu').find('>li').each(function (i, item) {
                var $item = $(item);
                var name = $item.find('>a').attr('href');
                var menuItem = {$html: $item};
                name && (menu[name] = menuItem);
                $item.find('>ul>li').each(function (j, subItem) {
                    var $subItem = $(subItem);
                    var subName = $subItem.find('>a').attr('href');
                    subName && (menu[subName] = {$html: $subItem, parent: menuItem});
                });
            });
            $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
                var mainnav = $('#mainnav');
                mainnav.find('li.active-link').removeClass('active-link');
                mainnav.find('li.active-sub').removeClass('active-sub');
                var path = current.$$route ? current.$$route.originalPath : '/dashboard';
                var menuItem = menu['#' + path];
                if (menuItem) {
                    menuItem.$html.addClass('active-link');
                    if (menuItem.parent) {
                        menuItem.parent.$html.addClass('active-sub');
                        menuItem.$html.parent('ul.collapse').addClass('in');
                    }
                }
            });

            //show welcome message
            $timeout(function () {
                $scope.utils.notification('info', '欢迎登陆云溯管理平台');
            }, 3000);

            console.log('[root controller end]');
        }
    ]);
//end of controller

})();
