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
      },
      getCurrentOrgGroups: function (fnSuccess) {
        var url = '/api/group';
        $http.get(url).success(fnSuccess);

        return this;
      },
      getCurrentAccountGroups: function (accountId, fnSuccess) {
        var url = '/api/account/' + accountId + '/group';
        $http.get(url).success(fnSuccess);

        return this;
      },
      addAccountsToGroup: function (accountId, groups, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/group';
        $http.put(url, groups).success(fnSuccess).error(fnError);

        return this;
      },
      authAccount: function (accountPolicyList, fnSuccess, fnError) {


        return this;
      }
    };
  }]);

  app.controller('AccountManageCtrl', [
    '$scope',
    '$timeout',
    'accountManageService',
    'YUNSOO_CONFIG',
    function ($scope, $timeout, accountManageService, YUNSOO_CONFIG) {

      var RESOURCE = YUNSOO_CONFIG.PAGE_ACCESS.RESOURCE;
      var ACTION = YUNSOO_CONFIG.PAGE_ACCESS.ACTION;

      var accountPermission = $scope.accountPermission = {
        dashBoardRead: '',
        productKeyRead: '',
        productKeyMng: '',
        packageRead: '',
        packageMng: '',
        logisticsRead: '',
        logisticsMng: '',
        reportRead: '',
        productRead: '',
        productMng: '',
        msgRead: '',
        msgMng: '',
        deviceRead: '',
        deviceMng: '',
        accountRead: '',
        accountMng: '',
        groupRead: '',
        groupMng: '',
        passwordRead: ''
      };

      var accountTable = {
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
      };

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
        }).on('success.field.bv', function (e, data) {

        });

      }, 0);

      accountManageService.getCurrentOrgGroups(function (data) {
        account.curOrgGroups = data;
      });

      var account = $scope.account = {
        spinnerShow: false,
        accountTable: new $scope.utils.DataTable(accountTable),
        selectAccount: '',
        curOrgGroups: '',
        curSelectedGroups: [],
        create: {
          accountIdentifier: '',
          lastName: '',
          firstName: '',
          email: '',
          phone: '',
          password: '',
          passwordConfirm: '',
          createAccount: function () {

            if (account.create.accountIdentifier == '')
              return;

            if (account.create.firstName == '')
              return;

            if (account.create.lastName == '')
              return;

            if (account.create.email == '')
              return;

            if (account.create.phone == '')
              return;

            if (account.create.password == '')
              return;

            var accountObj = {};

            accountObj.org_id = $scope.context.organization.id;
            accountObj.identifier = account.create.accountIdentifier;
            accountObj.first_name = account.create.firstName;
            accountObj.last_name = account.create.lastName;
            accountObj.email = account.create.email;
            accountObj.phone = account.create.phone;
            accountObj.password = account.create.password;

            account.spinnerShow = true;

            accountManageService.createAccount(accountObj, function (data) {

              account.spinnerShow = false;

              $('#createAccountModal').modal('hide');

              $scope.utils.alert('success', '创建账号成功');

              account.accountTable = new $scope.utils.DataTable(accountTable);

            }, function (data) {
              account.spinnerShow = false;
              $scope.utils.alert('danger', '创建账号失败', '#createAccountModal .modal-dialog', false);
            });
          },
          showCreateAccountModal: function () {
            $('#createAccountModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          },
          hideCreateAccountModal: function () {
            account.spinnerShow = false;
            $('#createAccountModal').modal('hide');
          }
        },
        auth: {
          authAccount: function () {
            var accountPolicyList = [];

            if (accountPermission.dashBoardRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.DASHBOARD + ':' + ACTION.READ
              });
            }
            if (accountPermission.productKeyRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.READ
              });
            }
            if (accountPermission.productKeyMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.packageRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.READ
              });
            }
            if (accountPermission.packageMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.logisticsRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.READ
              });
            }
            if (accountPermission.logisticsMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.reportRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.REPORT + ':' + ACTION.READ
              });
            }
            if (accountPermission.productRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.READ
              });
            }
            if (accountPermission.productMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.msgRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.READ
              });
            }
            if (accountPermission.msgMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.deviceRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.DEVICE + ':' + ACTION.READ
              });
            }
            if (accountPermission.deviceMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.DEVICE + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.accountRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.READ
              });
            }
            if (accountPermission.accountMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.groupRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.GROUP + ':' + ACTION.READ
              });
            }
            if (accountPermission.groupMng) {
              accountPolicyList.push({
                policy_code: RESOURCE.GROUP + ':' + ACTION.MANAGE
              });
            }
            if (accountPermission.passwordRead) {
              accountPolicyList.push({
                policy_code: RESOURCE.PROFILE + ':' + ACTION.READ
              });
            }


          },
          showAuthAccountModal: function (accountId) {

            account.selectAccount = accountId;

            $('#treeMenuModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          },
          hideAuthAccountModal: function () {
            $('#treeMenuModal').modal('hide');
          }
        },
        assignGroup: {
          showAddToGroupModal: function (accountId) {
            account.selectAccount = accountId;

            accountManageService.getCurrentAccountGroups(accountId, function (data) {

              if (account.curOrgGroups != '') {

                $.each(account.curOrgGroups, function (name, value) {
                  var option = "<option value=" + value.id;

                  if (data != undefined && data.length > 0) {
                    $.each(data, function (name1, value1) {
                      if (value1.id == value.id) {
                        option += " selected = 'selected'";
                      }
                    });
                  }

                  option += ">" + value.name + "</option>";
                  $('#selectGroup').append(option);
                });
              }

              $('#selectGroup').chosen({width: '100%'});
              $("#selectGroup").change(function () {
                account.curSelectedGroups = $(this).val();
              });

              $('#addToGroupModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            }, function () {
              if (account.curOrgGroups != '') {

                $.each(account.curOrgGroups, function (name, value) {
                  var option = "<option value=" + value.id + ">" + value.name + "</option>";
                  $('#selectGroup').append(option);
                });
              }

              $('#selectGroup').chosen({width: '100%'});
              $("#selectGroup").change(function () {
                account.curSelectedGroups = $(this).val();
              });

              $('#addToGroupModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            });
          },
          hideAddAccountsModal: function () {
            account.spinnerShow = false;
            account.curSelectedGroups = [];
            $("#selectGroup").chosen("destroy");
            $("#selectGroup").html("");
            $('#addToGroupModal').modal('hide');

          },
          addAccountsToGroup: function () {
            account.spinnerShow = true;

            if (account.curSelectedGroups != '') {
              accountManageService.addAccountsToGroup(account.selectAccount, account.curSelectedGroups || [], function (data) {
                account.assignGroup.hideAddAccountsModal();
                $scope.utils.alert('info', '账号组更新成功');
              }, function (data) {
                account.spinnerShow = false;
                $scope.utils.alert('danger', '账号组更新失败', '#addToGroupModal .modal-dialog', false);
              });
            }
          }
        }
      };

    }]);
})();