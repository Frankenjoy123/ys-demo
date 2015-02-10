(function(){
    var app = angular.module("msg",["interceptor"]);

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
        });

        $scope.getDateString = function (value) {
            var date = new Date(value);
            return new DateTime(date).toString('yyyy-MM-dd HH:mm:ss');
        };

    }]);
})();