(function () {
    var HEADER_CSRF_TOKEN = "CSRF-TOKEN";
    var E_CSRF_TOKEN = "SESSION-CSRF-TOKEN";

    function startAjax(otherSettings) {
        $("body").append('<div class="ajax-loading-wrap"><div class="ajax-loading"></div></div>');
    }

    function endAjax(loading) {
        $("body .ajax-loading-wrap").remove();
    }

    function absolute(url) {
        if (url.toUpperCase().indexOf('HTTP') === 0) {
            return url;
        }

        // We set the root url to <base> tag in <head>.
        var root = $('head').first().children('base').attr('href')

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

    function csrfSafeMethod(method) {
        // these HTTP methods do not require CSRF protection
        return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
    }

    function sameOrigin(url) {
        // test that a given url is a same-origin URL
        // url could be relative or scheme relative or absolute
        var host = document.location.host; // host + port
        var protocol = document.location.protocol;
        var sr_origin = '//' + host;
        var origin = protocol + sr_origin;
        // Allow absolute or scheme relative URLs to same origin
        return (url === origin || url.slice(0, origin.length + 1) === origin + '/') ||
            (url === sr_origin || url.slice(0, sr_origin.length + 1) === sr_origin + '/') ||
                // or any other URL that isn't scheme relative or absolute i.e relative.
            !(/^(\/\/|http:|https:).*/.test(url));
    }

    function getCsrfToken() {
        return $(E_CSRF_TOKEN).val();
    }

    function addCsrfHeader(jqXHR, ajaxOptions) {
        // add request header of Ajax CSRF Token for POST requests
        if (!csrfSafeMethod(ajaxOptions.type) && sameOrigin(ajaxOptions.url)) {
            jqXHR.setRequestHeader(HEADER_CSRF_TOKEN, getCsrfToken());
        }
    }

    angular.module("interceptor", [])
        .factory('interceptorTrack', ["$q", "$templateCache", function ($q, $templateCache) {
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
                        var accessToken = $.cookie(YUNSOO_CONFIG.AUTH_COOKIE_NAME);
                        accessToken && (config.headers[YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

                        if (!csrfSafeMethod(config.method) && sameOrigin(config.url)) {
                            config.headers[HEADER_CSRF_TOKEN] = getCsrfToken();
                        }

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
                    return $q.reject(rejection);
                }
            };
        }])
        .config(["$httpProvider", function ($httpProvider) {
            $httpProvider.interceptors.push('interceptorTrack');
        }]);
})();