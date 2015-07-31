'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('job.position', {
            url: '/position',
            displayName: '职位管理',
            templateUrl: 'app/job/position/position.list.html',
            controller: 'PositionListCtrl'
        });
    })

    .factory('Positions', function (Restangular) {
        return Restangular.service('position');
    })

    .filter('educationResult', function () {
        return function (input) {
            return {0: '无限制', 1: '高中', 2: '大专', 3: '本科', 4: '硕士以上'}[input];
        }
    })

    .filter('experienceResult', function () {
        return function (input) {
            return {0: '无限制', 1: '1-3年', 2: '3-5年'}[input];
        }
    })

    .controller('PositionListCtrl', function ($scope, $state, $modal, SimpleTable, Positions) {

        $scope.grid = SimpleTable(Positions.getList);

        $scope.createPosition = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/position/position.form.html',
                controller: 'PositionCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updatePosition = function (position) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/position/position.form.html',
                controller: 'PositionUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return position.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removePosition  = function (position) {
            Dialog.confirmDelete().then(function () {
                position.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
    })

    .controller('PositionCreateCtrl', function ($scope, $modalInstance, Positions) {

        $scope.position = {
            education : 0,
            experience : 0
        };

        $scope.title = '新增职位';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Positions.post($scope.position).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })

    .controller('PositionUpdateCtrl', function ($scope, $modalInstance, Restangular, Positions, id) {

        $scope.promise = Positions.one(id).get();

        $scope.position = $scope.promise.$object;

        $scope.title = '修改职位';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.position).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })
;
