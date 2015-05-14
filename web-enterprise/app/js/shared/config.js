(function () {
    angular.module('YUNSOO_CONFIG', [])
        .factory('YUNSOO_CONFIG', function () {
            return {
                NAME_ACCESS_TOKEN: 'YS_ACCESS_TOKEN',
                NAME_PERMANENT_TOKEN: 'YS_PERMANENT_TOKEN',

                HEADER_ACCESS_TOKEN: 'X-YS-AccessToken',
                HEADER_APP_ID: 'X-YS-AppId',

                PARAMETER_ACCESS_TOKEN: 'access_token',

                APP_ID: '2k2juhux4z5d6eceh4v'
            };
        });
})();