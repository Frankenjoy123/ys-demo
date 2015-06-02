(function () {
    var app = angular.module('root');

    app.factory("emulatorService", ["$http", function ($http) {
        return {
            createProWithDetail: function (proDetail, fnSuccess, fnError) {
                $http.post("/api/productbase/withdetail", proDetail).success(fnSuccess).error(fnError);

                return this;
            }
        };
    }]);

    app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "FileUploader", function ($scope, emulatorService, $timeout, FileUploader) {

        var uploader = $scope.uploader = new FileUploader({
            url: ''
        });

        //set AccessToken http header
        var accessToken = $scope.context.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        $scope.productInfos = [{key: '', value: ''}];
        $scope.barCode = '';
        $scope.productName = '';
        $scope.expireDate = 0;
        $scope.expireDateUnit = '';
        $scope.comment = '';
        $scope.fileInput = '';
        $scope.keyTypePubInput = '';
        $scope.keyTypePriInput = '';
        $scope.keyTypeRFIDInput = '';

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

        $scope.submit = function () {
            var proWithDetails = {};

            proWithDetails.categoryId = 0;
            proWithDetails.barcode = $scope.barCode;
            proWithDetails.name = $scope.productName;
            proWithDetails.comment = $scope.comment;
            proWithDetails.productKeyTypeCodes = [];
            if ($scope.keyTypePubInput)
                proWithDetails.productKeyTypeCodes.push("qr_public");
            if ($scope.keyTypePriInput)
                proWithDetails.productKeyTypeCodes.push("qr_secure");
            if ($scope.keyTypeRFIDInput)
                proWithDetails.productKeyTypeCodes.push("rfid");

            proWithDetails.shelfLife = $scope.expireDate - 0;
            if ($scope.expireDateUnit == "年")
                proWithDetails.shelfLifeInterval = 'year';
            else if ($scope.expireDateUnit == "月")
                proWithDetails.shelfLifeInterval = 'month';
            else if ($scope.expireDateUnit == "周")
                proWithDetails.shelfLifeInterval = 'week';
            else if ($scope.expireDateUnit == "天")
                proWithDetails.shelfLifeInterval = 'day';
            else if ($scope.expireDateUnit == "小时")
                proWithDetails.shelfLifeInterval = 'hour';

            proWithDetails.status = '待审核';

            var proDetails = {};
            proDetails.details = {};
            for (var proInfo in $scope.productInfos) {
                if ($scope.productInfos[proInfo].key != '')
                    proDetails.details[$scope.productInfos[proInfo].key] = $scope.productInfos[proInfo].value;
            }

            proWithDetails.proDetails = JSON.stringify(proDetails);

            try {
                emulatorService.createProWithDetail(proWithDetails, function (data) {
                        if (data.id != null && data.id != '') {
                            uploader.queue[0].url = '/api/productbase/withdetailfile/' + data.id + "/full-mobile";
                            uploader.queue[0].headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken;

                            uploader.uploadAll();

                            $scope.utils.alert('success', '创建产品成功');
                        }
                    },
                    function (data, state) {
                        $scope.utils.alert('error', '创建产品失败');
                    });
            }
            catch (ex) {

            }
        };

        $timeout(function () {

            var divImgWrap = $("#divImgWrap");
            var fileInput = $("#fileInput");
            var imgProductbase = $("#imgProductbase");

            if (typeof FileReader === 'undefined') {
                divImgWrap.html("您的浏览器不支持图片预览");
            } else {
                fileInput.change(function () {

                        var file = uploader.queue[0]._file;
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