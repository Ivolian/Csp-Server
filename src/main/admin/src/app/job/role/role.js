'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider.state('job.role', {
            url: '/role',
            displayName: '角色管理',
            templateUrl: 'app/job/role/role.html',
            controller: 'RoleCtrl'
        });
    })

    .factory('Role', function (Restangular) {
        return Restangular.service('role');
    })

    .controller('RoleCtrl', function ($scope, $state, $modal, SimpleTable, Role) {

        $scope.grid = SimpleTable(Role.getList);

        $scope.createRole = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/role/role.form.html',
                controller: 'RoleCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateRole = function (role) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/role/role.form.html',
                controller: 'RoleUpdateCtrl',
                resolve: {
                    id: function () {
                        return role.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeRole = function (role) {
            Dialog.confirmDelete().then(function () {
                role.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
        
    })

    .controller('RoleCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, Role) {

        $scope.role = {};

        $scope.title = '新增角色';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Role.post($scope.role).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })

    .controller('RoleUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, Role, id) {

        $scope.promise = Role.one(id).get();

        $scope.role = $scope.promise.$object;

        $scope.title = '修改角色';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.role).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })

;
