(function () {
    var app = angular.module('root');

    app.filter('startFrom', function () {
        return function (input, start) {
            if (!input || !input.length) { return; }

            return input.slice(start - 1);
        };
    });

    app.factory("logisticsManageService", ["$http", function ($http) {
        return {
            uploadLogisticsFile: function (file, fnSuccess, fnError) {
                $http.post("/api/logistics/file", file).success(function (data) {
                }).error(function (data, state) {
                });
                return this;
            },
            getLogisticsHistoryInfoCount: function (fnSuccess, fnError) {
                $http.get("/api/productfile/count?status=0&&filetype=2")
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            },
            getLogisticsHistoryInfo: function (pageIndex, fnSuccess, fnError) {
                $http.get("/api/productfile?status=0&&filetype=2&&pageIndex=" + pageIndex)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            }
        };
    }]);

    app.controller("LogisticsManageCtrl", ["$scope", "FileUploader", "logisticsManageService", function ($scope, FileUploader, logisticsManageService) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/api/logistics/file'
            /*headers:{"Content-Type":"multipart/form-data; charset=utf-8"}*/
        });

        //set AccessToken http header
        var accessToken = $scope.context.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

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

            $scope.utils.alert('success', '上传成功！');
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

            getLogisticsHistoryInfo(0);
        };
        uploader.onCompleteAll = function () {
            // console.info('onCompleteAll');
            getLogisticsHistoryInfo(0);
        };

        var LogisticsHistoryFile = function (data) {

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
                    getLogisticsHistoryInfo($scope.currentPage);
                }
            }

            function gotoLastPage() {
                if (!this.onLastPage()) {
                    $scope.currentPage = Math.ceil($scope.totalCounts / this.pageSize) - 1;
                    getLogisticsHistoryInfo($scope.currentPage);
                }
            }

            function goToPage(page) {
                $scope.currentPage = page;
                getLogisticsHistoryInfo($scope.currentPage);
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
                    getLogisticsHistoryInfo($scope.currentPage);
                }
            }

            function previous() {
                if (!this.onFirstPage()) {
                    $scope.currentPage -= 1;
                    getLogisticsHistoryInfo($scope.currentPage);
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

        logisticsManageService.getLogisticsHistoryInfoCount(function (data) {
            $scope.totalCounts = data;
        });

        var getLogisticsHistoryInfo = function (currentPage) {
            logisticsManageService.getLogisticsHistoryInfo(currentPage, function (data) {
                $scope.data = data;
                $scope.dataTable = new LogisticsHistoryFile($scope.data);

                $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
                $scope.itemIndex = 0;
            });
        };

        getLogisticsHistoryInfo(0);
    }]);
})();