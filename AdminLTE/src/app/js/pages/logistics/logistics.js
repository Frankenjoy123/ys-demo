(function(){
    var app = angular.module("logistics",["interceptor"]);

    app.factory("logisticsService", ["$http", function($http){
        return {
            getInfo: function(fnSuccess, fnError){
                $http.get("mock/logistics.json")
                    .success(function(data){
                        fnSuccess(data);
                    });
            }
        };
    }]);

    app.controller("logisticsCtrl", ["$scope", "logisticsService", function($scope, logisticsService){
        logisticsService.getInfo(function(data){
            $scope.data = data;
        });

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };
    }]);
})();