'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/product', '/product/question');

        $stateProvider.state('product', {
            parent: 'root',
            url: '/product',
            displayName: '问答管理',
            template: '<div ui-view></div>'
        });


    })

;
