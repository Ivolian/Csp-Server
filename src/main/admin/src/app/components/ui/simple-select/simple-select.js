'use strict';

angular.module('unicorn.ui')

    .directive('simpleSelect', function ($http, $timeout) {
        return {
            templateUrl: 'app/components/ui/template/simple-select.html',
            restrict: 'EA',
            replace: true,
            require: 'ngModel',
            scope: {
                ngModel: '=',
                options: '@',
                ngModelProperty: '@',
                ngValidator: '@'
            },
            compile: function (elenemt, attrs) {
                var input = elenemt.find('input');
                input.attr('name', attrs.ngModel);
                if (attrs.ngValidator !== undefined) {
                    input.attr('validator', '{{ngValidator}}');
                }
                input.attr('placeholder', attrs.placeholder);

                return function (scope, element, attrs, ngModelCtrl) {

                    var config = attrs['simpleSelect'];
                    var url = '', params = {};
                    if (config == 'Email') {
                        url = PageContext.path + '/api/v1/email/all';
                    }
                    if (config == 'Position') {
                        url = PageContext.path + '/api/v1/position/all';
                    }
                    if (config == 'Menu') {
                        url = PageContext.path + '/api/v1/menu/all';
                    }
                    if (config == 'Role') {
                        url = PageContext.path + '/api/v1/role/all';
                    }
                    if (config == 'District') {
                        url = PageContext.path + '/api/v1/district/all';
                    }

                    url = url || attrs.url;

                    scope.select = function (selectedItem) {
                        if (selectedItem) {
                            if (_.isEmpty(scope.ngModelProperty)) {
                                scope.ngModel = {
                                    id: selectedItem.id,
                                    name: selectedItem.name
                                };
                            } else {
                                scope.ngModel = selectedItem[scope.ngModelProperty];

                            }
                            scope.selectedItem = selectedItem;
                            elenemt.find('a.dropdown-toggle').dropdown('toggle');
                        }
                    };

                    scope.deselect = function () {
                        scope.ngModel = undefined;
                        scope.selectedItem = undefined;
                        elenemt.find('a.dropdown-toggle').dropdown('toggle');
                    };

                    scope.checkActive = function (item) {
                        if (_.isEmpty(scope.ngModelProperty)) {
                            return scope.ngModel && (item.id == scope.ngModel.id);
                        } else {
                            return item[scope.ngModelProperty] == scope.ngModel;
                        }
                    };

                    scope.checkShowClear = function () {
                        if (_.isEmpty(scope.ngModelProperty)) {
                            return scope.showClear && scope.ngModel && scope.ngModel.id;
                        } else {
                            return scope.showClear && scope.ngModel;
                        }
                    };

                    scope.showClear = attrs.showClear !== 'false';

                    scope.$watch('ngModel', function (ngModel) {
                        scope.checkSelect(ngModel);
                    });

                    scope.checkSelect = function (ngModel) {
                        if (ngModel) {
                            var obj;
                            if (_.isEmpty(scope.ngModelProperty)) {
                                obj = {
                                    id: ngModel.id
                                }
                            } else {
                                obj = {
                                };
                                obj[scope.ngModelProperty] = scope.ngModel;
                            }
                            scope.selectedItem = _.where(scope.items, obj)[0] || scope.ngModel;
                        } else {
                            scope.selectedItem = {};
                        }
                    };

                    $timeout(function () {
                        scope.$watch('options', function (value) {
                            if (value) {
                                scope.items = angular.fromJson(scope.options);
                                scope.checkSelect(scope.ngModel);
                            }
                        });
                    });


                    if (!_.isEmpty(url)) {
                        $http({
                            url: url,
                            method: 'GET',
                            params: params
                        }).then(function (response) {
                            scope.items = [];
                            _.forEach(response.data, function (item) {
                                scope.items.push({
                                    id: item.id,
                                    name: item.name
                                });

                                if (item.defaultValue == 1) {
                                    scope.ngModel = item;
                                }
                            });
                        });
                    }
                }
            }
        }
    })
;
