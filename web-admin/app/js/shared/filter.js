(function () {
  /*
   * condition:
   * orderBy
   * query
   */
  function query() {

  }

  function paging(data, index, size) {
    index = index || 0;
    size = size || 10;
    return data.slice(size * index, size * (index + 1));
  }

  var by = function (name, isAsc) {
    var min = -1, max = 1;
    isAsc = !!isAsc;
    if (!isAsc) {
      min = 1;
      max = -1;
    }
    return function (o, p) {
      var a, b;
      if (typeof o === "object" && typeof p === "object" && o && p) {
        a = o[name];
        b = p[name];
        if (a === b) {
          return 0;
        }
        if (typeof a === typeof b) {
          return a < b ? min : max;
        }
        return typeof a < typeof b ? min : max;
      }
      else {
        throw ("error");
      }
    }
  };

  function sort(items, field, isAsc) {
    return items.sort(by(field, isAsc));
  }

  angular.module("dataFilterService", [])
    .factory("dataFilterService", function () {
      return {
        filter: function (data, conditions) {
          if (conditions.orderBy) {
            data = sort(data, conditions.orderBy, conditions.orderAsc);
          }
          if (conditions.paging) {
            data = paging(data, conditions.paging.index, conditions.paging.size);
          }
          return data;
        }
      };
    });
})();