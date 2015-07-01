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
      //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      data.dimensions = {};
      data.dimensions.values = [];
      getQRCode(data);
    });

    function getData(year, mon, day) {
      echartsBarService.getQRCode(date.getDateStr(year, mon, day), getQRCode, function () {
        //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
        var data1 = {};
        data1.data = [];
        data1.dimensions = {};
        data1.dimensions.values = [];
        getQRCode(data1);
      });
    };

  }]);
})();