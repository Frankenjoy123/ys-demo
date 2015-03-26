(function () {
    var app = angular.module("head", []);
    app.controller("headCtrl", ["$scope", function ($scope) {
            if (!$scope.data) {
                $scope.data = {};
            }
            $scope.data.messages = [{
                    pic: "img/avatar3.png",
                title: "支持工程师",
                    desc: "Why not buy a new awesome theme?",
                time: "5 分钟前"
                }, {
                    pic: "img/avatar3.png",
                title: "设计工程师",
                    desc: "Why not buy a new awesome theme?",
                time: "2 小时前"
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
