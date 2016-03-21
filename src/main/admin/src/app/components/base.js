'use strict';

angular.module('base', [
    'ngSanitize', 'ngAnimate', 'restangular'
    , 'ui.router', 'ui.bootstrap.tpls', 'ui.bootstrap.modal', 'ui.bootstrap.tabs', 'ui.bootstrap.datetimepicker', 'ui.bootstrap.pagination', 'ui.bootstrap.buttons', 'ui.bootstrap.progressbar', 'ui.bootstrap.datepicker', 'ui.dateTimeInput'
    , 'validation', 'validation.rule', 'angular-loading-bar', 'toaster', 'cgBusy', 'focusOn', 'ngDialog'
    , 'uuid4'  , 'angularFileUpload', 'ui.tree', 'ui.select', 'summernote'
    , 'unicorn.common', 'unicorn.ui', 'unicorn.filter'
])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $modalProvider, cfpLoadingBarProvider, RestangularProvider) {

        $modalProvider.options.backdrop = 'static';

        cfpLoadingBarProvider.includeSpinner = false;   // ignoreLoadingBar

        $httpProvider.interceptors.push(function ($q, $location, $filter, cgBusyMessage) {
            return {
                'request': function (request) {
                    request.headers.admin = true;
                    if (request.method == 'GET') {
                        cgBusyMessage.setMessage('正在加载，请稍候...');
                    } else {
                        cgBusyMessage.setMessage('正在处理，请稍候...');
                    }
                    return request || $q.when(request);
                },
                'response': function (response) {
                    if (response.headers('login')) {
                        window.location.href = PageContext.path + '/login';
                        return $q.reject(response);
                    }
                    return response || $q.when(response);
                },

                'responseError': function (rejection) {
                    var data = rejection.data;
                    if (data.warning){
                        if (data.warning === "登录超时"){
                            window.location.href = PageContext.path + '/login';
                        }
                        Toaster.warning(data.warning);

                    }
                    if (data.error){
                        Toaster.warning(data.error);
                    }
                    return $q.reject(rejection);
                }
            };
        });
    })

    .value('PageContext', PageContext)

    .value('cgBusyDefaults', {
        delay: 300
    })
;