'use strict';

angular.module('unicorn.ui')

    .directive('staticTree', function () {
        return {
            replace: false,
            scope: {
                ngModel: '=',
                config: '=staticTree',
                selectedNode: '='
            },
            templateUrl: 'app/components/ui/simple-tree/static-tree-tpl.html',
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
                    $scope.treeHandleTemplate = 'app/components/ui/simple-tree/static-tree-handle-content-tpl.html';
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
;
