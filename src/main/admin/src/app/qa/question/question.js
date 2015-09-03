'use strict';

angular.module('app')

    .config(function ($stateProvider) {

        $stateProvider.state('qa.question', {
            url: '/question',
            displayName: '提问管理',
            templateUrl: 'app/qa/question/question.list.html',
            controller: 'QuestionListCtrl'
        });

    })

    .factory('Question', function (Restangular) {
        return Restangular.service('question');
    })

    .controller('QuestionListCtrl', function ($scope, SimpleTable, Question) {

        $scope.grid = SimpleTable(Question.getList);

        $scope.removeQuestion = function (question) {
            Dialog.confirmDelete().then(function () {
                question.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };

    })

;
