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
      }
    };
  }]);

  app.controller('groupCtrl', [
    '$scope',
    '$timeout',
    '$route',
    'groupService',
    'dataFilterService',
    function ($scope, $timeout, $route, groupService, dataFilterService) {

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