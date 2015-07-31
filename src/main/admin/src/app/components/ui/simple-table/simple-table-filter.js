'use strict';

angular.module('unicorn.ui')


    .directive('simpleTableFilter', function () {
        return {
            templateUrl: 'components/ui/template/simple-table-filter.html',
            restrict: 'EA',
            scope: {
                filterList: '=',
                grid: '='
            },
            replace: true
        }
    })

;


