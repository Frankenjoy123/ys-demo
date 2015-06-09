(function () {
    var app = angular.module('root');

    app.factory("deviceService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);

                return this;
            },
            getCurrentOrgAccounts: function (fnSuccess) {
                var url = '/api/account';
                $http.get(url).success(fnSuccess);

                return this;
            }
        };
    }]);

    app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, deviceService, $timeout) {

        $scope.curOrgAccounts = '';
        $scope.deviceComment = '';
        $scope.deviceName = '';
        $scope.selectAccount = '';

        (function newDataTable() {
            if ($scope.context.account) {
                $scope.deviceTable = new $scope.utils.DataTable({
                    sortable: {
                        target: '#sort-bar',
                        sort: 'createdDateTime,desc'
                    },
                    pageable: {
                        page: 0,
                        size: 20
                    },
                    flush: function (callback) {
                        deviceService.getDevices(this, $scope.context.account.org_id, function (data, status, headers) {
                            callback({data: data, headers: headers});
                        });
                    }
                });
            } else {
                $timeout(newDataTable, 1000);
            }
        })();

        deviceService.getCurrentOrgAccounts(function (data) {
            $scope.curOrgAccounts = data;
        });

        $scope.deviceAuth = function () {

            if ($scope.selectAccount == '') {
                $('#divSelectAccount').addClass('has-error').addClass('has-feedback');
                return;
            }

            if ($scope.deviceName == '') {
                $('#divDeviceName').addClass('has-error').addClass('has-feedback');
                return;
            }

            $('#divDeviceName').addClass('has-success').addClass('has-feedback');
            $('#divSelectAccount').addClass('has-success').addClass('has-feedback');

            $("#authQRCode").html('');

            var postObject = {};
            postObject.selectAccount = $scope.selectAccount;
            postObject.deviceName = $scope.deviceName;
            postObject.deviceComment = $scope.deviceComment;

            $scope.qrcode = $("#authQRCode").qrcode({
                render: "table", //table方式
                width: 300, //宽度
                height: 300, //高度
                foreground: "#337ab7",//前景颜色
                text: "token" //任意内容
            });
        };

        $scope.cancelDeviceAuth = function () {


        };

    }]);
})();