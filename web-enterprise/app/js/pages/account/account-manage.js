(function () {
  var app = angular.module('root');

  app.factory('accountManageService', ['$http', function ($http) {
    return {
      getAccounts: function (dataTable, fnSuccess, fnError) {
        $http.get('/api/account?' + dataTable.toString()).success(fnSuccess);

        return this;
      },
      createAccount: function (account, fnSuccess, fnError) {
        $http.post("/api/account", account).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller('AccountManageCtrl', [
    '$scope',
    '$timeout',
    'accountManageService',
    'dataFilterService',
    function ($scope, $timeout, accountManageService, dataFilterService) {

      $scope.accountIdentifier = '';
      $scope.lastName = '';
      $scope.firstName = '';
      $scope.email = '';
      $scope.phone = '';
      $scope.password = '';
      $scope.passwordConfirm = '';

      $scope.accountPermission = {
        dashBoardRead: '',
        productKeyRead: '',
        productKeyMng: '',
        packageRead: '',
        packageMng: '',
        logisticsRead: '',
        logisticsMng: '',
        tieMaRead: '',
        saoMaRead: '',
        monMaRead: '',
        placeMaRead: '',
        productRead: '',
        productMng: '',
        msgRead: '',
        deviceRead: '',
        deviceMng: '',
        accountRead: '',
        accountMng: '',
        passwordRead: ''
      };

      $scope.authAccount = function () {

      };

      $scope.accountTable = new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          accountManageService.getAccounts(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      });

      $timeout(function () {

        $('#accountForm').bootstrapValidator({
          message: '输入不合法',
          feedbackIcons: {
            valid: 'fa fa-check-circle fa-lg text-success',
            invalid: 'fa fa-times-circle fa-lg',
            validating: 'fa fa-refresh'
          },
          fields: {
            accountIdentifier: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入账号标识'
                }
              }
            },
            lastName: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入姓'
                }
              }
            },
            firstName: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入名'
                }
              }
            },
            email: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入邮箱'
                },
                emailAddress: {
                  message: '邮箱输入不合法'
                }
              }
            },
            phone: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入电话'
                },
                digits: {
                  message: '电话只能是数字'
                }
              }
            },
            password: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入密码'
                }
              }
            },
            passwordConfirm: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入确认密码'
                },
                identical: {
                  field: 'password',
                  message: '确认密码不一致'
                }
              }
            }
          }
        }).on('success.form.#accountForm', function (e, data) {

          var $parent = data.element.parents('.form-group');
          // Remove the has-success class
          $parent.removeClass('has-success');
        });

      }, 0);

      $scope.createAccount = function () {

        if ($scope.accountIdentifier == '')
          return;

        if ($scope.firstName == '')
          return;

        if ($scope.lastName == '')
          return;

        if ($scope.email == '')
          return;

        if ($scope.phone == '')
          return;

        if ($scope.password == '')
          return;

        var account = {};

        account.org_id = $scope.context.organization.id;
        account.identifier = $scope.accountIdentifier;
        account.first_name = $scope.firstName;
        account.last_name = $scope.lastName;
        account.email = $scope.email;
        account.phone = $scope.phone;
        account.password = $scope.password;

        accountManageService.createAccount(account, function (data) {
          $scope.utils.alert('success', '创建账号成功');
        }, function (data) {
          $scope.utils.alert('info', '创建账号失败');
        });
      };

    }]);
})();