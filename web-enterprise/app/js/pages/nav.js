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
                        ;
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
