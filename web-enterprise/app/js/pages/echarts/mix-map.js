(function () {
    var app = angular.module('root');

    app.factory("mixMapService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);

                return this;
            }
        };
    }]);

    app.controller("mixMapCtrl", ["$scope", "mixMapService", "$timeout", function ($scope, mixMapService, $timeout) {

        $timeout(function () {

            var mixMap = echarts.init($('#mixMap')[0]);

            var option = {
                title : {
                    text: '产品地域统计',
                    subtext: '数据来自云溯科技'
                },
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    x:'right',
                    selectedMode:false,
                    data:['北京','上海','广东']
                },
                dataRange: {
                    orient: 'horizontal',
                    min: 0,
                    max: 55000,
                    text:['高','低'],           // 文本，默认为数值文本
                    splitNumber:0
                },
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    x:'right',
                    y:'center',
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false}
                    }
                },
                series : [
                    {
                        name: '产品地域统计',
                        type: 'map',
                        mapType: 'china',
                        mapLocation: {
                            x: 'left'
                        },
                        selectedMode : 'multiple',
                        itemStyle:{
                            normal:{label:{show:true}},
                            emphasis:{label:{show:true}}
                        },
                        data:[
                            {name:'西藏', value:605.83},
                            {name:'青海', value:1670.44},
                            {name:'宁夏', value:2102.21},
                            {name:'海南', value:2522.66},
                            {name:'甘肃', value:5020.37},
                            {name:'贵州', value:5701.84},
                            {name:'新疆', value:6610.05},
                            {name:'云南', value:8893.12},
                            {name:'重庆', value:10011.37},
                            {name:'吉林', value:10568.83},
                            {name:'山西', value:11237.55},
                            {name:'天津', value:11307.28},
                            {name:'江西', value:11702.82},
                            {name:'广西', value:11720.87},
                            {name:'陕西', value:12512.3},
                            {name:'黑龙江', value:12582},
                            {name:'内蒙古', value:14359.88},
                            {name:'安徽', value:15300.65},
                            {name:'北京', value:16251.93, selected:true},
                            {name:'福建', value:17560.18},
                            {name:'上海', value:19195.69, selected:true},
                            {name:'湖北', value:19632.26},
                            {name:'湖南', value:19669.56},
                            {name:'四川', value:21026.68},
                            {name:'辽宁', value:22226.7},
                            {name:'河北', value:24515.76},
                            {name:'河南', value:26931.03},
                            {name:'浙江', value:32318.85},
                            {name:'山东', value:45361.85},
                            {name:'江苏', value:49110.27},
                            {name:'广东', value:53210.28, selected:true}
                        ]
                    },
                    {
                        name:'产品地域统计',
                        type:'pie',
                        roseType : 'area',
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        center: [$('#mixMap')[0].offsetWidth - 250, 225],
                        radius: [30, 120],
                        data:[
                            {name: '北京', value: 16251.93},
                            {name: '上海', value: 19195.69},
                            {name: '广东', value: 53210.28}
                        ]
                    }
                ],
                animation: false
            };

            var ecConfig = echarts.config;
            mixMap.on(ecConfig.EVENT.MAP_SELECTED, function (param){
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

        }, 0);

    }]);
})();