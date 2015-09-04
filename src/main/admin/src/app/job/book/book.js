'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('job.book', {
            url: '/book',
            displayName: '书籍管理',
            templateUrl: 'app/job/book/book.list.html',
            controller: 'BookListCtrl'
        });
    })

    .factory('Books', function (Restangular) {
        return Restangular.service('book');
    })

    .controller('BookListCtrl', function ($scope, $modal, SimpleTable, Books) {

        $scope.grid = SimpleTable(Books.getList);

        $scope.createBook = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/book/book.form.html',
                controller: 'BookCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateBook = function (book) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/book/book.form.html',
                controller: 'BookUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return book.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeBook  = function (book) {
            Dialog.confirmDelete().then(function () {
                book.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
    })

    .controller('BookCreateCtrl', function ($scope, $modalInstance, Books,FileUploader) {

        $scope.book = {
        };

        $scope.title = '新增书籍';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Books.post($scope.book).then(function () {
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
            $scope.book.pictureAttachment = response;
            $scope.book.pictureFilename = response.fileName;
            console.log($scope.book.pictureFilename)

        };

        // for ebook
        var uploader2 = $scope.uploader2 = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });
        uploader2.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.book.ebookAttachment = response;
            $scope.book.ebookFilename = response.fileName;
//            console.log($scope.book.ebookFilename)

        };
    })

    .controller('BookUpdateCtrl', function ($scope, $modalInstance, Restangular, Books, id,FileUploader) {

        $scope.promise = Books.one(id).get();

        $scope.book = $scope.promise.$object;

        $scope.title = '修改书籍';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.book).save().then(function () {
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
            $scope.book.pictureAttachment = response;
            $scope.book.pictureFilename = response.fileName;
        };

        // for ebook
        var uploader2 = $scope.uploader2 = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });
        uploader2.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.book.ebookAttachment = response;
            $scope.book.ebookFilename = response.fileName;
//            console.log($scope.book.ebookFilename)

        };
    })
;
