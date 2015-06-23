(function () {
  var app = angular.module('root');

  app.factory('groupService', ['$http', function ($http) {
    return {
      getGroups: function (dataTable, fnSuccess, fnError) {
        $http.get('/api/account?' + dataTable.toString()).success(fnSuccess);

        return this;
      }
    };
  }]);

  app.controller('groupCtrl', [
    '$scope',
    '$timeout',
    'groupService',
    'dataFilterService',
    function ($scope, $timeout, groupService, dataFilterService) {

      $scope.groupPermission = {
        dashBoardRead: '',
        productKeyRead: '',
        productKeyMng: '',
        packageRead: '',
        packageMng: '',
        logisticsRead: '',
        logisticsMng: '',
        tieMaRead: '',
        saoMaRead: '',
        monMaRead: '',
        placeMaRead: '',
        productRead: '',
        productMng: '',
        msgRead: '',
        deviceRead: '',
        deviceMng: '',
        accountRead: '',
        accountMng: '',
        passwordRead: ''
      };

      $scope.groupTable = new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          groupService.getGroups(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      });

    }]);
})();