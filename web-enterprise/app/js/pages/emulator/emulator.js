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
        $scope.barCode = '';
        $scope.productName = '';
        $scope.expireDate = '';
        $scope.expireDateUnit = '';
        $scope.comment = '';
        $scope.fileInput = '';

        $scope.addProductInfo = function () {
            $scope.productInfos.push({key: '', value: ''});
        }

        $scope.subProductInfo = function () {
            $scope.productInfos.pop();
        };

        $scope.preview = function () {

            var dataPreview = {};
            dataPreview.orgImgUrl = "/api/organization/" + $scope.context.organization.id + "/logo-mobile?access_token=" + $scope.context.getAccessToken();
            dataPreview.proImgUrl = $scope.fileInput;
            dataPreview.barcode = $scope.barCode;
            dataPreview.name = $scope.productName;
            dataPreview.details = $scope.productInfos;

            $('#iphone-6-portrait')[0].contentWindow.refresh(dataPreview);
        };

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
                            $scope.fileInput = this.result;
                        }
                    }
                );
            }

        }, 0);

    }]);
})();