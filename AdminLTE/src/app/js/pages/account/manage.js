(function(){
    var app = angular.module("accountManage", ["interceptor"]);
    
    app.factory("accountManageService", ["$http", function($http){
            return {
                getInfo: function(fnSuccess, fnError){
                    $http.get("mock/account.json")
                            .success(function(data){
                                fnSuccess(data);
                            });
                }
            };
    }]);
    
    app.controller("accountManageCtrl", ["$scope", "accountManageService", function($scope, accountManageService){
            $scope.data = {
                accounts: []
            };
            accountManageService.getInfo(function(data){
                $scope.data.accounts = data;
            });
    }]);
})();