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

    .controller('CourtDataCtrl', function ($scope, $state, $modal, SimpleTable, courtData,$http) {

        $scope.grid = SimpleTable(courtData.getList);

        $scope.grid.pageSize = 20;
        $scope.grid.queryInfo.beginDate = new Date();
        $scope.grid.queryInfo.endDate = new Date();
        $scope.grid.queryInfo.onlyNotLogin = false;

        $scope.export = function(){

            $http({
               url:            PageContext.path + "/api/v1/courtData/export"
        });

//    console.log(
//            PageContext.path + "/courtData/export");

//            $http({
//                url: PageContext.path + '/aems/exhibitionChart/outExecl',
//                method: 'GET',
//                params: {
//                    dataPoints: _.pluck(widget.attributes.dataPoints, 'objectId'),
//                    startTime: moment(widget.queryInfo.startTime).format('YYYY-MM-DD HH:mm:ss'),
//                    endTime: moment(widget.queryInfo.endTime).format('YYYY-MM-DD HH:mm:ss'),
//                    timeUnit: widget.attributes.defaultDateRange.unit,
//                    style: widget.attributes.chartStyle
//                }
//            }).success(function (response) {
//                window.location.href = PageContext.path + '/system/common/downloadTempFile?fileName=' + response.data.fileName + '&tempFileName=' + response.data.tempFileName;
//            });


        }


    })

;
