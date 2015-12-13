'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider.state('content.news', {
            url: '/news',
            displayName: '新闻管理',
            templateUrl: 'app/content/news/news.html',
            controller: 'NewsCtrl'
        });
    })

    .factory('News', function (Restangular) {
        return Restangular.service('news');
    })

    .controller('NewsCtrl', function ($scope, $state, $modal, SimpleTable, News) {

        $scope.grid = SimpleTable(News.getList);

        $scope.createNews = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/news/news.form.html',
                controller: 'NewsCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateNews = function (news) {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/news/news.form.html',
                controller: 'NewsUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return news.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeNews = function (news) {
            Dialog.confirmDelete().then(function () {
                news.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

    })

    .controller('NewsCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, News) {

        $scope.news = {
            newsData: {
                data: ''
            },
            hasVideo: 0,
            videoType: 0
        };

        $scope.title = '新增新闻';

        $scope.summernoteConfig = _.extend(angular.copy(SummernoteConfig), {height: 360});

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {

            $scope.promise = News.post($scope.news).then(function () {
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
            $scope.news.pictureAttachment = response;
            $scope.news.pictureFilename = response.fileName;
        };

    })

    .controller('NewsUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, News, id) {

        $scope.promise = News.one(id).get();

        $scope.news = $scope.promise.$object;

        $scope.title = '修改新闻';

        $scope.summernoteConfig = _.extend(angular.copy(SummernoteConfig), {height: 360});

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.news).save().then(function () {
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
            $scope.news.pictureAttachment = response;
            $scope.news.pictureFilename = response.fileName;
        };

    })
;
