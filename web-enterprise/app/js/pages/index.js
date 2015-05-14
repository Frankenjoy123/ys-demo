(function () {
    var app = angular.module('root', [
        'ngRoute',
        'ngAnimate',
        'YUNSOO_CONFIG',
        'interceptor'
        //'head',
        //'nav',
        //'accountManage',
        //'productBaseManage',
        //'productKeyManage',
        //'msg',
        //'logistics',
        //'logisticsManage',
        //'package',
        //'packageSearch',
        //'config'
    ]);


    //config root
    app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/account', {
                templateUrl: 'obsolete/pages/account/manage.html',
                controller: 'accountManageCtrl'
            })
            .when('/product-base-manage', {
                templateUrl: 'obsolete/pages/product/product-base-manage.html',
                controller: 'productBaseManageCtrl'
            })
            .when('/product-key-manage', {
                templateUrl: 'obsolete/pages/product/product-key-manage.html',
                controller: 'productKeyManageCtrl'
            })
            .when('/msg', {
                templateUrl: 'obsolete/pages/msg/msg.html',
                controller: 'msgCtrl'
            })
            .when('/test', {
                templateUrl: 'obsolete/pages/empty.html'
            })
            .when('/packageManage', {
                templateUrl: 'obsolete/pages/package/packageManage.html',
                controller: 'packageCtrl'
            })
            .when('/packageSearch', {
                templateUrl: 'obsolete/pages/package/packageSearch.html',
                controller: 'packageSearchCtrl'
            })
            .when('/logistics', {
                templateUrl: 'obsolete/pages/logistics/logistics.html',
                controller: 'logisticsManageCtrl'
            })
            .when('/search', {
                templateUrl: 'obsolete/pages/search/search.html'
            })
            .when('/config', {
                templateUrl: 'obsolete/pages/config/config.html',
                controller: 'configCtrl'
            })
            .otherwise({
                templateUrl: 'obsolete/pages/product/product-key-manage.html',
                controller: 'productKeyManageCtrl'
            });
    }]);


    app.controller('rootCtrl', ['$scope', '$timeout', '$http', 'YUNSOO_CONFIG', function ($scope, $timeout, $http, YUNSOO_CONFIG) {
        if (!$.cookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN)) {
            //redirect back to login page
            window.location.href = 'login.html';
        }

        //context
        $scope.context || ($scope.context = {});

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
                console.log('[lock screen]'); $.niftyNoty({
                    type: 'warning',
                    message: '锁屏界面开发中...',
                    container: 'floating',
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
            $.niftyNoty({
                type: 'info',
                message: '欢迎登陆云溯管理平台',
                container: 'floating',
                timer: 3000
            });
        }, 3000);

    }
    ])
    ;
})
();
