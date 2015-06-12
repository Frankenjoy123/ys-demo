(function () {
    var app = angular.module('root');

    app.factory("echartsBarService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);

                return this;
            }
        };
    }]);

    app.controller("echartsBarCtrl", ["$scope", "echartsBarService", "$timeout", function ($scope, echartsBarService, $timeout) {

        $timeout(function () {

            var echartBar = echarts.init($('#echartBar')[0]);

            var option = {
                title: {
                    text: '贴码量统计'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['贴码量统计']
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
                        data: ['小儿护脑退热贴', '成人广东护脑退热贴', '大闸蟹', '菲律宾凤梨', '生理性海水鼻腔喷雾器', '小儿腹泻理疗贴', '依云矿泉水', '远红外风湿骨痛贴']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '贴码量统计',
                        type: 'bar',
                        data: [56, 6, 35, 2, 7, 24, 4, 9]
                    }
                ]
            };

            echartBar.setOption(option);

        }, 0);

    }]);
})();