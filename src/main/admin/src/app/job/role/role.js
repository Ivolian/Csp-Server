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

        $scope.assignRole = function (role) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/role/role-assign.html',
                controller: 'RoleAssignCtrl',
                resolve: {
                    role: function () {
                        return role;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
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

    .controller('RoleAssignCtrl', function ($scope, $modalInstance, $timeout, SystemMenu, Role, role) {

        $scope.title = '分配权限';
        $scope.treeConfig = {
            selectable: false,
            treeHandleTemplate: 'app/components/ui/template/checkbox-tree-handler.html',
            checkedList: {},
            change: function (item) {
                toggleChange(item, true, true)
            }
        };

        var toggleChange = function (item, cascadeParent, cascadeChild) {
            var checked = $scope.treeConfig.checkedList[item.id];
            // 处理parent
            if (cascadeParent && item.parent) {
                var toggleParent = true;
                if (!checked) {
                    _.forEach(item.parent.items, function (child) {
                        if ($scope.treeConfig.checkedList[child.id]) {
                            toggleParent = false;
                            return false;
                        }
                    });
                }
                if (toggleParent) {
                    $scope.treeConfig.checkedList[item.parent.id] = checked;
                    toggleChange(item.parent, true, false);
                }
            }
            // 处理child
            if (cascadeChild && item.items && item.items.length > 0) {
                _.forEach(item.items, function (child) {
                    $scope.treeConfig.checkedList[child.id] = checked;
                    toggleChange(child, false, true)
                });
            }
        };

        $scope.promise = role.doGET('menu');

        $scope.promise.then(function (roleMenuList) {
            _.forEach(roleMenuList, function (item) {
                $scope.treeConfig.checkedList[item.menuId] = true;
            });
        });

        SystemMenu.doGET('tree').then(function (treeData) {
            var initMenuData = function (parent, data) {
                _.forEach(data, function (item) {
                    item.parent = parent;
                    initMenuData(item, item.items);
                });
            };
            initMenuData(undefined, treeData);
            $scope.treeData = treeData;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            var menuIds = [];
            _.forIn($scope.treeConfig.checkedList, function (value, key) {
                if (value) {
                    menuIds.push(key);
                }
            });

            $scope.promise = role.post('menu', menuIds, {}).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })


;
