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
        return Restangular.service('book');
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

    .controller('PositionCreateCtrl', function ($scope, $modalInstance, Positions,FileUploader) {

        $scope.position = {
        };

        $scope.title = '新增书籍';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Positions.post($scope.position).then(function () {
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
            $scope.position.pictureAttachment = response;
            $scope.position.pictureFilename = response.fileName;
            console.log($scope.position.pictureFilename)

        };

        // for ebook
        var uploader2 = $scope.uploader2 = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });
        uploader2.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.position.ebookAttachment = response;
            $scope.position.ebookFilename = response.fileName;
//            console.log($scope.position.ebookFilename)

        };
    })

    .controller('PositionUpdateCtrl', function ($scope, $modalInstance, Restangular, Positions, id,FileUploader) {

        $scope.promise = Positions.one(id).get();

        $scope.position = $scope.promise.$object;

        $scope.title = '修改书籍';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.position).save().then(function () {
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
            $scope.position.pictureAttachment = response;
            $scope.position.pictureFilename = response.fileName;
        };

        // for ebook
        var uploader2 = $scope.uploader2 = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });
        uploader2.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.position.ebookAttachment = response;
            $scope.position.ebookFilename = response.fileName;
//            console.log($scope.position.ebookFilename)

        };
    })
;
