var gulp = require('gulp'),
    minifycss = require('gulp-minify-css'),
    concat = require('gulp-concat'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename');

gulp.task('minifyjs', function () {
    return gulp.src('app/js/pages/**/*.js')
        .pipe(concat('main_page.js'))    //合并所有js到main.js
        .pipe(gulp.dest('app/js'))    //输出main.js到文件夹
        .pipe(rename({suffix: '.min'}))   //rename压缩后的文件名
        .pipe(uglify())    //压缩
        .pipe(gulp.dest('app/js'));  //输出
});

// 预设任务
gulp.task('default', function () {
    gulp.start('minifyjs');
});