'use strict';

angular.module('app')

    .config(function ($stateProvider) {

        $stateProvider.state('qa.answer', {
            url: '/answer',
            displayName: '回答管理',
            templateUrl: 'app/qa/answer/answer.list.html',
            controller: 'AnswerListCtrl'
        });

    })

    .factory('Answer', function (Restangular) {
        return Restangular.service('answer');
    })

    .controller('AnswerListCtrl', function ($scope, SimpleTable, Answer) {

        $scope.grid = SimpleTable(Answer.getList);

        $scope.removeAnswer = function (answer) {
            Dialog.confirmDelete().then(function () {
                answer.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

    })

;
