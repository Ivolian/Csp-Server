'use strict';

angular.module('app')

    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider.state('job.district', {
            url: '/district',
            displayName: '区域管理',
            templateUrl: 'app/job/district/district.html',
            controller: 'DistrictCtrl'
        });
    })

    .factory('District', function (Restangular) {
        return Restangular.service('district');
    })

    .controller('DistrictCtrl', function ($scope, $state, $modal, SimpleTable, District) {

        $scope.grid = SimpleTable(District.getList);

        $scope.createDistrict = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/district/district.form.html',
                controller: 'DistrictCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateDistrict = function (district) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/district/district.form.html',
                controller: 'DistrictUpdateCtrl',
                resolve: {
                    id: function () {
                        return district.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeDistrict = function (district) {
            Dialog.confirmDelete().then(function () {
                district.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

    })

    .controller('DistrictCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, District) {

        $scope.district = {};

        $scope.title = '新增区域';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = District.post($scope.district).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })

    .controller('DistrictUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, District, id) {

        $scope.promise = District.one(id).get();

        $scope.district = $scope.promise.$object;

        $scope.title = '修改区域';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.district).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })

;
