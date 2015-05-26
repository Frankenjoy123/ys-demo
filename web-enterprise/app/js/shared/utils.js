(function () {
    angular.module('utils', ['YUNSOO_CONFIG'])
        .factory('utils', ['YUNSOO_CONFIG', function (YUNSOO_CONFIG) {
            return {
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
                    this.notification('warning', '锁屏界面开发中...');
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
                },
                arrayToMap: function (array, keyName) {
                    var map = {};
                    if (Array.isArray(array) && keyName) {
                        $.each(array, function (i, item) {
                            if (item.hasOwnProperty(keyName)) {
                                map[item[keyName]] = item;
                            }
                        });
                    }
                    return map;
                },

                //classes
                Pageable: (function () {

                    /**
                     *
                     * @param options
                     * options.page number default is 0
                     * options.size number default is 20
                     * options.sort sort expression split different properties by comma, default is 'asc' while minus prefix stands for 'desc'
                     *              example: name,-age
                     * options.onTurn function
                     * @constructor
                     */
                    function Pageable(options) {
                        options || (options = {});
                        this.page = options.page || 0;
                        this.size = options.size || 20;
                        this.sort = options.sort || '';
                        this.onTurn = options.onTurn || function () {
                            };
                        this.totalPages = 0;
                        this.content = [];
                        //init
                        this.onTurn(callbackOnResponse.bind(this));
                    }

                    function callbackOnResponse(data, headers) {
                        this.content = data;
                        var expression = headers('Content-Range');
                        if (typeof expression === 'string' && expression) {
                            var match = /pages (\d+)\/(\d+)/.exec(expression);
                            this.page = +match[1];
                            this.totalPages = +match[2];
                        }
                    }

                    Pageable.prototype.first = function () {
                        return this.goto(0);
                    };
                    Pageable.prototype.last = function () {
                        return this.goto(this.totalPages - 1);
                    };
                    Pageable.prototype.previous = function () {
                        return this.goto(this.page - 1);
                    };
                    Pageable.prototype.next = function () {
                        return this.goto(this.page + 1);
                    };
                    Pageable.prototype.goto = function (page) {
                        if (page >= 0 && page < this.totalPages && this.page !== page) {
                            this.page = page;
                            console.log("[turn page]", page + '/' + this.totalPages);
                            this.onTurn(callbackOnResponse.bind(this));
                        }
                        return this;
                    };
                    Pageable.prototype.isFirst = function () {
                        return this.page === 0;
                    };
                    Pageable.prototype.isLast = function () {
                        return this.page === this.totalPages - 1;
                    };
                    Pageable.prototype.adjacentPages = function (count) {
                        var pages = [];
                        var half = count / 2 | 0;
                        var right = this.page + count / 2 | 0;
                        var left = right - count + 1;
                        if (left < 0) {
                            right -= left;
                            left = 0;
                        }
                        if (right > this.totalPages - 1) {
                            left -= right - (this.totalPages - 1);
                            right = this.totalPages - 1;
                            if (left < 0) {
                                left = 0;
                            }
                        }
                        for (var i = left; i <= right; i++) {
                            pages.push(i);
                        }
                        return pages;
                    };
                    Pageable.prototype.toQueryString = function () {
                        var query = 'page=' + this.page + '&size=' + this.size;
                        if (this.sort) {
                            query += ('&sort=' + this.sort);
                        }
                        return query;
                    };

                    return Pageable;
                })() //Pageable end

            };
        }]);
})();