(function () {
    var app = angular.module('root');

    app.factory("dashboardService", ["$http", function ($http) {
        return {
            getProductKeyQuantitySum: function (fnSuccess) {
                $http.get('/api/productkeybatch/sum/quantity').success(fnSuccess);
            },
            getUnVerifiedMessages: function (data, fnSuccess) {
                $http.get('/api/message/count/on?' + data).success(fnSuccess);
            },
            getAllMessages: function (data, fnSuccess) {
                $http.get('/api/message/count/on?' + data).success(fnSuccess);
            }
        }
    }]);

    app.controller("DashboardCtrl", ["$scope", "dashboardService", "$timeout", function ($scope, dashboardService, $timeout) {
        $scope.productKeyQuantitySum = 0;
        //get quantity sum of product key batches
        dashboardService.getProductKeyQuantitySum(function (data) {
            $scope.productKeyQuantitySum = data;
        });

        $scope.$on('context-organization-ready', function(event){
            dashboardService.getUnVerifiedMessages('org_id=' + $scope.context.organization.id + '&status_code_in=created', function (data) {
                var unVerifiedMessages = data;

                dashboardService.getAllMessages('org_id=' + $scope.context.organization.id, function (data) {
                    var allMessages = data;

                    var percent = Math.round((unVerifiedMessages / allMessages) * 100);
                    $('#maChart02').attr('data-text', unVerifiedMessages + '/' + allMessages);
                    $('#maChart02').attr('data-percent', percent);
                    $('#maChart02').circliful();
                });
            });
        });

        $timeout(function () {
            $('#maChart01').circliful();
            $('#maChart03').circliful();
        }, 0);

        $scope.$on('productKeyCreditSum-ready', function (event, data) {
            $('#maChart04').attr('data-text', data.percentage + '%');
            $('#maChart04').attr('data-percent', data.percentage);
            $('#maChart04').circliful();
        });

    }]);
})();