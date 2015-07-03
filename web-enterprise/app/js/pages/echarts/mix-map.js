(function () {
  var app = angular.module('root');

  app.factory("mixMapService", ["$http", function ($http) {
    return {
      getLocationCount: function (peirod, fnSuccess, fnError) {
        var url = '/api/report/myorganization/location_scan_count/' + peirod;
        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("mixMapCtrl", ["$scope", "mixMapService", "$timeout", function ($scope, mixMapService, $timeout) {

    var date = new Date();
    $scope.days = [];
    $scope.mons = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
    $scope.monsShow = {
      '01': '一月',
      '02': '二月',
      '03': '三月',
      '04': '四月',
      '05': '五月',
      '06': '六月',
      '07': '七月',
      '08': '八月',
      '09': '九月',
      '10': '十月',
      '11': '十一月',
      '12': '十二月'
    };
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

    mixMapService.getLocationCount(date.getDateStr(), getLocationCount, function () {
      //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      data.dimensions = {};
      data.dimensions.values = [];
      getLocationCount(data);
    });

    function getData(year, mon, day) {
      mixMapService.getLocationCount(date.getDateStr(year, mon, day), getLocationCount, function () {
        //$scope.utils.alert('info', date.getDateStr(year, mon, day) + '该日数据不存在');
        var data1 = {};
        data1.data = [];
        data1.dimensions = {};
        data1.dimensions.values = [];
        getLocationCount(data1);
      });
    };

    function getLocationCount(data) {

      var mixMap = echarts.init($('#mixMap')[0]);

      var dataShow = [];
      for (var i = 0; i < data.data.length; i++) {
        dataShow.push({name: data.dimensions.values[i], value: data.data[i]});
      }

      var option = {
        title: {
          text: '产品地域统计'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          x: 'right',
          selectedMode: false,
          data: data.dimensions.values
        },
        dataRange: {
          orient: 'horizontal',
          min: 0,
          max: 55000,
          text: ['高', '低'],           // 文本，默认为数值文本
          splitNumber: 0
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          x: 'right',
          y: 'center',
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false}
          }
        },
        series: [
          {
            name: '产品地域统计',
            type: 'map',
            mapType: 'china',
            mapLocation: {
              x: 'left'
            },
            selectedMode: 'multiple',
            itemStyle: {
              normal: {label: {show: true}},
              emphasis: {label: {show: true}}
            },
            data: dataShow
          },
          {
            name: '产品地域统计',
            type: 'pie',
            roseType: 'area',
            tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            center: [$('#mixMap')[0].offsetWidth - 250, 225],
            radius: [30, 120],
            data: dataShow
          }
        ],
        animation: false
      };

      var ecConfig = echarts.config;
      mixMap.on(ecConfig.EVENT.MAP_SELECTED, function (param) {
        var selected = param.selected;
        var mapSeries = option.series[0];
        var data = [];
        var legendData = [];
        var name;
        for (var p = 0, len = mapSeries.data.length; p < len; p++) {
          name = mapSeries.data[p].name;
          //mapSeries.data[p].selected = selected[name];
          if (selected[name]) {
            data.push({
              name: name,
              value: mapSeries.data[p].value
            });
            legendData.push(name);
          }
        }
        option.legend.data = legendData;
        option.series[1].data = data;
        mixMap.setOption(option, true);
      })

      mixMap.setOption(option);

    };

  }]);
})();