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
      createMessage: function (message, fnSuccess, fnError) {
        $http.post("/api/message", message).success(fnSuccess).error(fnError);
      }
    };
  }]);

  app.controller('MessageCtrl', ['$scope', '$timeout', 'messageService', function ($scope, $timeout, messageService) {

    $scope.spinnerShow = false;

    var message = $scope.message = {
      title: '',
      body: '',
      type: 'business',
      org_id: '',
      link: ''
    };

    if ($scope.context.organization) {
      $scope.message.org_id = $scope.context.organization.id;
      initMsgTable($scope.message.org_id)
    }
    else {
      $scope.$on('context-organization-ready', function (event) {
        $scope.message.org_id = $scope.context.organization.id;
        initMsgTable($scope.message.org_id)
      });
    }

    var messageTypes = {
      business: '商家信息',
      platform: '云溯平台信息'
    };
    var messageStatuses = {
      created: '已创建',
      approved: '通过审核',
      deleted: '已删除'
    };

    function initMsgTable(orgId) {
      $scope.messageTable = new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 200
        },
        flush: function (callback) {
          messageService.getMessages(this, orgId, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      });
    };

    $scope.formatMessageType = function (typeCode) {
      return messageTypes[typeCode] || typeCode;
    };

    $scope.formatMessageStatus = function (statusCode) {
      return messageStatuses[statusCode] || statusCode;
    };

    //init  validator
    $timeout(function () {
      $('#msgForm').bootstrapValidator({
        message: '非法输入',
        feedbackIcons: {
          valid: 'fa fa-check-circle fa-lg text-success',
          invalid: 'fa fa-times-circle fa-lg',
          validating: 'fa fa-refresh'
        },
        fields: {
          title: {
            validators: {
              notEmpty: {
                message: '消息标题不能为空'
              }
            }
          },
          body: {
            validators: {
              notEmpty: {
                message: '消息正文不能为空'
              }
            }
          },
          link: {
            validators: {
              notEmpty: {
                message: '链接不能为空'
              }
            }
          }
        }
      }).on('success.field.bv', function (e, data) {
        e.preventDefault();
        //login on validation success
      });

    }, 0); //end of $timeout

    $scope.submit = function () {

      if (message.title == '') {
        $scope.utils.alert('info', '消息标题不能为空', '#myModal .modal-dialog', false);
        return;
      }

      if (message.body == '') {
        $scope.utils.alert('info', '消息正文不能为空', '#myModal .modal-dialog', false);
        return;
      }

      if (message.link == '') {
        $scope.utils.alert('info', '链接不能为空', '#myModal .modal-dialog', false);
        return;
      }

      $scope.spinnerShow = true;

      messageService.createMessage(message, function () {

        $scope.spinnerShow = false;

        $('#myModal').modal('hide');

        if ($scope.message.org_id != '')
          initMsgTable($scope.message.org_id);

        $scope.utils.alert('info', '创建消息成功');
      });
    };

    $scope.showModal = function () {
      $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
      });
    };

    $scope.hideModal = function () {
      $('#myModal').modal('hide');
    };

  }]);
})();