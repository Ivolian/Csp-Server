'use strict';

angular.module('webapp')

    .filter('productClassificationResult', function () {
        return function (input) {
            return {1: '办案平台', 2: '政务平台', 3: '队伍平台', 4: '公开平台', 5: '数据平台', 6: '企业应用'}[input];
        }
    })

    .filter('productClassification', function () {
        return function (input, classification) {
            var result = [];
            if (classification == 6) {
                _.forEach(input, function (productClassification) {
                    if (productClassification.productClassification == 6) {
                        result.push(productClassification);
                    }
                });
            } else {
                _.forEach(input, function (productClassification) {
                    if (productClassification.productClassification != 6) {
                        result.push(productClassification);
                    }
                });
            }
            return result;
        }
    })

    .controller('ProductCtrl', function ($scope, $state) {

        if (_.isEmpty($state.params.id)) {
            $state.transitionTo('product.detail', {id: $scope.products[0].id});
        }
    });
