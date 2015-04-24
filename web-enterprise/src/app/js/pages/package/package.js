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
            },
            getPackageHistoryInfoCount: function (fnSuccess, fnError) {
                $http.get("/api/productfile/count?status=0&&filetype=1")
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            },
            getPackageHistoryInfo: function (pageIndex, fnSuccess, fnError) {
                $http.get("/api/productfile?status=0&&filetype=1&&pageIndex=" + pageIndex)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            }
        };
    }]);

    app.controller("packageCtrl", ["$scope", "FileUploader", "packageService", function ($scope, FileUploader, packageService) {
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

            getPackageHistoryInfo(0);
        };
        uploader.onCompleteAll = function () {
            // console.info('onCompleteAll');

            getPackageHistoryInfo(0);
        };

        var PackageHistoryFile = function (data) {

            var adt = {
                data: data,
                filteredData: {},
                pageSize: 10,
                pages: pages,
                isShowHisSec: isShowHisSec,
                goToFirstPage: goToFirstPage,
                gotoLastPage: gotoLastPage,
                goToPage: goToPage,
                currentPage: currentPage,
                next: next,
                previous: previous,
                onFirstPage: onFirstPage,
                onLastPage: onLastPage
            };
            return adt;

            function isShowHisSec() {
                return data.length > 0 ? 1 : 0;
            }

            function goToFirstPage() {
                if (!this.onFirstPage()) {
                    $scope.currentPage = 0;
                    getPackageHistoryInfo($scope.currentPage);
                }
            }

            function gotoLastPage() {
                if (!this.onLastPage()) {
                    $scope.currentPage = Math.ceil($scope.totalCounts / this.pageSize) - 1;
                    getPackageHistoryInfo($scope.currentPage);
                }
            }

            function goToPage(page) {
                $scope.currentPage = page;
                getPackageHistoryInfo($scope.currentPage);
            }

            function currentPage() {
                return $scope.currentPage;
            }

            function pages() {
                var p = [];
                for (var i = Math.max(0, $scope.currentPage - 4); i <= $scope.currentPage; i++) {
                    p.push(i);
                }
                return p;
            }

            function next() {
                if (!this.onLastPage()) {
                    $scope.currentPage += 1;
                    getPackageHistoryInfo($scope.currentPage);
                }
            }

            function previous() {
                if (!this.onFirstPage()) {
                    $scope.currentPage -= 1;
                    getPackageHistoryInfo($scope.currentPage);
                }

            }

            function onFirstPage() {
                return $scope.currentPage === 0;
            }

            function onLastPage() {
                return data.length < this.pageSize;
            }
        };

        $scope.currentPage = 0;
        $scope.totalCounts = 0;
        $scope.itemIndex = 0;

        $scope.getItemIndex = function() {
            $scope.itemIndex += 1;
            return $scope.itemIndex;
        }

        packageService.getPackageHistoryInfoCount(function (data) {
            $scope.totalCounts = data;
        });

        var getPackageHistoryInfo = function (currentPage) {
            packageService.getPackageHistoryInfo(currentPage, function (data) {
                $scope.data = data;
                $scope.dataTable = new PackageHistoryFile($scope.data);

                $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
                $scope.itemIndex = 0;
            });
        };

        getPackageHistoryInfo(0);

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };
    }]);
})();