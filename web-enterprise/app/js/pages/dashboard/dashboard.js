(function () {
    var app = angular.module("dashboard", ["interceptor"]);

    app.factory("dashboardService", ["$http", function ($http) {
        return {};
    }]);

    app.controller("dashboardCtrl", ["$scope", "dashboardService", function ($scope, dashboardService) {

    }]);
})();