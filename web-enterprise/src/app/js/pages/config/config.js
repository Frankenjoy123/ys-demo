(function () {
    var app = angular.module("config", ["interceptor"]);

    app.factory("configService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }
        };
    }]);

    app.controller("configCtrl", ["$scope", "configService", function ($scope, configService) {

        $scope.originalPassword = "";
        $scope.currentPassword = "";
        $scope.confirmPassword = "";

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };

        $scope.submit = function () {
            if ($scope.originalPassword == "") {
                $scope.addAlertMsg("请输入原密码！", "info", true);
                return;
            }
            if ($scope.currentPassword == "") {
                $scope.addAlertMsg("请输入修改密码！", "info", true);
                return;
            }
            if ($scope.currentPassword.length < 6) {
                $scope.addAlertMsg("新密码不能少于六位！", "info", true);
                return;
            }
            if ($scope.currentPassword != $scope.confirmPassword) {
                $scope.addAlertMsg("确认密码与输入密码不一致！", "info", true);
                return;
            }


        };
    }]);
})();