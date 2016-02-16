'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('job.systemMenu', {
            url: '/systemMenu',
            displayName: '系统菜单管理',
            templateUrl: 'app/job/system-menu/system-menu.html',
            controller: 'SystemMenuCtrl'
        });

    })

    .factory('SystemMenu', function ($http, Restangular) {
        return _.extend({
            loadTree: function (params) {
                return $http({
                    url: PageContext.path + '/api/v1/systemMenu/tree/' + params.id,
                    method: 'GET'
                });
            }
        }, Restangular.all('systemMenu'));
    })

    .controller('SystemMenuCtrl', function ($scope, $timeout, $q, $state, $modal, SimpleTree, SystemMenu, focus) {

        $scope.tree = SimpleTree(SystemMenu.loadTree, {defaultIcon: 'fa-sitemap'});

        $scope.$watch('regionForm', function (value) {
            $scope.regionForm = value;
        });

        $scope.$watch('region.id', function (value) {
            if (value && $scope.region.$snapshot) {
                $scope.promise = SystemMenu.one(value).get().then(function (response) {
                    _.extend($scope.region, {
                        name: response.name,
                        code: response.code,
                        orderNo: response.orderNo
                    });
                    delete $scope.region.$snapshot;
                });
            }
        });

        var getRegionTitle = function (region) {
            if (region.parent) {
                return getRegionTitle(region.parent) + ' / ' + region.name;
            } else {
                return region.name;
            }
        };

        $scope.getRegionTitle = function () {
            if (!$scope.region) {
                return '';
            }
            if ($scope.region.name == undefined) {
                return '新增菜单';
            } else {
                return (getRegionTitle($scope.region) || '_');
            }
        };

        // 只允许创建2层
        $scope.checkCreateRegion = function () {
//            if ($scope.region) {
//                if ($scope.region.id) {
//                    if ($scope.region.parent && $scope.region.parent.parent) {
//                        return false;
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
            return true;
        };

        $scope.createRegion = function () {
            var parent = $scope.region;
            var region = {
                parent: parent,
                leaf: 1
            };
            var defer = $q.defer();
            if (parent.items == undefined) {
                parent.items = [];
                $scope.tree.load(parent.id).then(defer.resolve)
            } else {
                defer.resolve();
            }
            defer.promise.then(function () {
                $timeout(function () {
                    parent.items.push(region);
                    parent.leaf = 0;
                    $scope.regionForm.$setPristine(true);
                    $scope.region = region;
                    focus('name');
                }, 10)
            });
        };

        $scope.saveRegion = function () {
            var region = angular.copy($scope.region);
            if (region.parent) {
                region.parent = {
                    id: region.parent.id
                };
            }
            delete region.items;
            delete region.leaf;
            if (_.isEmpty(region.id)) {
                $scope.promise = SystemMenu.post(region).then(function (response) {
                    $scope.region.id = response.id;
                    $scope.region.items = [];
                    Toaster.success("保存成功！");
                });
            } else {
                $scope.promise = SystemMenu.one(region.id).doPUT(region).then(function () {
                    Toaster.success("保存成功！");
                });
            }
        };

        $scope.deleteRegion = function () {
            if (_.isEmpty($scope.region.id)) {
                if ($scope.region.parent == undefined) {
                    $scope.treeData = _.reject($scope.treeData, $scope.region);
                } else {
                    $scope.region.parent.items = _.reject($scope.region.parent.items, $scope.region);
                }
                $scope.region = null;
            } else {
                Dialog.confirmDelete().then(function () {
                    SystemMenu.one($scope.region.id).doDELETE().then(function () {
                        Toaster.success("删除成功！");
                        if ($scope.region.parent == undefined) {
                            $scope.treeData = _.reject($scope.treeData, $scope.region);
                        } else {
                            $scope.region.parent.items = _.reject($scope.region.parent.items, $scope.region);
                        }
                        $scope.region = null;
                    });
                });
            }
        };
    })
;
