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

    .controller('CourtDataCtrl', function ($scope, $state, $modal, SimpleTable, courtData) {

        $scope.grid = SimpleTable(courtData.getList);

        $scope.grid.pageSize = 20;
        $scope.grid.queryInfo.beginDate = new Date();
        $scope.grid.queryInfo.endDate = new Date();
        $scope.grid.queryInfo.onlyNotLogin = false;

    })

;
