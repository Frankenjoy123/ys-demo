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
      authAccount: function (accountId, policy, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/accountpermissionpolicy';
        $http.post(url, policy).success(fnSuccess).error(fnError);

        return this;
      },
      cancelAuthAccount: function (accountId, policyCode, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/accountpermissionpolicy' + '?policy_code=' + policyCode;
        $http.delete(url).success(fnSuccess).error(fnError);

        return this;
      },
      getAccountAllPermissions: function (accountId, fnSuccess, fnError) {
        var url = '/api/account/' + accountId + '/permission';
        $http.get(url).success(fnSuccess);
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
            $('#createAccountModal').modal({
              backdrop: 'static',
              keyboard: false
            }).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          },
          hideCreateAccountModal: function () {
            account.spinnerShow = false;
            $('#createAccountModal').modal('hide');
          }
        },
        auth: {
          disableAll: false,
          checkAllAccess: function (isCheck) {
            accountPermission.dashBoardRead = isCheck;

            accountPermission.productKeyRead = isCheck;
            accountPermission.productKeyMng = isCheck;

            accountPermission.packageRead = isCheck;
            accountPermission.packageMng = isCheck;

            accountPermission.logisticsRead = isCheck;
            accountPermission.logisticsMng = isCheck;

            accountPermission.reportRead = isCheck;

            accountPermission.productRead = isCheck;
            accountPermission.productMng = isCheck;

            accountPermission.msgRead = isCheck;
            accountPermission.msgMng = isCheck;

            accountPermission.deviceRead = isCheck;
            accountPermission.deviceMng = isCheck;

            accountPermission.accountRead = isCheck;
            accountPermission.accountMng = isCheck;

            accountPermission.groupRead = isCheck;
            accountPermission.groupMng = isCheck;

            accountPermission.passwordRead = isCheck;
          },
          authDashBoardRead: function () {
            if (accountPermission.dashBoardRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.DASHBOARD + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.DASHBOARD + ':' + ACTION.READ, function () {
                  }, function () {
                  });
            }
          },
          authProductKeyRead: function () {
            if (accountPermission.productKeyRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTKEY + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductKeyMng: function () {
            if (accountPermission.productKeyMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authPackageRead: function () {
            if (accountPermission.packageRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PACKAGE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authPackageMng: function () {
            if (accountPermission.packageMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PACKAGE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authLogisticsRead: function () {
            if (accountPermission.logisticsRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.LOGISTICS + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authLogisticsMng: function () {
            if (accountPermission.logisticsMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.LOGISTICS + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authReportRead: function () {
            if (accountPermission.reportRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.REPORT + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.REPORT + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductRead: function () {
            if (accountPermission.productRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTBASE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductMng: function () {
            if (accountPermission.productMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
                  , function () {
                  }, function () {
                  });
            }
          },
          authMsgRead: function () {
            if (accountPermission.msgRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.MESSAGE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authMsgMng: function () {
            if (accountPermission.msgMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.MESSAGE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  });
            }
          },
          authDeviceRead: function () {
            if (accountPermission.deviceRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.DEVICE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.DEVICE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authDeviceMng: function () {
            if (accountPermission.deviceMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.DEVICE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.DEVICE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authAccountRead: function () {
            if (accountPermission.accountRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.ACCOUNT + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authAccountMng: function () {
            if (accountPermission.accountMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.ACCOUNT + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupRead: function () {
            if (accountPermission.groupRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.GROUP + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.GROUP + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupMng: function () {
            if (accountPermission.groupMng) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.GROUP + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.GROUP + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authPasswordRead: function () {
            if (accountPermission.passwordRead) {
              accountManageService.authAccount(account.selectAccount, {
                policy_code: RESOURCE.PROFILE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              accountManageService.cancelAuthAccount(account.selectAccount,
                  RESOURCE.PROFILE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          showAuthAccountModal: function (accountId) {

            account.selectAccount = accountId;

            account.auth.checkAllAccess(false);
            account.auth.disableAll = false;

            accountManageService.getAccountAllPermissions(accountId, function (data) {

              for (var i = 0; i < data.length; i++) {
                if (data[i].resource_code == '*' && (data[i].action_code == '*')) {
                  account.auth.disableAll = true;
                  account.auth.checkAllAccess(true);

                  break;
                }
                else {
                  if (data[i].resource_code == RESOURCE.DASHBOARD && (data[i].action_code == ACTION.READ)) {
                    accountPermission.dashBoardRead = true;
                  }

                  if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.READ)) {
                    accountPermission.productKeyRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.productKeyMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.packageRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.packageMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.READ)) {
                    accountPermission.logisticsRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.logisticsMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.REPORT && (data[i].action_code == ACTION.READ)) {
                    accountPermission.reportRead = true;
                  }

                  if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.productRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.productMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.msgRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.msgMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.deviceRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.deviceMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.READ)) {
                    accountPermission.accountRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.accountMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.READ)) {
                    accountPermission.groupRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.MANAGE)) {
                    accountPermission.groupMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.PROFILE && (data[i].action_code == ACTION.READ)) {
                    accountPermission.passwordRead = true;
                  }
                }
              }

              $('#treeMenuModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            });
          },
          hideAuthAccountModal: function () {
            account.selectAccount = '';

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
          }
          ,
          hideAddAccountsModal: function () {
            account.spinnerShow = false;
            account.curSelectedGroups = [];
            $("#selectGroup").chosen("destroy");
            $("#selectGroup").html("");
            $('#addToGroupModal').modal('hide');

          }
          ,
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

    }
  ])
  ;
})
();