'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider.state('job.app', {
            url: '/app',
            displayName: 'Apk管理',
            templateUrl: 'app/job/app/app.html',
            controller: 'AppCtrl'
        });
    })

    .factory('App', function (Restangular) {
        return Restangular.service('app');
    })

    .controller('AppCtrl', function ($scope, $state, $modal, SimpleTable, App) {

        $scope.grid = SimpleTable(App.getList);

        $scope.createApp = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/app/app.form.html',
                controller: 'AppCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateApp = function (app) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/app/app.form.html',
                controller: 'AppUpdateCtrl',
                resolve: {
                    id: function () {
                        return app.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeApp = function (app) {
            Dialog.confirmDelete().then(function () {
                app.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
        
    })

    .controller('AppCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, App) {

        $scope.title = '新增APK';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = App.post($scope.app).then(function () {
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
            $scope.app.apkAttachment = response;
            $scope.app.apkFilename = response.fileName;
        };
    })

    .controller('AppUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, App, id) {

        $scope.promise = App.one(id).get();

        $scope.app = $scope.promise.$object;

        $scope.title = '修改APK';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.app).save().then(function () {
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
            $scope.app.apkAttachment = response;
            $scope.app.apkFilename = response.fileName;
        };

    })
;
