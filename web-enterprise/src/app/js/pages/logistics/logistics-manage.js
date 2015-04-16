(function () {
    var app = angular.module("logisticsManage", ["interceptor", "angularFileUpload"]);

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
            getLogisticsHistoryInfo: function (fnSuccess, fnError) {
                $http.get("/api/productfile/createby/1/status/0/filetype/2")
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
                return this;
            }
        };
    }]);

    app.controller("logisticsManageCtrl", ["$scope", "FileUploader", "logisticsManageService", function ($scope, FileUploader, logisticsManageService) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/api/logistics/file'
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

            logisticsManageService.getLogisticsHistoryInfo(function(data){
                $scope.data = data;
                $scope.dataTable = new LogisticsHistoryFile($scope.data);

                $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
            });

            $scope.addAlertMsg(response.message, "info", true);
        };
        uploader.onCompleteAll = function () {
            // console.info('onCompleteAll');
            logisticsManageService.getLogisticsHistoryInfo(function(data){
                $scope.data = data;
                $scope.dataTable = new LogisticsHistoryFile($scope.data);

                $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
            });
        };

        logisticsManageService.getLogisticsHistoryInfo(function(data){
            $scope.data = data;
            $scope.dataTable = new LogisticsHistoryFile($scope.data);

            $scope.isShowHisSec = $scope.data.length > 0 ? 1 : 0;
        });

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };

        console.info('uploader', uploader);
    }]);

    var LogisticsHistoryFile = function (data) {

        var adt = {
            data: data,
            filteredData: {},
            filter: '',
            currentPage: 0,
            pageSize: 10,
            sortColumn: '',
            sortDescending: true,
            numberOfPages: numberOfPages,
            isShowHisSec : isShowHisSec,
            currentPageStart: currentPageStart,
            currentPageEnd: currentPageEnd,
            pages: pages,
            goToPage: goToPage,
            next: next,
            previous: previous,
            onFirstPage: onFirstPage,
            onLastPage: onLastPage,
            sort: sort,
            resetPaging: resetPaging
        };
        return adt;

        function numberOfPages() {
            return Math.min(Math.ceil(this.filteredData.length / this.pageSize), 10);
        }

        function isShowHisSec() {
            return this.filteredData.length > 0 ? 1 : 0;
        }

        function currentPageStart() {
            return this.currentPage * this.pageSize + 1;
        }

        function currentPageEnd() {
            return Math.min((this.currentPage + 1) * this.pageSize, this.filteredData.length);
        }

        function pages() {
            var p = [];
            for (var i = 1; i <= this.numberOfPages(); i++) {
                p.push(i);
            }
            return p;
        }

        function goToPage(page) {
            this.currentPage = page - 1;
        }

        function next() {
            if (!this.onLastPage())
                this.currentPage += 1;
        }

        function previous() {
            if (!this.onFirstPage())
                this.currentPage -= 1;
        }

        function onFirstPage() {
            return this.currentPage === 0;
        }

        function onLastPage() {
            return this.currentPage === this.numberOfPages() - 1;
        }

        function sort(column) {
            this.resetPaging();
            if (this.sortColumn === column) {
                this.sortDescending = !this.sortDescending;
            } else {
                this.sortColumn = column;
                this.sortDescending = false;
            }
        }

        function resetPaging() {
            this.currentPage = 0;
        }
    };
})();