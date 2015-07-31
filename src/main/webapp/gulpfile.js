'use strict';

var gulp = require('gulp');

gulp.paths = {
  src: 'src',
  dist: 'dist',
  tmp: '.tmp',
  e2e: 'e2e'
};

require('require-dir')('./gulp');

gulp.task('default', [], function () {
    gulp.start('serve');
});

gulp.task('rebuild', ['clean'], function () {
    gulp.start('build');
});
