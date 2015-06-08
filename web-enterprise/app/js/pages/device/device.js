(function () {
    var app = angular.module('root');

    app.factory("deviceService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';

                url += dataTable.toString();
                $http.get(url).success(fnSuccess);
            }
        };
    }]);

    app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, deviceService, $timeout) {

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

        $scope.deviceAuth = function (){

            $("#authQRCode").html('');

            $scope.qrcode = $("#authQRCode").qrcode({
                render: "table", //table方式
                width: 300, //宽度
                height:300, //高度
                foreground: "#337ab7",//前景颜色
                text: "token" //任意内容
            });
        }

    }]);
})();