'use strict';

angular.module('app')
    .config(function ($stateProvider) {

        $stateProvider.state('job.email', {
            url: '/email',
            displayName: '邮件管理',
            templateUrl: 'app/job/email/email.list.html',
            controller: 'EmailListCtrl'
        });

    })

    .factory('Email', function (Restangular) {
        return Restangular.service('user');
    })

    .controller('EmailListCtrl', function ($scope, $state, $modal, SimpleTable, Email) {

        $scope.grid = SimpleTable(Email.getList);

        $scope.createEmail = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/email/email.form.html',
                controller: 'EmailCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateEmail = function (email) {
            var modalInstance = $modal.open({
                templateUrl: 'app/job/email/email.form.html',
                controller: 'EmailUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return email.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeEmail = function (email) {
            Dialog.confirmDelete().then(function () {
                email.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
    })

    .controller('EmailCreateCtrl', function ($scope, $modalInstance, Email) {

        $scope.email = {
            defaultValue : 0
        };

        $scope.title = '新增邮件';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Email.post($scope.email).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })

    .controller('EmailUpdateCtrl', function ($scope, $modalInstance, Restangular, Email, id) {

        $scope.promise = Email.one(id).get();

        $scope.email = $scope.promise.$object;

        $scope.title = '修改邮件';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.email).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })
;
