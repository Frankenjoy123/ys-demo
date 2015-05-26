(function () {
    var app = angular.module('root');

    app.filter('startFrom', function () {
        return function (input, start) {
            if (!input || !input.length) {
                return;
            }

            return input.slice(start - 1);
        };
    });

    app.factory("messageService", ["$http", function ($http) {
        return {
            getInfo: function (org_id, pageIndex, fnSuccess, fnError) {
                $http.get("/api/message?orgid=" + org_id + "&&pageIndex=" + pageIndex)
                    .success(function (data) {
                        fnSuccess(data);
                    });
            }
        };
    }]);

    app.controller("MessageCtrl", ["$scope", "$timeout", "messageService", function ($scope, $timeout, messageService) {

        var AngularDataTable = function (data) {

            var adt = {
                data: data,
                filteredData: {},
                pageSize: 10,
                pages: pages,
                sortColumn: '',
                sortDescending: true,
                isShowHisSec: isShowHisSec,
                goToFirstPage: goToFirstPage,
                gotoLastPage: gotoLastPage,
                goToPage: goToPage,
                currentPage: currentPage,
                next: next,
                previous: previous,
                onFirstPage: onFirstPage,
                onLastPage: onLastPage,
                sort: sort,
                resetPaging: resetPaging
            };
            return adt;

            function isShowHisSec() {
                return data.length > 0 ? 1 : 0;
            }

            function goToFirstPage() {
                if (!this.onFirstPage()) {
                    $scope.currentPage = 0;
                    getMessageInfo($scope.currentPage);
                }
            }

            function gotoLastPage() {
                if (!this.onLastPage()) {
                    $scope.currentPage = Math.ceil($scope.totalCounts / this.pageSize) - 1;
                    getMessageInfo($scope.currentPage);
                }
            }

            function goToPage(page) {
                $scope.currentPage = page;
                getMessageInfo($scope.currentPage);
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
                    getMessageInfo($scope.currentPage);
                }
            }

            function previous() {
                if (!this.onFirstPage()) {
                    $scope.currentPage -= 1;
                    getMessageInfo($scope.currentPage);
                }

            }

            function onFirstPage() {
                return $scope.currentPage === 0;
            }

            function onLastPage() {
                return data.length < this.pageSize;
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

        $scope.currentPage = 0;
        $scope.totalCounts = 0;
        $scope.itemIndex = 0;

        function getMessageInfo(currentPage) {
            messageService.getInfo($scope.context.account.org_id, currentPage, function (data) {
                $scope.data = data;
                $scope.dataTable = new AngularDataTable($scope.data);
            });
        }

        $timeout(function () {
            getMessageInfo(0);
        }, 1000);

    }]);
})();