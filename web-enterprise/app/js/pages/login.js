(function () {
    var app = angular.module('login', ['interceptor', 'YUNSOO_CONFIG']);

    app.factory('loginService', ['$http', function ($http) {
        return {
            login: function (loginForm, onSuccess, onError) {
                $http.post('/api/auth/login', {
                    organization: loginForm.organization,
                    identifier: loginForm.identifier,
                    password: loginForm.password
                }).success(onSuccess).error(onError);
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

    app.controller('loginController', ['$scope', '$timeout', 'loginService', 'YUNSOO_CONFIG',
        function ($scope, $timeout, loginService, YUNSOO_CONFIG) {
            ($scope.loginForm = loginService.loginForm()) || ($scope.loginForm = {
                organization: '',
                identifier: '',
                password: '',
                rememberMe: false
            });

            $scope.alertMsgs = [];

            function login() {
                var loginForm = $scope.loginForm;
                console.log('[before login]', 'organization:', loginForm.organization, ', identifier:', loginForm.identifier);

                loginService.login(loginForm, function (data) {
                    if (!data || !data.access_token || !data.access_token.token) {
                        $.niftyNoty({
                            type: 'danger',
                            container: '#panel-login',
                            html: '登陆失败请稍后再试',
                            focus: false,
                            timer: 3000
                        });
                        return;
                    }
                    $.removeCookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN, {path: '/'});
                    $.cookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN, data.access_token.token, {
                        expires: data.access_token.expires_in / (60 * 60 * 24),
                        path: '/'
                    });

                    if (loginForm.rememberMe) {
                        //save current login form
                        loginForm.password = '';
                        console.log('[saving login form]');
                        loginService.loginForm(loginForm);
                    }
                    //redirect to index
                    window.location.href = 'index.html';

                }, function (data, code) {
                    console.log('[login failed]', data.message, code);
                    $scope.loginForm.password = '';
                    $.niftyNoty({
                        type: 'danger',
                        container: '#panel-login',
                        html: '账号或密码错误',
                        focus: false,
                        timer: 3000
                    });
                });
            }


            //init  validator
            $timeout(function () {
                $('form.bv-form').bootstrapValidator({
                    message: '非法输入',
                    feedbackIcons: {
                        valid: 'fa fa-check-circle fa-lg text-success',
                        invalid: 'fa fa-times-circle fa-lg',
                        validating: 'fa fa-refresh'
                    },
                    fields: {
                        organization: {
                            validators: {
                                notEmpty: {
                                    message: '组织名称不可空'
                                }
                            }
                        },
                        identifier: {
                            validators: {
                                notEmpty: {
                                    message: '用户名不可空'
                                }
                            }
                        },
                        password: {
                            validators: {
                                notEmpty: {
                                    message: '密码不可空'
                                }
                            }
                        }
                    }
                }).on('success.form.bv', function (e, data) {
                    e.preventDefault();
                    //login on validation success
                    login();
                });

            }, 0); //end of $timeout

        }]);

})();