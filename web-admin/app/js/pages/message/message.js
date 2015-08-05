(function () {
  var app = angular.module('root');

  app.factory('messageService', ['$http', function ($http) {
    return {
      getMessages: function (dataTable, orgId, fnSuccess) {
        var url = '/api/message?';
        if (orgId) {
          url += 'org_id=' + orgId + '&';
        }
        url += dataTable.toString();
        $http.get(url).success(fnSuccess);
      },
      getOrgList: function (fnSuccess, fnError) {
        $http.get('/api/organization').success(fnSuccess).error(fnError);
        return this;
      }
    };
  }]);

  app.controller('MessageCtrl', ['$scope', '$timeout', 'messageService', function ($scope, $timeout, messageService) {

    $scope.currOrg = $scope.context.account.org_id || '';

    var messageTypes = {
      business: '商家信息',
      platform: '云溯平台信息'
    };
    var messageStatuses = {
      created: '已创建',
      approved: '通过审核',
      deleted: '已删除'
    };

    var table = {
      sortable: {
        target: '#sort-bar',
        sort: 'createdDateTime,desc'
      },
      pageable: {
        page: 0,
        size: 20
      },
      flush: function (callback) {
        messageService.getMessages(this, $scope.currOrg , function (data, status, headers) {
          callback({data: data, headers: headers});
        });
      }
    };

    $scope.messageTable = new $scope.utils.DataTable(table);

    $scope.formatMessageType = function (typeCode) {
      return messageTypes[typeCode] || typeCode;
    };

    $scope.formatMessageStatus = function (statusCode) {
      return messageStatuses[statusCode] || statusCode;
    };

    messageService.getOrgList(function (data) {
      $scope.orgList = data;
    }, function () {
    });

    $scope.orgChange = function () {
      $scope.messageTable = new $scope.utils.DataTable(table);
    }

  }]);
})();