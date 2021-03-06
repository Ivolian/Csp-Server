'use strict';

angular.module('unicorn.ui')

    .directive('treePicker', function (uuid4, $injector, $http, $timeout, SimpleTree) {
        return {
            templateUrl: 'app/components/ui/template/tree-picker.html',
            restrict: 'EA',
            replace: true,
            require: 'ngModel',
            scope: {
                ngModel: '=',
                ngValidator: '@',
                dropdownWidth: '@'
            },

            compile: function (elenemt, attrs) {
                var input = elenemt.find('input');
                input.attr('name', attrs.ngModel);
                if (attrs.ngValidator !== undefined) {
                    input.attr('validator', '{{ngValidator}}');
                }
                input.attr('placeholder', attrs.placeholder);

                return function (scope, elenemt, attrs, ngModelCtrl) {

                    var config = attrs['treePicker'];

                    if (config == 'Region') {
                        var Region = $injector.get('Region');
                        scope.tree = SimpleTree(Region.loadTree, {defaultIcon: 'fa-sitemap'});
                    }
                    if (config == 'Court') {
                        var Court = $injector.get('Court');
                        scope.tree = SimpleTree(Court.loadTree, {defaultIcon: 'fa-sitemap'});
                    }
                    if (config == 'SystemMenu') {
                        var SystemMenu = $injector.get('SystemMenu');
                        scope.tree = SimpleTree(SystemMenu.loadTree, {defaultIcon: 'fa-sitemap'});
                    }

                    scope.$watch('selectedNode', function (selectedNode) {
                        if (selectedNode) {
                            $timeout(function () {
                                scope.ngModel = {
                                    id: selectedNode.id,
                                    name: selectedNode.name,
                                    getQueryValue: function () {
                                        return this.id;
                                    },
                                    getAttribute: function (key) {
                                        return selectedNode[key];
                                    }
                                };
                            });
                            elenemt.find('a.dropdown-toggle').dropdown('toggle');
                        }
                    }, false);
                }
            }
        }
    })

;