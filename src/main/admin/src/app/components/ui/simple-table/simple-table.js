'use strict';

angular.module('unicorn.ui')

    .constant('paginationConfig', {
        itemsPerPage: 10,
        boundaryLinks: true,
        directionLinks: true,
        firstText: '首页',
        previousText: '上一页',
        nextText: '下一页',
        lastText: '尾页',
        rotate: false,
        maxSize: 7
    })

    .run(function ($templateCache) {
        $templateCache.put("template/pagination/pagination.html",
                "<div class=\"pagination-wrapper\">\n" +
                "<ul class=\"pagination\">\n" +
                "  <li ng-if=\"boundaryLinks\" ng-class=\"{disabled: noPrevious()}\"><a href ng-click=\"selectPage(1)\">{{getText('first')}}</a></li>\n" +
                "  <li ng-if=\"directionLinks\" ng-class=\"{disabled: noPrevious()}\"><a href ng-click=\"selectPage(page - 1)\">{{getText('previous')}}</a></li>\n" +
                "  <li ng-repeat=\"page in pages track by $index\" ng-class=\"{active: page.active}\"><a href ng-click=\"selectPage(page.number)\">{{page.text}}</a></li>\n" +
                "  <li ng-if=\"directionLinks\" ng-class=\"{disabled: noNext()}\"><a href ng-click=\"selectPage(page + 1)\">{{getText('next')}}</a></li>\n" +
                "  <li ng-if=\"boundaryLinks\" ng-class=\"{disabled: noNext()}\"><a href ng-click=\"selectPage(totalPages)\">{{getText('last')}}</a></li>\n" +
                "</ul>\n" +
                "<div class=\"pagination-addition\">\n" +
                "共 {{totalPages}} 页， {{totalItems}} 条数据\n" +
                "</div>\n" +
                "</div>"
        );
    })

    .directive('recordNo', function ($timeout) {
        return {
            require: ['?^simpleTable'],
            template: '<span>{{recordNo}}</span>',
            link: function (scope, el, attr, controllers) {
                var simpleTableCtrl = controllers[0];
                var grid = simpleTableCtrl.scope.grid;
                scope.recordNo = grid.pageSize * (grid.currentPage - 1) + scope.$index + 1
            }
        }
    })

    .directive('simpleTable', function ($timeout) {

        return {
            controller: function ($scope) {
                this.scope = $scope;
            },
            link: function (scope, element, attrs) {
                var queryFlag = true;
                scope.$watch('grid.currentPage', function () {
                    if (scope.grid) {
                        $timeout(function () {
                            if (queryFlag) {
                                scope.grid.query();
                                queryFlag = false;
                            } else {
                                scope.grid.fetchData(scope.grid.currentPage);
                            }
                        });
                    }
                });

                scope.$watch('grid.loading', function () {
                    if (scope.grid) {
                        if (scope.grid.loading === true) {
                            element.find('tr:last').not('.loading-text')
                                .after('<tr class="loading-text"><td colspan="100"><i class="fa fa-spin fa-fw fa-spinner"></i>&nbsp;正在加载，请稍候...</td></tr>');
                            element.find('tr.empty-text').remove();
                        } else {
                            element.find('tr.loading-text').remove();
                            if (_.isEmpty(scope.grid.items)) {
                                element.find('tr:last').not('.empty-text').after('<tr class="empty-text"><td colspan="100">没有数据！</td></tr>');
                            } else {
                                element.find('tr.empty-text').remove();
                            }
                        }
                    }
                });
            }
        }
    })

    .provider('SimpleTable', function (paginationConfig) {
        this.$get = function ($rootScope) {
            return  function SimpleGrid(fetchFn, options) {
                if (!(this instanceof SimpleGrid)) {
                    return new SimpleGrid(fetchFn, options);
                }

                var me = this;
                options = options || {};

                // 根据窗口高度决定每页记录数
                var pageSize = $rootScope.windowHeight && $rootScope.windowHeight > 800 ? 15 : 10;

                var self = {
                    items: [],
                    queryInfo: {},
                    params: options.params || {},
                    currentPage: 1,
                    loading: false,
                    pageSize: options.pageSize || pageSize || paginationConfig.itemsPerPage,

                    fetchData: function (page) {
                        page = page || me.currentPage;
                        var params = {};
                        _.forIn(me.params, function(value, key){
                            params['search_' + key] = value;
                        });
                        _.extend(params || {}, {
                            pageNo: page,
                            pageSize: me.pageSize
                        });
                        me.items = [];
                        me.loading = true;
                        fetchFn(params).then(function (result) {
                            me.items = result;
                            me.total = result.meta.totalElements;
                        })['finally'](function () {
                            me.loading = false;
                        });
                    },
                    refresh: function () {
                        this.fetchData();
                    },
                    query: function (params) {
                        var queryInfo = {};
                        for (var key in me.queryInfo) {
                            var value = me.queryInfo[key], _value;
                            if (moment.isMoment(value)) {
                                _value = value.format('YYYY-MM-DD HH:mm:ss');
                            } else if (_.isDate(value)) {
                                _value = moment(value).format('YYYY-MM-DD HH:mm:ss');
                            } else if (_.isArray(value)) {
                                _value = _.difference(value, ['', null, undefined]);
                            } else {
                                if (value.getQueryValue) {
                                    _value = value.getQueryValue();
                                } else {
                                    _value = value;
                                }
                            }
                            queryInfo[key] = _value;
                        }
                        me.currentPage = 1;
                        _.extend(this.params, _.extend(queryInfo, params || {}));
                        this.fetchData(1);
                    }
                };
                _.extend(this, self);
            };
        };
    })
;


