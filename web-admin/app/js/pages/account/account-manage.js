(function () {
    var app = angular.module('root');

    app.factory('accountManageService', ['$http', function ($http) {
        return {
            getAccounts: function (fnSuccess, fnError) {
                $http.get('/api/account').success(fnSuccess);
            }
        };
    }]);

    app.controller('AccountManageCtrl', [
        '$scope',
        '$timeout',
        'accountManageService',
        'dataFilterService',
        function ($scope, $timeout, accountManageService, dataFilterService) {

            $scope.accountTable = new $scope.utils.DataTable({
                //sortable: {
                //    target: '#sort-bar',
                //},
                pageable: {
                    page: 0,
                    size: 20
                },
                flush: function (callback) {
                    accountManageService.getAccounts(function (data, status, headers) {
                        callback({data: data, headers: headers});
                    });
                }
            });

        }]);
})();