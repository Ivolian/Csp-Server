'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('job.user', {
            url: '/user',
            displayName: '用户管理',
            templateUrl: 'app/job/user/user.list.html',
            controller: 'UserListCtrl'
        });

    })

    .factory('User', function (Restangular) {
        return Restangular.service('user');
    })

    .controller('UserListCtrl', function ($scope, $state, $modal, SimpleTable, User, $http) {

        $scope.pushUpdate = function (user) {

            $http({
                url: PageContext.path + "/api/v1/user/pushUpdate",
                method: 'GET',
                params: {
                    userId: user.id
                }
            }).success(function (repsonce) {
                Toaster.success("推送完成！");
            });
        };

        $scope.enableOrDisable = function (user) {

            $http({
                url: PageContext.path + "/api/v1/user/enableOrDisable",
                method: 'GET',
                params: {
                    userId: user.id
                }
            }).success(function (repsonce) {
                $scope.grid.refresh();
            });
        };


        $scope.grid = SimpleTable(User.getList);
        $scope.grid.queryInfo.justOnline = false;

        $scope.createUser = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/user/user.form.html',
                controller: 'UserCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateUser = function (user) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/user/user.form.html',
                controller: 'UserUpdateCtrl',
                resolve: {
                    id: function () {
                        return user.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeUser = function (user) {
            Dialog.confirmDelete().then(function () {
                user.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

        $scope.resetPassword = function (user) {
            Dialog.confirm({
                title: '确认重置密码？'
            }).then(function () {
                $http({
                        method: 'POST',
                        url: '/withub/api/v1/user/resetPassword',
                        params: {
                            userId: user.id
                        }
                    }
                ).then(function (response) {
                        Toaster.success("重置成功！");
                    });
            });
        }

    })

    .
    controller('UserCreateCtrl', function ($scope, $modalInstance, User) {

        $scope.title = '新增用户';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = User.post($scope.user).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })

    .controller('UserUpdateCtrl', function ($scope, $modalInstance, Restangular, User, id) {

        $scope.promise = User.one(id).get();

        $scope.user = $scope.promise.$object;

        $scope.title = '修改用户';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.user).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })
;
