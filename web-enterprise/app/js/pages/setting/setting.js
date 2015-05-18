(function () {
    var app = angular.module('root');

    app.factory("settingService", ["$http", function ($http) {
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

    app.controller("settingCtrl", ["$scope", "settingService", function ($scope, settingService) {

        $scope.originalPassword = "";
        $scope.currentPassword = "";
        $scope.confirmPassword = "";

        $scope.submit = function () {
            if ($scope.originalPassword == "") {
                $scope.utils.alert('warning', '请输入原密码！');
                return;
            }
            if ($scope.currentPassword == "") {
                $scope.utils.alert('warning', '请输入修改密码！');
                return;
            }
            if ($scope.currentPassword.length < 6) {
                $scope.utils.alert('warning', '新密码不能少于六位！');
                return;
            }
            if ($scope.currentPassword != $scope.confirmPassword) {
                $scope.utils.alert('warning', '确认密码与输入密码不一致！');
                return;
            }
        };
    }]);
})();