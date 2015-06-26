(function () {
    function startAjax(otherSettings) {
        //$("body").append('<div class="ajax-loading-wrap"><div class="ajax-loading"></div></div>');
    }

    function endAjax(loading) {
        //$("body .ajax-loading-wrap").remove();
    }

    function absolute(url) {
        if (url.toUpperCase().indexOf('HTTP') === 0) {
            return url;
        }

        // We set the root url to <base> tag in <head>.
        var root = $('head').first().children('base').attr('href');

        if (!root) {
            return url;
        }

        // Make sure root ends with a slash.
        if (root.charAt(root.length - 1) !== '/') {
            root += '/';
        }

        // make sure url doesn't begin with a slash
        if (url.charAt(0) === '/') {
            url = url.substr(1);
        }

        return root + url;
    }

    angular.module("interceptor", ['YUNSOO_CONFIG', 'utils'])
        .factory('interceptorTrack', ["$q", "$templateCache", 'YUNSOO_CONFIG', 'utils', function ($q, $templateCache, YUNSOO_CONFIG, utils) {
            return {
                request: function (config) {
                    if ($templateCache && $templateCache.get(config.url)) {
                        return config;
                    } else {
                        // show global loading.gif
                        config.loading = startAjax();

                        config.url = absolute(config.url);
                        config.cache = false;
                        config.headers['X-Requested-With'] = 'XMLHttpRequest';
                        config.headers[YUNSOO_CONFIG.HEADER_APP_ID] = YUNSOO_CONFIG.APP_ID;

                        //access token
                        var accessToken = utils.auth.getAccessToken();
                        (typeof accessToken === 'string') && (config.headers[YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

                        return config;
                    }
                },
                requestError: function (rejection) {
                    return $q.reject(rejection);
                },
                response: function (response) {
                    if ($templateCache && $templateCache.get(response.config.url)) {
                        return response;
                    }
                    // hide global loading.gif
                    endAjax(response.config.loading);

                    try {
                        response = JSON.parse(response);
                    } catch (e) {
                        //return $q.reject(response);
                    }
                    return response;
                },
                responseError: function (rejection) {
                    if (rejection.config) {
                        endAjax(rejection.config.loading);
                    }
                    if (rejection.status == 401) {
                        if (rejection.config.url.indexOf('/api/auth/login') < 0) {
                            window.location.href = 'login.html';
                        }
                    } else if (rejection.status == 403) {
                        utils.notification('danger', '没有操作权限');
                    } else if (rejection.status >= 500 && rejection.status < 600) {
                        utils.notification('danger', '服务器错误，请稍后重试');
                    }
                    return $q.reject(rejection);
                }
            };
        }])
        .config(["$httpProvider", function ($httpProvider) {
            $httpProvider.interceptors.push('interceptorTrack');
        }]);
})();