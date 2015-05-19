(function () {
    var app = angular.module('root');

    app.factory("packageSearchService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/package/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }

            //getInfo: function(productKey, fnSuccess, fnError){
            //    $http.get("mock/package.json")
            //        .success(function(data){
            //            fnSuccess(data);
            //        });
            //}
        };
    }]);

    app.controller("packageSearchCtrl", ["$scope", "packageSearchService", function ($scope, packageSearchService) {

        $scope.productKey = "";

        $scope.bodyShow = 0;

        function listAllNode(thejson) {

            if (thejson == null)
                return;

            var item = {};
            item.text = thejson["key"];
            item.nodes = [];

            var rootNode = {};
            rootNode.text = "产品数量: " + '(' + thejson["productCount"] + ')';
            item.nodes.push(rootNode);

            if (thejson["subPackages"] == null)
                return;

            if (thejson["subPackages"] instanceof Array) {
                for (var e in thejson["subPackages"]) {
                    item.nodes.push(listAllNode(thejson["subPackages"][e]));
                }
            }
            else {
                item.nodes.push(listAllNode(thejson["subPackages"]));
            }

            return item;
        }

        $scope.productKeyClick = function () {

            if ($scope.productKey == null || $scope.productKey == "") {
                $scope.bodyShow = 0;
                return;
            }

            packageSearchService.getInfo($scope.productKey, function (data) {
                $scope.origialdata = data;
                $scope.resultdata = [];
                $scope.resultdata.push(listAllNode(data));
                $scope.bodyShow = 1;

                $('#tree').treeview({data: $scope.resultdata, color: "#3c8dbc"});
            });
        };
    }]);
})();