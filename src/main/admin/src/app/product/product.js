'use strict';

angular.module('app')
    .config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.when('/product', '/product/list');

        $stateProvider.state('product', {
            parent: 'root',
            url: '/product',
            displayName: '产品管理',
            template: '<div ui-view></div>',
            controller: 'ProductCtrl'
        });

        $stateProvider.state('product.list', {
            url: '/list',
            displayName: '产品列表',
            templateUrl: 'app/product/product.list.html',
            controller: 'ProductListCtrl'
        });

    })

    .filter('productClassificationResult', function () {
        return function (input) {
            return {1: '办案平台', 2: '政务平台', 3: '队伍平台', 4: '公开平台', 5: '数据平台', 6: '企业应用'}[input];
        }
    })


    .factory('Product', function (Restangular) {
        return Restangular.service('question');
    })

    .controller('ProductCtrl', function ($scope, $state) {

    })

    .controller('ProductListCtrl', function ($scope, $state, $modal, SimpleTable, Product) {

        $scope.grid = SimpleTable(Product.getList);

        $scope.createProduct = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/product/product.form.html',
                controller: 'ProductCreateCtrl',
                size: 'lg'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.updateProduct = function (product) {
            var modalInstance = $modal.open({
                templateUrl: 'app/product/product.form.html',
                controller: 'ProductUpdateCtrl',
                size: 'lg',
                resolve: {
                    id: function () {
                        return product.id;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.removeProduct = function (product) {
            Dialog.confirmDelete().then(function () {
                product.remove().then(function () {
                    $scope.grid.refresh();
                });
            });
        };
    })

    .controller('ProductCreateCtrl', function ($scope, $modalInstance, Product) {

        $scope.product = {
            productClassification: 1
        };

        $scope.title = '新增产品';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Product.post($scope.product).then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })

    .controller('ProductUpdateCtrl', function ($scope, $modalInstance, Restangular, Product, id) {

        $scope.promise = Product.one(id).get();

        $scope.product = $scope.promise.$object;

        $scope.title = '修改产品';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = Restangular.copy($scope.product).save().then(function () {
                Toaster.success("保存成功！");
                $modalInstance.close();
            });
        };
    })
;
