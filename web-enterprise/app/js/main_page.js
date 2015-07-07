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

(function () {
    var app = angular.module('login', ['interceptor', 'YUNSOO_CONFIG']);

    app.factory('loginService', ['$http', function ($http) {
        return {
            login: function (loginForm, onSuccess, onError) {
                $http.post('/api/auth/login', {
                    organization: loginForm.organization,
                    identifier: loginForm.identifier,
                    password: loginForm.password
                }).success(onSuccess).error(onError);
            },
            loginForm: function (loginForm) {
                if (loginForm) {
                    localStorage.loginForm = JSON.stringify(loginForm);
                } else {
                    return localStorage.loginForm ? JSON.parse(localStorage.loginForm) : null;
                }
            }
        };
    }]);

    app.controller('LoginCtrl', ['$scope', '$timeout', 'loginService', 'YUNSOO_CONFIG',
        function ($scope, $timeout, loginService, YUNSOO_CONFIG) {

            ($scope.loginForm = loginService.loginForm()) || ($scope.loginForm = {
                organization: '',
                identifier: '',
                password: '',
                rememberMe: false
            });

            function login() {
                var loginForm = $scope.loginForm;
                //trim
                loginForm.organization = $.trim(loginForm.organization);
                loginForm.identifier = $.trim(loginForm.identifier);

                console.log('[before login]', 'organization:', loginForm.organization, 'identifier:', loginForm.identifier);

                loginService.login(loginForm, function (data) {
                    if (!data || !data.access_token || !data.access_token.token) {
                        $.niftyNoty({
                            type: 'danger',
                            container: '#panel-login',
                            html: '登陆失败请稍后再试',
                            focus: false,
                            timer: 3000
                        });
                        return;
                    }

                    //save auth
                    $.removeCookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN, {path: '/'});
                    $.cookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN, data.access_token.token, {
                        expires: data.access_token.expires_in / (60 * 60 * 24),
                        path: '/'
                    });

                    //save current login form
                    if (loginForm.rememberMe) {
                        loginForm.password = '';
                        console.log('[saving login form]');
                        loginService.loginForm(loginForm);
                    }

                    //animation
                    $('body').addClass('animated fadeOut');
                    $('#panel-login').addClass('animated zoomOut');
                    //redirect to index
                    $timeout(function () {
                        window.location.href = 'index.html';
                    }, 1000);

                }, function (data, code) {
                    console.log('[login failed]', data.message, code);
                    $scope.loginForm.password = '';
                    $.niftyNoty({
                        type: 'danger',
                        container: '#panel-login',
                        html: '账号或密码错误',
                        focus: false,
                        timer: 3000
                    });
                });
            }

            //init  validator
            $timeout(function () {
                $('form.bv-form').bootstrapValidator({
                    message: '非法输入',
                    feedbackIcons: {
                        valid: 'fa fa-check-circle fa-lg text-success',
                        invalid: 'fa fa-times-circle fa-lg',
                        validating: 'fa fa-refresh'
                    },
                    fields: {
                        organization: {
                            validators: {
                                notEmpty: {
                                    message: '组织名称不可空'
                                }
                            }
                        },
                        identifier: {
                            validators: {
                                notEmpty: {
                                    message: '用户名不可空'
                                }
                            }
                        },
                        password: {
                            validators: {
                                notEmpty: {
                                    message: '密码不可空'
                                }
                            }
                        }
                    }
                }).on('success.form.bv', function (e, data) {
                    e.preventDefault();
                    //login on validation success
                    login();
                });

            }, 0); //end of $timeout

            //show login form
            $timeout(function () {
                $scope.showLoginFormDelay = true;
            }, 1500);
        }]);

})();
(function ($) {
    "use strict";
    var app = angular.module('root');

    app.factory('navService', ['$http', function ($http) {
        return {};
    }]);

    app.controller('NavCtrl', ['$scope', 'navService', function ($scope, navService) {

        var $menulink = $('#mainnav-menu > li > a, #mainnav-menu-wrap .mainnav-widget a[data-toggle="menu-widget"]'),
            mainNavHeight = $('#mainnav').height(),
            scrollbar = null,
            updateMethod = false,
            isSmallNav = false,
            screenCat = null,
            defaultSize = null;


        // Determine and bind hover or "touch" event
        // ===============================================
        var bindSmallNav = function () {
            var hidePopover;

            $menulink.each(function () {
                var $el = $(this),
                    $listTitle = $el.children('.menu-title'),
                    $listSub = $el.siblings('.collapse'),
                    $listWidget = $($el.attr('data-target')),
                    $listWidgetParent = ($listWidget.length) ? $listWidget.parent() : null,
                    $popover = null,
                    $poptitle = null,
                    $popcontent = null,
                    $popoverSub = null,
                    popoverPosBottom = 0,
                    popoverCssBottom = 0,
                    elPadding = $el.outerHeight() - $el.height() / 4,
                    listSubScroll = false,
                    elHasSub = function () {
                        if ($listWidget.length) {
                            $el.on('click', function (e) {
                                e.preventDefault()
                            });
                        }
                        if ($listSub.length) {
                            //$listSub.removeClass('in').removeAttr('style');
                            $el.on('click', function (e) {
                                e.preventDefault()
                            }).parent('li').removeClass('active');
                            return true;
                        } else {
                            return false;
                        }
                    }(),
                    updateScrollInterval = null,
                    updateScrollBar = function (el) {
                        clearInterval(updateScrollInterval);
                        updateScrollInterval = setInterval(function () {
                            el.nanoScroller({
                                preventPageScrolling: true,
                                alwaysVisible: true
                            });
                            clearInterval(updateScrollInterval);
                        }, 700);
                    };

                $(document).click(function (event) {
                    if (!$(event.target).closest('#mainnav-container').length) {
                        $el.removeClass('hover').popover('hide');
                    }
                });

                $('#mainnav-menu-wrap > .nano').on("update", function (event, values) {
                    $el.removeClass('hover').popover('hide');
                });


                $el.popover({
                    animation: false,
                    trigger: 'manual',
                    container: '#mainnav',
                    viewport: $el,
                    html: true,
                    title: function () {
                        if (elHasSub) return $listTitle.html();
                        return null
                    },
                    content: function () {
                        var $content;
                        if (elHasSub) {
                            $content = $('<div class="sub-menu"></div>');
                            $listSub.addClass('pop-in').wrap('<div class="nano-content"></div>').parent().appendTo($content);
                        } else if ($listWidget.length) {
                            $content = $('<div class="sidebar-widget-popover"></div>');
                            $listWidget.wrap('<div class="nano-content"></div>').parent().appendTo($content);
                        } else {
                            $content = '<span class="single-content">' + $listTitle.html() + '</span>';
                        }
                        return $content;
                    },
                    template: '<div class="popover menu-popover"><h4 class="popover-title"></h4><div class="popover-content"></div></div>'
                }).on('show.bs.popover', function () {
                    if (!$popover) {
                        $popover = $el.data('bs.popover').tip();
                        $poptitle = $popover.find('.popover-title');
                        $popcontent = $popover.children('.popover-content');

                        if (!elHasSub && $listWidget.length == 0)return;
                        $popoverSub = $popcontent.children('.sub-menu');
                    }
                    if (!elHasSub && $listWidget.length == 0)return;
                }).
                    on('shown.bs.popover', function () {
                        if (!elHasSub && $listWidget.length == 0) {
                            var margintop = 0 - (0.5 * $el.outerHeight());
                            $popcontent.css({'margin-top': margintop + 'px', 'width': 'auto'});
                            return;
                        }


                        var offsetTop = parseInt($popover.css('top')),
                            elHeight = $el.outerHeight(),
                            offsetBottom = function () {
                                if (nifty.container.hasClass('mainnav-fixed')) {
                                    return $(window).outerHeight() - offsetTop - elHeight;
                                } else {
                                    return $(document).height() - offsetTop - elHeight;
                                }
                            }(),
                            popoverHeight = $popcontent.find('.nano-content').children().css('height', 'auto').outerHeight();
                        $popcontent.find('.nano-content').children().css('height', '');


                        if (offsetTop > offsetBottom) {
                            if ($poptitle.length && !$poptitle.is(':visible')) elHeight = Math.round(0 - (0.5 * elHeight));
                            offsetTop -= 5;
                            $popcontent.css({
                                'top': '',
                                'bottom': elHeight + 'px',
                                'height': offsetTop
                            }).children().addClass('nano').css({'width': '100%'}).nanoScroller({
                                preventPageScrolling: true
                            });
                            updateScrollBar($popcontent.find('.nano'));
                        } else {
                            if (!nifty.container.hasClass('navbar-fixed') && nifty.mainNav.hasClass('affix-top')) offsetBottom -= 50;
                            if (popoverHeight > offsetBottom) {
                                if (nifty.container.hasClass('navbar-fixed') || nifty.mainNav.hasClass('affix-top')) offsetBottom -= (elHeight + 5);

                                offsetBottom -= 5;
                                $popcontent.css({
                                    'top': elHeight + 'px',
                                    'bottom': '',
                                    'height': offsetBottom
                                }).children().addClass('nano').css({'width': '100%'}).nanoScroller({
                                    preventPageScrolling: true
                                });

                                updateScrollBar($popcontent.find('.nano'));
                            } else {
                                if ($poptitle.length && !$poptitle.is(':visible')) elHeight = Math.round(0 - (0.5 * elHeight));
                                $popcontent.css({'top': elHeight + 'px', 'bottom': '', 'height': 'auto'});
                            }
                        }
                        if ($poptitle.length) $poptitle.css('height', $el.outerHeight());
                        $popcontent.on('click', function () {
                            $popcontent.find('.nano-pane').hide();
                            updateScrollBar($popcontent.find('.nano'));
                        });
                    })
                    .on('hidden.bs.popover', function () {
                        // detach from popover, fire event then clean up data
                        $el.removeClass('hover');
                        if (elHasSub) {
                            $listSub.removeAttr('style').appendTo($el.parent());
                        } else if ($listWidget.length) {
                            $listWidget.appendTo($listWidgetParent);
                        }
                        clearInterval(hidePopover);
                    })
                    .on('click', function () {
                        if (!nifty.container.hasClass('mainnav-sm')) return;
                        $menulink.popover('hide');
                        $el.addClass('hover').popover('show');
                    })
                    .hover(
                    function () {
                        $menulink.popover('hide');
                        $el.addClass('hover').popover('show');
                    },
                    function () {
                        clearInterval(hidePopover);
                        hidePopover = setInterval(function () {
                            if ($popover) {
                                $popover.one('mouseleave', function () {
                                    $el.removeClass('hover').popover('hide');
                                });
                                if (!$popover.is(":hover")) {
                                    $el.removeClass('hover').popover('hide');
                                }
                            }
                            ;
                            clearInterval(hidePopover);
                        }, 500);
                    }
                );
            });
            isSmallNav = true;
        };
        var unbindSmallNav = function () {
            var colapsed = $('#mainnav-menu').find('.collapse');
            if (colapsed.length) {
                colapsed.each(function () {
                    var cl = $(this);
                    if (cl.hasClass('in')) {
                        cl.parent('li').addClass('active');
                    } else {
                        cl.parent('li').removeClass('active');
                    }
                });
            }
            if (scrollbar != null && scrollbar.length) {
                scrollbar.nanoScroller({stop: true});
            }

            $menulink.popover('destroy').unbind('mouseenter mouseleave');
            isSmallNav = false;
        };
        var updateSize = function () {
            //if(!defaultSize) return;

            var sw = nifty.container.width(), currentScreen;


            if (sw <= 740) {
                currentScreen = 'xs';
            } else if (sw > 740 && sw < 992) {
                currentScreen = 'sm';
            } else if (sw >= 992 && sw <= 1200) {
                currentScreen = 'md';
            } else {
                currentScreen = 'lg';
            }

            if (screenCat != currentScreen) {
                screenCat = currentScreen;
                nifty.screenSize = currentScreen;

                if (nifty.screenSize == 'sm' && nifty.container.hasClass('mainnav-lg')) {
                    $.niftyNav('collapse');
                }
            }
        };
        var updateNav = function (e) {
            nifty.mainNav.niftyAffix('update');

            unbindSmallNav();
            updateSize();

            if (updateMethod == 'collapse' || nifty.container.hasClass('mainnav-sm')) {
                nifty.container.removeClass('mainnav-in mainnav-out mainnav-lg');
                bindSmallNav();
            }

            mainNavHeight = $('#mainnav').height();
            updateMethod = false;
            return null;
        };
        var init = function () {
            if (!defaultSize) {
                defaultSize = {
                    xs: 'mainnav-out',
                    sm: nifty.mainNav.data('sm') || nifty.mainNav.data('all'),
                    md: nifty.mainNav.data('md') || nifty.mainNav.data('all'),
                    lg: nifty.mainNav.data('lg') || nifty.mainNav.data('all')
                };

                var hasData = false;
                for (var item in defaultSize) {
                    if (defaultSize.hasOwnProperty(item) && defaultSize[item]) {
                        hasData = true;
                        break;
                    }
                }

                if (!hasData) defaultSize = null;
                updateSize();
            }
        };
        var methods = {
            'revealToggle': function () {
                if (!nifty.container.hasClass('reveal')) nifty.container.addClass('reveal');
                nifty.container.toggleClass('mainnav-in mainnav-out').removeClass('mainnav-lg mainnav-sm')
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'revealIn': function () {
                if (!nifty.container.hasClass('reveal')) nifty.container.addClass('reveal');
                nifty.container.addClass('mainnav-in').removeClass('mainnav-out mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'revealOut': function () {
                if (!nifty.container.hasClass('reveal')) nifty.container.addClass('reveal');
                nifty.container.removeClass('mainnav-in mainnav-lg mainnav-sm').addClass('mainnav-out');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'slideToggle': function () {
                if (!nifty.container.hasClass('slide')) nifty.container.addClass('slide');
                nifty.container.toggleClass('mainnav-in mainnav-out').removeClass('mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'slideIn': function () {
                if (!nifty.container.hasClass('slide')) nifty.container.addClass('slide');
                nifty.container.addClass('mainnav-in').removeClass('mainnav-out mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'slideOut': function () {
                if (!nifty.container.hasClass('slide')) nifty.container.addClass('slide');
                nifty.container.removeClass('mainnav-in mainnav-lg mainnav-sm').addClass('mainnav-out');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'pushToggle': function () {
                nifty.container.toggleClass('mainnav-in mainnav-out').removeClass('mainnav-lg mainnav-sm');
                if (nifty.container.hasClass('mainnav-in mainnav-out')) nifty.container.removeClass('mainnav-in');
                //if (nifty.container.hasClass('mainnav-in')) //nifty.container.removeClass('aside-in');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'pushIn': function () {
                nifty.container.addClass('mainnav-in').removeClass('mainnav-out mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'pushOut': function () {
                nifty.container.removeClass('mainnav-in mainnav-lg mainnav-sm').addClass('mainnav-out');
                if (isSmallNav) unbindSmallNav();
                return;
            },
            'colExpToggle': function () {
                if (nifty.container.hasClass('mainnav-lg mainnav-sm')) nifty.container.removeClass('mainnav-lg');
                nifty.container.toggleClass('mainnav-lg mainnav-sm').removeClass('mainnav-in mainnav-out');
                return nifty.window.trigger('resize');
            },
            'collapse': function () {
                nifty.container.addClass('mainnav-sm').removeClass('mainnav-lg mainnav-in mainnav-out');
                updateMethod = 'collapse';
                return nifty.window.trigger('resize');
            },
            'expand': function () {
                nifty.container.removeClass('mainnav-sm mainnav-in mainnav-out').addClass('mainnav-lg');
                return nifty.window.trigger('resize');
            },
            'togglePosition': function () {
                nifty.container.toggleClass('mainnav-fixed');
                nifty.mainNav.niftyAffix('update');
            },
            'fixedPosition': function () {
                nifty.container.addClass('mainnav-fixed');
                nifty.mainNav.niftyAffix('update');
            },
            'staticPosition': function () {
                nifty.container.removeClass('mainnav-fixed');
                nifty.mainNav.niftyAffix('update');
            },
            'update': updateNav,
            'forceUpdate': updateSize,
            'getScreenSize': function () {
                return screenCat
            }
        };


        $.niftyNav = function (method, complete) {
            if (methods[method]) {
                if (method == 'colExpToggle' || method == 'expand' || method == 'collapse') {
                    if (nifty.screenSize == 'xs' && method == 'collapse') {
                        method = 'pushOut';
                    } else if ((nifty.screenSize == 'xs' || nifty.screenSize == 'sm') && (method == 'colExpToggle' || method == 'expand') && nifty.container.hasClass('mainnav-sm')) {
                        method = 'pushIn';
                    }
                }
                var val = methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
                if (complete) return complete();
                else if (val) return val;
            }
            return null;
        };


        $.fn.isOnScreen = function () {
            var viewport = {
                top: nifty.window.scrollTop(),
                left: nifty.window.scrollLeft()
            };
            viewport.right = viewport.left + nifty.window.width();
            viewport.bottom = viewport.top + nifty.window.height();

            var bounds = this.offset();
            bounds.right = bounds.left + this.outerWidth();
            bounds.bottom = bounds.top + this.outerHeight();

            return (!(viewport.right < bounds.left || viewport.left > bounds.right || viewport.bottom < bounds.bottom || viewport.top > bounds.top));

        };

        nifty.window.on('resizeEnd', updateNav).trigger('resize');


        // nifty.window.on('load', function() {
        var toggleBtn = $('.mainnav-toggle');
        if (toggleBtn.length) {
            toggleBtn.on('click', function (e) {
                    e.preventDefault();

                    if (toggleBtn.hasClass('push')) {
                        $.niftyNav('pushToggle');
                    } else if (toggleBtn.hasClass('slide')) {
                        $.niftyNav('slideToggle');
                    } else if (toggleBtn.hasClass('reveal')) {
                        $.niftyNav('revealToggle');
                    } else {
                        $.niftyNav('colExpToggle');
                    }
                }
            )
        }

        var $menu = $('#mainnav-menu');
        if ($menu.length) {
            // COLLAPSIBLE MENU LIST
            // =================================================================
            // Require MetisMenu
            // http://demo.onokumus.com/metisMenu/
            // =================================================================
            $menu.metisMenu({
                toggle: true
            });

            // STYLEABLE SCROLLBARS
            // =================================================================
            // Require nanoScroller
            // http://jamesflorentino.github.io/nanoScrollerJS/
            // =================================================================
            scrollbar = nifty.mainNav.find('.nano');
            if (scrollbar.length) {
                scrollbar.nanoScroller({
                    preventPageScrolling: true
                });
            }

        }

        //});

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


    }]);
})(jQuery);

(function () {
    var app = angular.module('root');

    app.factory('accountManageService', ['$http', function ($http) {
        return {
            getAccounts: function (fnSuccess, fnError) {
                $http.get('/api/account').success(fnSuccess);
            }
        };
    }]);

    app.controller('AccountManageCtrl', [
        '$scope',
        '$timeout',
        'accountManageService',
        'dataFilterService',
        function ($scope, $timeout, accountManageService, dataFilterService) {

            $scope.accountTable = new $scope.utils.DataTable({
                //sortable: {
                //    target: '#sort-bar',
                //},
                pageable: {
                    page: 0,
                    size: 20
                },
                flush: function (callback) {
                    accountManageService.getAccounts(function (data, status, headers) {
                        callback({data: data, headers: headers});
                    });
                }
            });

        }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("dashboardService", ["$http", function ($http) {
        return {};
    }]);

    app.controller("DashboardCtrl", ["$scope", "dashboardService", function ($scope, dashboardService) {

    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("deviceService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);

                return this;
            },
            getCurrentOrgAccounts: function (fnSuccess) {
                var url = '/api/account';
                $http.get(url).success(fnSuccess);

                return this;
            }
        };
    }]);

    app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, deviceService, $timeout) {

        $scope.curOrgAccounts = '';
        $scope.deviceComment = '';
        $scope.deviceName = '';
        $scope.selectAccount = '';

        (function newDataTable() {
            if ($scope.context.account) {
                $scope.deviceTable = new $scope.utils.DataTable({
                    sortable: {
                        target: '#sort-bar',
                        sort: 'createdDateTime,desc'
                    },
                    pageable: {
                        page: 0,
                        size: 20
                    },
                    flush: function (callback) {
                        deviceService.getDevices(this, $scope.context.account.org_id, function (data, status, headers) {
                            callback({data: data, headers: headers});
                        });
                    }
                });
            } else {
                $timeout(newDataTable, 1000);
            }
        })();

        deviceService.getCurrentOrgAccounts(function (data) {
            $scope.curOrgAccounts = data;
        });

        $scope.deviceAuth = function () {

            if ($scope.selectAccount == '') {
                $('#divSelectAccount').addClass('has-error').addClass('has-feedback');
                return;
            }

            if ($scope.deviceName == '') {
                $('#divDeviceName').addClass('has-error').addClass('has-feedback');
                return;
            }

            $('#divDeviceName').addClass('has-success').addClass('has-feedback');
            $('#divSelectAccount').addClass('has-success').addClass('has-feedback');

            $("#authQRCode").html('');

            var postObject = {};
            postObject.selectAccount = $scope.selectAccount;
            postObject.deviceName = $scope.deviceName;
            postObject.deviceComment = $scope.deviceComment;

            $scope.qrcode = $("#authQRCode").qrcode({
                render: "table", //table方式
                width: 300, //宽度
                height: 300, //高度
                foreground: "#337ab7",//前景颜色
                text: "token" //任意内容
            });
        };

        $scope.cancelDeviceAuth = function () {


        };

    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("emulatorService", ["$http", function ($http) {
        return {
            createProWithDetail: function (proDetail, fnSuccess, fnError) {
                $http.post("/api/productbase/withdetail", proDetail).success(fnSuccess).error(fnError);

                return this;
            }
        };
    }]);

    app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "FileUploader", function ($scope, emulatorService, $timeout, FileUploader) {

        var uploader = $scope.uploader = new FileUploader({
            url: ''
        });

        //set AccessToken http header
        var accessToken = $scope.context.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        $scope.productInfos = [{key: '', value: ''}];
        $scope.productAddress = [{address: '', tel: ''}];

        $scope.barCode = '';
        $scope.productName = '';
        $scope.expireDate = 1;
        $scope.expireDateUnit = '';
        $scope.comment = '';
        $scope.fileInput = '';
        $scope.keyTypePubInput = '';
        $scope.keyTypePriInput = '';
        $scope.keyTypeRFIDInput = '';
        $scope.hotline = '';
        $scope.support = '';
        $scope.taobao = '';
        $scope.tmall = '';

        $scope.addProductInfo = function () {
            $scope.productInfos.push({key: '', value: ''});
        }

        $scope.subProductInfo = function () {
            $scope.productInfos.pop();
        };

        $scope.addProAddress = function () {
            $scope.productAddress.push({address: '', tel: ''});
        }

        $scope.subProAddress = function () {
            $scope.productAddress.pop();
        };

        $scope.preview = function () {

            var dataPreview = {};
            dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo-mobile?access_token=" + $scope.context.getAccessToken();
            dataPreview.proImgUrl = $scope.fileInput;
            dataPreview.barcode = $scope.barCode;
            dataPreview.name = $scope.productName;
            dataPreview.details = $scope.productInfos;

            $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);
        };

        $scope.submit = function () {
            if ($scope.barCode == '') {
                $scope.utils.alert('info', '产品BarCode不能为空');
                return;
            }

            if ($scope.productName == '') {
                $scope.utils.alert('info', '产品名不能为空');
                return;
            }

            if (!$scope.keyTypePubInput && !$scope.keyTypePriInput && !$scope.keyTypeRFIDInput) {
                $scope.utils.alert('info', '产品码类型至少要选择一种');
                return;
            }

            if ($scope.expireDateUnit == '') {
                $scope.utils.alert('info', '请选择产品过期单位');
                return;
            }

            if (!(/(^[1-9]\d*$)/.test($scope.expireDate))) {
                $scope.utils.alert('info', '产品过期时间应为正整数');
                return;
            }

            if(uploader.queue.length == 0)
            {
                $scope.utils.alert('info', '产品图片不能为空');
                return;
            }

            var proWithDetails = {};

            proWithDetails.category_id = 0;
            proWithDetails.barcode = $scope.barCode;
            proWithDetails.name = $scope.productName;
            proWithDetails.comments = $scope.comment;
            proWithDetails.product_key_type_codes = [];
            if ($scope.keyTypePubInput)
                proWithDetails.product_key_type_codes.push("qr_public");
            if ($scope.keyTypePriInput)
                proWithDetails.product_key_type_codes.push("qr_secure");
            if ($scope.keyTypeRFIDInput)
                proWithDetails.product_key_type_codes.push("rfid");

            proWithDetails.shelf_life = $scope.expireDate - 0;
            if ($scope.expireDateUnit == "年")
                proWithDetails.shelf_life_interval = 'year';
            else if ($scope.expireDateUnit == "月")
                proWithDetails.shelf_life_interval = 'month';
            else if ($scope.expireDateUnit == "周")
                proWithDetails.shelf_life_interval = 'week';
            else if ($scope.expireDateUnit == "天")
                proWithDetails.shelf_life_interval = 'day';
            else if ($scope.expireDateUnit == "小时")
                proWithDetails.shelf_life_interval = 'hour';

            proWithDetails.status_code = '待审核';

            var proDetails = {};
            proDetails.details = {};
            for (var proInfo in $scope.productInfos) {
                if ($scope.productInfos[proInfo].key != '' && $scope.productInfos[proInfo].value != '')
                    proDetails.details[$scope.productInfos[proInfo].key] = $scope.productInfos[proInfo].value;
            }

            proDetails.contact = {};
            proDetails.contact['hotline'] = $scope.hotline;
            proDetails.contact['support'] = $scope.support;

            proDetails['e-commerce'] = {};
            proDetails['e-commerce'].taobao = $scope.taobao;
            proDetails['e-commerce'].tmall = $scope.tmall;

            proDetails['t-commerce'] = [];
            for (var proAddress in $scope.productAddress) {
                if ($scope.productAddress[proAddress].address != '' && $scope.productAddress[proAddress].tel != '') {
                    var tComItem = {};
                    tComItem.address = $scope.productAddress[proAddress].address;
                    tComItem.tel = $scope.productAddress[proAddress].tel;

                    proDetails['t-commerce'].push(tComItem);
                }
            }

            proWithDetails.details = JSON.stringify(proDetails);

            try {
                emulatorService.createProWithDetail(proWithDetails, function (data) {
                        if (data.id != null && data.id != '') {
                            uploader.queue[0].url = '/api/productbase/withdetailfile/' + data.id + "/full-mobile";
                            uploader.queue[0].headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken;

                            uploader.uploadAll();

                            $scope.utils.alert('success', '创建产品成功');
                        }
                    },
                    function (data, state) {
                        $scope.utils.alert('info', '创建产品失败');
                    });
            }
            catch (ex) {

            }
        };

        $timeout(function () {

            /* ========================================================================
             * NIFTY CHECK v1.1
             * -------------------------------------------------------------------------
             * - ThemeOn.net -
             * ========================================================================*/
            !function ($) {
                "use strict";

                var allFormEl,
                    formElement = function(el){
                        if (el.data('nifty-check')){
                            return;
                        }else{
                            el.data('nifty-check', true);
                            if (el.text().trim().length){
                                el.addClass("form-text");
                            }else{
                                el.removeClass("form-text");
                            }
                        }


                        var input 	= el.find('input')[0],
                            groupName 	= input.name,
                            $groupInput	= function(){
                                if (input.type == 'radio' && groupName) {
                                    return $('.form-radio').not(el).find('input').filter('input[name='+groupName+']').parent();
                                }else{
                                    return false;
                                }
                            }(),
                            changed = function(){
                                if(input.type == 'radio' && $groupInput.length) {
                                    $groupInput.each(function(){
                                        var $gi = $(this);
                                        if ($gi.hasClass('active')) $gi.trigger('nifty.ch.unchecked');
                                        $gi.removeClass('active');
                                    });
                                }


                                if (input.checked) {
                                    el.addClass('active').trigger('nifty.ch.checked');
                                }else{
                                    el.removeClass('active').trigger('nifty.ch.unchecked');
                                }
                            };

                        if (input.checked) {
                            el.addClass('active');
                        }else{
                            el.removeClass('active');
                        }

                        $(input).on('change', changed);
                    },
                    methods = {
                        isChecked : function(){
                            return this[0].checked;
                        },
                        toggle : function(){
                            this[0].checked = !this[0].checked;
                            this.trigger('change');
                            return null;
                        },
                        toggleOn : function(){
                            if(!this[0].checked){
                                this[0].checked = true;
                                this.trigger('change');
                            }
                            return null;
                        },
                        toggleOff : function(){
                            if(this[0].checked && this[0].type == 'checkbox'){
                                this[0].checked = false;
                                this.trigger('change');
                            }
                            return null;
                        }
                    };

                $.fn.niftyCheck = function(method){
                    var chk = false;
                    this.each(function(){
                        if(methods[method]){
                            chk = methods[method].apply($(this).find('input'),Array.prototype.slice.call(arguments, 1));
                        }else if (typeof method === 'object' || !method) {
                            formElement($(this));
                        };
                    });
                    return chk;
                };

                nifty.document.ready(function() {
                    allFormEl = $('.form-checkbox, .form-radio');
                    if(allFormEl.length) allFormEl.niftyCheck();
                });

                nifty.document.on('change', '.btn-file :file', function() {
                    var input = $(this),
                        numFiles = input.get(0).files ? input.get(0).files.length : 1,
                        label = input.val().replace(/\\/g, '/').replace(/.*\//, ''),
                        size = function(){
                            try{
                                return input[0].files[0].size;
                            }catch(err){
                                return 'Nan';
                            }
                        }(),
                        fileSize = function(){
                            if (size == 'Nan' ) {
                                return "Unknown";
                            }
                            var rSize = Math.floor( Math.log(size) / Math.log(1024) );
                            return ( size / Math.pow(1024, rSize) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][rSize];
                        }();



                    input.trigger('fileselect', [numFiles, label, fileSize]);
                });
            }(jQuery);

            var divImgWrap = $("#divImgWrap");
            var fileInput = $("#fileInput");
            var imgProductbase = $("#imgProductbase");

            if (typeof FileReader === 'undefined') {
                divImgWrap.html("您的浏览器不支持图片预览");
            } else {
                fileInput.change(function () {

                        var file = uploader.queue[0]._file;
                        var reader = new FileReader();
                        reader.readAsDataURL(file);
                        reader.onload = function (e) {
                            imgProductbase.attr('src', this.result);
                            $scope.fileInput = this.result;
                        }
                    }
                );
            }

            $('#createProduct').bootstrapValidator({
                message: '该字段不能为空',
                feedbackIcons: {
                    valid: 'fa fa-check-circle fa-lg text-success',
                    invalid: 'fa fa-times-circle fa-lg',
                    validating: 'fa fa-refresh'
                },
                fields: {
                    barCode: {
                        validators: {
                            notEmpty: {
                                message: '请输入产品BarCode'
                            }
                        }
                    },
                    productName: {
                        validators: {
                            notEmpty: {
                                message: '请输入产品名'
                            }
                        }
                    },
                    greaterthan: {
                        validators: {
                            notEmpty: {
                                message: '请输入产品过期时间'
                            },
                            greaterThan: {
                                inclusive:false,
                                //If true, the input value must be greater than or equal to the comparison one.
                                //If false, the input value must be greater than the comparison one
                                value: 0,
                                message: '请输入大于1的正整数'
                            }
                        }
                    }
                }
            }).on('success.field.bv', function (e, data) {
                // $(e.target)  --> The field element
                // data.bv      --> The BootstrapValidator instance
                // data.field   --> The field name
                // data.element --> The field element

                var $parent = data.element.parents('.form-group');

                // Remove the has-success class
                $parent.removeClass('has-success');
            });
        }, 0);

    }]);
})();
(function () {
    var app = angular.module('root');

    app.filter('startFrom', function () {
        return function (input, start) {
            if (!input || !input.length) { return; }

            return input.slice(start - 1);
        };
    });

    app.factory("logisticsManageService", ["$http", function ($http) {
        return {
            uploadLogisticsFile: function (file, fnSuccess, fnError) {
                $http.post("/api/logistics/file", file).success(function (data) {
                }).error(function (data, state) {
                });
                return this;
            },
            getLogisticsHistoryInfoCount: function (fnSuccess, fnError) {
                $http.get("/api/productfile/count?status=0&&filetype=2")
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            },
            getLogisticsHistoryInfo: function (pageIndex, fnSuccess, fnError) {
                $http.get("/api/productfile?status=0&&filetype=2&&pageIndex=" + pageIndex)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            }
        };
    }]);

    app.controller("LogisticsManageCtrl", ["$scope", "FileUploader", "logisticsManageService", function ($scope, FileUploader, logisticsManageService) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/api/logistics/file'
            /*headers:{"Content-Type":"multipart/form-data; charset=utf-8"}*/
        });

        //set AccessToken http header
        var accessToken = $scope.context.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            },
            name: ''
        });
        // CALLBACKS

        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function (fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function (addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function (item) {
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function (fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function (progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
            console.info('onSuccessItem', response, status);

            $scope.utils.alert('success', '上传成功！');
        };
        uploader.onErrorItem = function (fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
            // var dataObj=eval("("+response+")");

        };
        uploader.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            // console.info('onCompleteItem', fileItem, response, status, headers);
            console.info('onCompleteItem', response, status);

            getLogisticsHistoryInfo(0);
        };
        uploader.onCompleteAll = function () {
            // console.info('onCompleteAll');
            getLogisticsHistoryInfo(0);
        };

        var LogisticsHistoryFile = function (data) {

            var adt = {
                data: data,
                filteredData: {},
                pageSize: 10,
                pages: pages,
                isShowHisSec: isShowHisSec,
                goToFirstPage: goToFirstPage,
                gotoLastPage: gotoLastPage,
                goToPage: goToPage,
                currentPage: currentPage,
                next: next,
                previous: previous,
                onFirstPage: onFirstPage,
                onLastPage: onLastPage
            };
            return adt;

            function isShowHisSec() {
                return data.length > 0 ? 1 : 0;
            }

            function goToFirstPage() {
                if (!this.onFirstPage()) {
                    $scope.currentPage = 0;
                    getLogisticsHistoryInfo($scope.currentPage);
                }
            }

            function gotoLastPage() {
                if (!this.onLastPage()) {
                    $scope.currentPage = Math.ceil($scope.totalCounts / this.pageSize) - 1;
                    getLogisticsHistoryInfo($scope.currentPage);
                }
            }

            function goToPage(page) {
                $scope.currentPage = page;
                getLogisticsHistoryInfo($scope.currentPage);
            }

            function currentPage() {
                return $scope.currentPage;
            }

            function pages() {
                var p = [];
                for (var i = Math.max(0, $scope.currentPage - 4); i <= $scope.currentPage; i++) {
                    p.push(i);
                }
                return p;
            }

            function next() {
                if (!this.onLastPage()) {
                    $scope.currentPage += 1;
                    getLogisticsHistoryInfo($scope.currentPage);
                }
            }

            function previous() {
                if (!this.onFirstPage()) {
                    $scope.currentPage -= 1;
                    getLogisticsHistoryInfo($scope.currentPage);
                }

            }

            function onFirstPage() {
                return $scope.currentPage === 0;
            }

            function onLastPage() {
                return data.length < this.pageSize;
            }
        };

        $scope.currentPage = 0;
        $scope.totalCounts = 0;
        $scope.itemIndex = 0;

        logisticsManageService.getLogisticsHistoryInfoCount(function (data) {
            $scope.totalCounts = data;
        });

        var getLogisticsHistoryInfo = function (currentPage) {
            logisticsManageService.getLogisticsHistoryInfo(currentPage, function (data) {
                $scope.data = data;
                $scope.dataTable = new LogisticsHistoryFile($scope.data);

                $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
                $scope.itemIndex = 0;
            });
        };

        getLogisticsHistoryInfo(0);
    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("logisticsService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/logistics/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }
        };
    }]);

    app.controller("LogisticsCtrl", ["$scope", "logisticsService", function ($scope, logisticsService) {

        $scope.productKey = "";

        $scope.bodyShow = 0;

        $scope.productKeyClick = function () {

            if ($scope.productKey == null || $scope.productKey == "") {
                $scope.bodyShow = 0;
                return;
            }

            logisticsService.getInfo($scope.productKey, function (data) {
                $scope.bodyShow = 1;
                $scope.data = data;
            });
        }
    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory('messageService', ['$http', function ($http) {
        return {
            getMessages: function (dataTable, orgId, fnSuccess) {
                var url = '/api/message?';
                if (orgId) {
                    url += 'org_id=' + orgId + '&';
                }
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);
            }
        };
    }]);

    app.controller('MessageCtrl', ['$scope', '$timeout', 'messageService', function ($scope, $timeout, messageService) {
        var messageTypes = {
            business: '商家信息',
            platform: '云溯平台信息'
        };
        var messageStatuses = {
            created: '已创建',
            approved: '通过审核',
            deleted: '已删除'
        };
        (function newDataTable() {
            if ($scope.context.account) {
                $scope.messageTable = new $scope.utils.DataTable({
                    sortable: {
                        target: '#sort-bar',
                        sort: 'createdDateTime,desc'
                    },
                    pageable: {
                        page: 0,
                        size: 20
                    },
                    flush: function (callback) {
                        messageService.getMessages(this, $scope.context.account.org_id, function (data, status, headers) {
                            callback({data: data, headers: headers});
                        });
                    }
                });
            } else {
                $timeout(newDataTable, 1000);
            }
        })();

        $scope.formatMessageType = function (typeCode) {
            return messageTypes[typeCode] || typeCode;
        };

        $scope.formatMessageStatus = function (statusCode) {
            return messageStatuses[statusCode] || statusCode;
        };

    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("operationService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/logistics/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }
        };
    }]);

    app.controller("OperationCtrl", ["$scope", "operationService", function ($scope, operationService) {

        $scope.productKey = "";

        $scope.bodyShow = 0;

        $scope.packageClick = function () {

            if ($scope.productKey == null || $scope.productKey == "") {
                $scope.bodyShow = 0;
                return;
            }

            logisticsService.getInfo($scope.productKey, function (data) {
                $scope.bodyShow = 1;
                $scope.data = data;
            });
        }
    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("packageSearchService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/package/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }

            //getInfo: function(productKey, fnSuccess, fnError){
            //    $http.get("mock/package.json")
            //        .success(function(data){
            //            fnSuccess(data);
            //        });
            //}
        };
    }]);

    app.controller("PackageSearchCtrl", ["$scope", "packageSearchService", function ($scope, packageSearchService) {

        $scope.productKey = "";

        $scope.bodyShow = 0;

        function listAllNode(thejson) {

            if (thejson == null)
                return;

            var item = {};
            item.text = thejson["key"];
            item.nodes = [];

            var rootNode = {};
            rootNode.text = "产品数量: " + '(' + thejson["productCount"] + ')';
            item.nodes.push(rootNode);

            if (thejson["subPackages"] == null)
                return;

            if (thejson["subPackages"] instanceof Array) {
                for (var e in thejson["subPackages"]) {
                    item.nodes.push(listAllNode(thejson["subPackages"][e]));
                }
            }
            else {
                item.nodes.push(listAllNode(thejson["subPackages"]));
            }

            return item;
        }

        $scope.productKeyClick = function () {

            if ($scope.productKey == null || $scope.productKey == "") {
                $scope.bodyShow = 0;
                return;
            }

            packageSearchService.getInfo($scope.productKey, function (data) {
                $scope.origialdata = data;
                $scope.resultdata = [];
                $scope.resultdata.push(listAllNode(data));
                $scope.bodyShow = 1;

                $('#tree').treeview({data: $scope.resultdata, color: "#3c8dbc"});
            });
        };
    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("packageService", ["$http", function ($http) {
        return {
            getInfo: function (packageKey, fnSuccess, fnError) {
                $http.get("/api/package/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            },
            uploadPackageFile: function (file, fnSuccess, fnError) {
                $http.post("/api/package/file", file).success(function (data) {
                }).error(function (data, state) {
                });
                return this;
            },
            getPackageHistoryInfoCount: function (fnSuccess, fnError) {
                $http.get("/api/productfile/count?status=0&&filetype=1")
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            },
            getPackageHistoryInfo: function (pageIndex, fnSuccess, fnError) {
                $http.get("/api/productfile?status=0&&filetype=1&&pageIndex=" + pageIndex)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            }
        };
    }]);

    app.controller("PackageCtrl", ["$scope", "FileUploader", "packageService", function ($scope, FileUploader, packageService) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/api/package/file'
            /*headers:{"Content-Type":"multipart/form-data; charset=utf-8"}*/
        });

        //set AccessToken http header
        var accessToken = $scope.context.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            },
            name: ''
        });
        // CALLBACKS

        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function (fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function (addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function (item) {
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function (fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function (progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
            console.info('onSuccessItem', response, status);

            $scope.utils.alert('success', '上传成功！');
        };
        uploader.onErrorItem = function (fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
            // var dataObj=eval("("+response+")");
        };
        uploader.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            // console.info('onCompleteItem', fileItem, response, status, headers);
            console.info('onCompleteItem', response, status);

            getPackageHistoryInfo(0);
        };
        uploader.onCompleteAll = function () {
            // console.info('onCompleteAll');

            getPackageHistoryInfo(0);
        };

        var PackageHistoryFile = function (data) {

            var adt = {
                data: data,
                filteredData: {},
                pageSize: 10,
                pages: pages,
                isShowHisSec: isShowHisSec,
                goToFirstPage: goToFirstPage,
                gotoLastPage: gotoLastPage,
                goToPage: goToPage,
                currentPage: currentPage,
                next: next,
                previous: previous,
                onFirstPage: onFirstPage,
                onLastPage: onLastPage
            };
            return adt;

            function isShowHisSec() {
                return data.length > 0 ? 1 : 0;
            }

            function goToFirstPage() {
                if (!this.onFirstPage()) {
                    $scope.currentPage = 0;
                    getPackageHistoryInfo($scope.currentPage);
                }
            }

            function gotoLastPage() {
                if (!this.onLastPage()) {
                    $scope.currentPage = Math.ceil($scope.totalCounts / this.pageSize) - 1;
                    getPackageHistoryInfo($scope.currentPage);
                }
            }

            function goToPage(page) {
                $scope.currentPage = page;
                getPackageHistoryInfo($scope.currentPage);
            }

            function currentPage() {
                return $scope.currentPage;
            }

            function pages() {
                var p = [];
                for (var i = Math.max(0, $scope.currentPage - 4); i <= $scope.currentPage; i++) {
                    p.push(i);
                }
                return p;
            }

            function next() {
                if (!this.onLastPage()) {
                    $scope.currentPage += 1;
                    getPackageHistoryInfo($scope.currentPage);
                }
            }

            function previous() {
                if (!this.onFirstPage()) {
                    $scope.currentPage -= 1;
                    getPackageHistoryInfo($scope.currentPage);
                }

            }

            function onFirstPage() {
                return $scope.currentPage === 0;
            }

            function onLastPage() {
                return data.length < this.pageSize;
            }
        };

        $scope.currentPage = 0;
        $scope.totalCounts = 0;
        $scope.itemIndex = 0;

        $scope.getItemIndex = function () {
            $scope.itemIndex += 1;
            return $scope.itemIndex;
        };

        packageService.getPackageHistoryInfoCount(function (data) {
            $scope.totalCounts = data;
        });

        var getPackageHistoryInfo = function (currentPage) {
            packageService.getPackageHistoryInfo(currentPage, function (data) {
                $scope.data = data;
                $scope.dataTable = new PackageHistoryFile($scope.data);

                $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
                $scope.itemIndex = 0;
            });
        };

        getPackageHistoryInfo(0);
    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory('productBaseManageService', ['$http', function ($http) {
        return {
            getProductBases: function (fnSuccess) {
                $http.get('/api/productbase').success(fnSuccess);
                return this;
            },
            getProductKeyCredits: function (fnSuccess) {
                $http.get('/api/productkeycredit').success(fnSuccess);
                return this;
            }
        };
    }]);

    app.controller('ProductBaseManageCtrl', ['$scope', 'productBaseManageService', function ($scope, productBaseManageService) {
        $scope.SHELFLIFE_INTERVALS = {
            'year': '年',
            'month': '月',
            'week': '周',
            'day': '天',
            'hour': '小时'
        };

        $scope.formatProductKeyTypes = function (productKeyTypes) {
            var result = '';
            if (productKeyTypes) {
                $.each(productKeyTypes, function (i, item) {
                    result += item.name;
                    if (i < productKeyTypes.length - 1) {
                        result += ', ';
                    }
                });
            }
            return result;
        };

        $scope.formatComments = function (comments) {
            comments || (comments = '');
            return comments.length > 30 ? comments.substring(0, 30) + '...' : comments;
        };

        $scope.dataTable = new $scope.utils.DataTable({
            pageable: {
                page: 0,
                size: 20
            },
            flush: function (callback) {
                productBaseManageService.getProductBases(function (data, status, headers) {
                    if ($scope.productKeyCredits) {
                        setProductKeyCredits(data, $scope.productKeyCredits);
                    } else {
                        getProductKeyCredits(function (productKeyCredits) {
                            setProductKeyCredits(data, productKeyCredits);
                        });
                    }
                    callback({data: data, headers: headers});
                });
            }
        });

        function getProductKeyCredits(callback) {
            productBaseManageService.getProductKeyCredits(function (data) {
                var productKeyCredits = {
                    general: {
                        total: 0,
                        remain: 0
                    },
                    creditMap: {}
                };
                $.each(data, function (i, item) {
                    if (item.product_base_id) {
                        productKeyCredits.creditMap[item.product_base_id] = {
                            total: item.total,
                            remain: item.remain
                        };
                    } else {
                        productKeyCredits.general = {
                            total: item.total,
                            remain: item.remain
                        };
                    }
                });
                $scope.productKeyCredits = productKeyCredits;
                console.log('[productKeyCredits loaded]', productKeyCredits);
                callback(productKeyCredits);
            });
        }

        function setProductKeyCredits(productBases, productKeyCredits) {
            $.each(productBases, function (i, item) {
                if (item && productKeyCredits) {
                    var credit = item.credit = {total: 0, remain: 0, general: productKeyCredits.general};
                    credit.total += productKeyCredits.general.total;
                    credit.remain += productKeyCredits.general.remain;
                    if (productKeyCredits.creditMap[item.id]) {
                        credit.total += productKeyCredits.creditMap[item.id].total;
                        credit.remain += productKeyCredits.creditMap[item.id].remain;
                    }
                    credit.percentage = (credit.remain * 100 / credit.total) | 0;
                }
            });
        }

    }]);
})();

(function () {
    var app = angular.module('root');

    app.factory("productKeyManageService", ["$http", function ($http) {
        return {
            getProductKeyBatches: function (productBaseId, fnSuccess) {
                $http.get("/api/productkeybatch" + (productBaseId ? "?product_base_id=" + productBaseId : "")).success(fnSuccess);
                return this;
            },
            getProductKeyBatchesPaged: function (table, productBaseId, fnSuccess) {
                var url = "/api/productkeybatch?";
                if (productBaseId) {
                    url += "product_base_id=" + productBaseId + '&';
                }
                url += table.toString();
                $http.get(url).success(fnSuccess);
                return this;
            },
            getProductBases: function (fnSuccess) {
                $http.get("/api/productbase").success(fnSuccess);
                return this;
            },
            getProductKeyCredits: function (fnSuccess) {
                $http.get("/api/productkeycredit").success(fnSuccess);
                return this;
            },
            getAccountById: function (accountId, fnSuccess) {
                $http.get("/api/account/" + accountId).success(fnSuccess);
                return this;
            },
            createProductKeyBatch: function (request, fnSuccess, fnFail) {
                $http.post("/api/productkeybatch", request).success(fnSuccess).error(fnFail);
                return this;
            },
            downloadProductKeys: function (listPanel, batchId, auth) {
                listPanel.downloadFrameSrc = '/api/productkeybatch/' + batchId + '/keys?' + auth;
            }
        };
    }]);

    app.controller("ProductKeyManageCtrl", ["$scope", "productKeyManageService", function ($scope, productKeyManageService) {

        $scope.cache || ($scope.cache = {});

        $scope.creationPanel = {
            model: {
                productBaseId: 0,
                quantity: 0
            },
            create: function () {
                var model = this.model;
                console.log('[before productKeyBatch create]', model);

                var requestData = {
                    quantity: model.quantity,
                    product_base_id: model.productBaseId
                };
                var selectedProductBase = this.selectedProductBase;
                productKeyManageService.createProductKeyBatch(requestData, function (data) {
                    console.log('[newProductKeyBatch created]', data);

                    data.product_base = selectedProductBase;
                    selectedProductBase.credit.remain -= requestData.quantity;
                    $scope.listPanel.newProductKeyBatches.push(data);

                    $scope.utils.alert('success', '产品码创建成功');
                }, function (error, data) {
                    console.log(error, data);
                    var message = (error.message || '').substring(0, 100);
                    $scope.utils.alert('danger', message);
                });
            },
            productBaseIdChanged: function (productBaseId) {
                console.log('[productBaseId changed]', productBaseId);
                var selectedProductBase = null;
                $.each(this.productBases, function (i, item) {
                    if (item && item.id === productBaseId) {
                        selectedProductBase = item;
                        console.log('[selectedProductBase]', selectedProductBase);
                    }
                });
                this.selectedProductBase = selectedProductBase;
            }
        };

        $scope.listPanel = {
            table: new $scope.utils.DataTable({
                sortable: {
                    target: '#sort-bar',
                    sort: 'createdDateTime,desc'
                },
                pageable: {
                    page: 0,
                    size: 20
                },
                flush: function (callback) {
                    productKeyManageService.getProductKeyBatchesPaged(this, null, function (data, status, headers) {
                        callback({data: data, headers: headers});
                    });
                }
            }),
            newProductKeyBatches: [],
            download: function (batchId) {
                if (batchId) {
                    var accessToken = $scope.context.getAccessToken();
                    var auth = accessToken ? $scope.YUNSOO_CONFIG.PARAMETER_ACCESS_TOKEN + '=' + accessToken : '';
                    productKeyManageService.downloadProductKeys(this, batchId, auth);
                }
            }

            ,
            downloadFrameSrc: ''
        };

        $scope.formatProductKeyTypes = function (productKeyTypes) {
            var result = '';
            if (productKeyTypes) {
                $.each(productKeyTypes, function (i, item) {
                    result += item.name;
                    if (i < productKeyTypes.length - 1) {
                        result += ', ';
                    }
                });
            }
            return result;
        };

        $scope.cache.accounts || ($scope.cache.accounts = []);
        $scope.getAccountCached = function (accountId) {
            var accounts = $scope.cache.accounts;
            if (accounts[accountId]) {
                return accounts[accountId];
            } else {
                accounts[accountId] = '加载中';
                productKeyManageService.getAccountById(accountId, function (data) {
                    accounts[accountId] = data;
                });
            }
        };

        //init
        //get product bases
        productKeyManageService.getProductBases(function (data) {
            $scope.productBases = $scope.creationPanel.productBases = data;
            $scope.productBaseMap = $scope.utils.arrayToMap($scope.productBases, "id");
            if ($scope.productBases) {

                //get product key credits
                productKeyManageService.getProductKeyCredits(function (data) {
                        var productKeyCredits = {
                            general: {
                                total: 0,
                                remain: 0
                            },
                            creditMap: {}
                        };
                        $.each(data, function (i, item) {
                            if (item.product_base_id) {
                                productKeyCredits.creditMap[item.product_base_id] = {
                                    total: item.total,
                                    remain: item.remain
                                };
                            }
                            else {
                                productKeyCredits.general = {
                                    total: item.total,
                                    remain: item.remain
                                };
                            }
                        });
                        $scope.productKeyCredits = productKeyCredits;
                        console.log('[productKeyCredits loaded]', productKeyCredits);

                        $.each($scope.productBases, function (i, item) {
                            setCredit(item, productKeyCredits);
                        });
                    }
                );

            }
        });

        function setCredit(productBase, productKeyCredits) {
            if (productBase && productKeyCredits) {
                var credit = productBase.credit = {total: 0, remain: 0, general: productKeyCredits.general};
                credit.total += productKeyCredits.general.total;
                credit.remain += productKeyCredits.general.remain;
                if (productKeyCredits.creditMap[productBase.id]) {
                    credit.total += productKeyCredits.creditMap[productBase.id].total;
                    credit.remain += productKeyCredits.creditMap[productBase.id].remain;
                }
                credit.percentage = (credit.remain * 100 / credit.total) | 0;
            }
        }

    }]);//end of controller

})();

(function () {
    var app = angular.module('root');

    app.controller("SearchCtrl", ["$scope", function ($scope) {

    }]);
})();
(function () {
    var app = angular.module('root');

    app.factory("settingService", ["$http", function ($http) {
        return {
            updatePassword: function (passwordObj, fnSuccess, fnFail) {
                $http.post("/api/account/current/password", passwordObj).success(fnSuccess).error(fnFail);
                return this;
            }
        };
    }]);

    app.controller("SettingCtrl", ["$scope", "settingService", '$timeout', function ($scope, settingService, $timeout) {

        $scope.originalPassword = "";
        $scope.currentPassword = "";
        $scope.confirmPassword = "";

        $timeout(function(){$('#updatePassword').bootstrapValidator({
            message: '输入密码不合法',
            feedbackIcons: {
                valid: 'fa fa-check-circle fa-lg text-success',
                invalid: 'fa fa-times-circle fa-lg',
                validating: 'fa fa-refresh'
            },
            fields: {
                oldPassword: {
                    validators: {
                        notEmpty: {
                            message: '请输入当前密码'
                        }
                    }
                },
                curPassword: {
                    validators: {
                        notEmpty: {
                            message: '请输入修改密码'
                        }
                    }
                },
                confirmPassword: {
                    validators: {
                        notEmpty: {
                            message: '请输入确认密码'
                        },
                        identical: {
                            field: 'curPassword',
                            message: '确认密码与修改密码不一致'
                        }
                    }
                }
            }
        }).on('success.field.bv', function(e, data) {
            // $(e.target)  --> The field element
            // data.bv      --> The BootstrapValidator instance
            // data.field   --> The field name
            // data.element --> The field element

            var $parent = data.element.parents('.form-group');

            // Remove the has-success class
            $parent.removeClass('has-success');
        });}, 0);

        $scope.submit = function () {
            if ($scope.originalPassword == "") {
                $scope.utils.alert('warning', '请输入原密码！');
                return;
            }
            if ($scope.currentPassword == "") {
                $scope.utils.alert('warning', '请输入修改密码！');
                return;
            }
            if ($scope.currentPassword != $scope.confirmPassword) {
                $scope.utils.alert('warning', '确认密码与修改入密码不一致！');
                return;
            }

            var passwordObj = {
                old_password: $scope.originalPassword,
                new_password: $scope.currentPassword
            };

            settingService.updatePassword(passwordObj, function (data) {
                $scope.utils.alert('success', '修改密码成功！');
            }, function (data) {
                $scope.utils.alert('warning', '修改密码失败！');
            });
        };
    }]);
})();