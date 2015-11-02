'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('content.book', {
            url: '/book',
            displayName: '书籍管理',
            templateUrl: 'app/content/book/book.list.html',
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
                templateUrl: 'app/content/book/book.form.html',
                controller: 'BookCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateBook = function (book) {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/book/book.form.html',
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

        $scope.removeBook = function (book) {
            Dialog.confirmDelete().then(function () {
                book.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };


        $scope.batchUploadBook = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/book/book-batch-upload-form.html',
                controller: 'BookBatchUploadCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

    })

    .controller('BookCreateCtrl', function ($scope, $modalInstance, Books, FileUploader) {

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


        uploader2.onProgressItem = function (fileItem, progress) {
            $scope.progress = progress;
        }

    })

    .controller('BookUpdateCtrl', function ($scope, $modalInstance, Restangular, Books, id, FileUploader) {

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

    .controller('BookBatchUploadCtrl', function ($scope, $modalInstance, $http, FileUploader, Restangular) {

        $scope.title = '批量导入书籍';

        $scope.bookList = [];

        var uploader = $scope.uploader = new FileUploader({
            url: PageContext.path + '/api/v1/file/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });

        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.currentBook.ebookAttachment = response;
            $scope.currentBook.ebookFilename = response.fileName;
        };

        uploader.onProgressItem = function (fileItem, progress) {
            $scope.currentBook.progress = progress;
        };

        $scope.cancel = function () {

            $modalInstance.dismiss();
        };

        $scope.addBook = function () {
            $scope.currentBook = {};
            $scope.bookList.push($scope.currentBook);
        };

        $scope.deleteBook = function (index) {
            $scope.bookList.splice(index, 1);
        };

        $scope.save = function () {

            angular.forEach($scope.bookList, function (book) {
                book.menu = {
                    id: $scope.menu.id
                };
                delete book.progress;
            });


            $scope.promise = Restangular.service('book/batchUpload')
                .post($scope.bookList).then(function () {
                    Toaster.success("保存成功！");
                    $modalInstance.close();
                });

//            angular.forEach($scope)
            // one book network
//        ebookAttachment: {fileName: "ndk.txt", tempFileName: "8428110310546203870"}
//        ebookFilename: "ndk.txt"
//        menu: {id: "p_1", name: "资讯热点"}
//        name: "111"


        };

        var copeBookList = function () {

        };

        $scope.isAllRight = function () {
            return $scope.bookList.length !== 0 && $scope.isBookListRight();
        };

        $scope.isBookListRight = function () {
            if ($scope.bookList.length === 0) {
                return true;
            }
            var result = true;
            angular.forEach($scope.bookList, function (book) {
                if (!isBookRight(book)) {
                    result = false;
                }
            });
            return result;
        };

        var isBookRight = function (book) {
            if (!book.name) {
                return false;
            }
            if (!book.progress || book.progress !== 100) {
                return false;
            }
            return true;
        };

    })

;
