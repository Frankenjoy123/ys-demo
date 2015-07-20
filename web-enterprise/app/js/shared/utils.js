(function () {
  'use strict';

  var utils = angular.module('utils', ['YUNSOO_CONFIG']);

  utils.factory('utils', ['YUNSOO_CONFIG', function (YUNSOO_CONFIG) {
    var u = {
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
      alert: function (type, message, container, focus) {
        $.niftyNoty && $.niftyNoty({
          type: type,
          container: container || 'page',
          focus: (focus === undefined || focus === true) ? true : false,
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

      auth: new Auth(),

      DateHelp: DateHelp,

      DataTable: DataTable

    };

    window._debug && (window._debug.utils = u); //debug only
    return u;
  }]);

  utils.directive('dtPageable', function () {
    return {
      restrict: 'A',
      scope: {
        pageable: '=dtPageable'
      },
      templateUrl: 'partials/widgets/pageable.html'
    };
  });

  utils.directive('echartDate', function () {
    return {
      restrict: 'A',
      scope: {
        dataTable: '=echartDate'
      },
      templateUrl: 'partials/widgets/echart-date.html'
    };
  });

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

      //init
      this.flush(this.refresh.bind(this));
    }

    function bindFlush(dataTable, target) {
      var targetFlush = target.flush;
      target.flush = function () {
        typeof targetFlush === 'function' && targetFlush.apply(target, arguments);
        dataTable.flush.call(dataTable, dataTable.refresh.bind(dataTable));
      };
    }

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
       * options.sort   string,   sort expression of properties that should be sorted by in the format property,property(,ASC|DESC).
       *                          Default sort direction is ascending.
       *                array,    Use array of sort expressions if you want to switch directions.
       *                          e.g. ['first_name', 'age,desc'].
       * options.target selector, sort header container
       * options.flush  function, It will be invoked when sort expression is changed.
       *                          e.g. function(callback) {
             *                                   ...
             *                                   callback({data: data, headers: headers}); //with data and headers from server response;
             *                                   ...
             *                               }
       * @constructor
       */
      function Sortable(options) {
        options || (options = {});
        this.sort = options.sort;
        this.target = options.target;
        this.flush = typeof options.flush === 'function' ? options.flush : function () {
        };
      }

      Sortable.prototype.refresh = function (response) {
        if (response) {
          this.data = response.data || [];
        }
        //refresh sort header
        refreshSortHeader.apply(this);

        return this;
      };

      //depend on jQuery
      function refreshSortHeader() {
        if (this.target) {
          var $target = $(this.target);
          var $sorts = $target.find('[data-sort-field]');
          if ($sorts.length > 0) {
            //reset classes
            $sorts.each(function (i, item) {
              $(item).addClass('sortable').removeClass('sortable-asc').removeClass('sortable-desc');
            });
            //add sort direction class
            $.each(parseSort(this.sort), function (i, item) {
              $target.find('[data-sort-field="' + item.name + '"]').addClass('sortable-' + item.direction);
            });
            //bind event
            if (!$target.data('hasTrigger')) {
              $target.on('click', (function (sortable) {
                return trigger.bind(sortable);
              })(this));
              $target.data('hasTrigger', true);
            }
          }
        }
      }

      //depend on jQuery
      function trigger($event) {
        var $src = $($event.target);
        var field = $src.attr('data-sort-field');
        if (field) {
          var currentDirection = $src.hasClass('sortable-asc') ? 'asc' : $src.hasClass('sortable-desc') ? 'desc' : null;
          $src.removeClass('sortable-asc').removeClass('sortable-desc');
          var direction = currentDirection !== 'asc' ? 'asc' : 'desc';
          this.sortBy(field, direction);
          $src.addClass('sortable-' + direction);
        }
      }

      function parseSort(sort) {
        var result = [];
        if (Array.isArray(sort)) {
          for (var i = 0; i < sort.length; i++) {
            result = result.concat(parseSort(sort[i]));
          }
        } else if (typeof sort === 'string') {
          var sorts = sort.split(',');
          if (sorts.length > 0) {
            var last = sorts[sorts.length - 1];
            if (last !== 'asc' && last !== 'desc') {
              sorts.push('asc');
            }
            for (var j = 0; j < sorts.length - 1; j++) {
              result.push({name: sorts[j], direction: sorts[sorts.length - 1]});
            }
          }
        }
        return result;
      }

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
          var oldSortExpression = this.toString();
          this.sort = sort.length === 1 ? sort[0] : sort;
          if (oldSortExpression !== this.toString()) {
            this.flush(this.refresh.bind(this));
          }
        }
        return this;
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
        this.data = [];

        //init
        this.flush(this.refresh.bind(this));
      }

      Pageable.prototype.refresh = function (response) {
        if (response) {
          this.data = response.data || [];
          this.totalPages = '*';
          var expression = null;
          if (response.headers) {
            expression = typeof response.headers === 'function'
                ? response.headers('Content-Range')
                : response.headers['Content-Range'];
          }
          var match = /^pages (\d+)\/(\d+|\*)$/.exec(expression);
          if (match) {
            this.page = +match[1];
            this.totalPages = match[2] === '*' ? '*' : +match[2];
          }
          if (this.totalPages === '*') {
            //this.totalPages = this.data.length === this.size ? this.page + 2 : this.page + 1;
            this.totalPages = Math.ceil(this.data.length / this.size);
            this.data = this.data.slice((this.page) * this.size, (this.page + 1) * this.size);
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
        count || (count = 5);
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
          if (right < left) {
            right = left;
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

  var Auth = (function () {
    function Auth() {
      this._auth = null;
    }

    Auth.prototype.getAccessToken = function () {
      var auth = this.getAuth();
      return auth ? checkExpires(auth.accessToken) : null;
    };

    Auth.prototype.setAccessToken = function (token, expires) {
      if (token) {
        var auth = this.getAuth() || {};
        auth.accessToken = {token: token, expires: expires};
        saveAuth(auth);
      }
      return this;
    };

    Auth.prototype.getPermanentToken = function () {
      var auth = this.getAuth();
      return auth ? checkExpires(auth.permanentToken) : null;
    };

    Auth.prototype.setPermanentToken = function (token, expires) {
      if (token) {
        var auth = this.getAuth() || {};
        auth.permanentToken = {token: token, expires: expires};
        saveAuth(auth);
      }
      return this;
    };

    Auth.prototype.getAuth = function () {
      return this._auth ? this._auth : (this._auth = getAuth());
    };

    Auth.prototype.clear = function () {
      this._auth = saveAuth(null);
    };

    function checkExpires(tokenObj) {
      if (tokenObj && tokenObj.token) {
        var token = tokenObj.token;
        if (tokenObj.expires && new DateTime(tokenObj.expires) - DateTime.now() < 0) {
          token = null;
        }
        return token;
      }
      return null;
    }

    function getAuth() {
      var auth = localStorage.auth;
      return auth ? JSON.parse(auth) : null;
    }

    function saveAuth(auth) {
      if (auth) {
        localStorage.auth = JSON.stringify(auth);
      } else {
        localStorage.removeItem('auth');
      }
      return auth;
    }

    return Auth;
  })(); //Auth end

  var DateHelp = (function () {

    function DateHelp(getData) {
      this.datetime = DateTime.now();

      this.timeLine = [];

      this.selUnits = this.units[3];

      this.selYear = this.datetime.year.toString();
      this.selMon = this.selYear + this.datetime.month.toString();
      this.selWeek = getCurrWeek(this.datetime);
      this.selDay = this.selYear + this.selMon + this.datetime.day.toString();

      this.selTimes = this.datetime.addDays(-1).toString('yyyyMMdd');

      initTimeline.apply(this);

      this.getData = getData;
    }

    DateHelp.prototype.units = ['年', '月', '周', '日'];

    function getCurrWeek(dateTime) {
      var weekFirstDay = dateTime.addDays(1 - dateTime.dayOfWeek).toString('yyyyMMdd');
      var weekLastDay = dateTime.addDays(7 - dateTime.dayOfWeek).toString('yyyyMMdd');

      return weekFirstDay + '-' + weekLastDay;
    };

    DateHelp.prototype.setTimes = function (data) {
      this.selTimes = data;

      if (this.selUnits == this.units[0]) {
        this.selYear = this.selTimes;
      }
      else if (this.selUnits == this.units[1]) {
        this.selMon = this.selTimes;
      }
      else if (this.selUnits == this.units[2]) {
        this.selWeek = this.selTimes;
      }
      else if (this.selUnits == this.units[3]) {
        this.selDay = this.selTimes;
      }

      this.getData();
    };

    DateHelp.prototype.goLeft = function () {
      var i;

      if (this.selUnits == this.units[0]) {
        var currMinYear = this.timeLine[0];
        this.timeLine = [];
        for (i = currMinYear - 7; i <= currMinYear - 1; i++) {
          this.timeLine.push(i + '');
        }
      }
      else if (this.selUnits == this.units[1]) {
        var currMinMon = this.timeLine[0];
        var year = currMinMon.substring(0, 4);
        this.timeLine = [];
        for (i = 1; i <= 12; i++) {
          if (i < 10)
            this.timeLine.push(year - 1 + '0' + i);
          else
            this.timeLine.push(year - 1 + '' + i);
        }
      }
      else if (this.selUnits == this.units[2]) {
        var currMinWeek = this.timeLine[0];
        var year = currMinWeek.substring(0, 4) - 0;
        var month = currMinWeek.substring(4, 6) - 1;
        var day = currMinWeek.substring(6, 8) - 0;

        this.timeLine = [];
        for (i = 5; i >= 1; i--) {
          this.timeLine.push(getCurrWeek(new DateTime(new Date(year, month, day - i * 7))));
        }
      }
      else if (this.selUnits == this.units[3]) {
        var week = this.timeLine[0];

        var firstWeekYear = week.substring(0, 4) - 0;
        var firstWeekMon = week.substring(4, 6) - 1;
        var firstWeekDay = week.substring(6, 8) - 0;

        var firstWeekDateTime = new DateTime(new Date(firstWeekYear, firstWeekMon, firstWeekDay));

        this.timeLine = [];
        for (i = 6; i >= 0; i--) {
          this.timeLine.push(firstWeekDateTime.addDays(-i - 1).toString("yyyyMMdd"));
        }
      }
    };

    DateHelp.prototype.goRight = function () {
      var i;

      if (this.selUnits == this.units[0]) {
        var currMaxYear = parseInt(this.timeLine[6]);
        this.timeLine = [];
        for (i = currMaxYear + 1; i <= currMaxYear + 7; i++) {
          this.timeLine.push(i + '');
        }
      }
      else if (this.selUnits == this.units[1]) {
        var currMinMon = this.timeLine[0];
        var year = currMinMon.substring(0, 4);
        this.timeLine = [];
        for (i = 1; i <= 12; i++) {
          if (i < 10)
            this.timeLine.push(parseInt(year) + 1 + '0' + i);
          else
            this.timeLine.push(parseInt(year) + 1 + '' + i);
        }
      }
      else if (this.selUnits == this.units[2]) {
        var currMinWeek = this.timeLine[4];
        var year = currMinWeek.substring(0, 4) - 0;
        var month = currMinWeek.substring(4, 6) - 1;
        var day = currMinWeek.substring(6, 8) - 0;

        this.timeLine = [];
        for (i = 1; i <= 5; i++) {
          this.timeLine.push(getCurrWeek(new DateTime(new Date(year, month, day + i * 7))));
        }
      }
      else if (this.selUnits == this.units[3]) {
        var week = this.timeLine[6];

        var lastWeekYear = week.substring(0, 4) - 0;
        var lastWeekMon = week.substring(4, 6) - 1;
        var lastWeekDay = week.substring(6, 8) - 0;

        var lastWeekDateTime = new DateTime(new Date(lastWeekYear, lastWeekMon, lastWeekDay));

        this.timeLine = [];
        for (i = 1; i <= 7; i++) {
          this.timeLine.push(lastWeekDateTime.addDays(i).toString("yyyyMMdd"));
        }
      }
    };

    DateHelp.prototype.setUnits = function (data) {
      this.selUnits = data;

      initTimeline.apply(this);
    };

    function initTimeline() {
      this.timeLine = [];
      var i;

      if (this.selUnits == this.units[0]) {
        for (i = this.datetime.year - 3; i <= this.datetime.year + 3; i++) {
          this.timeLine.push('' + i);
        }
      }
      else if (this.selUnits == this.units[1]) {
        for (i = 1; i <= 12; i++) {
          if (i < 10)
            this.timeLine.push(this.selYear + '0' + i);
          else
            this.timeLine.push(this.selYear + i);
        }
      }
      else if (this.selUnits == this.units[2]) {
        var year = this.selYear - 0;
        var month = this.selMon.substring(4, 6) - 1;

        for (i = 0; i < 5; i++) {
          this.timeLine.push(getCurrWeek(new DateTime(new Date(year, month, i * 7))));
        }
      }
      else if (this.selUnits == this.units[3]) {
        var week = this.selWeek;

        var firstWeekYear = week.substring(0, 4) - 0;
        var firstWeekMon = week.substring(4, 6) - 1;
        var firstWeekDay = week.substring(6, 8) - 0;

        var firstWeekDateTime = new DateTime(new Date(firstWeekYear, firstWeekMon, firstWeekDay));

        for (i = 0; i <= 6; i++) {
          this.timeLine.push(firstWeekDateTime.addDays(i).toString("yyyyMMdd"));
        }
      }
    };

    return DateHelp;

  })();

})();