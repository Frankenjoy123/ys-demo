(function(){
    var app = angular.module("a", ["interceptor"]);
    
    app.factory("aService", ["$http", function($http){
            return {
                getInfo: function(fnSuccess, fnError){
                    $http.get("mock/a.json")
                            .success(function(data){
                                fnSuccess(data);
                            });
                }
            };
    }]);
    
    app.controller("aCtrl", ["$scope", "aService", function($scope, aService){
            aService.getInfo(function(data){
                $scope.data = data;
            });
    }]);
})();