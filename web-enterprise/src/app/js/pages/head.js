(function () {
    var app = angular.module("head", []);
    app.controller("headCtrl", ["$scope", function ($scope) {
            if (!$scope.data) {
                $scope.data = {};
            }
            $scope.data.messages = [{
                    pic: "img/avatar3.png",
                    title: "Support Team",
                    desc: "Why not buy a new awesome theme?",
                    time: "5 mins"
                }, {
                    pic: "img/avatar3.png",
                    title: "AdminLTE Design Team",
                    desc: "Why not buy a new awesome theme?",
                    time: "2 hours"
                }
            ];
            $scope.data.notifications = [{
                    type: "warning",
                    title: "warnings"
                }];
            $scope.data.tasks = [{
                    percentage: "20%"
                }];
        }]);
})();
