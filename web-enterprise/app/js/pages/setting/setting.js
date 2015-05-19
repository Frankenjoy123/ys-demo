(function () {
    var app = angular.module('root');

    app.factory("settingService", ["$http", function ($http) {
        return {
            updatePassword: function (passwordObj, fnSuccess, fnFail) {
                $http.post("/api/account/current/password", passwordObj).success(fnSuccess).error(fnFail);
                return this;
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
            if ($scope.currentPassword != $scope.confirmPassword) {
                $scope.utils.alert('warning', '确认密码与输入密码不一致！');
                return;
            }

            var passwordObj = {
                old_password: $scope.originalPassword,
                new_password: $scope.currentPassword
            };

            settingService.updatePassword(passwordObj, function (data) {
                $scope.utils.alert('success', '修改密码成功！');
            }, function (data) {
                $scope.utils.alert('warning', '修改密码失败！');
            });
        };
    }]);
})();