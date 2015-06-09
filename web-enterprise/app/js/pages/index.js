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


            //show welcome message
            $timeout(function () {
                $scope.utils.notification('info', '欢迎登陆云溯管理平台');
            }, 3000);

            console.log('[root controller end]');
        }
    ]);//end of controller

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
    }).directive('dtPageable', function () {
        return {
            restrict: 'A',
            scope: {
                pageable: '=dtPageable'
            },
            templateUrl:'partials/widgets/pageable.html'
        };
    });

})(jQuery);
