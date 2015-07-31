'use strict';

angular.module('webapp')
    .controller('NavbarCtrl', function ($scope, $state) {
        $scope.menus = [
            {name: '解决方案', state: 'product', items: [
                {name: '办案平台', productClassification: 1, icon: 'fa fa-graduation-cap b1', items: []},
                {name: '政务平台', productClassification: 2, icon: 'fa fa-suitcase b2', items: []},
//                {name: '队伍平台',  productClassification: 3, icon: 'fa fa-flag b3', items: [ ]},
                {name: '公开平台', productClassification: 4, icon: 'fa fa-folder-open-o  b3', items: []},
                {name: '数据平台', productClassification: 5, icon: 'fa fa-database b4', items: []},
                {name: '企业应用', productClassification: 6, icon: 'fa fa-cubes b5', items: []}
            ]
            },
            {name: '软件产品', state: 'software'},
            {name: '行业新闻', state: 'news'},
            {name: '服务支持', state: 'support'},
            {name: '人才招聘', state: 'job'},
            {name: '企业文化', state: 'culture'},

            {name: '关于我们', state: 'about'}
        ];

        $scope.state = $state;

        $scope.$watch('products', function (value) {
            if (value) {
                var productMenu = $scope.menus[0];
                _.forEach(value, function (product) {
                    _.forEach(productMenu.items, function (menu) {
                        if (menu.productClassification == product.productClassification) {
                            menu.items.push(
                                {
                                    id: product.id,
                                    name: product.productName
                                }
                            )
                        }
                    });

                });
            }
        });
    });
