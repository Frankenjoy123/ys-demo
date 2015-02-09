(function () {
    angular.module("interceptor", [])
            .factory('interceptorTrack', ["$q","$templateCache", function ($q, $templateCache) {
                    var tracker = {
                        request: function (config) {
                            return config || $q.when(config);
                        },
                        requestError: function (rejection) {
                            return $q.reject(rejection);
                        },
                        response: function (response) {
                            return response || $q.when(response);
                        },
                        responseError: function (rejection) {
                            return $q.reject(rejection);
                        }
                    };
                    return tracker;
                }])
            .config(["$httpProvider", function ($httpProvider) {
                    $httpProvider.interceptors.push('interceptorTrack');
                }]);
})();