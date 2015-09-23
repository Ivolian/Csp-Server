'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('job.court', {
            url: '/court',
            displayName: '法院管理',
            templateUrl: 'app/job/court/court.html',
            controller: 'CourtCtrl'
        });

    })

    .factory('Court', function ($http, Restangular) {
        return _.extend({
            loadTree: function (params) {
                return $http({
                    url: PageContext.path + '/api/v1/court/tree/' + params.id,
                    method: 'GET'
                });
            }
        }, Restangular.service('court'));
    })

    .controller('CourtCtrl', function ($scope, $timeout, $q, $state, $modal, SimpleTree, Court, focus, $http) {

        $scope.pushUpdate = function () {

            $http({
                url: PageContext.path + "/api/v1/court/pushUpdate",
                method: 'GET',
                params: {
                    courtId: $scope.court.id
                }
            }).success(function (repsonce) {
                Toaster.success("推送完成！");
            });
        };

        $scope.tree = SimpleTree(Court.loadTree, {defaultIcon: 'fa-sitemap'});

        $scope.$watch('courtForm', function (value) {
            $scope.courtForm = value;
        });

        $scope.$watch('court.id', function (value) {
            if (value && $scope.court.$snapshot) {
                $scope.promise = Court.one(value).get().then(function (response) {
                    _.extend($scope.court, {
                        name: response.name,
                        code: response.code,
                        orderNo: response.orderNo
                    });
                    delete $scope.court.$snapshot;
                });
            }
        });

        var getCourtTitle = function (court) {
            if (court.parent) {
                return getCourtTitle(court.parent) + ' / ' + court.name;
            } else {
                return court.name;
            }
        };

        $scope.getCourtTitle = function () {
            if (!$scope.court) {
                return '';
            }
            if ($scope.court.name == undefined) {
                return '新增法院';
            } else {
                return (getCourtTitle($scope.court) || '_');
            }
        };

        // 只允许创建2层
        $scope.checkCreateCourt = function () {
//            if ($scope.court) {
//                if ($scope.court.id) {
//                    if ($scope.court.parent && $scope.court.parent.parent) {
//                        return false;
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
            return true;
        };

        $scope.createCourt = function () {
            var parent = $scope.court;
            var court = {
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
                    parent.items.push(court);
                    parent.leaf = 0;
                    $scope.courtForm.$setPristine(true);
                    $scope.court = court;
                    focus('name');
                }, 10)
            });
        };

        $scope.saveCourt = function () {
            var court = angular.copy($scope.court);
            if (court.parent) {
                court.parent = {
                    id: court.parent.id
                };
            }
            delete court.items;
            delete court.leaf;
            if (_.isEmpty(court.id)) {
                $scope.promise = Court.post(court).then(function (response) {
                    $scope.court.id = response.id;
                    $scope.court.items = [];
                    Toaster.success("保存成功！");
                });
            } else {
                $scope.promise = Court.one(court.id).doPUT(court).then(function () {
                    Toaster.success("保存成功！");
                });
            }
        };

        $scope.deleteCourt = function () {
            if (_.isEmpty($scope.court.id)) {
                if ($scope.court.parent == undefined) {
                    $scope.treeData = _.reject($scope.treeData, $scope.court);
                } else {
                    $scope.court.parent.items = _.reject($scope.court.parent.items, $scope.court);
                }
                $scope.court = null;
            } else {
                Dialog.confirmDelete().then(function () {
                    Court.one($scope.court.id).doDELETE().then(function () {
                        Toaster.success("删除成功！");
                        if ($scope.court.parent == undefined) {
                            $scope.treeData = _.reject($scope.treeData, $scope.court);
                        } else {
                            $scope.court.parent.items = _.reject($scope.court.parent.items, $scope.court);
                        }
                        $scope.court = null;
                    });
                });
            }
        };
    })
;
