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

      var group = $scope.group = {
        name: '',
        description: ''
      };

      $scope.spinnerShow = false;

      $timeout(function () {
        $('#demo-cs-multiselect').chosen({width: '100%'});
      }, 0);

      $scope.groupPermission = {
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

      $scope.deleteGroup = function(id){
        groupService.deleteGroup(id, function (data, status) {
          $scope.utils.alert('info', '账号组删除成功');

          setTimeout("location.reload();", 1500);
        });
      };

      $scope.submit = function (isValid) {
        if (!isValid) {
          $scope.utils.alert('info', '页面验证有错误，请返回检查');
          return;
        }

        $scope.spinnerShow = true;

        groupService.createGroup(group, function (data) {

          $scope.spinnerShow = false;
          $('#myModal').modal('hide');
          $scope.utils.alert('info', '账号组创建成功');

          setTimeout("location.reload();", 1500);
        }, function (error) {
          $.niftyNoty({
            type: 'purple',
            icon: 'fa fa-check',
            message: "账号组创建失败",
            container: 'floating',
            timer: 3000
          });
        });
      };

      $scope.showGroupModal = function () {
        $('#myModal').modal({backdrop: 'static', keyboard: false});
      };

      $scope.groupTable = new $scope.utils.DataTable({
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
      });

    }]);
})();