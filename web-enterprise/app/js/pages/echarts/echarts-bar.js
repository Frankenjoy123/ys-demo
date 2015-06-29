(function () {
  var app = angular.module('root');

  app.factory("echartsBarService", ["$http", function ($http) {
    return {
      getQRCode: function (peirod, fnSuccess, fnError) {
        var url = '/api/report/myorganization/product_qrcode_count/' + peirod;
        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("echartsBarCtrl", ["$scope", "echartsBarService", "$timeout", function ($scope, echartsBarService, $timeout) {

    var date = new Date();
    $scope.monDays = [];
    $scope.currDay = date.getCurrDay();

    var getQRCode = function (data) {

      var echartBar = echarts.init($('#echartBar')[0]);

      var option = {
        title: {
          text: '产品贴码统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['产品贴码统计']
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            show: true,
            data: data.dimensions.values
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '产品贴码统计',
            type: 'bar',
            data: data.data
          }
        ]
      };

      echartBar.setOption(option);
    };

    echartsBarService.getQRCode(date.getDateStr(), getQRCode, function () {
      $scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      data.dimensions = {};
      data.dimensions.values = [];
      getQRCode(data);
    });

    $scope.getData = function (data) {
      echartsBarService.getQRCode(date.getDateStr(data), getQRCode, function () {
        $scope.utils.alert('info', date.getDateStr(data) + '该日数据不存在');
        var data1 = {};
        data1.data = [];
        data1.dimensions = {};
        data1.dimensions.values = [];
        getQRCode(data1);
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

  }]);
})();