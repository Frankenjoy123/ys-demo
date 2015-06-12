(function () {
    var app = angular.module('root');

    app.factory("stackedBarService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);

                return this;
            }
        };
    }]);

    app.controller("stackedBarCtrl", ["$scope", "stackedBarService", "$timeout", function ($scope, stackedBarService, $timeout) {

        $timeout(function () {

            var stackedBar = echarts.init($('#stackedBar')[0]);

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
                    data: ['儿童感冒咳嗽理疗贴', '蜜炼琵琶膏', '生理性海水鼻腔喷雾器', '小儿护脑退热贴', '远红外风湿骨痛贴']
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
                        data: ['01月', '02月', '03月', '04月', '05月']
                    }
                ],
                series: [
                    {
                        name: '儿童感冒咳嗽理疗贴',
                        type: 'bar',
                        stack: '总量',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [320, 302, 301, 334, 390, 330, 320]
                    },
                    {
                        name: '蜜炼琵琶膏',
                        type: 'bar',
                        stack: '总量',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [120, 132, 101, 134, 90, 230, 210]
                    },
                    {
                        name: '生理性海水鼻腔喷雾器',
                        type: 'bar',
                        stack: '总量',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [220, 182, 191, 234, 290, 330, 310]
                    },
                    {
                        name: '小儿护脑退热贴',
                        type: 'bar',
                        stack: '总量',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [150, 212, 201, 154, 190, 330, 410]
                    },
                    {
                        name: '远红外风湿骨痛贴',
                        type: 'bar',
                        stack: '总量',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [820, 832, 901, 934, 1290, 1330, 1320]
                    }
                ]
            };

            stackedBar.setOption(option);

        }, 0);

    }]);
})();