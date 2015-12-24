'use strict';

angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider.state('content.notice', {
            url: '/notice',
            displayName: '公告管理',
            templateUrl: 'app/content/notice/notice.html',
            controller: 'NoticeCtrl'
        });
    })

    .factory('Notice', function (Restangular) {
        return Restangular.service('notice');
    })

    .controller('NoticeCtrl', function ($scope, $state, $modal, SimpleTable, Notice) {

        $scope.grid = SimpleTable(Notice.getList);

        $scope.createNotice = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/notice/notice.form.html',
                controller: 'NoticeCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateNotice = function (notice) {
            var modalInstance = $modal.open({
                templateUrl: 'app/content/notice/notice.form.html',
                controller: 'NoticeUpdateCtrl',
                resolve: {
                    id: function () {
                        return notice.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeNotice = function (notice) {
            Dialog.confirmDelete().then(function () {
                notice.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
        
    })

    .controller('NoticeCreateCtrl', function ($scope, $modalInstance, SummernoteConfig, FileUploader, Notice) {

        $scope.notice = {};

        $scope.title = '新增公告';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Notice.post($scope.notice).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })

    .controller('NoticeUpdateCtrl', function ($scope, $modalInstance, Restangular, SummernoteConfig, FileUploader, Notice, id) {

        $scope.promise = Notice.one(id).get();

        $scope.notice = $scope.promise.$object;

        $scope.title = '修改公告';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.notice).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };

    })
;
