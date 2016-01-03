'use strict';

angular.module('app')
    
    .config(function ($stateProvider) {
        $stateProvider.state('job.appDiff', {
            url: '/appDiff',
            displayName: 'APK差分包管理',
            templateUrl: 'app/job/app-diff/app-diff.html',
            controller: 'AppDiffCtrl'
        });
    })

    .factory('AppDiff', function (Restangular) {
        return Restangular.service('appDiff');
    })

    .controller('AppDiffCtrl', function ($scope, $state, $modal, SimpleTable, AppDiff) {

        $scope.grid = SimpleTable(AppDiff.getList);

        $scope.createAppDiff = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/app-diff/app-diff.form.html',
                controller: 'AppDiffCreateCtrl',
                size:'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateAppDiff = function (appDiff) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/app-diff/app-diff.form.html',
                controller: 'AppDiffUpdateCtrl',
                size:'lg',
                resolve: {
                    id: function () {
                        return appDiff.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeAppDiff = function (appDiff) {
            Dialog.confirmDelete().then(function () {
                appDiff.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
        
    })

    .controller('AppDiffCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, AppDiff) {

        $scope.appDiff = {};

        $scope.title = '新增差分包';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = AppDiff.post($scope.appDiff).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

        var uploader = $scope.uploader = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.appDiff.diffAttachment = response;
            $scope.appDiff.diffFilename = response.fileName;
        };
    })

    .controller('AppDiffUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, AppDiff, id) {

        $scope.promise = AppDiff.one(id).get();

        $scope.appDiff = $scope.promise.$object;

        $scope.title = '修改差分包';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.appDiff).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

        var uploader = $scope.uploader = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.appDiff.diffAttachment = response;
            $scope.appDiff.diffFilename = response.fileName;
        };

    })
;
