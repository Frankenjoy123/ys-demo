(function () {
    var app = angular.module("login", ["interceptor"]);

    app.factory("loginService", ["$http", function ($http) {
        return {
            login: function (username, password, fnSuccess, fnError) {
                $http.post("/account/login", {
                    username: username,
                    password: password
                }).success(function (data) {
                    fnSuccess(data);
                }).error(function (data) {
                    fnError();
                });
            }
        };
    }]);

    app.controller("loginController", ["$scope", "$timeout", "loginService",
        function ($scope, $timeout, loginService) {
            $scope.username = "";
            $scope.password = "";
            $scope.alertMsgs = [];
            $scope.login = function () {
                if (!$scope.username) {
                    $scope.addAlertMsg("用户名不能为空", "danger");
                    return;
                }
                if (!$scope.password) {
                    $scope.addAlertMsg("密码不能为空", "danger");
                    return;
                }
                loginService.login($scope.username, $scope.password, function(data){
                    if (!data || !data.accessToken) {
                        $scope.addAlertMsg("登陆失败，请再次尝试", "danger");
                        return;
                    }
                    $.cookie('AdminLTEToken', data.accessToken, {expires: 7, path: '/'});
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