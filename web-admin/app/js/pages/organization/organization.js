;(function () {
  var app = angular.module('root');

  app.factory('orgService', ['$http', function ($http) {
    return {
      getOrgList: function (dataTable, fnSuccess, fnError) {
        $http.get('/api/organization/list?' + dataTable.toString()).success(fnSuccess);

        return this;
      },
      createAccount: function (account, fnSuccess, fnError) {
        $http.post("/api/account", account).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller('orgCtrl', [
    '$scope',
    '$timeout',
    'orgService',
    function ($scope, $timeout, orgService) {

      $scope.orgTable = new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          orgService.getOrgList(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      });
    }]);
})();