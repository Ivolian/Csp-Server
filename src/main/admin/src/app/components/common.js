'use strict';

angular.module('unicorn.common', [])
    .constant('DateFormat', {
        DAY: 'YYYY-MM-DD',
        HOUR: 'YYYY-MM-DD HH',
        MINUTE: 'YYYY-MM-DD HH:mm',
        SECOND: 'YYYY-MM-DD HH:mm:ss',
        TIMESTAMP: 'YYYY-MM-DD HH:mm:ss.SSS'
    })

    .directive('unicornCommon', function ($q, $http, $templateCache, $document, $compile, $timeout, $rootScope, toaster, ngDialog) {
        var $body = $document.find('body');
        var toaster_ = {
            success: function (message) {
                return toaster.pop('success', "信息", message);
            },
            info: function (message) {
                return toaster.pop('info', "信息", message);
            },
            warning: function (message) {
                return toaster.pop('warning', "警告", message);
            },
            error: function (message) {
                return toaster.pop('error', "错误", message);
            }
        };

        var openModal = function (templateUrl, config) {
            var defer = $q.defer();
            var scope = $rootScope.$new();
            var defaultConfig = {title: '', content: ''};
            if (_.isObject(config)) {
                _.extend(scope, defaultConfig, config)
            } else {
                _.extend(scope, defaultConfig, {title: config})
            }
            ngDialog.open({
                closeByDocument: false,
                templateUrl: templateUrl,
                scope: scope
            }).closePromise.then(function (result) {
                    if (result.value == '$closeButton' || result.value == undefined) {
                        defer.reject();
                    } else {
                        defer.resolve(result.value);
                    }
                });
            return defer.promise;
        };

        var dialog_ = {
            alert: function (arg0) {
                return openModal('app/components/ui/template/dialog/alert.html', arg0);
            },
            confirm: function (arg0) {
                return openModal('app/components/ui/template/dialog/confirm.html', arg0);
            },
            prompt: function (arg0) {
                return openModal('app/components/ui/template/dialog/prompt.html', arg0);
            },
            modal: function (templateUrl, arg0) {
                return openModal(templateUrl, arg0);
            }
        };
        window.Dialog = _.extend({
            confirmDelete: function () {
                return dialog_.confirm({
                    title: '确认删除？'
                });
            }
        }, dialog_);
        window.Toaster = toaster_;
        return {
            link: function (scope, element, attrs) {

            }
        }
    })

    .directive('tof', [function () {
        return {
            restrict: 'EA',
            template: '<i class="fa fa-lg fa-fw {{icon}}" ng-style="{color: color}"></i>',
            scope: {
                value: '=value'
            },
            link: function (scope) {
                scope.$watch('value', function () {
                    if (scope.value === true || scope.value == 1) {
                        scope.color = 'green';
                        scope.icon = 'fa-check';
                    } else {
                        scope.color = 'red';
                        scope.icon = 'fa-times';
                    }
                });
            }
        }
    }])

    .directive('stopPropagation', [function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                element.bind('click', function (event) {
                    event.stopPropagation()
                });
            }
        }
    }])

    .directive('panelFitHeight', [function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                var panelBody = element.find('.panel-body').css({overflow: 'auto', minHeight: 180});
                scope.$watch(function () {
                    return PageContext.panelHeight;
                }, function (value) {
                    panelBody.css({
                        height: value - 24
                    });
                });
            }
        }
    }])

    .directive('specialCode', [function () {
        return {
            restrict: 'EA',
            link: function (scope, element) {
                element.addClass("special-code");
            }
        }
    }])
;
