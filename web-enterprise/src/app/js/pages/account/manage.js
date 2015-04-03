(function () {
    var app = angular.module("accountManage", ["interceptor", "dataFilterService"]);

    app.factory("accountManageService", ["$http", "$q", function ($http, $q) {
            return {
                getAccounts: function () {
                    var defer = $q.defer();
                    $http.get("mock/account.json")
                            .success(function (data) {
                                defer.resolve(data);
                            }).error(function () {
                        defer.reject();
                    });
                    return defer.promise;
                },
                getRoles: function () {
                    var defer = $q.defer();
                    $http.get("mock/account_org_role.json")
                            .success(function (data) {
                                defer.resolve(data);
                            }).error(function () {
                        defer.reject();
                    });
                    return defer.promise;
                }
            };
        }]);

    app.controller("accountManageCtrl", [
        "$scope",
        "$q",
        "accountManageService",
        "dataFilterService",
        function ($scope, $q, accountManageService, dataFilterService) {
            $scope.data = {
                accounts: [],
                roles: []
            };
            $scope.paging = {
                size: 10,
                index: 0
            };
            $scope.orderBy = "employeeID";
            $scope.orderAsc = true;
            $scope.loadRoles = function () {
                return accountManageService.getRoles().then(function (data) {
                    $scope.data.roles = data;
                });
            };
            $scope.loadAccounts = function () {
                accountManageService.getAccounts().then(function (data) {
                    $scope.data.accounts = data;
                    $scope.setFilteredData({
                        orderBy: $scope.orderBy,
                        paging: $scope.paging
                    });
                });
            };
            $scope.setFilteredData = function (conditions) {
                conditions = $.extend({
                    orderBy: $scope.orderBy,
                    orderAsc: $scope.orderAsc,
                    paging: $scope.paging
                }, conditions);
                $scope.data.filteredAccounts = dataFilterService.filter($scope.data.accounts, conditions);
            };
            $scope.getRole = function (id) {
                var filtered = dataFilterService.filter($scope.data.roles, {
                    query: {
                        id: id
                    }
                });
            };
            $scope.sort = function (field) {
                if ($scope.orderBy === field) {
                    $scope.orderAsc = !$scope.orderAsc;
                } else {
                    $scope.orderBy = field;
                    $scope.orderAsc = true;
                }
                $scope.setFilteredData();
            }
            $scope.setPaging = function (size) {
                $scope.paging.size = size;
                $scope.setFilteredData();
            }
            $scope.gotoFirstPage = function () {
                if ($scope.paging.index < 1) {
                    return;
                }
                $scope.paging.index = 0;
                $scope.setFilteredData();
            }
            $scope.gotoLastPage = function () {
                if ($scope.data.accounts.length < ($scope.paging.index + 1) * $scope.paging.size) {
                    return;
                }
                $scope.paging.index = Math.floor($scope.data.accounts.length / $scope.paging.size);
                $scope.setFilteredData();
            }
            $scope.gotoPage = function (index) {
                $scope.paging.index = index;
                $scope.setFilteredData();
            }
            $scope.editRow = function (account) {
                account.mode = 1;
                account.tmpEmployeeID = account.employeeID;
                account.tmpName = account.name;
                account.tmpStatus = account.status;
                account.tmpRole = account.role;
                account.tmpPhone = account.phone;
                account.tmpEmail = account.email;
                account.tmpSSID = account.SSID;
            }
            $scope.cancelRow = function (account) {
                account.mode = 0;
                account.employeeID = account.tmpEmployeeID;
                account.name = account.tmpName;
                account.status = account.tmpStatus;
                account.role = account.tmpRole;
                account.phone = account.tmpPhone;
                account.email = account.tmpEmail;
                account.SSID = account.tmpSSID;
            }
            $scope.saveRow = function (account) {
                account.mode = 0;
            }

            $scope.loadRoles()
                    .then($scope.loadAccounts);
        }]);
})();