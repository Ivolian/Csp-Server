'use strict';

angular.module('webapp')
    .controller('NewsCtrl', function ($scope, $http, $state) {

        $scope.pageNo = parseInt($state.params.pageNo || 1, 10) || 1;
        $scope.pageNo = Math.max($scope.pageNo, 1);
        $http({
            url: PageContext.path + '/news.do',
            params: {pageNo: $scope.pageNo, pageSize: 5}
        }).then(function (response) {
            $scope.newsList = response.data.page.content;
            if ($scope.newsList.length == 0) {
                $state.transitionTo('news', {pageNo: response.data.page.totalPages})
            }
            $scope.paginationText = '第 ' + $scope.pageNo + ' 页 / 共 ' + response.data.page.totalPages + ' 页';
            $scope.showPagination = response.data.page.totalPages > 1;
            $scope.showPrevious = !response.data.page.firstPage;
            $scope.showNext = !response.data.page.lastPage;
        });

    })
    .controller('NewsDetailCtrl', function ($scope, $http, $state, $sce) {

        $http({
            url: PageContext.path + '/news/' + $state.params.id + '.do'
        }).then(function (response) {
            console.log(response)
            $scope.news = response.data.content;
        });

        $scope.getContentHtml = function (news) {
            if (news) {
                return $sce.trustAsHtml(news.newsData.data);
            }
        }

    });