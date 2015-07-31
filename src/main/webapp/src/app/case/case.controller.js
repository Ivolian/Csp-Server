'use strict';

angular.module('webapp')
    .controller('CaseCtrl', function ($scope, $timeout, $interval) {

        var mapOption = {
            dataRange: {
                show: false,
                x: 'right',
                min: 1,
                max: 1,
                color: ['#00B0F0'],
                text: ['High'],           // 文本，默认为数值文本
                calculable: true
            },
            tooltip: {
                trigger: 'item',
                formatter: '{b}'
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data: ['审判', '执行', '信访', '办公']
            },
            series: [
                {
                    name: '审判',
                    type: 'map',
                    mapType: 'china',
                    selectedMode: 'single',
                    itemStyle: {
                        normal: {label: {show: true}},
                        emphasis: {label: {show: true}}
                    },
                    data: [
                        {name: '陕西', value: 1, selected: true},
                        {name: '重庆', value: 1},
                        {name: '贵州', value: 1}
                    ]
                },
                {
                    name: '执行',
                    type: 'map',
                    mapType: 'china',
                    selectedMode: 'single',
                    itemStyle: {
                        normal: {label: {show: true}},
                        emphasis: {label: {show: true}}
                    },
                    data: [
                        {name: '陕西', value: 1},
                        {name: '重庆', value: 1}
                    ]
                },
                {
                    name: '信访',
                    type: 'map',
                    mapType: 'china',
                    selectedMode: 'single',
                    itemStyle: {
                        normal: {label: {show: true}},
                        emphasis: {label: {show: true}}
                    },
                    data: [
                        {name: '北京', value: 1},
                        {name: '陕西', value: 1},
                        {name: '重庆', value: 1},
                        {name: '甘肃', value: 1},
                        {name: '贵州', value: 1}
                    ]
                },
                {
                    name: '办公',
                    type: 'map',
                    mapType: 'china',
                    selectedMode: 'single',
                    itemStyle: {
                        normal: {label: {show: true}},
                        emphasis: {label: {show: true}}
                    },
                    data: [
                        {name: '广东', value: 1}
                    ]
                }
            ]
        };

        var barData = [];
        for (var i = 0; i < 13; i++) {
            var data = [];
            for (var j = 0; j < 11; j++) {
                var value = 0;
                if (i == 0) {
                    value += _.random(200);
                } else {
                    var a = 1, b = _.random(5);
                    value = barData[i - 1][j];
                    if (value > 1500) {
                        a = _.sample([-1, 1]);
                        b = _.random(2) + 1;
                        value -= _.random(200);
                    }
                    value = value * (1 + (b / 10) * a);
                }

                data.push(value);
            }
            barData.push(data);
        }


        var barOption = {
            timeline: {
                data: [
                    '2002-01-01', '2003-01-01', '2004-01-01', '2005-01-01', '2006-01-01',
                    '2007-01-01', '2008-01-01', '2009-01-01', '2010-01-01', '2011-01-01',
                    '2012-01-01', '2013-01-01', '2014-01-01'
                ],
                label: {
                    formatter: function (s) {
                        return s.slice(0, 4);
                    }
                },
                autoPlay: true,
                playInterval: 1000
            },
            options: [
                {
                    title: {
                        'text': '历年产品市场占有率'
                    },
                    tooltip: {'trigger': 'axis'},
                    grid: {'y': 80, 'y2': 100},
                    xAxis: [
                        {
                            'type': 'category',
                            'axisLabel': {'interval': 0},
                            'data': ['审判', '执行', '信访', '办公', '绩效', '电子送达', '电子档案', '网上文书' , '司法统计', '一键搜', '数据中心']
                        }
                    ],
                    yAxis: [
                        {
                            'type': 'value',
                            'name': '',
                            'max': 3000
                        }
                    ],
                    series: [
                        {
                            'name': '审判',
                            'type': 'bar',
                            'data': barData[0]
                        }
                    ]
                }
            ]
        };

        _.forEach(barData, function (data, index) {
            if (index > 0) {
                barOption.options.push({
                    series: [
                        {
                            data: data
                        }
                    ]
                })
            }
        });

        var mapChart = echarts.init(document.getElementById('echart_case_map'));
        var barChart = echarts.init(document.getElementById('echart_case_bar'));

        $scope.selected = '陕西';

        $timeout(function () {
            mapChart.setOption(mapOption);
            barChart.setOption(barOption);
            mapChart.setTheme({
                textStyle: {
                    fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                }
            });
            barChart.setTheme({
                textStyle: {
                    fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                }
            });
            mapChart.on(echarts.config.EVENT.MAP_SELECTED, function (param) {
                $scope.selected = param.target;
                $scope.$apply();
            });
        }, 200);
    });
