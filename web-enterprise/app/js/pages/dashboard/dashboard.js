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

    var Conversion = $scope.Conversion = function (num) {
      if (isNaN(num)) {
        return "这不是一个数字";
      }
      else {
        if (num < 0) {
          return "这是一个负数";
        }
        else if ((num >= 0) && (num < 10000)) {
          return num + "";
        }
        else if ((10000 <= num) && (num < 100000000)) {
          if (num % 10000 == 0) {
            return num / 10000 + "万"
          }
          else {
            return parseInt(num / 10000) + "万+";
          }
        }
        else if (100000000 <= num) {
          if (num % 100000000 == 0) {
            return num / 100000000 + "亿"
          }
          else {
            return parseInt(num / 100000000) + "亿+"
          }
        }
      }
    }

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

          $('#maChart02 .circle-info-half').css('padding-top', '5px');
        });
      });
    }

    function showProductKeyCreditSum(data) {
      $('#maChart04').empty();
      $('#maChart04').data('text', Conversion(data.remain));
      $('#maChart04').data('percent', data.percentage);
      $('#maChart04').circliful();

      $('#maChart04 .circle-info-half').css('padding-top', '5px');
    }

    $scope.$on('$routeChangeSuccess', function (angularEvent, current, previous) {
      var path = current.$$route ? current.$$route.originalPath : '/dashboard';
      if (path === '/dashboard') {

        $('#maChart01').circliful();
        $('#maChart02').circliful();
        $('#maChart03').circliful();
        $('#maChart04').circliful();

        $('#maChart01 .circle-info-half').css('padding-top', '5px');
        $('#maChart02 .circle-info-half').css('padding-top', '5px');
        $('#maChart03 .circle-info-half').css('padding-top', '5px');
        $('#maChart04 .circle-info-half').css('padding-top', '5px');

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