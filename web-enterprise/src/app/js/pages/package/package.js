(function () {
    var app = angular.module("package", ["interceptor", "angularFileUpload"]);

    app.factory("packageService", ["$http", function ($http) {
        return {
            getInfo: function (packageKey, fnSuccess, fnError) {
                $http.get("/api/package/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            },
            uploadPackageFile: function (file, fnSuccess, fnError) {
                $http.post("/api/package/file", file).success(function (data) {
                }).error(function (data, state) {
                });
                return this;
            }
        };
    }]);

    app.controller("packageCtrl", ["$scope", "FileUploader", function ($scope, FileUploader) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/api/package/file'
            /*headers:{"Content-Type":"multipart/form-data; charset=utf-8"}*/
        });

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            },
            name: ''
        });
        // CALLBACKS

        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function (fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function (addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function (item) {
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function (fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function (progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
            console.info('onSuccessItem', response, status);
        };
        uploader.onErrorItem = function (fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
           // var dataObj=eval("("+response+")");

        };
        uploader.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            // console.info('onCompleteItem', fileItem, response, status, headers);
            console.info('onCompleteItem', response, status);
            $scope.addAlertMsg(response.message, "info", true);
        };
        uploader.onCompleteAll = function () {
           // console.info('onCompleteAll');
        };

        console.info('uploader', uploader);
    }]);
})();