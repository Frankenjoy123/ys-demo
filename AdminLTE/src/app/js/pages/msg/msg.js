(function(){
    var app = angular.module("msg",["interceptor"]);

    app.filter('startFrom', function () {
        return function (input, start) {
            return input.slice(start - 1);
        };
    });

    app.factory("msgService", ["$http", function($http){
        return {
            getInfo: function(fnSuccess, fnError){
                $http.get("mock/msg.json")
                    .success(function(data){
                        fnSuccess(data);
                    });
            }
        };
    }]);

    app.controller("msgCtrl", ["$scope", "msgService", function($scope, msgService){
        msgService.getInfo(function(data){
            $scope.data = data;
            $scope.dataTable = new AngularDataTable($scope.data);
        });

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };
    }]);

    var AngularDataTable = function (data) {

        var adt = {
            data: data,
            filteredData: {},
            filter: '',
            currentPage: 0,
            pageSize: 10,
            sortColumn: '',
            sortDescending: true,
            numberOfPages: numberOfPages,
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
            return Math.ceil(this.filteredData.length / this.pageSize);
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