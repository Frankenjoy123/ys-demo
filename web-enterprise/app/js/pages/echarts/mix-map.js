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

    var dataTable = $scope.dataTable = new $scope.utils.DateHelp(getData);

    mixMapService.getLocationCount(dataTable.selTimes, getLocationCount, function () {
      //$scope.utils.alert('info', date.getDateStr() + '该日数据不存在');
      var data = {};
      data.data = [];
      data.dimensions = {};
      data.dimensions.values = [];
      getLocationCount(data);
    });

    function getData() {
      mixMapService.getLocationCount(dataTable.selTimes, getLocationCount, function () {
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
        dataShow.push({name: data.dimensions.values[i].replace('市','').replace('省','').replace('自治区',''), value: data.data[i]});
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