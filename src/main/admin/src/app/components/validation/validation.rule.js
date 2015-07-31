'use strict';

angular.module('validation.rule', ['validation'])
    .config(function ($validationProvider) {

        var expression = {
            required: function (value) {
                return !!value;
            },
            url: /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/,
            email: /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/,
            number: /^\d+$/,
            decimal:/^\d+(\.\d+)?$/,
            minlength: function (value, scope, element, attrs) {
                console.log(attrs.minLength)
                return value.length >= attrs.minLength;
            },
            maxlength: function (value, scope, element, attrs) {
                return value.length <= attrs.maxLength;
            },
            password: /^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).*$/,
            matching: function (value, scope, element, attrs) {
                return value === attrs.validatorPassword;
            },
            daterange: function (value, scope, element, attrs) {
                if (scope.$parent.dateBefore) {
                    return moment(value).isBefore(moment(scope.$parent.dateBefore));
                }
                if (scope.$parent.dateAfter) {
                    return moment(value).isAfter(moment(scope.$parent.dateAfter));
                }
                return true;
            },
            length: function (value, scope, element, attrs) {
                if (value) {
                    return value.length == attrs.length;
                }
                return false;

            }
        };

        var defaultMsg = {
            required: {
                error: '必填项',
                success: '<i class="fa fa-check"></i>'
            },
            url: {
                error: 'Url格式不正确',
                success: '<i class="fa fa-check"></i>'
            },
            email: {
                error: 'Email格式不正确',
                success: '<i class="fa fa-check"></i>'
            },
            number: {
                error: '请输入数字',
                success: '<i class="fa fa-check"></i>'
            },
            decimal: {
                error: '请输入数字或小数',
                success: '<i class="fa fa-check"></i>'
            },
            minlength: {
                error: '最小长度不匹配',
                success: '<i class="fa fa-check"></i>'
            },
            maxlength: {
                error: '最大长度不匹配',
                success: '<i class="fa fa-check"></i>'
            },
            password: {
                error: '必须包含至少一个大写字母,一个小写字母,一个数字',
                success: '<i class="fa fa-check"></i>'
            },
            matching: {
                error: '密码不一致',
                success: '<i class="fa fa-check"></i>'
            },
            daterange: {
                error: '日期区间错误',
                success: '<i class="fa fa-check"></i>'
            },
            length: {
                error: '长度不匹配',
                success: '<i class="fa fa-check"></i>'
            }
        };

        $validationProvider.setExpression(expression).setDefaultMsg(defaultMsg);

        $validationProvider.showSuccessMessage = false;

        $validationProvider.setErrorHTML(function (msg) {
            return '<p class="validation validation-invalid">' + msg + '</p>';
        });

        $validationProvider.setSuccessHTML(function (msg) {
            return '<p class="validation validation-valid">' + msg + '</p>';
        });

    })
;