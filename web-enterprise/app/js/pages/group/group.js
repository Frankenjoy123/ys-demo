(function () {
  var app = angular.module('root');

  app.factory('groupService', ['$http', function ($http) {
    return {
      getGroups: function (fnSuccess, fnError) {
        $http.get('/api/group').success(fnSuccess);

        return this;
      },
      createGroup: function (group, fnSuccess, fnError) {
        $http.post("/api/group", group).success(fnSuccess).error(fnError);

        return this;
      },
      getCurrentOrgAccounts: function (fnSuccess) {
        var url = '/api/account';
        $http.get(url).success(fnSuccess);

        return this;
      },
      getCurrentGroupAccounts: function (groupId, fnSuccess) {
        var url = '/api/group/' + groupId + '/account';
        $http.get(url).success(fnSuccess);

        return this;
      },
      addAccountsToGroup: function (groupId, accounts, fnSuccess, fnError) {
        var url = '/api/group/' + groupId + '/account';
        $http.put(url, accounts).success(fnSuccess).error(fnError);

        return this;
      },
      deleteGroup: function (groupId, fnSuccess, fnError) {
        $http.delete('/api/group/' + groupId).success(fnSuccess);

        return this;
      },
      authGroup: function (groupId, policy, fnSuccess, fnError) {
        var url = '/api/group/' + groupId + '/grouppermissionpolicy';
        $http.post(url, policy).success(fnSuccess).error(fnError);

        return this;
      },
      cancelAuthGroup: function (groupId, policyCode, fnSuccess, fnError) {
        var url = '/api/group/' + groupId + '/grouppermissionpolicy' + '?policy_code=' + policyCode;
        $http.delete(url).success(fnSuccess).error(fnError);

        return this;
      },
      getGroupAllPermissions: function (groupId, fnSuccess, fnError) {
        var url = '/api/group/' + groupId + '/permission';
        $http.get(url).success(fnSuccess);
      }
    };
  }]);

  app.controller('groupCtrl', [
    '$scope',
    '$timeout',
    '$route',
    'groupService',
    'YUNSOO_CONFIG',
    function ($scope, $timeout, $route, groupService, YUNSOO_CONFIG) {

      var RESOURCE = YUNSOO_CONFIG.PAGE_ACCESS.RESOURCE;
      var ACTION = YUNSOO_CONFIG.PAGE_ACCESS.ACTION;

      var groupDatatable = {
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          groupService.getGroups(function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      };

      var groupPermission = $scope.groupPermission = {
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

      groupService.getCurrentOrgAccounts(function (data) {
        group.curOrgAccounts = data;
      });

      var group = $scope.group = {
        name: '',
        description: '',
        spinnerShow: false,
        curDeleteGroup: '',
        curDeleteGroupName: '',
        curSelectGroup: '',
        groupTable: new $scope.utils.DataTable(groupDatatable),
        curOrgAccounts: '',
        selectAccounts: [],
        auth: {
          disableAll: false,
          checkAllAccess: function (isCheck) {
            groupPermission.dashBoardRead = isCheck;

            groupPermission.productKeyRead = isCheck;
            groupPermission.productKeyMng = isCheck;

            groupPermission.packageRead = isCheck;
            groupPermission.packageMng = isCheck;

            groupPermission.logisticsRead = isCheck;
            groupPermission.logisticsMng = isCheck;

            groupPermission.reportRead = isCheck;

            groupPermission.productRead = isCheck;
            groupPermission.productMng = isCheck;

            groupPermission.msgRead = isCheck;
            groupPermission.msgMng = isCheck;

            groupPermission.deviceRead = isCheck;
            groupPermission.deviceMng = isCheck;

            groupPermission.accountRead = isCheck;
            groupPermission.accountMng = isCheck;

            groupPermission.groupRead = isCheck;
            groupPermission.groupMng = isCheck;

            groupPermission.passwordRead = isCheck;
          },
          authDashBoardRead: function () {
            if (groupPermission.dashBoardRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.DASHBOARD + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.DASHBOARD + ':' + ACTION.READ, function () {
                  }, function () {
                  });
            }
          },
          authProductKeyRead: function () {
            if (groupPermission.productKeyRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PRODUCTKEY + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductKeyMng: function () {
            if (groupPermission.productKeyMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PRODUCTKEY + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authPackageRead: function () {
            if (groupPermission.packageRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PACKAGE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authPackageMng: function () {
            if (groupPermission.packageMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PACKAGE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PACKAGE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authLogisticsRead: function () {
            if (groupPermission.logisticsRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.LOGISTICS + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authLogisticsMng: function () {
            if (groupPermission.logisticsMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.LOGISTICS + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.LOGISTICS + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authReportRead: function () {
            if (groupPermission.reportRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.REPORT + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.REPORT + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductRead: function () {
            if (groupPermission.productRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PRODUCTBASE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authProductMng: function () {
            if (groupPermission.productMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PRODUCTBASE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authMsgRead: function () {
            if (groupPermission.msgRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.MESSAGE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authMsgMng: function () {
            if (groupPermission.msgMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.MESSAGE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.MESSAGE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  });
            }
          },
          authDeviceRead: function () {
            if (groupPermission.deviceRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.DEVICE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.DEVICE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authDeviceMng: function () {
            if (groupPermission.deviceMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.DEVICE + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.DEVICE + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupRead: function () {
            if (groupPermission.accountRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.ACCOUNT + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupMng: function () {
            if (groupPermission.accountMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.ACCOUNT + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.ACCOUNT + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupRead: function () {
            if (groupPermission.groupRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.GROUP + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.GROUP + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          authGroupMng: function () {
            if (groupPermission.groupMng) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.GROUP + ':' + ACTION.MANAGE
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.GROUP + ':' + ACTION.MANAGE, function () {
                  }, function () {
                  }
              );
            }
          },
          authPasswordRead: function () {
            if (groupPermission.passwordRead) {
              groupService.authGroup(group.curSelectGroup, {
                policy_code: RESOURCE.PROFILE + ':' + ACTION.READ
              }, function () {
              }, function () {
              });
            }
            else {
              groupService.cancelAuthGroup(group.curSelectGroup,
                  RESOURCE.PROFILE + ':' + ACTION.READ, function () {
                  }, function () {
                  }
              );
            }
          },
          showAuthGroupModal: function (id) {

            group.curSelectGroup = id;

            group.auth.checkAllAccess(false);
            group.auth.disableAll = false;

            groupService.getGroupAllPermissions(id, function (data) {

              for (var i = 0; i < data.length; i++) {
                if (data[i].resource_code == '*' && (data[i].action_code == '*')) {
                  group.auth.disableAll = true;
                  group.auth.checkAllAccess(true);

                  break;
                }
                else {
                  if (data[i].resource_code == RESOURCE.DASHBOARD && (data[i].action_code == ACTION.READ)) {
                    groupPermission.dashBoardRead = true;
                  }

                  if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.READ)) {
                    groupPermission.productKeyRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PRODUCTKEY && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.productKeyMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.READ)) {
                    groupPermission.packageRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PACKAGE && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.packageMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.READ)) {
                    groupPermission.logisticsRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.LOGISTICS && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.logisticsMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.REPORT && (data[i].action_code == ACTION.READ)) {
                    groupPermission.reportRead = true;
                  }

                  if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.READ)) {
                    groupPermission.productRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.PRODUCTBASE && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.productMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.READ)) {
                    groupPermission.msgRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.MESSAGE && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.msgMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.READ)) {
                    groupPermission.deviceRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.DEVICE && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.deviceMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.READ)) {
                    groupPermission.accountRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.ACCOUNT && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.accountMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.READ)) {
                    groupPermission.groupRead = true;
                  }
                  if (data[i].resource_code == RESOURCE.GROUP && (data[i].action_code == ACTION.MANAGE)) {
                    groupPermission.groupMng = true;
                  }

                  if (data[i].resource_code == RESOURCE.PROFILE && (data[i].action_code == ACTION.READ)) {
                    groupPermission.passwordRead = true;
                  }
                }
              }

              $('#treeMenuModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
                $(this).removeData("bs.modal");
              });
            });
          },
          hideAuthGroupModal: function () {
            group.curSelectGroup = '';

            $('#treeMenuModal').modal('hide');
          }
        },
        showGroupModal: function () {
          $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
            $(this).removeData("bs.modal");
          });
        },
        showAddAccountModal: function (id) {
          group.curSelectGroup = id;

          groupService.getCurrentGroupAccounts(id, function (data) {

            if (group.curOrgAccounts != '') {

              $.each(group.curOrgAccounts, function (name, value) {
                var option = "<option value=" + value.id;

                if (data != undefined && data.length > 0) {
                  $.each(data, function (name1, value1) {
                    if (value1.id == value.id) {
                      option += " selected = 'selected'";
                    }
                  });
                }

                option += ">" + value.last_name + value.first_name + "</option>";
                $('#selectAccount').append(option);
              });
            }

            $('#selectAccount').chosen({width: '100%'});
            $("#selectAccount").change(function () {
              group.selectAccounts = $(this).val();
            });

            $('#addAccountsModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          }, function () {
            if (group.curOrgAccounts != '') {

              $.each(group.curOrgAccounts, function (name, value) {
                var option = "<option value=" + value.id + ">" + value.last_name + value.first_name + "</option>";
                $('#selectAccount').append(option);
              });
            }

            $('#selectAccount').chosen({width: '100%'});
            $("#selectAccount").change(function () {
              group.selectAccounts = $(this).val();
            });

            $('#addAccountsModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
              $(this).removeData("bs.modal");
            });
          });
        },
        hideAddAccountsModal: function () {
          group.curSelectGroup = '';
          group.spinnerShow = false;
          $("#selectAccount").chosen("destroy");
          $("#selectAccount").html("");
          $('#addAccountsModal').modal('hide');
        },
        addAccountsToGroup: function () {
          group.spinnerShow = true;

          if (group.curSelectGroup != '') {
            groupService.addAccountsToGroup(group.curSelectGroup, group.selectAccounts || [], function (data) {
              group.hideAddAccountsModal();
              $scope.utils.alert('info', '账号更新成功');
            }, function (data) {
              group.spinnerShow = false;
              $scope.utils.alert('danger', '账号更新失败', '#addAccountsModal .modal-dialog', false);
            });
          }
        },
        deleteGroup: function (id, name) {
          group.curDeleteGroup = id;
          group.curDeleteGroupName = name;

          $('#deleteConfirmDialog').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
            $(this).removeData("bs.modal");
          });
        },
        deleteGroupConfirm: function () {
          if (group.curDeleteGroup != '') {
            groupService.deleteGroup(group.curDeleteGroup, function (data, status) {
              $('#deleteConfirmDialog').modal('hide');
              $scope.utils.alert('info', '账号组删除成功');
              group.groupTable = new $scope.utils.DataTable(groupDatatable);
            });
          }
        },
        deleteGroupCancel: function () {
          group.curDeleteGroup = '';
          $('#deleteConfirmDialog').modal('hide');
        },
        createGroup: function (isValid) {
          if (!isValid) {
            $scope.utils.alert('info', '页面验证有错误，请返回检查');
            return;
          }

          group.spinnerShow = true;

          var groupObj = {
            name: group.name,
            description: group.description
          };

          groupService.createGroup(groupObj, function (data) {

            group.spinnerShow = false;

            $('#myModal').modal('hide');

            $scope.utils.alert('info', '账号组创建成功');

            this.groupTable = new $scope.utils.DataTable(groupDatatable);
          }, function (error) {
            $scope.utils.alert('danger', '账号组创建失败', '#myModal .modal-dialog', false);
          });
        }
      };

    }]);
})();