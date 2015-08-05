(function () {
  var app = angular.module('root');

  app.factory('accountManageService', ['$http', function ($http) {
    return {
      getAccounts: function (orgId, fnSuccess, fnError) {
        if (orgId == '') {
          $http.get('/api/account').success(fnSuccess);
        }
        else {
          $http.get('/api/account?org_id=' + orgId).success(fnSuccess);
        }
      },
      getOrgList: function (fnSuccess, fnError) {
        $http.get('/api/organization').success(fnSuccess).error(fnError);
        return this;
      }
    };
  }]);

  app.controller('AccountManageCtrl', [
    '$scope',
    '$timeout',
    'accountManageService',
    'dataFilterService',
    function ($scope, $timeout, accountManageService, dataFilterService) {

      $scope.currOrg = '';

      var table = {
        //sortable: {
        //    target: '#sort-bar',
        //},
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          accountManageService.getAccounts($scope.currOrg, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      };

      $scope.accountTable = new $scope.utils.DataTable(table);

      accountManageService.getOrgList(function (data) {
        $scope.orgList = data;
      }, function(){});

      $scope.orgChange = function () {
        $scope.accountTable = new $scope.utils.DataTable(table);
      }

    }]);
})();