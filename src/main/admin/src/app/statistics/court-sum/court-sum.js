'use strict';

angular.module('app')

    .config(function ($stateProvider) {

        $stateProvider.state('statistics.courtSum', {
            url: '/courtSum',
            displayName: '法院总汇数据',
            templateUrl: 'app/statistics/court-sum/court-sum-list.html',
            controller: 'CourtSumCtrl'
        });
    })

    .factory('courtSum', function (Restangular) {
        return Restangular.service('courtSum');
    })

    .controller('CourtSumCtrl', function ($scope, $state, $modal, SimpleTable, courtSum, $http) {

        $scope.grid = SimpleTable(courtSum.getList);

        $scope.grid.pageSize = 20;
        $scope.grid.queryInfo.beginDate = new Date();
        $scope.grid.queryInfo.endDate = new Date();

        $scope.exportExcel = function () {
            $modal.open({
                templateUrl: 'app/statistics/court-sum/court-sum-export-form.html',
                controller: 'CourtSumExportCtrl'
            });
        };

    })

    .controller('CourtSumExportCtrl', function ($scope, $modalInstance, $http) {

        $scope.courtSumExport = {};

        $scope.title = '导出法院汇总数据';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.export = function () {

            $http({
                url: PageContext.path + "/api/v1/courtSum/export",
                method: 'GET',
                params: {
                    fileName: $scope.courtSumExport.fileName,
                    beginTime: moment($scope.courtSumExport.beginTime).format('YYYY-MM-DD HH:mm'),
                    endTime: moment($scope.courtSumExport.endTime).format('YYYY-MM-DD HH:mm')
                }
            }).success(function (repsonce) {

                var tempFileName = repsonce["tempFileName"];
                var fileName = repsonce['fileName'];
                console.log(tempFileName);
                window.location.href = PageContext.path + '/api/v1/file/download?tempFileName=' + tempFileName +
                    '&fileName=' + fileName + '.xls';
                $modalInstance.close();

            });
        };

    })

;
