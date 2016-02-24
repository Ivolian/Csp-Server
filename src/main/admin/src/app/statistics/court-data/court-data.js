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
        $scope.grid.queryInfo.beginDate = moment().add(-1, 'days');
        $scope.grid.queryInfo.endDate = new Date();
        $scope.grid.queryInfo.department = {};

//        $scope.grid.queryInfo.onlyNotLogin = false;


        $scope.$watch('grid.queryInfo.court', function (court) {
            if (court && court.id) {
                $http({
                    url: PageContext.path + "/api/v1/department/listByCourtId",
                    method: 'GET',
                    params: {
                        courtId: court.id
                    }
                }).success(function (departmentList) {
                    $scope.departmentList = departmentList;
                });
                $scope.grid.queryInfo.department = {};
            }
        });

        $scope.exportExcel = function () {
            $modal.open({
                templateUrl: 'app/statistics/court-data/court-data-export-form.html',
                controller: 'CourtDataExportCtrl',
                resolve: {
                    exportParams: function () {
                        return {
                            beginTime: $scope.grid.queryInfo.beginDate,
                            endTime: $scope.grid.queryInfo.endDate,
                            court: $scope.grid.queryInfo.court,
                            department: $scope.grid.queryInfo.department,
                            departmentList: $scope.departmentList
                        };
                    }
                }
            });
        };


    })

    .controller('CourtDataExportCtrl', function ($scope, $modalInstance, $http, exportParams) {

        $scope.title = '导出法院实时数据';
        $scope.departmentList = exportParams.departmentList;
        delete  exportParams.departmentList;
        $scope.courtDataExport = exportParams;
        $scope.courtDataExport.underling = true;

        var firstLoad = true;

        $scope.$watch('courtDataExport.court', function (court) {

            if (court && court.id && !firstLoad) {
                console.log(court)
                $http({
                    url: PageContext.path + "/api/v1/department/listByCourtId",
                    method: 'GET',
                    params: {
                        courtId: court.id
                    }
                }).success(function (departmentList) {
                    $scope.departmentList = departmentList;
                });
                $scope.courtDataExport.department = {};
            }
            firstLoad = false;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.exportExcel = function () {

            var params = {
                underling: $scope.courtDataExport.underling,
                fileName: $scope.courtDataExport.fileName,
                courtId: $scope.courtDataExport.court.id,
                beginTime: moment($scope.courtDataExport.beginTime).format('YYYY-MM-DD HH:mm'),
                endTime: moment($scope.courtDataExport.endTime).format('YYYY-MM-DD HH:mm')
            };

            if ($scope.courtDataExport.department && $scope.courtDataExport.department.id) {
                params.departmentId = $scope.courtDataExport.department.id;
            }

            $http({
                url: PageContext.path + "/api/v1/courtData/export",
                method: 'GET',
                params: params
            }).success(function (repsonce) {
                var tempFileName = repsonce["tempFileName"];
                var fileName = repsonce['fileName'];
                window.location.href = PageContext.path + '/api/v1/file/download?tempFileName=' + tempFileName +
                    '&fileName=' + fileName + '.xls';
                $modalInstance.close();
            });
        };

    })

;
