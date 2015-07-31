'use strict';

angular.module('webapp')
    .controller('CultureCtrl', function ($scope, $state, $http, $timeout) {

        $scope.pageNo = parseInt($state.params.pageNo || 1, 10) || 1;
        $scope.pageNo = Math.max($scope.pageNo, 1);
        $http({
            url: PageContext.path + '/culture.do',
            params: {pageNo: $scope.pageNo, pageSize: 9}
        }).then(function (response) {
            $scope.cultureList = response.data.page.content;
            $timeout(function () {
                $scope.initThumbs();
            }, 10)
        });

        $scope.showThumbs = false;

        $scope.initThumbs = function () {
            (function ($, sr) {
                // debouncing function from John Hann
                var debounce = function (func, threshold, execAsap) {
                    var timeout;
                    return function debounced() {
                        var obj = this, args = arguments;

                        function delayed() {
                            if (!execAsap)
                                func.apply(obj, args);
                            timeout = null;
                        };
                        if (timeout)
                            clearTimeout(timeout);
                        else if (execAsap)
                            func.apply(obj, args);
                        timeout = setTimeout(delayed, threshold || 100);
                    };
                };
                //smartresize
                jQuery.fn[sr] = function (fn) {
                    return fn ? this.bind('resize', debounce(fn)) : this.trigger(sr);
                };
            })(jQuery, 'smartresize');


            $(function () {
                //check if the user made the
                //mistake to open it with IE
                var ie = false;
                if ($.browser.msie)
                    ie = true;
                //flag to control the click event
                var flg_click = true;
                //the wrapper
                var $im_wrapper = $('#im_wrapper');
                //the thumbs
                var $thumbs = $im_wrapper.children('div');
                //all the images
                var $thumb_imgs = $thumbs.find('img');
                //number of images
                var nmb_thumbs = $thumbs.length;
                //image loading status
                var $im_loading = $('#im_loading');
                //number of thumbs per line
                var per_line = 3;
                //number of thumbs per column
                var per_col = Math.ceil(nmb_thumbs / per_line);
                //index of the current thumb
                var current = -1;
                //mode = grid | single
                var mode = 'grid';
                //an array with the positions of the thumbs
                //we will use it for the navigation in single mode
                var positionsArray = [];
                for (var i = 0; i < nmb_thumbs; ++i) {
                    positionsArray[i] = i;
                }
                //preload all the images
                $im_loading.show();
                var loaded = 0;
                $thumb_imgs.each(function () {
                    var $this = $(this);
                    $('<img/>').load(function () {
                        ++loaded;
                        if (loaded == nmb_thumbs) {
                            start();
                            $scope.showThumbs = true;
                            $scope.$apply('showThumbs');
                        }

                    }).attr('src', $this.attr('src'));
                });

                function start() {
                    $im_loading.hide();
                    disperse();
                }

                //disperses the thumbs in a grid based on windows dimentions
                function disperse() {
                    if (!flg_click) return;
                    setflag();
                    mode = 'grid';
                    //center point for first thumb along the width of the window
                    var spaces_w = $('#im_wrapper').width() / (per_line + 1);
                    //center point for first thumb along the height of the window
                    var spaces_h = $('#im_wrapper').height() / (per_col + 1);
                    //let's disperse the thumbs equally on the page
                    $thumbs.each(function (i) {
                        var $thumb = $(this);
                        //calculate left and top for each thumb,
                        //considering how many we want per line
                        var left = spaces_w * ((i % per_line) + 1) - $thumb.width() / 2;
                        var top = spaces_h * (Math.ceil((i + 1) / per_line)) - $thumb.height() / 2;
                        //lets give a random degree to each thumb
                        var r = Math.floor(Math.random() * 41) - 20;
                        /*
                         now we animate the thumb to its final positions;
                         we also fade in its image, animate it to 115x115,
                         and remove any background image	of the thumb - this
                         is not relevant for the first time we call disperse,
                         but when changing from single to grid mode
                         */
                        if (ie)
                            var param = {
                                'left': left + 'px',
                                'top': top + 'px'
                            };
                        else
                            var param = {
                                'left': left + 'px',
                                'top': top + 'px',
                                'rotate': r + 'deg'
                            };
                        $thumb.stop()
                            .animate(param, 400, function () {
                                if (i == nmb_thumbs - 1)
                                    setflag();
                            })
                            .find('img')
                            .fadeIn(400, function () {
                                $thumb.css({
                                    'background-image': 'none'
                                });
                                $(this).animate({
                                    'width': '165px',
                                    'height': '115px',
                                    'marginTop': '5px',
                                    'marginLeft': '5px'
                                }, 150);
                            });
                    });
                }

                //controls if we can click on the thumbs or not
                //if theres an animation in progress
                //we don't want the user to be able to click
                function setflag() {
                    flg_click = !flg_click
                }

                //on windows resize call the disperse function
                $(window).smartresize(function () {
                    disperse();
                });

                //function to shuffle an array
                Array.shuffle = function (array) {
                    for (
                        var j, x, i = array.length; i;
                        j = parseInt(Math.random() * i),
                            x = array[--i], array[i] = array[j], array[j] = x
                        );
                    return array;
                };
            });
        }
    })
    .controller('CultureDetailCtrl', function ($scope, $http, $state, $sce) {

        $http({
            url: PageContext.path + '/culture/' + $state.params.id + '.do'
        }).then(function (response) {
            $scope.culture = response.data.content;
        });

        $scope.getContentHtml = function (culture) {
            if (culture) {
                return $sce.trustAsHtml(culture.contentData.data);
            }
        }
    });
