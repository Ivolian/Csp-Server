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

//        toolbar : [
//            ['group', [ 'video','codeview' ]]
//        ],
        height: 300,
        fontNames: [
            '微软雅黑', '黑体', '宋体', '楷体',
            'Arial', 'Courier New', 'monospace'
        ],
        fontSizes: [12, 14, 16, 18, 20, 22, 24, 26, 28, 30],
        lang: "zh-CN"
    })
    .run(function ($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    })
    .controller('MainCtrl', function ($scope, $rootScope, $timeout, $window, PageContext, DateFormat, $http, $state) {

        // 加载登录用户信息
        $http({
            url: PageContext.path + '/security/getApplicationInfo',
            method: 'GET'
        }).then(function (response) {
            $rootScope.currentUser = {
                roleTag: response.data.roleTag,
                username: response.data.username
            };


            $scope.menuList = response.data.menuList;
//            console.log($scope.menuList)


            if ($scope.currentUser.roleTag === "Admin") {
                $state.transitionTo('content');

            }
            else {
                $state.transitionTo('statistics');

            }

        });

        $scope.menuList = [];
//        $scope.menuList = [
//            {
//                title: '内容管理',
//                state: 'content',
//                iconCls: 'fa fa-newspaper-o',
//                children: [
//                    {
//                        title: '新闻管理',
//                        state: 'content.news'
//                    },
//                    {
//                        title: '书籍管理',
//                        state: 'content.book'
//                    },
//                    {
//                        title: '公告管理',
//                        state: 'content.notice'
//                    }
//                ]
//            },
//            {
//                title: '数据管理',
//                state: 'job',
//                iconCls: 'fa fa-users',
//                children: [
//                    {
//                        title: '菜单管理',
//                        state: 'job.region'
//                    },
//                    {
//                        title: '法院管理',
//                        state: 'job.court'
//                    },
//                    {
//                        title: '部门管理',
//                        state: 'job.department'
//                    },
//                    {
//                        title: '用户管理',
//                        state: 'job.user'
//                    },
//                    {
//                        title: '差分包管理',
//                        state: 'job.appDiff'
//                    },
//                    {
//                        title: 'APK管理',
//                        state: 'job.app'
//                    },
//                    {
//                        title: '角色管理',
//                        state: 'job.role'
//                    },
//                    {
//                        title: '系统菜单管理',
//                        state: 'job.systemMenu'
//                    }
//                ]
//            }
//            ,
//            {
//                title: '问答管理',
//                state: 'qa',
//                iconCls: 'fa fa-comments',
//                children: [
//                    {
//                        title: '提问管理',
//                        state: 'qa.question'
//                    },
//                    {
//                        title: '回答管理',
//                        state: 'qa.answer'
//                    },
//                    {
//                        title: '评论管理',
//                        state: 'qa.comment'
//                    }
//                ]
//            },
//            {
//                title: '统计管理',
//                state: 'statistics',
//                iconCls: 'fa fa-users',
//                children: [
//                    {
//                        title: '法院实时数据',
//                        state: 'statistics.courtData'
//                    },
//                    {
//                        title: '法院总汇数据',
//                        state: 'statistics.courtSum'
//                    }
//                ]
//            }
//        ];

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

        $rootScope.$on('$stateChangeStart', function (event, state) {
            var stateNames = [];
            for (var i = 0; i != $scope.menuList.length; i++) {
                var menu = $scope.menuList[i];
                getStateNameFromMenu(stateNames, menu);
            }

            var prevent = true;
            angular.forEach(stateNames, function (stateName) {
                if (stateName === state.name) {
                    prevent = false;
                }
            });

            console.log(state.name)
            console.log(prevent)
            if (prevent)
                event.preventDefault();
        });

        var getStateNameFromMenu = function (arr, menu) {
            arr.push(menu.state);
            if (menu.children) {
                angular.forEach(menu.children, function (child) {
                    getStateNameFromMenu(arr, child);
                });
            }
        };


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
    }
)

    .
    controller('SideController', function ($scope, $modal) {
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
            console.log(PageContext.path)

            Dialog.confirm("确认退出系统？")
                .then(function () {
                    window.location.href = PageContext.path + '/logout';
                })
        };
    })


;
