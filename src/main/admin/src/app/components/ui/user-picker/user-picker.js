'use strict';

angular.module('unicorn.ui')
    .directive('userPicker', function ($http, $modal) {
        return {
            templateUrl: 'components/ui/template/user-picker.html',
            restrict: 'EA',
            replace: true,
            scope: {
                users: '=ngModel',
                placeholder: '@'
            },
            link: function (scope, element, attrs, ngModel) {
                scope.users = scope.users || [];
                scope.removeUser = function (user) {
                    scope.users = _.reject(scope.users, user);
                };
                scope.selectUser = function (user) {
                    var modalInstance = $modal.open({
                        templateUrl: 'components/ui/template/user-picker-select.html',
                        controller: 'UserPickerSelectCtrl',
                        resolve: {
                            users: function () {
                                return _.clone(scope.users);
                            }
                        }
                    });
                    modalInstance.result.then(function (result) {
                        scope.users = result;
                    });
                };
            }
        };
    })
    .controller('UserPickerSelectCtrl', function ($scope, $http, $modalInstance, users) {

        var loadOrganization = function (id) {
            return $http({
                url: PageContext.path + '/system/organization/loadTree',
                method: 'GET',
                params: {
                    node: id
                }
            });
        };

        var loadUser = function (id) {
            return $http({
                url: PageContext.path + '/system/user/listByOrganizationId/' + id,
                method: 'GET'
            });
        };

        $scope.organizationUsers = {};
        $scope.selectedUserList = users || [];
        _.forEach($scope.selectedUserList, function (user) {
            user.selected = true;
        });

        $scope.promise = loadOrganization('Root').success(function (response) {
            $scope.organizations = response.nodes;
            $scope.loadOrganization($scope.organizations[0]);
        });

        $scope.loadOrganization = function (item) {
            $scope.promise = loadOrganization(item.id).success(function (response) {
                item.items = response.nodes;
                _.forEach(item.items, function (item) {
                    item.icon = 'fa fa-fw fa-file';
                });
            });
        };

        $scope.selectOrganization = function (item) {
            if ($scope.selectedOrganization == item) {
                return;
            }
            $scope.selectedOrganization = item;

            $scope.userList = $scope.organizationUsers[item.id];
            if (!$scope.userList) {
                $scope.promise = loadUser(item.id).success(function (response) {
                    $scope.userList = [];
                    _.forEach(response.items, function (item) {
                        var selected = _.where($scope.selectedUserList, {objectId: item.objectId});
                        if (selected.length > 0) {
                            $scope.userList.push(selected[0]);
                        } else {
                            $scope.userList.push({
                                objectId: item.objectId,
                                name: item.name,
                                organization: {
                                    objectId: item.organizationId
                                }
                            });
                        }

                    });
                    $scope.organizationUsers[item.id] = $scope.userList;
                });
            }
        };

        $scope.toggleUser = function (item) {
            item.selected = !item.selected;
            if (item.selected === true) {
                $scope.selectedUserList.push(item);
            } else {
                $scope.selectedUserList = _.reject($scope.selectedUserList, item);
            }
        };

        $scope.getOrganizationUserSelectCount = function (item) {
            var count = 0;
            _.forEach($scope.selectedUserList, function (user) {
                if (user.organization.objectId == item.id) {
                    count++
                }
            });
            return count;
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            _.forEach($scope.selectedUserList, function (user) {
                delete user.selected;
            });
            $modalInstance.close($scope.selectedUserList);
        };
    })
;
