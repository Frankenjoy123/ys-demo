(function () {
    'use strict';

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

                DataTable: DataTable

            };
        }]);

    //classes
    var DataTable = (function () {
        /**
         *
         * @param options
         * options.sortable
         * options.pageable
         * options.filterable
         * options.flush      function, It will be invoked when any condition is changed.
         *                    e.g. function(callback) {
         *                             ...
         *                             callback(data, headers); //with data(body) and headers from server response;
         *                             ...
         *                         }
         * @constructor
         */
        function DataTable(options) {
            options || (options = {});
            this.data = options.data || [];
            this.fields = options.fields || [];
            this.flush = typeof options.flush === 'function' ? options.flush : function () {
            };
            if (options.filterable) {
                this.filterable = options.filterable instanceof Filterable ? options.filterable : new Filterable(options.filterable);
                bindFlush(this, this.filterable);
            }
            if (options.sortable) {
                this.sortable = options.sortable instanceof Sortable ? options.sortable : new Sortable(options.sortable);
                bindFlush(this, this.sortable);
            }
            if (options.pageable) {
                this.pageable = options.pageable instanceof Pageable ? options.pageable : new Pageable(options.pageable);
                bindFlush(this, this.pageable);
            }
        }

        function bindFlush(dataTable, target) {
            var targetFlush = target.flush;
            target.flush = function () {
                typeof targetFlush === 'function' && targetFlush.apply(target, arguments);
                dataTable.flush.call(dataTable, dataTable.refresh.bind(dataTable));
            };
        }

        DataTable.prototype.init = function () {
            this.flush(this.refresh.bind(this));
            return this;
        };

        DataTable.prototype.refresh = function (response) {
            if (response) {
                this.filterable && this.filterable.refresh(response);
                this.sortable && this.sortable.refresh(response);
                this.pageable && this.pageable.refresh(response);
                this.data = response.data || [];
            }
            return this;
        };

        DataTable.prototype.toString = function () {
            var params = [];
            if (this.filterable) {
                params.push(this.filterable.toString());
            }
            if (this.sortable) {
                //sortable should always be ahead of pageable
                //in order to have higher priority than static sort expressions in pageable
                params.push(this.sortable.toString());
            }
            if (this.pageable) {
                params.push(this.pageable.toString());
            }
            return params.join('&');
        };


        var Filterable = (function () {
            /**
             *
             * @param options
             * @constructor
             */
            function Filterable(options) {

            }

            Filterable.prototype.refresh = function (response) {
                if (response) {
                    this.data = response.data || [];
                }
                return this;
            };

            Filterable.prototype.toString = function () {
                return 'filterable=todo';
            };

            return Filterable;
        })(); //Filterable end

        var Sortable = (function () {
            /**
             *
             * @param options
             * options.sort string, sort expression of properties that should be sorted by in the format property,property(,ASC|DESC).
             *                      Default sort direction is ascending.
             *              array,  Use array of sort expressions if you want to switch directions.
             *                      e.g. ['first_name', 'age,desc'].
             * @constructor
             */
            function Sortable(options) {
                options || (options = {});
                this.sort = options.sort;
                this.flush = typeof options.flush === 'function' ? options.flush : function () {
                };
            }

            Sortable.prototype.refresh = function (response) {
                if (response) {
                    this.data = response.data || [];
                }
                return this;
            };

            Sortable.prototype.sortBy = function () {
                var sort = [];
                for (var i = 0, endWithDirection = true; i < arguments.length; i++) {
                    var arg = arguments[i];
                    if (typeof arg === 'string' && arg.length > 0) {
                        if (endWithDirection) {
                            if (arg !== 'asc' && arg !== 'desc') {
                                sort.push(arg);
                                endWithDirection = false;
                            }
                        } else {
                            sort[sort.length - 1] += ',' + arg;
                            endWithDirection = (arg === 'asc' || arg === 'desc');
                        }
                    }
                }
                if (sort.length > 0) {
                    this.sort = sort.length === 1 ? sort[0] : sort;
                    this.flush(this.refresh.bind(this));
                }
                return this;
            };

            Sortable.prototype.test = function ($event) {
                console.log($($event.srcElement));
                console.log($($event.srcElement).attr('data-sort-name'));
            };

            Sortable.prototype.toString = function () {
                var params = [];
                if (this.sort) {
                    if (Array.isArray(this.sort)) {
                        for (var i = 0; i < this.sort.length; i++) {
                            if (typeof this.sort[i] === 'string') {
                                params.push('sort=' + this.sort[i]);
                            }
                        }
                    } else if (typeof this.sort === 'string') {
                        params.push('sort=' + this.sort);
                    }
                }
                return params.join('&');
            };

            return Sortable;
        })(); //Sortable end

        var Pageable = (function () {
            /**
             *
             * @param options
             * options.page   number,   Page you want to retrieve. default is 0
             * options.size   number,   Size of the page you want to retrieve. default is 20
             * options.sort   string,   sort expression of properties that should be sorted by in the format property,property(,ASC|DESC).
             *                          Default sort direction is ascending.
             *                array,    Use array of sort expressions if you want to switch directions.
             *                          e.g. ['first_name', 'age,desc'].
             * options.flush  function, It will be invoked when page is changed.
             *                          e.g. function(callback) {
             *                                   ...
             *                                   callback({data: data, headers: headers}); //with data and headers from server response;
             *                                   ...
             *                               }
             * @constructor
             */
            function Pageable(options) {
                options || (options = {});
                this.page = options.page || 0;
                this.size = options.size || 20;
                this.sort = options.sort || '';
                this.flush = typeof options.flush === 'function' ? options.flush : function () {
                };
                this.totalPages = 0;
                this.content = [];
            }

            Pageable.prototype.init = function () {
                this.flush(this.refresh.bind(this));
                return this;
            };

            Pageable.prototype.refresh = function (response) {
                if (response) {
                    this.data = response.data || [];
                    var expression;
                    this.totalPages = '*';
                    if (typeof response.headers === 'function' && (expression = response.headers('Content-Range'))) {
                        var match = /^pages (\d+)\/(\d+|\*)$/.exec(expression);
                        if (match) {
                            this.page = +match[1];
                            this.totalPages = +match[2];
                        }
                    }
                    if (this.totalPages === '*') {
                        this.totalPages = this.data.length === this.size ? this.page + 2 : this.page + 1;
                    }
                }
            };

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
                    this.flush(this.refresh.bind(this));
                }
                return this;
            };

            Pageable.prototype.isFirst = function () {
                return this.page === 0;
            };

            Pageable.prototype.isLast = function () {
                return this.page >= this.totalPages - 1;
            };

            Pageable.prototype.adjacentPages = function (count) {
                var pages = [];
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

            Pageable.prototype.toString = function () {
                var params = [];
                params.push('page=' + this.page);
                params.push('size=' + this.size);
                if (this.sort) {
                    if (Array.isArray(this.sort)) {
                        for (var i = 0; i < this.sort.length; i++) {
                            params.push('sort=' + this.sort[i]);
                        }
                    } else if (typeof this.sort === 'string') {
                        params.push('sort=' + this.sort);
                    }
                }
                return params.join('&');
            };

            return Pageable;
        })(); //Pageable end


        DataTable.Filterable = Filterable;
        DataTable.Sortable = Sortable;
        DataTable.Pageable = Pageable;

        return DataTable;
    })(); //DataTable end

})();