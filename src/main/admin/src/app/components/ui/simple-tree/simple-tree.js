'use strict';

angular.module('unicorn.ui')

    // todo :exceed
    .directive('staticTree', function () {
        return {
            replace: false,
            scope: {
                ngModel: '=',
                config: '=staticTree',
                selectedNode: '='
            },
            templateUrl: 'app/components/ui/simple-tree/static/simple-tree-tpl.html',
            link: function ($scope, elenemt, attrs) {

                var config = $scope.config || {};
                var defaultRender = function (node, collapsed) {
                    var iconHtml = '';
                    if (node.icon) {
                        var icon = node.icon;
                        if (icon == 'fa-folder-o') {
                            icon = collapsed ? icon : 'fa-folder-open-o';
                        }
                        iconHtml = '<i class="fa ' + icon + '"></i>&nbsp;'
                    }
                    return iconHtml + ((node.title || node.name) || '');
                };

                if (config.nodeRender) {
                    $scope.nodeRender = config.nodeRender;
                } else {
                    $scope.nodeRender = defaultRender;
                }

                if (config.selectable === false) {
                    $scope.selectNode = function (node) {
                    };
                } else {
                    $scope.selectNode = function (node) {
                        $scope.selectedNode = node;
                    };
                }

                if (config.treeHandleTemplate) {
                    $scope.treeHandleTemplate = config.treeHandleTemplate;
                } else {
                    $scope.treeHandleTemplate = 'app/components/ui/simple-tree/static/simple-tree-handle-content-tpl.html';
                }

                $scope.checkDisabled = function (node) {
                    if (!config.disabledNode || _.isEmpty(config.disabledNode)) {
                        return;
                    }
                    return _.pluck(config.disabledNode, '$$hashKey').indexOf(node.$$hashKey) >= 0;
                };
            }
        };
    })

    .directive('simpleTree', function () {
        return {
            replace: false,
            scope: {
                simpleTree: '=',
                selectedNode: '='
            },
            templateUrl: 'app/components/ui/simple-tree/simple-tree-tpl.html',

            link: function ($scope, elenemt, attrs) {

                $scope.items = [];
                var simpleTree = $scope.simpleTree || {};

                var options = simpleTree.options;

                var defaultNodeRender = function (node, collapsed) {
                    var iconHtml = '';
                    if (options.defaultIcon) {
                        iconHtml = '<i class="fa ' + options.defaultIcon + '"></i>&nbsp;'
                    }
                    if (node.icon) {
                        var icon = node.icon;
                        if (icon == 'fa-folder-o') {
                            icon = collapsed ? icon : 'fa-folder-open-o';
                        }
                        iconHtml = '<i class="fa ' + icon + '"></i>&nbsp;'
                    }
                    return iconHtml + ((node.title || node.name) || '');
                };

                if (options.nodeRender) {
                    $scope.nodeRender = options.nodeRender;
                } else {
                    $scope.nodeRender = defaultNodeRender;
                }

                if (options.selectable === false) {
                    $scope.selectNode = function (node) {
                    };
                } else {
                    $scope.selectNode = function (node) {
                        $scope.selectedNode = node;
                    };
                }

                if (options.treeHandleTemplate) {
                    $scope.treeHandleTemplate = options.treeHandleTemplate;
                } else {
                    $scope.treeHandleTemplate = 'app/components/ui/simple-tree/simple-tree-handle-content-tpl.html';
                }

                $scope.checkDisabled = function (node) {
                    if (!options.disabledNode || _.isEmpty(options.disabledNode)) {
                        return;
                    }
                    return _.pluck(options.disabledNode, '$$hashKey').indexOf(node.$$hashKey) >= 0;
                };

                $scope.buildHandleClass = function (node, collapsed) {
                    if (node.leaf == 1) {
                        return '';
                    } else {
                        if (node.items === undefined || node.items.length == 0) {
                            return 'fa fa-plus-square-o';
                        } else {
                            return collapsed ? 'fa fa-plus-square-o' : 'fa fa-minus-square-o';
                        }
                    }
                };

                $scope.toggleNode = function (scope, node) {
                    if (node.items === undefined || node.items.length == 0) {
                        simpleTree.load(node.id);
                    } else {
                        scope.toggle();
                    }
                };

                var findItem = function (id) {
                    var fn = function (items) {
                        var result = null;
                        _.forEach(items, function (node) {
                            if (id == node.id) {
                                result = node;
                                return false;
                            }
                            if (node.items && node.items.length > 0) {
                                result = fn(node.items);
                            }
                        });
                        return result;
                    };
                    return fn($scope.items);
                };

                $scope.$watch(function () {
                    return simpleTree.$nodeCache.id
                }, function (value) {
                    if (value == undefined || angular.lowercase(value) == 'root') {
                        $scope.items = simpleTree.$nodeCache.items
                    } else {
                        var parent = findItem(value);
                        parent.items = simpleTree.$nodeCache.items;
                        _.forEach(parent.items, function (item) {
                            item.parent = parent;
                            item.$snapshot = true;
                        });
                        parent.leaf = parent.items.length == 0;
                    }
                });

                simpleTree.load('Root');
            }
        };
    })

    .provider('SimpleTree', function () {
        this.$get = function ($rootScope) {
            return  function SimpleTree(fetchFn, options) {
                if (!(this instanceof SimpleTree)) {
                    return new SimpleTree(fetchFn, options);
                }

                var me = this;
                options = options || {};

                var self = {
                    load: function (id) {
                        return me.promise = fetchFn({id: id}).then(function (response) {
                            me.$nodeCache = {
                                id: id,
                                items: response.data
                            };
                        })['finally'](function () {
                            me.loading = false;
                        });
                    },
                    $nodeCache: {},
                    options: options
                };

                _.extend(this, self);
            };
        };
    })
;
