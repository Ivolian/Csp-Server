'use strict';

angular.module('app')

    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/qa', '/qa/question');

        $stateProvider.state('qa', {
            parent: 'root',
            url: '/qa',
            displayName: '问答管理',
            template: '<div ui-view></div>'
        });

    })

;
