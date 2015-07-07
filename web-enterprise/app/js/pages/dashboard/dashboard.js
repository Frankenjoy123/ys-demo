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

    function showMessages() {
      dashboardService.getUnVerifiedMessages('org_id=' + $scope.context.organization.id + '&status_code_in=created', function (data) {
        var unVerifiedMessages = data;

        dashboardService.getAllMessages('org_id=' + $scope.context.organization.id, function (data) {
          var allMessages = data;

          var percent = Math.round((unVerifiedMessages / allMessages) * 100);

          $('#maChart02').empty();
          //$('#maChart02').data('text', unVerifiedMessages + '/' + allMessages);
          $('#maChart02').data('text', unVerifiedMessages);
          $('#maChart02').data('percent', percent);
          $('#maChart02').circliful();
        });
      });
    }

    function showProductKeyCreditSum(data) {
      $('#maChart04').empty();
      $('#maChart04').data('text', data.remain);
      $('#maChart04').data('percent', data.percentage);
      $('#maChart04').circliful();
    }

    $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
      var path = current.$$route ? current.$$route.originalPath : '/dashboard';
      if (path === '/dashboard') {

        $('#maChart01').circliful();
        $('#maChart02').circliful();
        $('#maChart03').circliful();
        $('#maChart04').circliful();

        if ($scope.context.organization) {
          showMessages();
        }
        else {
          $scope.$on('context-organization-ready', function (event) {
            showMessages();
          });
        }

        if ($scope.productKeyCreditSum && $scope.productKeyCreditSum.ready) {
          showProductKeyCreditSum($scope.productKeyCreditSum);
        } else {
          $scope.$on('productKeyCreditSum-ready', function (event, data) {
            showProductKeyCreditSum(data);
          });
        }
      }
    });

  }]);
})();