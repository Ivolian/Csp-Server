'use strict';

angular.module('app')

    .config(function ($stateProvider) {

        $stateProvider.state('qa.comment', {
            url: '/comment',
            displayName: '评论管理',
            templateUrl: 'app/qa/comment/comment.list.html',
            controller: 'CommentListCtrl'
        });

    })

    .factory('Comment', function (Restangular) {
        return Restangular.service('comment');
    })

    .controller('CommentListCtrl', function ($scope, $state, $modal, SimpleTable, Comment, $http) {

        $scope.grid = SimpleTable(Comment.getList);

        $scope.removeComment = function (comment) {
            Dialog.confirmDelete().then(function () {
                comment.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

    })

;
