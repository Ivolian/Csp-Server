'use strict';

angular.module('webapp', ['ngSanitize', 'ui.router', 'ui.bootstrap', 'focusOn'])
    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $urlRouterProvider.when('/', '/home');

        $urlRouterProvider.otherwise('/home');

        $stateProvider
            .state('root', {
                url: '/',
                views: {
                    '': {
                        templateUrl: 'app/main/main.html',
                        controller: 'MainCtrl'
                    },
                    'navbar@root': {
                        templateUrl: 'components/navbar/navbar.html',
                        controller: 'NavbarCtrl'
                    },
                    'contact@root': {
                        templateUrl: 'components/contact/contact.html',
                        controller: 'ContactCtrl'
                    }
                }
            })

            .state('home', {
                url: 'home',
                parent: 'root',
                templateUrl: 'app/home/home.html',
                controller: 'HomeCtrl'
            })
            .state('news', {
                url: 'news/page/{pageNo}',
                parent: 'root',
                templateUrl: 'app/news/news.html',
                controller: 'NewsCtrl'
            })
            .state('news.detail', {
                url: 'news/{id}',
                parent: 'root',
                templateUrl: 'app/news/news.detail.html',
                controller: 'NewsDetailCtrl'
            })
            .state('support', {
                url: 'support',
                parent: 'root',
                templateUrl: 'app/support/support.html',
                controller: 'SupportCtrl'
            })
            .state('product', {
                url: 'product',
                abstract: true,
                parent: 'root',
                templateUrl: 'app/product/product.html',
                controller: 'ProductCtrl'
            })
            .state('product.detail', {
                url: '/{id}',
                templateUrl: 'app/product/product.detail.html',
                controller: 'ProductDetailCtrl'
            })
            .state('case', {
                url: 'case',
                parent: 'root',
                templateUrl: 'app/case/case.html',
                controller: 'CaseCtrl'
            })
            .state('software', {
                url: 'software',
                parent: 'root',
                templateUrl: 'app/software/software.html',
                controller: 'AboutCtrl'
            })
            .state('about', {
                url: 'about',
                parent: 'root',
                templateUrl: 'app/about/about.html',
                controller: 'AboutCtrl'
            })
            .state('culture', {
                url: 'culture',
                parent: 'root',
                templateUrl: 'app/culture/culture.html',
                controller: 'CultureCtrl'
            })
            .state('culture.detail', {
                url: 'culture/{id}',
                parent: 'root',
                templateUrl: 'app/culture/culture.detail.html',
                controller: 'CultureDetailCtrl'
            })
            .state('job', {
                url: 'job',
                parent: 'root',
                templateUrl: 'app/job/job.html',
                controller: 'JobCtrl'
            })
            .state('job.detail', {
                url: 'job/{id}',
                parent: 'root',
                templateUrl: 'app/job/job.detail.html',
                controller: 'JobDetailCtrl'
            })
            .state('login', {
                url: 'login',
                parent: 'root',
                templateUrl: 'app/login/login.html',
                controller: 'LoginCtrl'
            })
            .state('loginFail', {
                url: 'loginFail',
                parent: 'root',
                templateUrl: 'app/login/login.fail.html',
                controller: 'LoginCtrl'
            })
        ;

        $locationProvider.hashPrefix('!');
//        $locationProvider.html5Mode(true);
    })

    .value('PageContext', PageContext)
;
