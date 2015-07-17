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
        .when('/no_access', {
          templateUrl: '403.html'
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
      getAccountAllPermissions: function (fnSuccess, fnError) {
        var url = '/api/account/current/permission';
        $http.get(url).success(fnSuccess).error(fnError);
      }
    };
  }]);

  app.controller('rootCtrl', ['$scope', '$timeout', '$http', 'YUNSOO_CONFIG', 'utils', 'rootService', 'productBaseManageService', '$location',
    function ($scope, $timeout, $http, YUNSOO_CONFIG, utils, rootService, productBaseManageService, $location) {
      console.log('[root controller start]');

      var RESOURCE = YUNSOO_CONFIG.PAGE_ACCESS.RESOURCE;
      var ACTION = YUNSOO_CONFIG.PAGE_ACCESS.ACTION;

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
              setTimeout(refresh, (data.expires_in / 1.2 /*seconds*/) * 1000);
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

      rootService.getAccountAllPermissions(function (data) {
        $scope.currAccountAllPermissions = data;
        $scope.$broadcast('accountPermissions-ready', data);
      });

      var hasAccess = $scope.hasAccess = function (resource, action) {

        var data = $scope.currAccountAllPermissions;

        if (data) {
          for (var i = 0; i < data.length; i++) {
            if (data[i].resource_code == '*' && (data[i].action_code == '*')) {
              return true;
            }
            else {
              if (data[i].resource_code == resource && (data[i].action_code == action)) {
                return true;
              }
            }
          }
        }

        return false;
      };

      function ApplyAccess(path) {

        var isShowSetting = false;

        if (!hasAccess(RESOURCE.DASHBOARD, ACTION.READ)) {
          $('#liDashBoard').attr('style', "display:none");
          if (path == '/dashboard') {
            $location.path('/no_access');
          }
        }

        if (!hasAccess(RESOURCE.PRODUCTKEY, ACTION.READ) && !hasAccess(RESOURCE.PRODUCTKEY, ACTION.MANAGE)) {
          $('#liProductKey').attr('style', "display:none");
          if (path == '/product-key-manage') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.PRODUCTKEY, ACTION.READ) && !hasAccess(RESOURCE.PRODUCTKEY, ACTION.MANAGE)) {
          $scope.productKeyButtonShow = false;
        }
        else if (hasAccess(RESOURCE.PRODUCTKEY, ACTION.MANAGE)) {
          $scope.productKeyButtonShow = true;
        }

        if (!hasAccess(RESOURCE.PACKAGE, ACTION.READ) && !hasAccess(RESOURCE.PACKAGE, ACTION.MANAGE)) {
          $('#liPackageManage').attr('style', "display:none");
          if (path == '/package-manage') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.PACKAGE, ACTION.READ) && !hasAccess(RESOURCE.PACKAGE, ACTION.MANAGE)) {
          $scope.packageButtonShow = false;
        }
        else if (hasAccess(RESOURCE.PACKAGE, ACTION.MANAGE)) {
          $scope.packageButtonShow = true;
        }

        if (!hasAccess(RESOURCE.LOGISTICS, ACTION.READ) && !hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $('#liLogistics').attr('style', "display:none");
          if (path == '/logistics') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.READ) && !hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.logisticsButtonShow = false;
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.logisticsButtonShow = true;
        }

        if (!hasAccess(RESOURCE.REPORT, ACTION.READ)) {
          $('#liAllCharts').attr('style', "display:none");
          if (path == '/echarts-bar' || path == '/stacked-bar' || path == '/mix-map' || path == '/pie-map') {
            $location.path('/no_access');
          }
        }

        if (!hasAccess(RESOURCE.PRODUCTBASE, ACTION.READ) && !hasAccess(RESOURCE.PRODUCTBASE, ACTION.MANAGE)) {
          $('#liProductBase').attr('style', "display:none");
          if (path == '/product-base-manage' || path == '/emulator') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.READ) && !hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.productBaseButtonShow = false;
        }
        else if (hasAccess(RESOURCE.LOGISTICS, ACTION.MANAGE)) {
          $scope.productBaseButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.MESSAGE, ACTION.READ) && !hasAccess(RESOURCE.MESSAGE, ACTION.MANAGE)) {
          $('#liMessage').attr('style', "display:none");
          if (path == '/message') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.MESSAGE, ACTION.READ) && !hasAccess(RESOURCE.MESSAGE, ACTION.MANAGE)) {
          $scope.messageButtonShow = false;
        }
        else if (hasAccess(RESOURCE.MESSAGE, ACTION.MANAGE)) {
          $scope.messageButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.DEVICE, ACTION.READ) && !hasAccess(RESOURCE.DEVICE, ACTION.MANAGE)) {
          $('#liDevice').attr('style', "display:none");
          if (path == '/device') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.DEVICE, ACTION.READ) && !hasAccess(RESOURCE.DEVICE, ACTION.MANAGE)) {
          $scope.deviceButtonShow = false;
        }
        else if (hasAccess(RESOURCE.DEVICE, ACTION.MANAGE)) {
          $scope.deviceButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.ACCOUNT, ACTION.READ) && !hasAccess(RESOURCE.ACCOUNT, ACTION.MANAGE)) {
          $('#liAccount').attr('style', "display:none");
          if (path == '/account') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.ACCOUNT, ACTION.READ) && !hasAccess(RESOURCE.ACCOUNT, ACTION.MANAGE)) {
          $scope.accountButtonShow = false;
        }
        else if (hasAccess(RESOURCE.ACCOUNT, ACTION.MANAGE)) {
          $scope.accountButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.GROUP, ACTION.READ) && !hasAccess(RESOURCE.GROUP, ACTION.MANAGE)) {
          $('#liGroup').attr('style', "display:none");
          if (path == '/group') {
            $location.path('/no_access');
          }
        }
        else if (hasAccess(RESOURCE.GROUP, ACTION.READ) && !hasAccess(RESOURCE.GROUP, ACTION.MANAGE)) {
          $scope.groupButtonShow = false;
        }
        else if (hasAccess(RESOURCE.GROUP, ACTION.MANAGE)) {
          $scope.groupButtonShow = true;
        }
        else {
          isShowSetting = true;
        }

        if (!hasAccess(RESOURCE.PROFILE, ACTION.READ)) {
          $('#liPassword').attr('style', "display:none");
          if (path == '/setting') {
            $location.path('/no_access');
          }
        }
        else {
          isShowSetting = true;
        }

        if (!isShowSetting) {
          $('#liAllSettings').attr('style', "display:none");
        }
      }

      $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
        var path = current.$$route ? current.$$route.originalPath : '/dashboard';

        if ($scope.currAccountAllPermissions) {
          ApplyAccess(path);
        }
        else {
          $scope.$on('accountPermissions-ready', function (data) {
            ApplyAccess(path);
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

(function () {
  var app = angular.module('login', ['interceptor', 'YUNSOO_CONFIG', 'utils']);

  app.factory('loginService', ['$http', function ($http) {
    return {
      login: function (loginForm, onSuccess, onError) {
        $http.post('/api/auth/login/password', {
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

  app.controller('LoginCtrl', ['$scope', '$timeout', 'loginService', 'YUNSOO_CONFIG', 'utils',
    function ($scope, $timeout, loginService, YUNSOO_CONFIG, utils) {

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
          utils.auth.setAccessToken(data.access_token.token, DateTime.now().addSeconds(data.access_token.expires_in).valueOf());
          utils.auth.setPermanentToken(data.permanent_token.token, DateTime.now().addSeconds(data.permanent_token.expires_in).valueOf());

          //save current login form
          if (loginForm.rememberMe) {
            loginForm.password = '';
            console.log('[saving login form]');
            loginService.loginForm(loginForm);
          }

          //animation
          $('body').addClass('animated fadeOut');
          $('#panel-login').addClass('animated zoomOut');
          //redirect to index.html
          $timeout(function () {
            window.location.href = './';
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
                  if (!elHasSub && $listWidget.length == 0)
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
              nifty.container.toggleClass('mainnav-in mainnav-out').removeClass('mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();

            },
            'revealIn': function () {
                if (!nifty.container.hasClass('reveal')) nifty.container.addClass('reveal');
                nifty.container.addClass('mainnav-in').removeClass('mainnav-out mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();

            },
            'revealOut': function () {
                if (!nifty.container.hasClass('reveal')) nifty.container.addClass('reveal');
                nifty.container.removeClass('mainnav-in mainnav-lg mainnav-sm').addClass('mainnav-out');
                if (isSmallNav) unbindSmallNav();

            },
            'slideToggle': function () {
                if (!nifty.container.hasClass('slide')) nifty.container.addClass('slide');
                nifty.container.toggleClass('mainnav-in mainnav-out').removeClass('mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();

            },
            'slideIn': function () {
                if (!nifty.container.hasClass('slide')) nifty.container.addClass('slide');
                nifty.container.addClass('mainnav-in').removeClass('mainnav-out mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();

            },
            'slideOut': function () {
                if (!nifty.container.hasClass('slide')) nifty.container.addClass('slide');
                nifty.container.removeClass('mainnav-in mainnav-lg mainnav-sm').addClass('mainnav-out');
                if (isSmallNav) unbindSmallNav();

            },
            'pushToggle': function () {
                nifty.container.toggleClass('mainnav-in mainnav-out').removeClass('mainnav-lg mainnav-sm');
                if (nifty.container.hasClass('mainnav-in mainnav-out')) nifty.container.removeClass('mainnav-in');
                //if (nifty.container.hasClass('mainnav-in')) //nifty.container.removeClass('aside-in');
                if (isSmallNav) unbindSmallNav();

            },
            'pushIn': function () {
                nifty.container.addClass('mainnav-in').removeClass('mainnav-out mainnav-lg mainnav-sm');
                if (isSmallNav) unbindSmallNav();

            },
            'pushOut': function () {
                nifty.container.removeClass('mainnav-in mainnav-lg mainnav-sm').addClass('mainnav-out');
                if (isSmallNav) unbindSmallNav();

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

          /* ========================================================================
           * NIFTY CHECK v1.1
           * -------------------------------------------------------------------------
           * - ThemeOn.net -
           * ========================================================================*/
          !function ($) {
            "use strict";

            var allFormEl,
                formElement = function (el) {
                  if (el.data('nifty-check')) {
                    return;
                  } else {
                    el.data('nifty-check', true);
                    if (el.text().trim().length) {
                      el.addClass("form-text");
                    } else {
                      el.removeClass("form-text");
                    }
                  }


                  var input = el.find('input')[0],
                      groupName = input.name,
                      $groupInput = function () {
                        if (input.type == 'radio' && groupName) {
                          return $('.form-radio').not(el).find('input').filter('input[name=' + groupName + ']').parent();
                        } else {
                          return false;
                        }
                      }(),
                      changed = function () {
                        if (input.type == 'radio' && $groupInput.length) {
                          $groupInput.each(function () {
                            var $gi = $(this);
                            if ($gi.hasClass('active')) $gi.trigger('nifty.ch.unchecked');
                            $gi.removeClass('active');
                          });
                        }


                        if (input.checked) {
                          el.addClass('active').trigger('nifty.ch.checked');
                        } else {
                          el.removeClass('active').trigger('nifty.ch.unchecked');
                        }
                      };

                  if (input.checked) {
                    el.addClass('active');
                  } else {
                    el.removeClass('active');
                  }

                  $(input).on('change', changed);
                },
                methods = {
                  isChecked: function () {
                    return this[0].checked;
                  },
                  toggle: function () {
                    this[0].checked = !this[0].checked;
                    this.trigger('change');
                    return null;
                  },
                  toggleOn: function () {
                    if (!this[0].checked) {
                      this[0].checked = true;
                      this.trigger('change');
                    }
                    return null;
                  },
                  toggleOff: function () {
                    if (this[0].checked && this[0].type == 'checkbox') {
                      this[0].checked = false;
                      this.trigger('change');
                    }
                    return null;
                  }
                };

            $.fn.niftyCheck = function (method) {
              var chk = false;
              this.each(function () {
                if (methods[method]) {
                  chk = methods[method].apply($(this).find('input'), Array.prototype.slice.call(arguments, 1));
                } else if (typeof method === 'object' || !method) {
                  formElement($(this));
                }
              });
              return chk;
            };

            nifty.document.ready(function () {
              allFormEl = $('.form-checkbox, .form-radio');
              if (allFormEl.length) allFormEl.niftyCheck();
            });

            nifty.document.on('change', '.btn-file :file', function () {
              var input = $(this),
                  numFiles = input.get(0).files ? input.get(0).files.length : 1,
                  label = input.val().replace(/\\/g, '/').replace(/.*\//, ''),
                  size = function () {
                    try {
                      return input[0].files[0].size;
                    } catch (err) {
                      return 'Nan';
                    }
                  }(),
                  fileSize = function () {
                    if (size == 'Nan') {
                      return "Unknown";
                    }
                    var rSize = Math.floor(Math.log(size) / Math.log(1024));
                    return ( size / Math.pow(1024, rSize) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][rSize];
                  }();


              input.trigger('fileselect', [numFiles, label, fileSize]);
            });
          }(jQuery);
        });

    }]);
})(jQuery);

(function () {
  var app = angular.module('root');

  app.factory('accountManageService', ['$http', function ($http) {
    return {
      getAccounts: function (dataTable, fnSuccess, fnError) {
        $http.get('/api/account?' + dataTable.toString()).success(fnSuccess);

        return this;
      },
      createAccount: function (account, fnSuccess, fnError) {
        $http.post("/api/account", account).success(fnSuccess).error(fnError);

        return this;
      },
      getCurrentOrgGroups: function (fnSuccess) {
        var url = '/api/group';
        $http.get(url).success(fnSuccess);

        return this;
      },
      getCurrentAccountGroups: function (accountId, fnSuccess) {
        var url = '/api/account/' + accountId + '/group';
        $http.get(url).success(fnSuccess);

        return this;
      },
      addAccountsToGroup: function (accountId, groups, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/group';
        $http.put(url, groups).success(fnSuccess).error(fnError);

        return this;
      },
      authAccount: function (accountId, policy, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/accountpermissionpolicy';
        $http.post(url, policy).success(fnSuccess).error(fnError);

        return this;
      },
      cancelAuthAccount: function (accountId, policyCode, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/accountpermissionpolicy' + '?policy_code=' + policyCode;
        $http.delete(url).success(fnSuccess).error(fnError);

        return this;
      },
      getAccountAllPermissions: function (accountId, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/permission';
        $http.get(url).success(fnSuccess);
      }
    };
  }]);

  app.controller('AccountManageCtrl', [
    '$scope',
    '$timeout',
    'accountManageService',
    'YUNSOO_CONFIG',
    function ($scope, $timeout, accountManageService, YUNSOO_CONFIG) {

      var RESOURCE = YUNSOO_CONFIG.PAGE_ACCESS.RESOURCE;
      var ACTION = YUNSOO_CONFIG.PAGE_ACCESS.ACTION;

      var accountPermission = $scope.accountPermission = {
        dashBoardRead: '',
        productKeyRead: '',
        productKeyMng: '',
        packageRead: '',
        packageMng: '',
        logisticsRead: '',
        logisticsMng: '',
        reportRead: '',
        productRead: '',
        productMng: '',
        msgRead: '',
        msgMng: '',
        deviceRead: '',
        deviceMng: '',
        accountRead: '',
        accountMng: '',
        groupRead: '',
        groupMng: '',
        passwordRead: ''
      };

      var accountTable = {
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          accountManageService.getAccounts(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      };

      $timeout(function () {
        $('#accountForm').bootstrapValidator({
          message: '输入不合法',
          feedbackIcons: {
            valid: 'fa fa-check-circle fa-lg text-success',
            invalid: 'fa fa-times-circle fa-lg',
            validating: 'fa fa-refresh'
          },
          fields: {
            accountIdentifier: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入账号标识'
                }
              }
            },
            lastName: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入姓'
                }
              }
            },
            firstName: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入名'
                }
              }
            },
            email: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入邮箱'
                },
                emailAddress: {
                  message: '邮箱输入不合法'
                }
              }
            },
            phone: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入电话'
                },
                digits: {
                  message: '电话只能是数字'
                }
              }
            },
            password: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入密码'
                }
              }
            },
            passwordConfirm: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入确认密码'
                },
                identical: {
                  field: 'password',
                  message: '确认密码不一致'
                }
              }
            }
          }
        }).on('success.field.bv', function (e, data) {

        });

      }, 0);

      accountManageService.getCurrentOrgGroups(function (data) {
        account.curOrgGroups = data;
      });

      var account = $scope.account = {
        spinnerShow: false,
        accountTable: new $scope.utils.DataTable(accountTable),
        selectAccount: '',
        curOrgGroups: '',
        curSelectedGroups: [],
        create: {
          accountIdentifier: '',
          lastName: '',
          firstName: '',
          email: '',
          phone: '',
          password: '',
          passwordConfirm: '',
          createAccount: function () {

            if (account.create.accountIdentifier == '')
              return;

            if (account.create.firstName == '')
              return;

            if (account.create.lastName == '')
              return;

            if (account.create.email == '')
              return;

            if (account.create.phone == '')
              return;

            if (account.create.password == '')
              return;

            var accountObj = {};

            accountObj.org_id = $scope.context.organization.id;
            accountObj.identifier = account.create.accountIdentifier;
            accountObj.first_name = account.create.firstName;
            accountObj.last_name = account.create.lastName;
            accountObj.email = account.create.email;
            accountObj.phone = account.create.phone;
            accountObj.password = account.create.password;

            account.spinnerShow = true;

            accountManageService.createAccount(accountObj, function (data) {

              account.spinnerShow = false;

              $('#createAccountModal').modal('hide');

              $scope.utils.alert('success', '创建账号成功');

              account.accountTable = new $scope.utils.DataTable(accountTable);

            }, function (data) {
              account.spinnerShow = false;
              $scope.utils.alert('danger', '创建账号失败', '#createAccountModal .modal-dialog', false);
            });
          },
          showCreateAccountModal: function () {
            $('#createAccountModal').modal({
              backdrop: 'static',
              keyboard: false
            }).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          },
          hideCreateAccountModal: function () {
            account.spinnerShow = false;
            $('#createAccountModal').modal('hide');
          }
        },
        auth: {
          disableAll: false,
          checkAllAccess: function (isCheck) {
            accountPermission.dashBoardRead = isCheck;

            accountPermission.productKeyRead = isCheck;
            accountPermission.productKeyMng = isCheck;

            accountPermission.packageRead = isCheck;
            accountPermission.packageMng = isCheck;

            accountPermission.logisticsRead = isCheck;
            accountPermission.logisticsMng = isCheck;

            accountPermission.reportRead = isCheck;

            accountPermission.productRead = isCheck;
            accountPermission.productMng = isCheck;

            accountPermission.msgRead = isCheck;
            accountPermission.msgMng = isCheck;

            accountPermission.deviceRead = isCheck;
            accountPermission.deviceMng = isCheck;

            accountPermission.accountRead = isCheck;
            accountPermission.accountMng = isCheck;

            accountPermission.groupRead = isCheck;
            accountPermission.groupMng = isCheck;

            accountPermission.passwordRead = isCheck;
          },
          authDashBoardRead: function () {
            if (accountPermission.dashBoardRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.DASHBOARD + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.DASHBOARD + ':' + ACTION.READ, function () {
                  }, function () {
                  });
            }
          },
          authProductKeyRead: function () {
            if (accountPermission.productKeyRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTKEY + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductKeyMng: function () {
            if (accountPermission.productKeyMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authPackageRead: function () {
            if (accountPermission.packageRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PACKAGE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authPackageMng: function () {
            if (accountPermission.packageMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PACKAGE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authLogisticsRead: function () {
            if (accountPermission.logisticsRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.LOGISTICS + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authLogisticsMng: function () {
            if (accountPermission.logisticsMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.LOGISTICS + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authReportRead: function () {
            if (accountPermission.reportRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.REPORT + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.REPORT + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductRead: function () {
            if (accountPermission.productRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTBASE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductMng: function () {
            if (accountPermission.productMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
                  , function () {
                  }, function () {
                  });
            }
          },
          authMsgRead: function () {
            if (accountPermission.msgRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.MESSAGE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authMsgMng: function () {
            if (accountPermission.msgMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.MESSAGE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  });
            }
          },
          authDeviceRead: function () {
            if (accountPermission.deviceRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.DEVICE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.DEVICE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authDeviceMng: function () {
            if (accountPermission.deviceMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.DEVICE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.DEVICE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authAccountRead: function () {
            if (accountPermission.accountRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.ACCOUNT + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authAccountMng: function () {
            if (accountPermission.accountMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.ACCOUNT + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupRead: function () {
            if (accountPermission.groupRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.GROUP + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.GROUP + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupMng: function () {
            if (accountPermission.groupMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.GROUP + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.GROUP + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authPasswordRead: function () {
            if (accountPermission.passwordRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PROFILE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PROFILE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          showAuthAccountModal: function (accountId) {

            account.selectAccount = accountId;

            account.auth.checkAllAccess(false);
            account.auth.disableAll = false;

            accountManageService.getAccountAllPermissions(accountId, function (data) {

              for (var i = 0; i < data.length; i++) {
                if (data[i].resource_code == '*' && (data[i].action_code == '*')) {
                  account.auth.disableAll = true;
                  account.auth.checkAllAccess(true);

                  break;
                }
                else {
                  if (data[i].resource_code == RESOURCE.DASHBOARD && (data[i].action_code == ACTION.READ)) {
                    accountPermission.dashBoardRead = true;
                  }

                  if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.READ)) {
                    accountPermission.productKeyRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.productKeyMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.packageRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.packageMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.READ)) {
                    accountPermission.logisticsRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.logisticsMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.REPORT && (data[i].action_code == ACTION.READ)) {
                    accountPermission.reportRead = true;
                  }

                  if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.productRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.productMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.msgRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.msgMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.deviceRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.deviceMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.READ)) {
                    accountPermission.accountRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.accountMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.READ)) {
                    accountPermission.groupRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.groupMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.PROFILE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.passwordRead = true;
                  }
                }
              }

              $('#treeMenuModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            });
          },
          hideAuthAccountModal: function () {
            account.selectAccount = '';

            $('#treeMenuModal').modal('hide');
          }
        },
        assignGroup: {
          showAddToGroupModal: function (accountId) {
            account.selectAccount = accountId;

            accountManageService.getCurrentAccountGroups(accountId, function (data) {

              if (account.curOrgGroups != '') {

                $.each(account.curOrgGroups, function (name, value) {
                  var option = "<option value=" + value.id;

                  if (data != undefined && data.length > 0) {
                    $.each(data, function (name1, value1) {
                      if (value1.id == value.id) {
                        option += " selected = 'selected'";
                      }
                    });
                  }

                  option += ">" + value.name + "</option>";
                  $('#selectGroup').append(option);
                });
              }

              $('#selectGroup').chosen({width: '100%'});
              $("#selectGroup").change(function () {
                account.curSelectedGroups = $(this).val();
              });

              $('#addToGroupModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            }, function () {
              if (account.curOrgGroups != '') {

                $.each(account.curOrgGroups, function (name, value) {
                  var option = "<option value=" + value.id + ">" + value.name + "</option>";
                  $('#selectGroup').append(option);
                });
              }

              $('#selectGroup').chosen({width: '100%'});
              $("#selectGroup").change(function () {
                account.curSelectedGroups = $(this).val();
              });

              $('#addToGroupModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            });
          }
          ,
          hideAddAccountsModal: function () {
            account.spinnerShow = false;
            account.curSelectedGroups = [];
            $("#selectGroup").chosen("destroy");
            $("#selectGroup").html("");
            $('#addToGroupModal').modal('hide');

          }
          ,
          addAccountsToGroup: function () {
            account.spinnerShow = true;

            if (account.curSelectedGroups != '') {
              accountManageService.addAccountsToGroup(account.selectAccount, account.curSelectedGroups || [], function (data) {
                account.assignGroup.hideAddAccountsModal();
                $scope.utils.alert('info', '账号组更新成功');
              }, function (data) {
                account.spinnerShow = false;
                $scope.utils.alert('danger', '账号组更新失败', '#addToGroupModal .modal-dialog', false);
              });
            }
          }
        }
      };

    }
  ])
  ;
})
();
  (function () {
    var app = angular.module('root');

    app.factory("dashboardService", ["$http", function ($http) {
      return {
        getProductKeyQuantitySum: function (fnSuccess) {
          $http.get('/api/productkeybatch/sum/quantity').success(fnSuccess);
        },
        getUnVerifiedMessages: function (data, fnSuccess) {
          $http.get('/api/message/count/on?' + data).success(fnSuccess);
        },
        getAllMessages: function (data, fnSuccess) {
          $http.get('/api/message/count/on?' + data).success(fnSuccess);
        }
      }
    }]);

    app.controller("DashboardCtrl", ["$scope", "dashboardService", "$timeout", function ($scope, dashboardService, $timeout) {
      $scope.productKeyQuantitySum = 0;
      //get quantity sum of product key batches
      dashboardService.getProductKeyQuantitySum(function (data) {
        $scope.productKeyQuantitySum = data;
      });

      var Conversion = $scope.Conversion = function (num) {
        if (isNaN(num)) {
          return "这不是一个数字";
        }
        else {
          if (num < 0) {
            return "这是一个负数";
          }
          else if ((num >= 0) && (num < 10000)) {
            return num + "";
          }
          else if ((10000 <= num) && (num < 100000000)) {
            if (num % 10000 == 0) {
              return num / 10000 + "万"
            }
            else {
              return parseInt(num / 10000) + "万+";
            }
          }
          else if (100000000 <= num) {
            if (num % 100000000 == 0) {
              return num / 100000000 + "亿"
            }
            else {
              return parseInt(num / 100000000) + "亿+"
            }
          }
        }
      };

      function showMessages() {
        dashboardService.getUnVerifiedMessages('org_id=' + $scope.context.organization.id + '&status_code_in=created', function (data) {
          var unVerifiedMessages = data;

          dashboardService.getAllMessages('org_id=' + $scope.context.organization.id, function (data) {
            var allMessages = data;

            var percent = Math.round((unVerifiedMessages / allMessages) * 100);

            $('#maChart02').empty();
            //$('#maChart02').data('text', unVerifiedMessages + '/' + allMessages);
            $('#maChart02').data('text', unVerifiedMessages);
            $('#maChart02').data('percent', percent);
            $('#maChart02').circliful();

            $('#maChart02 .circle-info-half').css('padding-top', '5px');
          });
        });
      }

      function showProductKeyCreditSum(data) {
        $('#maChart04').empty();
        $('#maChart04').data('text', Conversion(data.remain));
        $('#maChart04').data('percent', data.percentage);
        $('#maChart04').circliful();

        $('#maChart04 .circle-info-half').css('padding-top', '5px');
      }

      $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
        var path = current.$$route ? current.$$route.originalPath : '/dashboard';
        if (path === '/dashboard') {

          $('#maChart01').circliful();
          $('#maChart02').circliful();
          $('#maChart03').circliful();
          $('#maChart04').circliful();

          $('#maChart01 .circle-info-half').css('padding-top', '5px');
          $('#maChart02 .circle-info-half').css('padding-top', '5px');
          $('#maChart03 .circle-info-half').css('padding-top', '5px');
          $('#maChart04 .circle-info-half').css('padding-top', '5px');

          if ($scope.context.organization) {
            showMessages();
          }
          else {
            $scope.$on('context-organization-ready', function (event) {
              showMessages();
            });
          }

          if ($scope.productKeyCreditSum && $scope.productKeyCreditSum.ready) {
            showProductKeyCreditSum($scope.productKeyCreditSum);
          } else {
            $scope.$on('productKeyCreditSum-ready', function (event, data) {
              showProductKeyCreditSum(data);
            });
          }
        }
      });

    }]);
})();
(function () {
  var app = angular.module('root');

  app.factory("deviceService", ["$http", function ($http) {
    return {
      getCurrentOrgDevices: function (dataTable, fnSuccess) {
        var url = '/api/device?' + dataTable.toString();
        $http.get(url).success(fnSuccess);

        return this;
      },
      getCurrentOrgAccounts: function (fnSuccess) {
        var url = '/api/account';
        $http.get(url).success(fnSuccess);

        return this;
      },
      getDeviceAuthToken: function (accountId, fnSuccess, fnError) {
        var url = '/api/auth/logintoken?account_id=' + accountId;
        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      },
      createAccount: function (account, fnSuccess, fnError) {
        $http.post("/api/account", account).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, deviceService, $timeout) {

    var myTime = null;

    deviceService.getCurrentOrgAccounts(function (data) {
      device.account.curOrgAccounts = data;
    });

    function utf16to8(str) {
      var out, i, len, c;

      out = "";
      len = str.length;
      for (i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
          out += str.charAt(i);
        } else if (c > 0x07FF) {
          out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
          out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
          out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
          out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
          out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
      }
      return out;
    }

    $timeout(function () {
      $('#createForm').bootstrapValidator({
        message: '输入不合法',
        feedbackIcons: {
          valid: 'fa fa-check-circle fa-lg text-success',
          invalid: 'fa fa-times-circle fa-lg',
          validating: 'fa fa-refresh'
        },
        fields: {
          accountIdentifier: {
            container: 'tooltip',
            validators: {
              notEmpty: {
                message: '请输入账号标识'
              }
            }
          },
          accountPassword: {
            container: 'tooltip',
            validators: {
              notEmpty: {
                message: '请输入账号密码'
              }
            }
          },
          accountPasswordConfirm: {
            container: 'tooltip',
            validators: {
              notEmpty: {
                message: '请输入确认密码'
              },
              identical: {
                field: 'accountPassword',
                message: '确认密码不一致'
              }
            }
          }
        }
      }).on('success.field.bv', function (e, data) {
        device.account.createEnabled = false;
        $scope.$apply();
      });

    }, 0);

    var device = $scope.device = {
      account: {
        curOrgAccounts: '',
        showCreateAccount: false,
        createEnabled: true,
        accountIdentifier: '',
        accountPassword: '',
        accountPasswordConfirm: '',
        spinnerShow: false,
        createAccount: function () {

          if (device.account.accountIdentifier == '') {
            return;
          }

          if (device.account.accountPassword == '') {
            return;
          }

          if (device.account.accountPasswordConfirm == '') {
            return;
          }

          device.account.spinnerShow = true;

          var accountObj = {};

          accountObj.org_id = $scope.context.organization.id;
          accountObj.identifier = device.account.accountIdentifier;
          accountObj.password = device.account.accountPassword;
          accountObj.first_name = '设备';
          accountObj.last_name = '设备';
          accountObj.email = 'device@device.com';
          accountObj.phone = '111';

          deviceService.createAccount(accountObj, function (data) {

            device.account.spinnerShow = false;

            $scope.utils.alert('success', '创建账号成功', '#myModal .modal-dialog', false);

            deviceService.getCurrentOrgAccounts(function (data) {
              device.account.curOrgAccounts = data;
            });

            device.account.showCreateAccount = false;

          }, function (data) {
            device.account.spinnerShow = false;
            $scope.utils.alert('danger', '创建账号失败', '#myModal .modal-dialog', false);
          });
        },
        cancelAccount: function () {
          device.account.showCreateAccount = false;
          device.account.spinnerShow = false;
        }
      },
      deviceTable: new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          deviceService.getCurrentOrgDevices(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      }),
      time_distance: 0,
      deviceComment: '',
      deviceName: '',
      selectAccount: '',
      deviceAuth: function () {

        if (device.selectAccount == '') {
          $('#divSelectAccount').addClass('has-error').addClass('has-feedback');
          return;
        }

        if (device.deviceName == '') {
          $('#divDeviceName').addClass('has-error').addClass('has-feedback');
          return;
        }

        if (device.deviceComment.length > 20) {
          $('#divDeviceComment').addClass('has-error').addClass('has-feedback');
          return;
        }

        $('#divDeviceName').addClass('has-success').addClass('has-feedback');
        $('#divSelectAccount').addClass('has-success').addClass('has-feedback');
        $('#divDeviceComment').addClass('has-success').addClass('has-feedback');

        var getAuthToken = function (data) {

          var postObject = {};
          postObject.a = $scope.context.account.id;
          postObject.t = data.token;
          postObject.dn = device.deviceName;
          postObject.dc = device.deviceComment;

          var qrCodeContent = JSON.stringify(postObject);

          $("#authQRCode").html('');

          $scope.qrcode = $("#authQRCode").qrcode({
            render: "canvas", //table方式
            width: 300, //宽度
            height: 300, //高度
            foreground: "#337ab7",//前景颜色
            correctLevel: 3,//纠错等级
            text: utf16to8(qrCodeContent) //任意内容
          });

        };

        device.time_distance = 120;
        if (myTime == null) {
          myTime = setInterval(function () {
            device.time_distance--;

            var int_minute = Math.floor(device.time_distance / 60);
            var int_second = Math.floor(device.time_distance - int_minute * 60);

            if (int_minute < 10) {
              int_minute = "0" + int_minute;
            }
            if (int_second < 10) {
              int_second = "0" + int_second;
            }

            $('#time_refresh').html("设备授权码将在" + int_minute + "分" + int_second + "秒内刷新");

            if (device.time_distance == 0) {
              device.time_distance = 120;
              deviceService.getDeviceAuthToken(device.selectAccount, getAuthToken, function () {
                $('#time_refresh').html("");
                clearInterval(myTime);
                myTime = null;
                $("#authQRCode").html('');
                $scope.utils.alert('danger', '获取账号Token失败', '#myModal .modal-dialog', false);
              });
            }
          }, 1000);
        }

        setTimeout(function () {
          clearInterval(myTime);
        }, 10 * 120 * 1000);

        deviceService.getDeviceAuthToken(device.selectAccount, getAuthToken, function () {
          $('#time_refresh').html("");
          clearInterval(myTime);
          myTime = null;
          $("#authQRCode").html('');
          $scope.utils.alert('danger', '获取账号Token失败', '#myModal .modal-dialog', false);
        });
      }

      ,
      cancelDeviceAuth: function () {
        $scope.utils.alert('info', '设备取消授权正在开发中');
      }
      ,
      showDeviceModal: function () {
        $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
          $(this).removeData("bs.modal");
        });
      }
      ,
      hideDeviceModal: function () {
        device.account.spinnerShow = false;
        $('#myModal').modal('hide');
      }
    };

  }])
  ;
})
();
  (function () {
    var app = angular.module('root');

    app.factory("echartsBarService", ["$http", function ($http) {
      return {
        getQRCode: function (peirod, fnSuccess, fnError) {
          var url = '/api/report/myorganization/product_qrcode_count/' + peirod;
          $http.get(url).success(fnSuccess).error(fnError);

          return this;
        }
      };
    }]);

    app.controller("echartsBarCtrl", ["$scope", "echartsBarService", "$timeout", function ($scope, echartsBarService, $timeout) {

      var dataTable = $scope.dataTable = new $scope.utils.DateHelp(getData);

      var getQRCode = function (data) {

        var echartBar = echarts.init($('#echartBar')[0]);

        var option = {
          title: {
            text: '产品贴码统计'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['产品贴码统计']
          },
          toolbox: {
            show: true,
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          calculable: true,
          xAxis: [
            {
              type: 'category',
              show: true,
              data: data.dimensions.values
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '产品贴码统计',
              type: 'bar',
              data: data.data
            }
          ]
        };

        echartBar.setOption(option);
      };

      echartsBarService.getQRCode(dataTable.getDateStr(), getQRCode, function () {
        //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
        var data = {};
        data.data = [];
        data.dimensions = {};
        data.dimensions.values = [];
        getQRCode(data);
      });

      function getData(year, mon, day) {
        echartsBarService.getQRCode(dataTable.getDateStr(year, mon, day), getQRCode, function () {
          //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
          var data1 = {};
          data1.data = [];
          data1.dimensions = {};
          data1.dimensions.values = [];
          getQRCode(data1);
        });
      }
    }]);
  })();
  (function () {
    var app = angular.module('root');

    app.factory("mixMapService", ["$http", function ($http) {
      return {
        getLocationCount: function (peirod, fnSuccess, fnError) {
          var url = '/api/report/myorganization/location_scan_count/' + peirod;
          $http.get(url).success(fnSuccess).error(fnError);

          return this;
        }
      };
    }]);

    app.controller("mixMapCtrl", ["$scope", "mixMapService", "$timeout", function ($scope, mixMapService, $timeout) {

      var dataTable = $scope.dataTable = new $scope.utils.DateHelp(getData);

      mixMapService.getLocationCount(dataTable.getDateStr(), getLocationCount, function () {
        //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
        var data = {};
        data.data = [];
        data.dimensions = {};
        data.dimensions.values = [];
        getLocationCount(data);
      });

      function getData(year, mon, day) {
        mixMapService.getLocationCount(dataTable.getDateStr(year, mon, day), getLocationCount, function () {
          //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
          var data1 = {};
          data1.data = [];
          data1.dimensions = {};
          data1.dimensions.values = [];
          getLocationCount(data1);
        });
      }

      function getLocationCount(data) {

        var mixMap = echarts.init($('#mixMap')[0]);

        var dataShow = [];
        for (var i = 0; i < data.data.length; i++) {
          dataShow.push({
            name: data.dimensions.values[i].replace('市', '').replace('省', '').replace('自治区', ''),
            value: data.data[i]
          });
        }

        var option = {
          title: {
            text: '产品地域统计'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            x: 'right',
            selectedMode: false,
            data: data.dimensions.values
          },
          dataRange: {
            orient: 'horizontal',
            min: 0,
            max: 55000,
            text: ['高', '低'],           // 文本，默认为数值文本
            splitNumber: 0
          },
          toolbox: {
            show: true,
            orient: 'vertical',
            x: 'right',
            y: 'center',
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false}
            }
          },
          series: [
            {
              name: '产品地域统计',
              type: 'map',
              mapType: 'china',
              mapLocation: {
                x: 'left'
              },
              selectedMode: 'multiple',
              itemStyle: {
                normal: {label: {show: true}},
                emphasis: {label: {show: true}}
              },
              data: dataShow
            },
            {
              name: '产品地域统计',
              type: 'pie',
              roseType: 'area',
              tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
              },
              center: [$('#mixMap')[0].offsetWidth - 250, 225],
              radius: [30, 120],
              data: dataShow
            }
          ],
          animation: false
        };

        var ecConfig = echarts.config;
        mixMap.on(ecConfig.EVENT.MAP_SELECTED, function (param) {
          var selected = param.selected;
          var mapSeries = option.series[0];
          var data = [];
          var legendData = [];
          var name;
          for (var p = 0, len = mapSeries.data.length; p < len; p++) {
            name = mapSeries.data[p].name;
            //mapSeries.data[p].selected = selected[name];
            if (selected[name]) {
              data.push({
                name: name,
                value: mapSeries.data[p].value
              });
              legendData.push(name);
            }
          }
          option.legend.data = legendData;
          option.series[1].data = data;
          mixMap.setOption(option, true);
        });

        mixMap.setOption(option);

      }
    }]);
  })();
  (function () {
    var app = angular.module('root');

    app.factory("pieMapService", ["$http", function ($http) {
      return {
        getScanCount: function (peirod, fnSuccess, fnError) {
          var url = '/api/report/myorganization/product_scan_count/' + peirod;
          $http.get(url).success(fnSuccess).error(fnError);

          return this;
        }
      };
    }]);

    app.controller("pieMapCtrl", ["$scope", "pieMapService", "$timeout", function ($scope, pieMapService, $timeout) {

      var dataTable = $scope.dataTable = new $scope.utils.DateHelp(getData);

      pieMapService.getScanCount(dataTable.getDateStr(), getScanCount, function () {
        //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
        var data = {};
        data.data = [];
        data.dimensions = {};
        data.dimensions.values = [];
        getScanCount(data);
      });

      function getData(year, mon, day) {
        pieMapService.getScanCount(dataTable.getDateStr(year, mon, day), getScanCount, function () {
          //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
          var data1 = {};
          data1.data = [];
          data1.dimensions = {};
          data1.dimensions.values = [];
          getScanCount(data1);
        });
      }

      function getScanCount(data) {

        var pieMap = echarts.init($('#pieMap')[0]);

        var dataShow = [];
        for (var i = 0; i < data.data.length; i++) {
          dataShow.push({value: data.data[i], name: data.dimensions.values[i]});
        }

        var option = {
          title: {
            text: '产品扫码统计',
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            x: 'left',
            data: data.dimensions.values
          },
          toolbox: {
            show: true,
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              magicType: {
                show: true,
                type: ['pie', 'funnel'],
                option: {
                  funnel: {
                    x: '25%',
                    width: '50%',
                    funnelAlign: 'left',
                    max: 1548
                }
                }
              },
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          calculable: true,
          series: [
            {
              name: '扫码统计',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: dataShow
            }
          ]
        };

        pieMap.setOption(option);

      }
    }]);
  })();
  (function () {
    var app = angular.module('root');

    app.factory("stackedBarService", ["$http", function ($http) {
      return {
        getScanCount: function (peirod, fnSuccess, fnError) {
          var url = '/api/report/myorganization/product_month_scan_count/' + peirod;
          $http.get(url).success(fnSuccess).error(fnError);

          return this;
        }
      };
    }]);

    app.controller("stackedBarCtrl", ["$scope", "stackedBarService", "$timeout", function ($scope, stackedBarService, $timeout) {

      var dataTable = $scope.dataTable = new $scope.utils.DateHelp(getData);

      stackedBarService.getScanCount(dataTable.getDateStr(), getScanCount, function () {
        //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
        var data = {};
        data.data = [];
        data.dimensions = {};
        data.dimensions.values = [];
        data.dimensions.values.push([]);
        data.dimensions.values.push([]);
        getScanCount(data);
      });

      function getData(year, mon, day) {
        stackedBarService.getScanCount(dataTable.getDateStr(year, mon, day), getScanCount, function () {
          //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
          var data1 = {};
          data1.data = [];
          data1.dimensions = {};
          data1.dimensions.values = [];
          data1.dimensions.values.push([]);
          data1.dimensions.values.push([]);
          getScanCount(data1);
        });
      }

      function getScanCount(data) {

        var stackedBar = echarts.init($('#stackedBar')[0]);

        var dataShow = [];

        if (data.data.length == 0) {
          dataShow.push({});
        }
        else {
          for (var i = 0; i < data.data.length; i++) {

            for (var j = 0; j < data.data[i].length; j++) {
              if (data.data[i][j] == null)
                data.data[i][j] = 0;
            }

            dataShow.push({
              name: data.dimensions.values[0][i],
              type: 'bar',
              stack: '总量',
              itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
              data: data.data[i]
            });
          }
        }

        var option = {
          title: {
            text: '产品扫码月统计'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          legend: {
            orient: 'vertical',
            x: 'right',
            y: 'center',
            data: data.dimensions.values[0]
          },
          toolbox: {
            show: true,
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          calculable: true,
          xAxis: [
            {
              type: 'value'
            }
          ],
          yAxis: [
            {
              type: 'category',
              show: true,
              data: data.dimensions.values[1]
            }
          ],
          series: dataShow
        };

        stackedBar.setOption(option);

      }
    }]);
  })();
  (function () {
    var app = angular.module('root');

    app.value('ProductDetailsVersion', '1.0');

    app.factory("emulatorService", ["$http", function ($http) {
      return {
        createProWithDetail: function (proDetail, fnSuccess, fnError) {
          $http.post("/api/productbase/withdetail", proDetail).success(fnSuccess).error(fnError);

          return this;
        }
      };
    }]);

    app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "FileUploader", "$location", "ProductDetailsVersion", function ($scope, emulatorService, $timeout, FileUploader, $location, ProductDetailsVersion) {

      var uploader = $scope.uploader = new FileUploader({
        url: ''
      });

      //set AccessToken http header
      var accessToken = $scope.utils.auth.getAccessToken();
      accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

      $scope.fileInput = '';

      var product = $scope.product = {
        productInfos: [{key: '', value: ''}],
        productAddress: [{address: '', tel: ''}],
        productCommerce: [{title: '', url: ''}],
        barCode: '',
        productName: '',
        expireDate: 1,
        expireDateUnitValue: [{key: 'year', value: '年'}, {key: 'month', value: '月'}, {key: 'week', value: '周'}, {
          key: 'day',
          value: '天'
        }, {key: 'hour', value: '时'}],
        expireDateUnit: '',
        comment: '',
        keyTypePubInput: '',
        keyTypePriInput: '',
        keyTypeRFIDInput: '',
        hotline: '',
        support: '',
        proPicPreview: false,
        addProductInfo: function () {
          this.productInfos.push({key: '', value: ''});
        },
        subProductInfo: function () {
          this.productInfos.pop();
        },
        addProAddress: function () {
          this.productAddress.push({address: '', tel: ''});
        },
        subProAddress: function () {
          this.productAddress.pop();
        },
        addProductCommerce: function () {
          this.productCommerce.push({title: '', url: ''});
        },
        subProductCommerce: function () {
          this.productCommerce.pop();
        }
      };

      $scope.preview = function () {

        var dataPreview = {};
        dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo-mobile?access_token=" + $scope.utils.auth.getAccessToken();
        dataPreview.proImgUrl = $scope.fileInput;
        dataPreview.barcode = product.barCode;
        dataPreview.name = product.productName;
        dataPreview.details = product.productInfos;

        $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);
      };

      $scope.submit = function (isValid) {
        //if (product.barCode == '') {
        //  $scope.utils.alert('info', '产品BarCode不能为空');
        //  return;
        //}
        //
        //if (product.productName == '') {
        //  $scope.utils.alert('info', '产品名不能为空');
        //  return;
        //}

        if (!isValid) {
          $scope.utils.alert('info', '页面验证有错误，请返回检查');
          return;
        }

        if (!product.keyTypePubInput && !product.keyTypePriInput && !product.keyTypeRFIDInput) {
          $scope.utils.alert('info', '产品码类型至少要选择一种');
          return;
        }

        if (product.expireDateUnit == '') {
          $scope.utils.alert('info', '请选择产品过期单位');
          return;
        }
        //
        //if (!(/(^[1-9]\d*$)/.test(product.expireDate))) {
        //  $scope.utils.alert('info', '产品过期时间应为正整数');
        //  return;
        //}

        if (uploader.queue.length == 0) {
          $scope.utils.alert('info', '产品图片不能为空');
          return;
        }

        var proWithDetails = {};

        proWithDetails.category_id = 0;
        proWithDetails.barcode = product.barCode;
        proWithDetails.name = product.productName;
        proWithDetails.comments = product.comment;
        proWithDetails.product_key_type_codes = [];
        if (product.keyTypePubInput)
          proWithDetails.product_key_type_codes.push("qr_public");
        if (product.keyTypePriInput)
          proWithDetails.product_key_type_codes.push("qr_secure");
        if (product.keyTypeRFIDInput)
          proWithDetails.product_key_type_codes.push("rfid");

        proWithDetails.shelf_life = product.expireDate - 0;
        proWithDetails.shelf_life_interval = product.expireDateUnit;

        proWithDetails.status_code = '待审核';

        var proDetails = {};
        proDetails.version = ProductDetailsVersion;

        proDetails.details = [];
        for (var proInfo in product.productInfos) {
          if (product.productInfos[proInfo].key != '' && product.productInfos[proInfo].value != '')
            proDetails.details.push({
              name: product.productInfos[proInfo].key,
              value: product.productInfos[proInfo].value
            });
        }

        proDetails.contact = {hotline: product.hotline, support: product.support};

        proDetails['e_commerce'] = [];
        for (var proCommerce in product.productCommerce) {
          if (product.productCommerce[proCommerce].title != '' && product.productCommerce[proCommerce].url != '') {
            proDetails['e_commerce'].push({
              title: product.productCommerce[proCommerce].title,
              url: product.productCommerce[proCommerce].url
            });
          }
        }

        proDetails['t_commerce'] = [];
        for (var proAddress in product.productAddress) {
          if (product.productAddress[proAddress].address != '' && product.productAddress[proAddress].tel != '') {
            proDetails['t_commerce'].push({
              address: product.productAddress[proAddress].address,
              tel: product.productAddress[proAddress].tel
            });
          }
        }

        proWithDetails.details = JSON.stringify(proDetails);

        try {
          emulatorService.createProWithDetail(proWithDetails, function (data) {
                if (data.id != null && data.id != '') {
                  uploader.queue[uploader.queue.length - 1].url = '/api/productbase/withdetailfile/' + data.id + "/full-mobile";
                  uploader.queue[uploader.queue.length - 1].headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken;

                  uploader.uploadAll();

                  $scope.utils.alert('success', '创建产品成功');

                  $location.path('/product-base-manage');
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
        var divImgWrap = $("#divImgWrap");
        var fileInput = $("#fileInput");
        var imgProductbase = $("#imgProductbase");

        if (typeof FileReader === 'undefined') {
          divImgWrap.html("您的浏览器不支持图片预览");
        } else {
          fileInput.change(function () {

                var file = uploader.queue[uploader.queue.length - 1]._file;
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function (e) {
                  product.proPicPreview = true;
                  $scope.$apply();

                  imgProductbase.attr('src', this.result);
                  $scope.fileInput = this.result;
                };

                uploader.queue = uploader.queue.slice(uploader.queue.length - 1, uploader.queue.length);
            }
          );
        }

        //$('#createProduct').bootstrapValidator({
        //  message: '该字段不能为空',
        //  feedbackIcons: {
        //    valid: 'fa fa-check-circle fa-lg text-success',
        //    invalid: 'fa fa-times-circle fa-lg',
        //    validating: 'fa fa-refresh'
        //  },
        //  fields: {
        //    barCode: {
        //      validators: {
        //        notEmpty: {
        //          message: '请输入产品BarCode'
        //        }
        //      }
        //    },
        //    productName: {
        //      validators: {
        //        notEmpty: {
        //          message: '请输入产品名'
        //        }
        //      }
        //    },
        //    greaterthan: {
        //      validators: {
        //        notEmpty: {
        //          message: '请输入产品过期时间'
        //        },
        //        greaterThan: {
        //          inclusive: false,
        //          //If true, the input value must be greater than or equal to the comparison one.
        //          //If false, the input value must be greater than the comparison one
        //          value: 0,
        //          message: '请输入大于1的正整数'
        //        }
        //      }
        //    }
        //  }
        //}).on('success.field.bv', function (e, data) {
        //  // $(e.target)  --> The field element
        //  // data.bv      --> The BootstrapValidator instance
        //  // data.field   --> The field name
        //  // data.element --> The field element
        //
        //  var $parent = data.element.parents('.form-group');
        //
        //  // Remove the has-success class
        //  $parent.removeClass('has-success');
        //});
      }, 0);

    }]);
  })();
  (function () {
    var app = angular.module('root');

    app.factory('groupService', ['$http', function ($http) {
      return {
        getGroups: function (fnSuccess, fnError) {
          $http.get('/api/group').success(fnSuccess);

          return this;
        },
        createGroup: function (group, fnSuccess, fnError) {
          $http.post("/api/group", group).success(fnSuccess).error(fnError);

          return this;
        },
        getCurrentOrgAccounts: function (fnSuccess) {
          var url = '/api/account';
          $http.get(url).success(fnSuccess);

          return this;
        },
        getCurrentGroupAccounts: function (groupId, fnSuccess) {
          var url = '/api/group/' + groupId + '/account';
          $http.get(url).success(fnSuccess);

          return this;
        },
        addAccountsToGroup: function (groupId, accounts, fnSuccess, fnError) {
          var url = '/api/group/' + groupId + '/account';
          $http.put(url, accounts).success(fnSuccess).error(fnError);

          return this;
        },
        deleteGroup: function (groupId, fnSuccess, fnError) {
          $http.delete('/api/group/' + groupId).success(fnSuccess);

          return this;
        },
        authGroup: function (groupId, policy, fnSuccess, fnError) {
          var url = '/api/group/' + groupId + '/grouppermissionpolicy';
          $http.post(url, policy).success(fnSuccess).error(fnError);

          return this;
        },
        cancelAuthGroup: function (groupId, policyCode, fnSuccess, fnError) {
          var url = '/api/group/' + groupId + '/grouppermissionpolicy' + '?policy_code=' + policyCode;
          $http.delete(url).success(fnSuccess).error(fnError);

          return this;
        },
        getGroupAllPermissions: function (groupId, fnSuccess, fnError) {
          var url = '/api/group/' + groupId + '/permission';
          $http.get(url).success(fnSuccess);
        }
      };
    }]);

    app.controller('groupCtrl', [
      '$scope',
      '$timeout',
      '$route',
      'groupService',
      'YUNSOO_CONFIG',
      function ($scope, $timeout, $route, groupService, YUNSOO_CONFIG) {

        var RESOURCE = YUNSOO_CONFIG.PAGE_ACCESS.RESOURCE;
        var ACTION = YUNSOO_CONFIG.PAGE_ACCESS.ACTION;

        var groupDatatable = {
          sortable: {
            target: '#sort-bar',
            sort: 'createdDateTime,desc'
          },
          pageable: {
            page: 0,
            size: 20
          },
          flush: function (callback) {
            groupService.getGroups(function (data, status, headers) {
              callback({data: data, headers: headers});
            });
          }
        };

        var groupPermission = $scope.groupPermission = {
          dashBoardRead: '',
          productKeyRead: '',
          productKeyMng: '',
          packageRead: '',
          packageMng: '',
          logisticsRead: '',
          logisticsMng: '',
          reportRead: '',
          productRead: '',
          productMng: '',
          msgRead: '',
          msgMng: '',
          deviceRead: '',
          deviceMng: '',
          accountRead: '',
          accountMng: '',
          groupRead: '',
          groupMng: '',
          passwordRead: ''
        };

        groupService.getCurrentOrgAccounts(function (data) {
          group.curOrgAccounts = data;
        });

        var group = $scope.group = {
          name: '',
          description: '',
          spinnerShow: false,
          curDeleteGroup: '',
          curDeleteGroupName: '',
          curSelectGroup: '',
          groupTable: new $scope.utils.DataTable(groupDatatable),
          curOrgAccounts: '',
          selectAccounts: [],
          auth: {
            disableAll: false,
            checkAllAccess: function (isCheck) {
              groupPermission.dashBoardRead = isCheck;

              groupPermission.productKeyRead = isCheck;
              groupPermission.productKeyMng = isCheck;

              groupPermission.packageRead = isCheck;
              groupPermission.packageMng = isCheck;

              groupPermission.logisticsRead = isCheck;
              groupPermission.logisticsMng = isCheck;

              groupPermission.reportRead = isCheck;

              groupPermission.productRead = isCheck;
              groupPermission.productMng = isCheck;

              groupPermission.msgRead = isCheck;
              groupPermission.msgMng = isCheck;

              groupPermission.deviceRead = isCheck;
              groupPermission.deviceMng = isCheck;

              groupPermission.accountRead = isCheck;
              groupPermission.accountMng = isCheck;

              groupPermission.groupRead = isCheck;
              groupPermission.groupMng = isCheck;

              groupPermission.passwordRead = isCheck;
            },
            authDashBoardRead: function () {
              if (groupPermission.dashBoardRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.DASHBOARD + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
            }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.DASHBOARD + ':' + ACTION.READ, function () {
                    }, function () {
                    });
              }
            },
            authProductKeyRead: function () {
              if (groupPermission.productKeyRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PRODUCTKEY + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authProductKeyMng: function () {
              if (groupPermission.productKeyMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authPackageRead: function () {
              if (groupPermission.packageRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PACKAGE + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PACKAGE + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authPackageMng: function () {
              if (groupPermission.packageMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PACKAGE + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PACKAGE + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authLogisticsRead: function () {
              if (groupPermission.logisticsRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.LOGISTICS + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.LOGISTICS + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authLogisticsMng: function () {
              if (groupPermission.logisticsMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.LOGISTICS + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.LOGISTICS + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authReportRead: function () {
              if (groupPermission.reportRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.REPORT + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.REPORT + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authProductRead: function () {
              if (groupPermission.productRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PRODUCTBASE + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authProductMng: function () {
              if (groupPermission.productMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authMsgRead: function () {
              if (groupPermission.msgRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.MESSAGE + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.MESSAGE + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authMsgMng: function () {
              if (groupPermission.msgMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.MESSAGE + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.MESSAGE + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    });
              }
            },
            authDeviceRead: function () {
              if (groupPermission.deviceRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.DEVICE + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.DEVICE + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authDeviceMng: function () {
              if (groupPermission.deviceMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.DEVICE + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.DEVICE + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authGroupRead: function () {
              if (groupPermission.accountRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.ACCOUNT + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.ACCOUNT + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authGroupMng: function () {
              if (groupPermission.accountMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.ACCOUNT + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.ACCOUNT + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authGroupRead: function () {
              if (groupPermission.groupRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.GROUP + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.GROUP + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            authGroupMng: function () {
              if (groupPermission.groupMng) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.GROUP + ':' + ACTION.MANAGE
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.GROUP + ':' + ACTION.MANAGE, function () {
                    }, function () {
                    }
                );
              }
            },
            authPasswordRead: function () {
              if (groupPermission.passwordRead) {
                groupService.authGroup(group.curSelectGroup, {
                  policy_code: RESOURCE.PROFILE + ':' + ACTION.READ
                }, function () {
                }, function () {
                });
              }
              else {
                groupService.cancelAuthGroup(group.curSelectGroup,
                    RESOURCE.PROFILE + ':' + ACTION.READ, function () {
                    }, function () {
                    }
                );
              }
            },
            showAuthGroupModal: function (id) {

              group.curSelectGroup = id;

              group.auth.checkAllAccess(false);
              group.auth.disableAll = false;

              groupService.getGroupAllPermissions(id, function (data) {

                for (var i = 0; i < data.length; i++) {
                  if (data[i].resource_code == '*' && (data[i].action_code == '*')) {
                    group.auth.disableAll = true;
                    group.auth.checkAllAccess(true);

                    break;
                  }
                  else {
                    if (data[i].resource_code == RESOURCE.DASHBOARD && (data[i].action_code == ACTION.READ)) {
                      groupPermission.dashBoardRead = true;
                    }

                    if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.READ)) {
                      groupPermission.productKeyRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.productKeyMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.READ)) {
                      groupPermission.packageRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.packageMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.READ)) {
                      groupPermission.logisticsRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.logisticsMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.REPORT && (data[i].action_code == ACTION.READ)) {
                      groupPermission.reportRead = true;
                    }

                    if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.READ)) {
                      groupPermission.productRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.productMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.READ)) {
                      groupPermission.msgRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.msgMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.READ)) {
                      groupPermission.deviceRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.deviceMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.READ)) {
                      groupPermission.accountRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.accountMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.READ)) {
                      groupPermission.groupRead = true;
                    }
                    if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.MANAGE)) {
                      groupPermission.groupMng = true;
                    }

                    if (data[i].resource_code == RESOURCE.PROFILE && (data[i].action_code == ACTION.READ)) {
                      groupPermission.passwordRead = true;
                    }
                  }
                }

                $('#treeMenuModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                  $(this).removeData("bs.modal");
                });
              });
            },
            hideAuthGroupModal: function () {
              group.curSelectGroup = '';

              $('#treeMenuModal').modal('hide');
            }
          },
          showGroupModal: function () {
            $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          },
          showAddAccountModal: function (id) {
            group.curSelectGroup = id;

            groupService.getCurrentGroupAccounts(id, function (data) {

              if (group.curOrgAccounts != '') {

                $.each(group.curOrgAccounts, function (name, value) {
                  var option = "<option value=" + value.id;

                  if (data != undefined && data.length > 0) {
                    $.each(data, function (name1, value1) {
                      if (value1.id == value.id) {
                        option += " selected = 'selected'";
                    }
                    });
                }

                  option += ">" + value.last_name + value.first_name + "</option>";
                  $('#selectAccount').append(option);
                });
              }

              $('#selectAccount').chosen({width: '100%'});
              $("#selectAccount").change(function () {
                group.selectAccounts = $(this).val();
            });

              $('#addAccountsModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            }, function () {
              if (group.curOrgAccounts != '') {

                $.each(group.curOrgAccounts, function (name, value) {
                  var option = "<option value=" + value.id + ">" + value.last_name + value.first_name + "</option>";
                  $('#selectAccount').append(option);
                });
              }

              $('#selectAccount').chosen({width: '100%'});
              $("#selectAccount").change(function () {
                group.selectAccounts = $(this).val();
              });

              $('#addAccountsModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            });
          },
          hideAddAccountsModal: function () {
            group.curSelectGroup = '';
            group.spinnerShow = false;
            $("#selectAccount").chosen("destroy");
            $("#selectAccount").html("");
            $('#addAccountsModal').modal('hide');
          },
          addAccountsToGroup: function () {
            group.spinnerShow = true;

            if (group.curSelectGroup != '') {
              groupService.addAccountsToGroup(group.curSelectGroup, group.selectAccounts || [], function (data) {
                group.hideAddAccountsModal();
                $scope.utils.alert('info', '账号更新成功');
              }, function (data) {
                group.spinnerShow = false;
                $scope.utils.alert('danger', '账号更新失败', '#addAccountsModal .modal-dialog', false);
              });
            }
          },
          deleteGroup: function (id, name) {
            group.curDeleteGroup = id;
            group.curDeleteGroupName = name;

            $('#deleteConfirmDialog').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          },
          deleteGroupConfirm: function () {
            if (group.curDeleteGroup != '') {
              groupService.deleteGroup(group.curDeleteGroup, function (data, status) {
                $('#deleteConfirmDialog').modal('hide');
                $scope.utils.alert('info', '账号组删除成功');
                group.groupTable = new $scope.utils.DataTable(groupDatatable);
              });
            }
          },
          deleteGroupCancel: function () {
            group.curDeleteGroup = '';
            $('#deleteConfirmDialog').modal('hide');
          },
          createGroup: function (isValid) {
            if (!isValid) {
              $scope.utils.alert('info', '页面验证有错误，请返回检查');
              return;
            }

            group.spinnerShow = true;

            var groupObj = {
              name: group.name,
              description: group.description
            };

            groupService.createGroup(groupObj, function (data) {

              group.spinnerShow = false;

              $('#myModal').modal('hide');

              $scope.utils.alert('info', '账号组创建成功');

              this.groupTable = new $scope.utils.DataTable(groupDatatable);
            }, function (error) {
              $scope.utils.alert('danger', '账号组创建失败', '#myModal .modal-dialog', false);
            });
          }
        };

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

      $scope.showDesc = false;

        //set AccessToken http header
      var accessToken = $scope.utils.auth.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
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
      },
      createMessage: function (message, fnSuccess, fnError) {
        $http.post("/api/message", message).success(fnSuccess).error(fnError);
      }
    };
  }]);

  app.controller('MessageCtrl', ['$scope', '$timeout', 'messageService', function ($scope, $timeout, messageService) {

    $scope.spinnerShow = false;

    var message = $scope.message = {
      title: '',
      body: '',
      type: 'business',
      org_id: '',
      link: ''
    };

    if ($scope.context.organization) {
      $scope.message.org_id = $scope.context.organization.id;
      initMsgTable($scope.message.org_id)
    }
    else {
      $scope.$on('context-organization-ready', function (event) {
        $scope.message.org_id = $scope.context.organization.id;
        initMsgTable($scope.message.org_id)
      });
    }

    var messageTypes = {
      business: '商家信息',
      platform: '云溯平台信息'
    };
    var messageStatuses = {
      created: '已创建',
      approved: '通过审核',
      deleted: '已删除'
    };

    function initMsgTable(orgId) {
      $scope.messageTable = new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 200
        },
        flush: function (callback) {
          messageService.getMessages(this, orgId, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      });
    }

    $scope.formatMessageType = function (typeCode) {
      return messageTypes[typeCode] || typeCode;
    };

    $scope.formatMessageStatus = function (statusCode) {
      return messageStatuses[statusCode] || statusCode;
    };

    //init  validator
    $timeout(function () {
      $('#msgForm').bootstrapValidator({
        message: '非法输入',
        feedbackIcons: {
          valid: 'fa fa-check-circle fa-lg text-success',
          invalid: 'fa fa-times-circle fa-lg',
          validating: 'fa fa-refresh'
        },
        fields: {
          title: {
            validators: {
              notEmpty: {
                message: '消息标题不能为空'
              }
            }
          },
          body: {
            validators: {
              notEmpty: {
                message: '消息正文不能为空'
              }
            }
          },
          link: {
            validators: {
              notEmpty: {
                message: '链接不能为空'
              }
            }
          }
        }
      }).on('success.field.bv', function (e, data) {
        e.preventDefault();
        //login on validation success
      });

    }, 0); //end of $timeout

    $scope.submit = function () {

      if (message.title == '') {
        $scope.utils.alert('info', '消息标题不能为空', '#myModal .modal-dialog', false);
        return;
      }

      if (message.body == '') {
        $scope.utils.alert('info', '消息正文不能为空', '#myModal .modal-dialog', false);
        return;
      }

      if (message.link == '') {
        $scope.utils.alert('info', '链接不能为空', '#myModal .modal-dialog', false);
        return;
      }

      $scope.spinnerShow = true;

      messageService.createMessage(message, function () {

        $scope.spinnerShow = false;

        $('#myModal').modal('hide');

        if ($scope.message.org_id != '')
          initMsgTable($scope.message.org_id);

        $scope.utils.alert('info', '创建消息成功');
      });
    };

    $scope.showModal = function () {
      $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
      });
    };

    $scope.hideModal = function () {
      $('#myModal').modal('hide');
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

      $scope.showDesc = false;

        //set AccessToken http header
      var accessToken = $scope.utils.auth.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
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

  app.controller("ProductKeyManageCtrl", ["$scope", "productKeyManageService", "$timeout", function ($scope, productKeyManageService, $timeout) {

    $scope.cache || ($scope.cache = {});

    var creationPanel = $scope.creationPanel = {
      disableSubmit: true,
      model: {
        productBaseId: 0,
        quantity: 0
      },
      hideModal: function () {
        $scope.spinnerShow = false;
        $('#myModal').modal('hide');
      },
      create: function () {
        var model = this.model;
        console.log('[before productKeyBatch create]', model);

        model.quantity = $("#rangeResult").html().replace(/\,/g, '') - 0;

        var requestData = {
          quantity: model.quantity,
          product_base_id: model.productBaseId
        };
        var selectedProductBase = this.selectedProductBase;

        $scope.spinnerShow = true;

        productKeyManageService.createProductKeyBatch(requestData, function (data) {
          console.log('[newProductKeyBatch created]', data);

          data.product_base = selectedProductBase;
          selectedProductBase.credit.remain -= requestData.quantity;
          $scope.listPanel.newProductKeyBatches.push(data);

          $scope.spinnerShow = false;
          $('#myModal').modal('hide');

          $scope.utils.alert('success', '产品码创建成功');
        }, function (error, data) {
          console.log(error, data);
          var message = (error.message || '').substring(0, 100);
          $scope.spinnerShow = false;
          $scope.utils.alert('danger', message, '#myModal .modal-dialog', false);
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

        if (selectedProductBase == null) {
          creationPanel.disableSubmit = true;
        }
        else {
          creationPanel.disableSubmit = false;
        }
        this.selectedProductBase = selectedProductBase;

        $('#chartProKeyRemain').empty();
        $('#chartProKeyRemain').data('text', selectedProductBase.credit.remain);
        $('#chartProKeyRemain').data('info', '总额：' + selectedProductBase.credit.total);
        if ((selectedProductBase.credit.remain / selectedProductBase.credit.total) < 0.01) {
          $('#chartProKeyRemain').data('percent', 1);
        }
        else {
          $('#chartProKeyRemain').data('percent', selectedProductBase.credit.percentage);
        }
        $('#chartProKeyRemain').circliful();

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
          var accessToken = $scope.utils.auth.getAccessToken();
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

    $scope.showModal = function () {
      $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
      });

      function quantityFormat(str, step, splitor) {
        str = str.toString();
        var len = str.length;

        if (len > step) {
          var l1 = len % step,
              l2 = parseInt(len / step),
              arr = [],
              first = str.substr(0, l1);
          if (first != '') {
            arr.push(first);
          }

          for (var i = 0; i < l2; i++) {
            arr.push(str.substr(l1 + i * step, step));
          }

          str = arr.join(splitor);
        }

        return str;
      }

      var rangeKey = {
        "个": "1", "十": "10", "百": "100",
        "千": "1000", "万": "10000", "十万": "100000"
      };

      $("#rangeNum").ionRangeSlider({
            type: "single",
            min: 1,
            max: 100,
            grid: true,
            onChange: function (data) {
              var selectNum = (data.from - 0) * (rangeKey[$("#rangeKey").data("from")] - 0);
              var remainNum = $scope.creationPanel.selectedProductBase.credit.remain;

              if (selectNum > remainNum) {
                $("#btnSubmit").attr("disabled", "disabled");
                $("#rangeResult").css('color', 'red');
                $("#rangeResult").html(quantityFormat(selectNum, 3, ',') + '  余额不足');
              }
              else {
                $("#btnSubmit").removeAttr("disabled");
                $("#rangeResult").css('color', '#515151');
                $("#rangeResult").html(quantityFormat(selectNum, 3, ','));
              }
            }
          }
      );

      $("#rangeKey").ionRangeSlider({
        grid: true,
        values: [
          "个", "十", "百",
          "千", "万", "十万"
        ],
        onChange: function (data) {
          var selectNum = ($("#rangeNum").data("from") - 0) * (rangeKey[$("#rangeKey").data("from")] - 0);
          var remainNum = $scope.creationPanel.selectedProductBase.credit.remain;

          if (selectNum > remainNum) {
            $("#btnSubmit").attr("disabled", "disabled");
            $("#rangeResult").css('color', 'red');
            $("#rangeResult").html(quantityFormat(selectNum, 3, ',') + '  余额不足');
          }
          else {
            $("#btnSubmit").removeAttr("disabled");
            $("#rangeResult").css('color', '#515151');
            $("#rangeResult").html(quantityFormat(selectNum, 3, ','));
          }
        }
      });
    }
  }
  ]);
//end of controller

})
();

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