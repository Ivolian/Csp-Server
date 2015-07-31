'use strict';

angular.module('unicorn.ui')


    .directive('simpleTableTools', function () {
        return {
            templateUrl: 'components/ui/template/simple-table-tools.html',
            restrict: 'EA',
            scope: true,
            replace: true
        }
    })

;


