(function () {
    var app = angular.module('root');

    app.factory("pieMapService", ["$http", function ($http) {
        return {
            getDevices: function (dataTable, orgId, fnSuccess) {
                var url = '/api/device/org/' + orgId + '?';
                url += dataTable.toString();
                $http.get(url).success(fnSuccess);

                return this;
            }
        };
    }]);

    app.controller("pieMapCtrl", ["$scope", "pieMapService", "$timeout", function ($scope, pieMapService, $timeout) {

        $timeout(function () {

            var pieMap = echarts.init($('#pieMap')[0]);

            var option = {
                title : {
                    text: '产品扫码统计',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 'left',
                    data:['小儿护脑退热贴','大闸蟹','菲律宾凤梨','蜜炼琵琶膏','远红外风湿骨痛贴']
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {
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
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                series : [
                    {
                        name:'扫码统计',
                        type:'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {value:335, name:'小儿护脑退热贴'},
                            {value:310, name:'大闸蟹'},
                            {value:234, name:'菲律宾凤梨'},
                            {value:135, name:'蜜炼琵琶膏'},
                            {value:1548, name:'远红外风湿骨痛贴'}
                        ]
                    }
                ]
            };

            pieMap.setOption(option);

        }, 0);

    }]);
})();