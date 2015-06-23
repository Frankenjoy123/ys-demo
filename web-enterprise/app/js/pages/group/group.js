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


    }]);
})();