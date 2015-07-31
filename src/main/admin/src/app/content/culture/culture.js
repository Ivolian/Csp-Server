'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider.state('content.culture', {
            url: '/culture',
            displayName: '企业文化',
            templateUrl: 'app/content/culture/culture.html',
            controller: 'CultureCtrl'
        });
    })

    .factory('Culture', function (Restangular) {
        return Restangular.service('culture');
    })

    .controller('CultureCtrl', function ($scope, $state, $modal, SimpleTable, Culture) {

        $scope.grid = SimpleTable(Culture.getList);

        $scope.createCulture = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/culture/culture.form.html',
                controller: 'CultureCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateCulture = function (culture) {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/culture/culture.form.html',
                controller: 'CultureUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return culture.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeCulture = function (culture) {
            Dialog.confirmDelete().then(function () {
                culture.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

        $scope.publishCulture = function (culture, publish) {
            culture.customPUT({}, 'publish/' + publish).then(function () {
                $scope.grid.refresh();
            });
        };
    })

    .controller('CultureCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, Culture) {

        $scope.culture = {
            contentData: {data: ''}
        };

        $scope.title = '新增企业文化';

        $scope.summernoteConfig = _.extend(angular.copy(SummernoteConfig), {height: 360});

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Culture.post($scope.culture).then(function () {
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
            $scope.culture.attachment = response;
            $scope.culture.pictureFilename = response.fileName;
        };
    })

    .controller('CultureUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, Culture, id) {

        $scope.promise = Culture.one(id).get();

        $scope.culture = $scope.promise.$object;

        $scope.title = '修改企业文化';

        $scope.summernoteConfig = _.extend(angular.copy(SummernoteConfig), {height: 360});

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.culture).save().then(function () {
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
            $scope.culture.attachment = response;
            $scope.culture.pictureFilename = response.fileName;
        };
    })
;
