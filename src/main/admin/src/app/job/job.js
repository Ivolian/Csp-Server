'use strict';

angular.module('app')

    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/job', '/job/user');

        $stateProvider.state('job', {
            parent: 'root',
            url: '/job',
            displayName: '数据管理',
            template: '<div ui-view></div>'
        });

    })

;
