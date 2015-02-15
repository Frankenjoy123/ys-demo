if (!$.lib) {
    $.lib = {};
}
if (!$.lib.custom) {
    $.lib.custom = {};
}
(function () {
    function fn(callback, wrap, wrapWidth, wrapHeight) {
        require.config({
            packages: [
                {
                    name: 'echarts',
                    location: '../libs/echarts',
                    main: 'echarts'
                }
            ]
        });
        require([
            'echarts',
            'echarts/chart/bar',
            'echarts/chart/map',
            'echarts/chart/k',
            'echarts/chart/gauge'
        ], function (ec) {
            $.lib.custom.setEChartWindow(wrap, wrapWidth, wrapHeight);
            var myChart = ec.init(wrap.get(0));
            callback && callback(myChart);
        });
    }
    $.lib.custom.requireEcharts = fn;
})();