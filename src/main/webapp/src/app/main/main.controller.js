'use strict';

angular.module('webapp')
    .controller('MainCtrl', function ($scope, $rootScope, $timeout, $http, PageContext) {

        $scope.goUp = function () {
            $('html,body').animate({ scrollTop: 0 }, 'normal');
        };

        $rootScope.showSubMenu = true;

        $rootScope.PageContext = PageContext;

        $rootScope.$on('$stateChangeSuccess', function (event, to) {
            if (['product', 'product.detail'].indexOf(to.name) >= 0) {
                $rootScope.showSubMenu = false;
                $timeout(function () {
                    $rootScope.showSubMenu = true;
                }, 1000);
            }
        });

        $http({
            url: PageContext.path + '/product.do',
            method: 'get'
        }).success(function (response) {
            $rootScope.products = response.productList;
        });
    });
