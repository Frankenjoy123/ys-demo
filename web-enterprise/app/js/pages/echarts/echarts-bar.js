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

    $scope.monDays = [];
    $scope.currDay = getCurrDay();

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

    $scope.getData = function (data) {
      echartsBarService.getQRCode(getDateStr(data), getQRCode, function () {
        $scope.utils.alert('info', getDateStr(data) + '该日数据不存在');

        var data1 = {};
        data1.data = [];
        getQRCode(data1);
      });
    };

    function getDateStr(day) {

      var date = new Date();
      var xYear = date.getYear();
      xYear = xYear + 1900;

      var xMonth = date.getMonth() + 1;
      if (xMonth < 10) {
        xMonth = "0" + xMonth;
      }

      var xDay = getCurrDay();
      if (day != undefined) {
        xDay = day;
      }

      return xYear + xMonth + xDay;
    };

    function getDays() {
      var date = new Date();
      var y = date.getFullYear();
      var m = date.getMonth() + 1;
      if (m == 2) {
        return y % 4 == 0 ? 29 : 28;
      } else if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
        return 31;
      } else {
        return 30;
      }
    };

    function getCurrDay() {
      var date = new Date();
      var d = date.getDate();
      if (d < 10) {
        d = "0" + d;
      }
      return d;
    };

    for (var i = 1; i <= getDays(); i++) {

      var day = 0;

      if (i < 10)
        day = '0' + i;
      else
        day = i;

      $scope.monDays.push(day);
    }

    echartsBarService.getQRCode(getDateStr(), getQRCode, function () {
      $scope.utils.alert('info', getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      getQRCode(data);
    });
  }]);
})();