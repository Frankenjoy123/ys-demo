(function () {
    var app = angular.module("login", ["interceptor"]);

    app.factory("loginService", ["$http", function ($http) {
        return {
            login: function (orgId, identifier, password, fnSuccess, fnError) {
                $http.post("/api/auth/login", {
                    org_id: orgId,
                    identifier: identifier,
                    password: password
                }).success(function (data) {
                    fnSuccess(data);
                }).error(function (data) {
                    fnError(data);
                });
            },
            loginForm: function (loginForm) {
                if (loginForm) {
                    localStorage.loginForm = JSON.stringify(loginForm);
                } else {
                    return localStorage.loginForm ? JSON.parse(localStorage.loginForm) : null;
                }
            }
        };
    }]);

    app.controller("loginController", ["$scope", "$timeout", "loginService",
        function ($scope, $timeout, loginService) {
            $scope.loginForm = loginService.loginForm();
            $scope.loginForm || ($scope.loginForm = {
                orgId: "",
                identifier: "",
                password: "",
                rememberMe: false
            });

            $scope.alertMsgs = [];
            $scope.login = function () {
                var loginForm = $scope.loginForm;
                if (!loginForm.orgId) {
                    $scope.addAlertMsg("组织ID不能为空", "danger");
                    return;
                }
                if (!loginForm.identifier) {
                    $scope.addAlertMsg("用户名不能为空", "danger");
                    return;
                }
                if (!loginForm.password) {
                    $scope.addAlertMsg("密码不能为空", "danger");
                    return;
                }
                loginService.login($scope.org_id, $scope.username, $scope.password, function(data){
                    if (!data || !data.access_token || !data.access_token.token) {
                        $scope.addAlertMsg("登陆失败，请再次尝试", "danger");
                        return;
                    }
                    $.cookie(YUNSOO_CONFIG.AUTH_COOKIE_NAME, data.access_token.token, {expires: data.access_token.expires_in / (60 * 60 * 24), path: '/'});
                    window.location.href = "index.html";
                },function(){
                    $scope.addAlertMsg("登陆失败，请再次尝试", "danger");
                });
            };

            function getMsgIndex(msgs, msg, level) {
                var index = -1;
                for (var i = 0; i < msgs.length; i++) {
                    var item = msgs[i];
                    if (item.level == level && item.message == msg) {
                        index = i;
                        break;
                    }
                }
                return index;
            }

            $scope.addAlertMsg = function (msg, level, autoHide) {
                $scope.alertMsgs.push({
                    level: level,
                    message: msg
                });
                if (autoHide != false) {
                    var index = getMsgIndex($scope.alertMsgs, msg, level);
                    $timeout((function (i) {
                        return function (i) {
                            $scope.alertMsgs.splice(i, 1);
                        };
                    })(index), 3 * 1000);
                }
            }
        }]);
})();