(function () {
  var app = angular.module('login', ['interceptor', 'YUNSOO_CONFIG', 'utils']);

  app.factory('loginService', ['$http', function ($http) {
    return {
      login: function (loginForm, onSuccess, onError) {
        $http.post('/api/auth/login/password', {
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

  app.controller('LoginCtrl', ['$scope', '$timeout', 'loginService', 'YUNSOO_CONFIG', 'utils',
    function ($scope, $timeout, loginService, YUNSOO_CONFIG, utils) {

      ($scope.loginForm = loginService.loginForm()) || ($scope.loginForm = {
        organization: '',
        identifier: '',
        password: '',
        rememberMe: false
      });

      var lockForm = $scope.lockForm = {
        organization: (JSON.parse(localStorage.loginForm || '{}')).organization,
        identifier: (JSON.parse(localStorage.loginForm || '{}')).identifier,
        password: '',
        submit: function () {
          loginService.login({
            organization: lockForm.organization,
            identifier: lockForm.identifier,
            password: lockForm.password,
            rememberMe: false
          }, function (data) {
            if (!data || !data.access_token || !data.access_token.token) {
              $.niftyNoty({
                type: 'danger',
                container: '#panel-lock',
                html: '登陆失败请稍后再试',
                focus: false,
                timer: 3000
              });
              return;
            }

            //save auth
            utils.auth.setAccessToken(data.access_token.token, DateTime.now().addSeconds(data.access_token.expires_in).valueOf());
            utils.auth.setPermanentToken(data.permanent_token.token, DateTime.now().addSeconds(data.permanent_token.expires_in).valueOf());

            //animation
            $('body').addClass('animated fadeOut');
            $('#panel-lock').addClass('animated zoomOut');
            //redirect to index.html
            $timeout(function () {
              window.location.href = './';
            }, 1000);

          }, function (data, code) {
            console.log('[login failed]', data.message, code);
            $scope.lockForm.password = '';
            $.niftyNoty({
              type: 'danger',
              container: '#panel-lock',
              html: '密码错误',
              focus: false,
              timer: 3000
            });
          });
        }
      };

      function login() {
        var loginForm = $scope.loginForm;
        //trim
        loginForm.organization = $.trim(loginForm.organization);
        loginForm.identifier = $.trim(loginForm.identifier);

        console.log('[before login]', 'organization:', loginForm.organization, 'identifier:', loginForm.identifier);

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

          //save auth
          utils.auth.setAccessToken(data.access_token.token, DateTime.now().addSeconds(data.access_token.expires_in).valueOf());
          utils.auth.setPermanentToken(data.permanent_token.token, DateTime.now().addSeconds(data.permanent_token.expires_in).valueOf());

          //save current login form
          if (loginForm.rememberMe) {
            loginForm.rememberMe = true;
            //console.log('[saving login form]');
          }

          loginForm.password = '';
          loginService.loginForm(loginForm);

          //animation
          $('body').addClass('animated fadeOut');
          $('#panel-login').addClass('animated zoomOut');
          //redirect to index.html
          $timeout(function () {
            window.location.href = './';
          }, 1000);

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

      //show login form
      $timeout(function () {
        $scope.showLoginFormDelay = true;
        $scope.showLockFormDelay = true;
      }, 1500);
    }]);

})();