'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider, RestangularProvider) {

        console.log(PageContext.path);

        RestangularProvider.setBaseUrl(PageContext.path + '/api/v1');

        RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response, deferred) {

            var extractedData;
            // .. to look for getList operations
            if (operation === "getList") {
                // .. and handle the data and meta data
                extractedData = response.data.content;
                extractedData.meta = response.data;
            } else {
                extractedData = response.data;
            }
            return extractedData;

        });

        $stateProvider
            .state('root', {
                views: {
                    'side-wrapper': {
                        templateUrl: 'app/include/side-wrapper.html',
                        controller: 'SideController'
                    },
                    'content-wrapper': {
                        templateUrl: 'app/include/content-wrapper.html'
                    },
                    'content-header@root': {
                        templateUrl: 'app/include/content-header.html'
                    }
                }
            })
        ;
    })
    .value('SummernoteConfig', {
        height: 300,
        fontNames: [
            '微软雅黑', '黑体', '宋体', '楷体',
            'Arial', 'Courier New', 'monospace'
        ],
        lang: "zh-CN"
    })
    .run(function ($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    })
    .controller('MainCtrl', function ($scope, $rootScope, $timeout, $window, PageContext, DateFormat) {

        $scope.menuList = [
            {
                title: '内容管理',
                state: 'content',
                iconCls: 'fa fa-newspaper-o',
                children: [
                    {
                        title: '新闻管理',
                        state: 'content.news'
                    }
//                    ,
//                    {
//                        title: '企业文化',
//                        state: 'content.culture'
//                    }
                ]
            },
            {
                title: '数据管理',
                state: 'job',
                iconCls: 'fa fa-users',
                children: [
                    {
                        title: '用户管理',
                        state: 'job.email'
                    },
                    {
                        title: '书籍管理',
                        state: 'job.position'
                    },
                    {
                        title: '菜单管理',
                        state: 'job.region'
                    },
                    {
                        title: 'APK管理',
                        state: 'job.app'
                    }
                ]
            }
            ,
            {
                title: '问答管理',
                state: 'product',
                iconCls: 'fa fa-users',
                children: [
                    {
                        title: '提问列表',
                        state: 'product.list'
                    },
                    {
                        title: '回答列表',
                        state: 'product.list2'
                    }
                ]
            }
        ];

        $rootScope.PageContext = PageContext;
        $rootScope.DateFormat = DateFormat;

        var findState = function (menuList, stateName, tab) {
            tab = tab || undefined;
            _.forEach(menuList, function (menu) {
                if (menu.state == stateName) {
                    tab = menu;
                    return false;
                }
                tab = findState(menu.children, stateName, tab);
            });
            return tab;
        };
        var refreshContentTabs = function () {
            $scope.currentState = $rootScope.$state.$current;
            var tab = findState($scope.menuList, $scope.currentState.name);
            if (tab !== undefined) {
                if (tab.state.split('.').length == 3) {
                    $scope.tabs = findState($scope.menuList, tab.state.substring(0, tab.state.lastIndexOf('.'))).children;
                } else {
                    $scope.tabs = [tab];
                }
            }
        };
        $rootScope.$on('$stateChangeSuccess', refreshContentTabs);

        // 自适应窗口大小
        $rootScope.updateWindowSize = function (ignoreScroll) {
            var windowHeight = document.documentElement.clientHeight;
            if (ignoreScroll) {
            } else {
                windowHeight = Math.max(windowHeight
                    , document.documentElement.scrollHeight - 15
                    , $scope.menuList.length * 68 + angular.element('.side-header').outerHeight());
            }
            var menuHeight = windowHeight - angular.element('.side-header').outerHeight();
            var contentHeight = windowHeight - ($rootScope.headerHide ? 0 : angular.element('.content-header').outerHeight());
            var panelHeight = contentHeight - 48;

            PageContext.menuHeight = menuHeight;
            PageContext.contentHeight = contentHeight;
            PageContext.panelHeight = panelHeight;
            $scope.$apply(PageContext);
        };

        angular.element($window).bind('resize', function () {
            $rootScope.updateWindowSize();
        });

        $scope.toggleSide = function () {
            $rootScope.sideHide = !$rootScope.sideHide;
            $timeout(function () {
                $rootScope.updateWindowSize();
            });
        };

        $scope.toggleHeader = function () {
            $rootScope.headerHide = !$rootScope.headerHide;
            $timeout(function () {
                $rootScope.updateWindowSize();
            });
        };

        $timeout(function () {
            angular.element('body').toggleClass('rendering');
            $rootScope.updateWindowSize(true);
            $timeout(function () {
                $rootScope.updateWindowSize(true);
                angular.element('body').toggleClass('rendering');
            }, 1000);
        });

        refreshContentTabs();
    })

    .controller('SideController', function ($scope, $modal) {
        $scope.modifyPassword = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/include/modify-password-form.html',
                controller: ['$scope', '$modalInstance', '$http', function (scope, $modalInstance, $http) {

                    scope.cancel = function () {
                        $modalInstance.dismiss();
                    };

                    scope.submit = function () {
                        scope.promise = $http({
                            url: PageContext.path + '/security/modifyPassword',
                            method: 'POST',
                            data: {
                                oldPassword: scope.oldPassword,
                                newPassword: scope.newPassword
                            }
                        }).then(function (response) {
                            $modalInstance.close();
                        })
                    };
                }]
            });
            modalInstance.result.then(function (result) {
                Toaster.success("密码修改成功！");
            });
        };
        $scope.exitSystem = function () {
            Dialog.confirm("确认退出系统？")
                .then(function () {
                    window.location.href = PageContext.path + '/logout';
                })
        };
    })
;
