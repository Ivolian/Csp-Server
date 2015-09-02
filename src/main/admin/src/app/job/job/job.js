'use strict';

angular.module('app')

    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/job', '/job/user');

        $stateProvider.state('job', {
            parent: 'root',
            url: '/job',
            displayName: '数据管理',
            template: '<div ui-view></div>'
        });

        $stateProvider.state('product.list2', {
            url: '/job',
            displayName: '回答列表',
            templateUrl: 'app/job/job/job.list.html',
            controller: 'JobListCtrl'
        });

    })

    .factory('Job', function (Restangular) {
        return Restangular.service('answer');
    })

    .controller('JobListCtrl', function ($scope, $state, $modal, SimpleTable, Job) {

        $scope.grid = SimpleTable(Job.getList);

        $scope.createJob = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/job/job.form.html',
                controller: 'JobCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateJob = function (job) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/job/job.form.html',
                controller: 'JobUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return job.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeJob = function (job) {
            Dialog.confirmDelete().then(function () {
                job.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

        $scope.publishJob = function (job, publish) {
            job.customPUT({}, 'publish/' + publish).then(function () {
                $scope.grid.refresh();
            });
        };

        $scope.getRegionText = function (job) {
            return (job.region.parent && job.region.parent.parent) ? (job.region.parent.name + ' / ' + job.region.name) : job.region.name
        };
    })

    .controller('JobCreateCtrl', function ($scope, $modalInstance, Job) {

        $scope.job = {};

        $scope.title = '新增回答';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Job.post($scope.job).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })

    .controller('JobUpdateCtrl', function ($scope, $modalInstance, Restangular, Job, id) {

        $scope.promise = Job.one(id).get();

        $scope.job = $scope.promise.$object;

        $scope.title = '修改回答';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.job).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    });
