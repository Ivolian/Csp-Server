'use strict';

angular.module('webapp')
    .controller('HomeCtrl', function ($scope, $state, $timeout, $interval) {

        $scope.imageList = [
            ['assets/images/s3.jpg', 'about', {}],
            ['assets/images/s5.jpg', 'product.detail', {}],
            ['assets/images/s7.jpg', 'support', {}]

        ];

        $scope.$watch('products', function (products) {
            if (products) {
                $scope.imageList[1][2] = {id: products[0].id};
            }
        });

        $scope.currentImageIndex = 0;

        $scope.prevImage = function (pause) {
            $scope.currentImageIndex = ($scope.currentImageIndex == 0
                ? $scope.imageList.length : $scope.currentImageIndex) - 1;
            if (pause) {
                $interval.cancel(rolling);
                rolling = $scope.startRolling();
            }
            $scope.delayHide();
        };

        $scope.nextImage = function (pause) {
            $scope.currentImageIndex = ($scope.currentImageIndex == $scope.imageList.length - 1
                ? -1 : $scope.currentImageIndex) + 1;
            if (pause) {
                $interval.cancel(rolling);
                rolling = $scope.startRolling();
            }
            $scope.delayHide();
        };

        $scope.changeImage = function (index) {
            $scope.currentImageIndex = index;
            $interval.cancel(rolling);
            rolling = $scope.startRolling();
        };

        //
        $scope.delayHide = function () {
            $scope.animatedHide = false;
            $timeout(function () {
                $scope.animatedHide = true;
            }, 1000);
        };

        $scope.checkVisible = function (index) {
            return !$scope.animatedHide || $scope.currentImageIndex == index;
        };

        $scope.startRolling = function () {
            return $interval(function () {
                $scope.nextImage();
            }, 12 * 1000);
        };

        var rolling = $scope.startRolling();

        $scope.$on('$destroy', function () {
            $interval.cancel(rolling);
        });

    });
