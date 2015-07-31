'use strict';

angular.module('unicorn.ui')

    .directive('datePicker', function (uuid4) {
        return {
            templateUrl: 'components/ui/template/date-picker.html',
            restrict: 'EA',
            replace: true,
            scope: {
                ngModel: '=',
                ngValidator: '@',
                dateBefore: '=',
                dateAfter: '='
            },
            compile: function (elenemt, attrs) {

                var format = attrs.format || 'YYYY-MM-DD';
                var minView = attrs.minView;
                if (minView === undefined) {
                    if (format == 'YYYY-MM-DD HH:mm') {
                        minView = 'minute'
                    } else if (format == 'YYYY-MM-DD HH') {
                        minView = 'hour'
                    } else if (format == 'YYYY-MM-DD') {
                        minView = 'day'
                    }
                }

                var gid = uuid4.generate();
                var datetimePickerConfig = {
                    dropdownSelector: '#' + gid,
                    minView: minView
                };

                if (attrs.ngValidator !== undefined) {
                    elenemt.find('input[date-time-input]').attr('validator', '{{ngValidator}}')
                        .attr('daterange-error-message', attrs.daterangeErrorMessage);
                }
                elenemt.find('a.dropdown-toggle').attr('id', gid);
                elenemt.find('input[date-time-input]')
                    .attr('placeholder', attrs.placeholder)
                    .attr('date-time-input', format)
                    .attr('name', attrs.ngModel);
                elenemt.find('datetimepicker').attr('datetimepicker-config', angular.toJson(datetimePickerConfig));

                return function (scope, element, attrs) {
                    scope.dropdownMenuRight = !_.isEmpty(attrs.$attr.dropdownMenuRight);
                }
            }
        }
    })

;