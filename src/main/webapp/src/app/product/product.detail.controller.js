'use strict';

angular.module('webapp')
    .controller('ProductDetailCtrl', function ($scope, $timeout, $state) {

        $scope.state = $state;

        $scope.$watch('products', function (value) {
            if (value) {
                _.forEach(value, function (product) {
                    if (product.id == $state.params.id) {
                        $scope.product = product;
                        return false;
                    }
                });
            }
        });
    });
