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

        $scope.arr = [
            {
                id:"1",
                name:"13"
        },
            {
                id:"2",
                name:"14"
            }
        ];

        $scope.courtDataExport = {
            court: {id: ""},
            department: {id: ""}
        };

        $scope.title = '导出法院实时数据';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.export = function () {

            var courtId = $scope.courtDataExport.court.id;
            var departmentId = $scope.courtDataExport.department.id;
            if (courtId === "" && departmentId === "") {
                Toaster.error("法院和部门不能同时为空！");
                return;
            }

            $http({
                url: PageContext.path + "/api/v1/courtData/export",
                method: 'GET',
                params: {
                    fileName: $scope.courtDataExport.fileName,
                    courtId: courtId,
                    departmentId: departmentId,
                    beginTime: moment($scope.courtDataExport.beginTime).format('YYYY-MM-DD HH:mm'),
                    endTime: moment($scope.courtDataExport.endTime).format('YYYY-MM-DD HH:mm')
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
