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
            }
        };
    }]);

    app.controller('MessageCtrl', ['$scope', '$timeout', 'messageService', function ($scope, $timeout, messageService) {
        var messageTypes = {
            business: '商家信息',
            platform: '云溯平台信息'
        };
        var messageStatuses = {
            created: '已创建',
            approved: '通过审核',
            deleted: '已删除'
        };
        (function newDataTable() {
            if ($scope.context.account) {
                $scope.messageTable = new $scope.utils.DataTable({
                    sortable: {
                        target: '#sort-bar',
                        sort: 'createdDateTime,desc'
                    },
                    pageable: {
                        page: 0,
                        size: 20
                    },
                    flush: function (callback) {
                        messageService.getMessages(this, $scope.context.account.org_id, function (data, status, headers) {
                            callback({data: data, headers: headers});
                        });
                    }
                });
            } else {
                $timeout(newDataTable, 1000);
            }
        })();

        $scope.formatMessageType = function (typeCode) {
            return messageTypes[typeCode] || typeCode;
        };

        $scope.formatMessageStatus = function (statusCode) {
            return messageStatuses[statusCode] || statusCode;
        };

    }]);
})();