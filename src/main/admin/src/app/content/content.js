'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/content', '/content/news');

        $stateProvider.state('content', {
            parent: 'root',
            url: '/content',
            displayName: '内容管理',
            template: '<div ui-view></div>',
            controller: 'ContentCtrl'
        });
    })
    .controller('ContentCtrl', function ($scope, $state) {

    })
;
