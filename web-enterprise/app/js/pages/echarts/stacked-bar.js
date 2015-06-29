(function () {
  var app = angular.module('root');

  app.factory("stackedBarService", ["$http", function ($http) {
    return {
      getScanCount: function (peirod, fnSuccess, fnError) {
        var url = '/api/report/myorganization/product_month_scan_count/' + peirod;
        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("stackedBarCtrl", ["$scope", "stackedBarService", "$timeout", function ($scope, stackedBarService, $timeout) {

    var date = new Date();
    $scope.monDays = [];
    $scope.currDay = date.getCurrDay();

    stackedBarService.getScanCount(date.getDateStr(), getScanCount, function () {
      $scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      data.dimensions = {};
      data.dimensions.values = [];
      getScanCount(data);
    });

    $scope.getData = function (data) {
      stackedBarService.getScanCount(date.getDateStr(data), getScanCount, function () {
        $scope.utils.alert('info', date.getDateStr(data) + '该日数据不存在');
        var data1 = {};
        data1.data = [];
        data1.dimensions = {};
        data1.dimensions.values = [];
        getScanCount(data1);
      });
    };

    for (var i = 1; i <= date.getCurrentMonthMaxDay(); i++) {

      var day = 0;

      if (i < 10)
        day = '0' + i;
      else
        day = i;

      $scope.monDays.push(day);
    }

    function getScanCount(data) {

      var stackedBar = echarts.init($('#stackedBar')[0]);

      var dataShow = [];
      for (var i = 0; i < data.data.length; i++) {

        for (var j = 0; j < data.data[i].length; j++) {
          if (data.data[i][j] == null)
            data.data[i][j] = 0;
        }

        dataShow.push({
          name: data.dimensions.values[0][i],
          type: 'bar',
          stack: '总量',
          itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
          data: data.data[i]
        });
      }

      var option = {
        title: {
          text: '产品扫码月统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          orient: 'vertical',
          x: 'right',
          y: 'center',
          data: data.dimensions.values[0]
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'value'
          }
        ],
        yAxis: [
          {
            type: 'category',
            show: true,
            data: data.dimensions.values[1]
          }
        ],
        series: dataShow
      };

      stackedBar.setOption(option);

    };

  }]);
})();