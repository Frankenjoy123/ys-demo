(function () {
    var app = angular.module('root');

    app.factory("deviceService", ["$http", function ($http) {
        return {
            createProWithDetail: function (proDetail, fnSuccess, fnError) {
                $http.post("/api/productbase/withdetail", proDetail).success(fnSuccess).error(fnError);

                return this;
            }
        };
    }]);

    app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, emulatorService, $timeout) {

        $scope.preview = function () {

        };

        $timeout(function () {

            $("#code").qrcode({
                render: "table",
                width: 200, //宽度
                height: 200, //高度
                text: "www.163.com" //任意内容
            });

            $("#code").qrcode({
                render: "table",
                width: 200, //宽度
                height: 200, //高度
                text: "www.qq.com" //任意内容
            });

        }, 0);
    }]);
})();