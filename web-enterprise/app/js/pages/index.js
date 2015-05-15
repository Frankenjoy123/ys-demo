(function () {
    var app = angular.module('root', [
        'ngRoute',
        //'ngAnimate',
        'YUNSOO_CONFIG',
        'interceptor',
        //'head',
        //'nav',
        'dashboard',
        'accountManage',
        'productBaseManage',
        'productKeyManage',
        'msg',
        'logistics',
        'logisticsManage',
        'package',
        'packageSearch',
        'setting'
    ]);


    //config root
    app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/dashboard', {
                templateUrl: 'pages/dashboard/dashboard.html',
                controller: 'dashboardCtrl'
            })
            .when('/account', {
                templateUrl: 'pages/account/account-manage.html',
                controller: 'accountManageCtrl'
            })
            .when('/product-base-manage', {
                templateUrl: 'pages/product/product-base-manage.html',
                controller: 'productBaseManageCtrl'
            })
            .when('/product-key-manage', {
                templateUrl: 'pages/product/product-key-manage.html',
                controller: 'productKeyManageCtrl'
            })
            .when('/msg', {
                templateUrl: 'pages/msg/msg.html',
                controller: 'msgCtrl'
            })
            .when('/test', {
                templateUrl: 'pages/empty.html'
            })
            .when('/packageManage', {
                templateUrl: 'pages/package/packageManage.html',
                controller: 'packageCtrl'
            })
            .when('/packageSearch', {
                templateUrl: 'pages/package/packageSearch.html',
                controller: 'packageSearchCtrl'
            })
            .when('/logistics', {
                templateUrl: 'pages/logistics/logistics.html',
                controller: 'logisticsManageCtrl'
            })
            .when('/search', {
                templateUrl: 'pages/search/search.html'
            })
            .when('/setting', {
                templateUrl: 'pages/setting/setting.html',
                controller: 'settingCtrl'
            })
            .otherwise({
                templateUrl: 'pages/dashboard/dashboard.html',
                controller: 'dashboardCtrl'
            });
    }]);


    app.controller('rootCtrl', ['$scope', '$timeout', '$http', 'YUNSOO_CONFIG', function ($scope, $timeout, $http, YUNSOO_CONFIG) {
        console.log('[root controller start]');
        //YUNSOO_CONFIG
        $scope.YUNSOO_CONFIG = YUNSOO_CONFIG;

        //context
        $scope.context || ($scope.context = {
            getAccessToken: function () {
                var accessToken = $.cookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN);
                return typeof accessToken === 'string' ? accessToken : null;
            }
        });

        //check authentication
        if (!$scope.context.getAccessToken()) {
            //redirect back to login page
            window.location.href = 'login.html';
        }

        //utils
        $scope.utils || ($scope.utils = {
            formatDateString: function (value) {
                return new DateTime(value).toString('yyyy-MM-dd HH:mm:ss');
            },
            logout: function () {
                console.log('[logout]');
                $.removeCookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN, {path: '/'});
                window.location.href = 'login.html';
            },
            lock: function () {
                console.log('[lock screen]');
                $.niftyNoty({
                    type: 'warning',
                    message: '锁屏界面开发中...',
                    container: 'floating',
                    timer: 3000
                });
            },
            /**
             * shortcut of $.niftyNoty, show floating notification on the top right
             * @param type string ["info", "primary", "success", "warning", "danger", "mint", "purple", "pink", "dark"]
             * @param message string
             * @param title string
             */
            notification: function (type, message, title) {
                $.niftyNoty && $.niftyNoty({
                    type: type,
                    container: 'floating',
                    title: title,
                    message: message,
                    timer: 3000
                });
            },
            /**
             * shortcut of $.niftyNoty, show alert, default in page top
             * @param type string ["info", "primary", "success", "warning", "danger", "mint", "purple", "pink", "dark"]
             * @param message string
             * @param container string ["floating", "page"] | "jQuery selector"
             */
            alert: function (type, message, container) {
                $.niftyNoty && $.niftyNoty({
                    type: type,
                    container: container || 'page',
                    message: message,
                    timer: 3000
                });
            }
        });

        //load current account info
        $http.get('/api/account/current').success(function (data) {
            $scope.context.account = data;
            console.log('[get current account]', data);
        }).error(function (data, code) {
            console.log('[get current account]', 'failed', code);
        });

        //show welcome message
        $timeout(function () {
            $scope.utils.notification('info', '欢迎登陆云溯管理平台');
        }, 3000);

        console.log('[root controller end]');
    }]);//end of controller

})();
