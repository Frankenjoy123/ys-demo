(function () {
  var app = angular.module('root');

  app.factory("pieMapService", ["$http", function ($http) {
    return {
      getScanCount: function (peirod, fnSuccess, fnError) {
        var url = '/api/report/myorganization/product_scan_count/' + peirod;
        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("pieMapCtrl", ["$scope", "pieMapService", "$timeout", function ($scope, pieMapService, $timeout) {

    var date = new Date();
    $scope.days = [];
    $scope.mons = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
    $scope.years = [];

    $scope.selYear = date.getCurrYear();
    $scope.selMon = date.getCurrMonth();
    $scope.selDay = date.getCurrDay();

    $scope.setYear = function (data) {
      $scope.selYear = data;

      initDays();
      getData($scope.selYear, $scope.selMon, $scope.selDay);
    };

    $scope.setMon = function (data) {
      $scope.selMon = data;

      initDays();
      getData($scope.selYear, $scope.selMon, $scope.selDay);
    };

    $scope.setDay = function (data) {
      $scope.selDay = data;

      initDays();
      getData($scope.selYear, $scope.selMon, $scope.selDay);
    };

    var initDays = function () {

      $scope.days = [];

      for (var i = 1; i <= date.getMonthMaxDay($scope.selYear, $scope.selMon); i++) {
        if (i < 10)
          $scope.days.push('0' + i);
        else
          $scope.days.push('' + i);
      }

      return initDays;
    };

    initDays();

    for (var j = date.getFullYear() - 2; j <= date.getFullYear() + 2; j++) {
      $scope.years.push('' + j);
    }

    pieMapService.getScanCount(date.getDateStr(), getScanCount, function () {
      //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      data.dimensions = {};
      data.dimensions.values = [];
      getScanCount(data);
    });

    function getData (year, mon, day) {
      pieMapService.getScanCount(date.getDateStr(year, mon, day), getScanCount, function () {
        //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
        var data1 = {};
        data1.data = [];
        data1.dimensions = {};
        data1.dimensions.values = [];
        getScanCount(data1);
      });
    };

    function getScanCount(data) {

      var pieMap = echarts.init($('#pieMap')[0]);

      var dataShow = [];
      for (var i = 0; i < data.data.length; i++) {
        dataShow.push({value: data.data[i], name: data.dimensions.values[i]});
      }

      var option = {
        title: {
          text: '产品扫码统计',
          x: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          data: data.dimensions.values
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            magicType: {
              show: true,
              type: ['pie', 'funnel'],
              option: {
                funnel: {
                  x: '25%',
                  width: '50%',
                  funnelAlign: 'left',
                  max: 1548
                }
              }
            },
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        calculable: true,
        series: [
          {
            name: '扫码统计',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: dataShow
          }
        ]
      };

      pieMap.setOption(option);

    };

  }]);
})();