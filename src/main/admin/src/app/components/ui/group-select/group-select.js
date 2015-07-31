'use strict';

angular.module('unicorn.ui')

    .directive('groupSelect', function ($http) {

        var urls = {
            'Region': PageContext.path + '/api/v1/region/group'
        };

        return {
            restrict: 'EA',
            replace: true,
            templateUrl: 'app/components/ui/template/group-select.html',
            scope: {
                ngModel: '=',
                ngValidator: '@'
            },

            compile: function (elenemt, attrs) {
                var input = elenemt.find('input');
                input.attr('name', attrs.ngModel);
                if (attrs.ngValidator !== undefined) {
                    input.attr('validator', '{{ngValidator}}');
                }
                input.attr('placeholder', attrs.placeholder);

                return function (scope, elenemt, attrs) {

                    var config = attrs['groupSelect'];

                    scope.select = function (item) {
                        scope.ngModel = _.clone(item);
                        elenemt.find('a.dropdown-toggle').dropdown('toggle');
                    };

                    scope.deselect = function () {

                        scope.ngModel = undefined;
                        elenemt.find('a.dropdown-toggle').dropdown('toggle');
                    };

                    scope.showClear = attrs.showClear !== 'false';

                    $http({
                        url: urls[config],
                        method: 'GET'
                    }).then(function (response) {
                        scope.groups = response.data;
                    });
                }
            }
        };
    })
;