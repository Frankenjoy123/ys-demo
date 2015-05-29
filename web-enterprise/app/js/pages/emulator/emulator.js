(function () {
    var app = angular.module('root');

    app.factory("emulatorService", ["$http", function ($http) {
        return {
            getInfo: function (productKey, fnSuccess, fnError) {
                $http.get("/api/logistics/" + productKey)
                    .success(function (data) {
                        fnSuccess(data);
                    }).error(function (data, state) {
                        fnSuccess();
                    });
            }
        };
    }]);

    app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", function ($scope, emulatorService, $timeout) {

        $scope.productInfos = [{key: '', value: ''}];

        $scope.addProductInfo = function () {
            $scope.productInfos.push({key: '', value: ''});
        }

        $scope.subProductInfo = function (data) {
            $scope.productInfos.pop();
        }

        $timeout(function () {

            var divImgWrap = $("#divImgWrap");
            var fileInput = $("#fileInput");
            var imgProductbase = $("#imgProductbase");

            if (typeof FileReader === 'undefined') {
                divImgWrap.html("您的浏览器不支持图片预览");
            } else {
                fileInput.change(function () {
                        var file = this.files[0];
                        var reader = new FileReader();
                        reader.readAsDataURL(file);
                        reader.onload = function (e) {
                            imgProductbase.attr('src', this.result);
                        }
                    }
                );
            }

        }, 0);

    }]);
})();