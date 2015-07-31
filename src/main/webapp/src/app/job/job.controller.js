'use strict';

angular.module('webapp')
    .filter('educationResult', function () {
        return function (input) {
            return {0: '无限制', 1: '高中', 2: '大专', 3: '本科', 4: '硕士以上'}[input];
        }
    })

    .filter('experienceResult', function () {
        return function (input) {
            return {0: '无限制', 1: '1-3年', 2: '3-5年'}[input];
        }
    })
    .filter('regionText', function () {
        return function (region) {
            if (region) {
                return (region.parent && region.parent.parent) ? (region.parent.name + ' / ' + region.name) : region.name;
            }
        }
    })
    .filter('jobRegionFilter', function () {
        return function (jobList, currentRegionId) {
            if (currentRegionId && currentRegionId != 'all') {
                var result = [];
                _.forEach(jobList, function (job) {
                    if (job.region.id == currentRegionId || job.region.parent.id == currentRegionId) {
                        result.push(job);
                    }
                });
                return result;
            }
            return jobList;
        }
    })
    .controller('JobCtrl', function ($scope, $state, $http, $modal) {

        $http({
            url: PageContext.path + '/job.do',
            method: 'get'
        }).success(function (response) {
            $scope.jobList = response.jobList;
        });

        $http({
            url: PageContext.path + '/job/region.do',
            method: 'get'
        }).success(function (response) {
            $scope.regionList = response.regionList;
        });

        $scope.currentRegionId = 'all';

        $scope.selectRegion = function (id) {
            $scope.currentRegionId = id;
        };

        $scope.showJob = function (x) {

            $modal.open({
                templateUrl: 'app/job/job.modal.html',
                size: 'lg',
                controller: 'JobModalCtrl',
                resolve: {
                    job: function () {
                        return x;
                    }
                }
            });
        };
    })
    .controller('JobModalCtrl', function ($scope, $modalInstance, job, $timeout) {
        $scope.job = job;
        $scope.dismiss = function(){
            $modalInstance.dismiss();
        };
        $timeout(function(){
            Ps.initialize(document.getElementById('jobRequirements'));
        });
    });
