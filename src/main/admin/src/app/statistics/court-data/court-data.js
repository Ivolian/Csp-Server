'use strict';

angular.module('app')

    .config(function ($stateProvider) {

        $stateProvider.state('statistics.courtData', {
            url: '/courtData',
            displayName: '法院实时数据',
            templateUrl: 'app/statistics/court-data/court-data-list.html',
            controller: 'CourtDataCtrl'
        });
    })

    .factory('courtData', function (Restangular) {
        return Restangular.service('courtData');
    })

    .controller('CourtDataCtrl', function ($scope, $state, $modal, SimpleTable, courtData, $http) {

        $scope.grid = SimpleTable(courtData.getList);

        $scope.grid.pageSize = 20;
        $scope.grid.queryInfo.beginDate = new Date();
        $scope.grid.queryInfo.endDate = new Date();
        $scope.grid.queryInfo.onlyNotLogin = false;

        $scope.exportExcel = function () {
            $modal.open({
                templateUrl: 'app/statistics/court-data/court-data-export-form.html',
                controller: 'CourtDataExportCtrl'
            });
        };

    })


    .controller('CourtDataExportCtrl', function ($scope, $modalInstance, $http) {

        $scope.courtDataExport = {};

        $scope.title = '导出法院实时数据';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.export = function () {

            $http({
                url: PageContext.path + "/api/v1/courtData/export",
                method: 'GET',
                params: {
                    fileName: $scope.courtDataExport.fileName,
                    courtId: $scope.courtDataExport.court.id,
                    beginTime: $scope.courtDataExport.beginTime,
                    endTime: $scope.courtDataExport.endTime
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
