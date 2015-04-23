(function () {
    var app = angular.module("accountManage", ["interceptor", "dataFilterService"]);

    app.factory("accountManageService", ["$http", function ($http) {
        return {
            getAccounts: function (org_id, fnSuccess, fnError) {
                $http.get("/api/account?org_id=" + org_id)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }
        };
    }]);

    app.controller("accountManageCtrl", [
        "$scope",
        "accountManageService",
        "dataFilterService",
        function ($scope, accountManageService, dataFilterService) {

            $scope.getDateString = function (value) {
                var date = new Date(value);
                return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
            };

            $scope.data = {
                accounts: []
            };

            $scope.paging = {
                size: 10,
                index: 0
            };

            $scope.orderBy = "created_datetime";
            $scope.orderAsc = true;

            $scope.setFilteredData = function (conditions) {
                conditions = $.extend({
                    orderBy: $scope.orderBy,
                    orderAsc: $scope.orderAsc,
                    paging: $scope.paging
                }, conditions);
                $scope.data.filteredAccounts = dataFilterService.filter($scope.data.accounts, conditions);
            };

            $scope.sort = function (field) {
                if ($scope.orderBy === field) {
                    $scope.orderAsc = !$scope.orderAsc;
                } else {
                    $scope.orderBy = field;
                    $scope.orderAsc = true;
                }
                $scope.setFilteredData();
            };

            $scope.setPaging = function (size) {
                $scope.paging.size = size;
                $scope.setFilteredData();
            };

            $scope.gotoFirstPage = function () {
                if ($scope.paging.index < 1) {
                    return;
                }
                $scope.paging.index = 0;
                $scope.setFilteredData();
            };

            $scope.gotoLastPage = function () {
                if ($scope.data.accounts.length < ($scope.paging.index + 1) * $scope.paging.size) {
                    return;
                }
                $scope.paging.index = Math.floor($scope.data.accounts.length / $scope.paging.size);
                $scope.setFilteredData();
            };

            $scope.pages = function () {
                var p = [];
                for (var i = 1; i <= Math.floor($scope.data.accounts.length / $scope.paging.size + 1); i++) {
                    p.push(i);
                }
                return p;
            };

            $scope.currentPageStart = function () {
                return $scope.paging.index * $scope.paging.size + 1;
            };

            $scope.currentPageEnd = function () {
                return Math.min(($scope.paging.index + 1) * $scope.paging.size, $scope.data.accounts.length);
            };

            $scope.gotoPage = function (index) {
                $scope.paging.index = index;
                $scope.setFilteredData();
            };

            $scope.loadAccounts = function () {
                accountManageService.getAccounts("2k0r1l55i2rs5544wz5", function (data) {
                    $scope.data.accounts = data;
                    $scope.setFilteredData({
                        orderBy: $scope.orderBy,
                        paging: $scope.paging
                    });
                });
            };

            $scope.loadAccounts();
        }]);
})();