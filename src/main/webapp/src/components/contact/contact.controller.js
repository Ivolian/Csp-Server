'use strict';

angular.module('webapp')
    .controller('ContactCtrl', function ($scope, $http) {

        $scope.comment = {};

        $scope.checkDisabled = function () {
            return _.isEmpty($scope.comment.name) || _.isEmpty($scope.comment.phone) || _.isEmpty($scope.comment.email)
                || _.isEmpty($scope.comment.company) || _.isEmpty($scope.comment.content) || _.isEmpty($scope.captchaText);
        };

        $scope.submit = function () {

            $http({
                url: PageContext.path + '/comment.do',
                method: 'POST',
                data: $scope.comment,
                params: {
                    captchaText: $scope.captchaText
                }
            }).then(function () {
                alert('提交成功！');
                $scope.captchaText = '';
                $scope.comment = {};
            }).catch(function (response) {
                alert(response.data.error)
            })
        };
    });
