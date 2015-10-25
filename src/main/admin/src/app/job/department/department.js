'use strict';

angular.module('app')

    .config(function ($stateProvider) {

        $stateProvider.state('job.department', {
            url: '/department',
            displayName: '部门管理',
            templateUrl: 'app/job/department/department.html',
            controller: 'DepartmentCtrl'
        });

    })

    .factory('Department', function (Restangular) {
        return Restangular.service('department');
    })

    .controller('DepartmentCtrl', function ($scope, $state, $modal, SimpleTable, Department) {

        $scope.grid = SimpleTable(Department.getList);

        $scope.createDepartment = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/department/department.form.html',
                controller: 'DepartmentCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateDepartment = function (department) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/department/department.form.html',
                controller: 'DepartmentUpdateCtrl',
                resolve: {
                    id: function () {
                        return department.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeDepartment = function (department) {
            Dialog.confirmDelete().then(function () {
                department.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
        
    })

    .controller('DepartmentCreateCtrl', function ($scope, $modalInstance, Department) {

        $scope.department = {};

        $scope.title = '新增部门';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Department.post($scope.department).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })

    .controller('DepartmentUpdateCtrl', function ($scope, $modalInstance, Restangular, Department, id) {

        $scope.promise = Department.one(id).get();

        $scope.department = $scope.promise.$object;

        $scope.title = '修改部门';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.department).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })
;
