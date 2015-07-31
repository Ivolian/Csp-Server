'use strict';

angular.module('unicorn.ui')
    .directive('crumb', function () {
        return {
            // Inherit parent scope
            scope: true,
            template: '{{ name }}',
            // Use the link function to do any special voodoo
            link: function (scope, element, attrs) {
                var getDisplayName = function () {

                    // Use displayName if provided else use default name property
                    return scope.state.displayName || scope.state.name;
                };

                scope.name = getDisplayName();
            }
        };
    })
    .directive('breadcrumbs', function () {
        return {
            replace: true,
            template: [
                '<ul class="breadcrumbs">',
                '<li ng-repeat="state in currentState.path|filter:filterState" ng-class="{first:$first}" >',
                '<a href="#{{ state.url.format(state.currentState.params) }}" crumb></a>',
                '</li>',
                '</ul>'
            ].join(''),
            controller: function ($scope) {
                $scope.filterState = function (state) {
                    return state.displayName != undefined;
                }
            }
        };
    })
;