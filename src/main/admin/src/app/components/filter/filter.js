'use strict';

angular.module('unicorn.filter', ['unicorn.common'])
    .filter('$date', function (DateFormat) {
        return function (input, format) {

            if (input) {
                format = format || DateFormat.DAY;
                return moment(input).format(format);
            }
        };
    })

    .filter('emptyText', function () {
        return function (input) {
            if (input) {
                return input;
            }
            return '/'
        };
    })
    .filter('yon', function () {
        return function (input) {
            if (input) {
                return '是';
            }
            return '否'
        };
    })
;