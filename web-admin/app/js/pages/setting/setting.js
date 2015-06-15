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

    app.controller("SettingCtrl", ["$scope", "settingService", '$timeout', function ($scope, settingService, $timeout) {

        $scope.originalPassword = "";
        $scope.currentPassword = "";
        $scope.confirmPassword = "";

        $timeout(function(){$('#updatePassword').bootstrapValidator({
            message: '输入密码不合法',
            feedbackIcons: {
                valid: 'fa fa-check-circle fa-lg text-success',
                invalid: 'fa fa-times-circle fa-lg',
                validating: 'fa fa-refresh'
            },
            fields: {
                oldPassword: {
                    validators: {
                        notEmpty: {
                            message: '请输入当前密码'
                        }
                    }
                },
                curPassword: {
                    validators: {
                        notEmpty: {
                            message: '请输入修改密码'
                        }
                    }
                },
                confirmPassword: {
                    validators: {
                        notEmpty: {
                            message: '请输入确认密码'
                        },
                        identical: {
                            field: 'curPassword',
                            message: '确认密码与修改密码不一致'
                        }
                    }
                }
            }
        }).on('success.field.bv', function(e, data) {
            // $(e.target)  --> The field element
            // data.bv      --> The BootstrapValidator instance
            // data.field   --> The field name
            // data.element --> The field element

            var $parent = data.element.parents('.form-group');

            // Remove the has-success class
            $parent.removeClass('has-success');
        });}, 0);

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
                $scope.utils.alert('warning', '确认密码与修改入密码不一致！');
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