'use strict';

angular.module('app')

    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/statistics', '/statistics/courtData');

        $stateProvider.state('statistics', {
            parent: 'root',
            url: '/statistics',
            displayName: '统计管理',
            template: '<div ui-view></div>'
        });

    })

;
