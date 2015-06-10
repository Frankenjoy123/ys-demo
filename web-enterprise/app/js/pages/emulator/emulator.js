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

    app.controller("emulatorCtrl", ["$scope", "emulatorService", "$timeout", "FileUploader", "$location", function ($scope, emulatorService, $timeout, FileUploader, $location) {

        var uploader = $scope.uploader = new FileUploader({
            url: ''
        });

        //set AccessToken http header
        var accessToken = $scope.context.getAccessToken();
        accessToken && (uploader.headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken);

        $scope.productInfos = [{key: '', value: ''}];
        $scope.productAddress = [{address: '', tel: ''}];

        $scope.barCode = '';
        $scope.productName = '';
        $scope.expireDate = 1;
        $scope.expireDateUnit = '';
        $scope.comment = '';
        $scope.fileInput = '';
        $scope.keyTypePubInput = '';
        $scope.keyTypePriInput = '';
        $scope.keyTypeRFIDInput = '';
        $scope.hotline = '';
        $scope.support = '';
        $scope.taobao = '';
        $scope.tmall = '';

        $scope.addProductInfo = function () {
            $scope.productInfos.push({key: '', value: ''});
        }

        $scope.subProductInfo = function () {
            $scope.productInfos.pop();
        };

        $scope.addProAddress = function () {
            $scope.productAddress.push({address: '', tel: ''});
        }

        $scope.subProAddress = function () {
            $scope.productAddress.pop();
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
            if ($scope.barCode == '') {
                $scope.utils.alert('info', '产品BarCode不能为空');
                return;
            }

            if ($scope.productName == '') {
                $scope.utils.alert('info', '产品名不能为空');
                return;
            }

            if (!$scope.keyTypePubInput && !$scope.keyTypePriInput && !$scope.keyTypeRFIDInput) {
                $scope.utils.alert('info', '产品码类型至少要选择一种');
                return;
            }

            if ($scope.expireDateUnit == '') {
                $scope.utils.alert('info', '请选择产品过期单位');
                return;
            }

            if (!(/(^[1-9]\d*$)/.test($scope.expireDate))) {
                $scope.utils.alert('info', '产品过期时间应为正整数');
                return;
            }

            if (uploader.queue.length == 0) {
                $scope.utils.alert('info', '产品图片不能为空');
                return;
            }

            var proWithDetails = {};

            proWithDetails.category_id = 0;
            proWithDetails.barcode = $scope.barCode;
            proWithDetails.name = $scope.productName;
            proWithDetails.comments = $scope.comment;
            proWithDetails.product_key_type_codes = [];
            if ($scope.keyTypePubInput)
                proWithDetails.product_key_type_codes.push("qr_public");
            if ($scope.keyTypePriInput)
                proWithDetails.product_key_type_codes.push("qr_secure");
            if ($scope.keyTypeRFIDInput)
                proWithDetails.product_key_type_codes.push("rfid");

            proWithDetails.shelf_life = $scope.expireDate - 0;
            if ($scope.expireDateUnit == "年")
                proWithDetails.shelf_life_interval = 'year';
            else if ($scope.expireDateUnit == "月")
                proWithDetails.shelf_life_interval = 'month';
            else if ($scope.expireDateUnit == "周")
                proWithDetails.shelf_life_interval = 'week';
            else if ($scope.expireDateUnit == "天")
                proWithDetails.shelf_life_interval = 'day';
            else if ($scope.expireDateUnit == "小时")
                proWithDetails.shelf_life_interval = 'hour';

            proWithDetails.status_code = '待审核';

            var proDetails = {};
            proDetails.details = {};
            for (var proInfo in $scope.productInfos) {
                if ($scope.productInfos[proInfo].key != '' && $scope.productInfos[proInfo].value != '')
                    proDetails.details[$scope.productInfos[proInfo].key] = $scope.productInfos[proInfo].value;
            }

            proDetails.contact = {};
            proDetails.contact['hotline'] = $scope.hotline;
            proDetails.contact['support'] = $scope.support;

            proDetails['e-commerce'] = {};
            proDetails['e-commerce'].taobao = $scope.taobao;
            proDetails['e-commerce'].tmall = $scope.tmall;

            proDetails['t-commerce'] = [];
            for (var proAddress in $scope.productAddress) {
                if ($scope.productAddress[proAddress].address != '' && $scope.productAddress[proAddress].tel != '') {
                    var tComItem = {};
                    tComItem.address = $scope.productAddress[proAddress].address;
                    tComItem.tel = $scope.productAddress[proAddress].tel;

                    proDetails['t-commerce'].push(tComItem);
                }
            }

            proWithDetails.details = JSON.stringify(proDetails);

            try {
                emulatorService.createProWithDetail(proWithDetails, function (data) {
                        if (data.id != null && data.id != '') {
                            uploader.queue[uploader.queue.length - 1].url = '/api/productbase/withdetailfile/' + data.id + "/full-mobile";
                            uploader.queue[uploader.queue.length - 1].headers[$scope.YUNSOO_CONFIG.HEADER_ACCESS_TOKEN] = accessToken;

                            uploader.uploadAll();

                            $scope.utils.alert('success', '创建产品成功');

                            $location.path('/product-base-manage');
                        }
                    },
                    function (data, state) {
                        $scope.utils.alert('info', '创建产品失败');
                    });
            }
            catch (ex) {

            }
        };

        $timeout(function () {

            /* ========================================================================
             * NIFTY CHECK v1.1
             * -------------------------------------------------------------------------
             * - ThemeOn.net -
             * ========================================================================*/
            !function ($) {
                "use strict";

                var allFormEl,
                    formElement = function (el) {
                        if (el.data('nifty-check')) {
                            return;
                        } else {
                            el.data('nifty-check', true);
                            if (el.text().trim().length) {
                                el.addClass("form-text");
                            } else {
                                el.removeClass("form-text");
                            }
                        }


                        var input = el.find('input')[0],
                            groupName = input.name,
                            $groupInput = function () {
                                if (input.type == 'radio' && groupName) {
                                    return $('.form-radio').not(el).find('input').filter('input[name=' + groupName + ']').parent();
                                } else {
                                    return false;
                                }
                            }(),
                            changed = function () {
                                if (input.type == 'radio' && $groupInput.length) {
                                    $groupInput.each(function () {
                                        var $gi = $(this);
                                        if ($gi.hasClass('active')) $gi.trigger('nifty.ch.unchecked');
                                        $gi.removeClass('active');
                                    });
                                }


                                if (input.checked) {
                                    el.addClass('active').trigger('nifty.ch.checked');
                                } else {
                                    el.removeClass('active').trigger('nifty.ch.unchecked');
                                }
                            };

                        if (input.checked) {
                            el.addClass('active');
                        } else {
                            el.removeClass('active');
                        }

                        $(input).on('change', changed);
                    },
                    methods = {
                        isChecked: function () {
                            return this[0].checked;
                        },
                        toggle: function () {
                            this[0].checked = !this[0].checked;
                            this.trigger('change');
                            return null;
                        },
                        toggleOn: function () {
                            if (!this[0].checked) {
                                this[0].checked = true;
                                this.trigger('change');
                            }
                            return null;
                        },
                        toggleOff: function () {
                            if (this[0].checked && this[0].type == 'checkbox') {
                                this[0].checked = false;
                                this.trigger('change');
                            }
                            return null;
                        }
                    };

                $.fn.niftyCheck = function (method) {
                    var chk = false;
                    this.each(function () {
                        if (methods[method]) {
                            chk = methods[method].apply($(this).find('input'), Array.prototype.slice.call(arguments, 1));
                        } else if (typeof method === 'object' || !method) {
                            formElement($(this));
                        }
                        ;
                    });
                    return chk;
                };

                nifty.document.ready(function () {
                    allFormEl = $('.form-checkbox, .form-radio');
                    if (allFormEl.length) allFormEl.niftyCheck();
                });

                nifty.document.on('change', '.btn-file :file', function () {
                    var input = $(this),
                        numFiles = input.get(0).files ? input.get(0).files.length : 1,
                        label = input.val().replace(/\\/g, '/').replace(/.*\//, ''),
                        size = function () {
                            try {
                                return input[0].files[0].size;
                            } catch (err) {
                                return 'Nan';
                            }
                        }(),
                        fileSize = function () {
                            if (size == 'Nan') {
                                return "Unknown";
                            }
                            var rSize = Math.floor(Math.log(size) / Math.log(1024));
                            return ( size / Math.pow(1024, rSize) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][rSize];
                        }();


                    input.trigger('fileselect', [numFiles, label, fileSize]);
                });
            }(jQuery);

            var divImgWrap = $("#divImgWrap");
            var fileInput = $("#fileInput");
            var imgProductbase = $("#imgProductbase");

            if (typeof FileReader === 'undefined') {
                divImgWrap.html("您的浏览器不支持图片预览");
            } else {
                fileInput.change(function () {

                        var file = uploader.queue[uploader.queue.length - 1]._file;
                        var reader = new FileReader();
                        reader.readAsDataURL(file);
                        reader.onload = function (e) {
                            imgProductbase.attr('src', this.result);
                            $scope.fileInput = this.result;
                        }

                        uploader.queue = uploader.queue.slice(uploader.queue.length - 1, uploader.queue.length);
                    }
                );
            }

            $('#createProduct').bootstrapValidator({
                message: '该字段不能为空',
                feedbackIcons: {
                    valid: 'fa fa-check-circle fa-lg text-success',
                    invalid: 'fa fa-times-circle fa-lg',
                    validating: 'fa fa-refresh'
                },
                fields: {
                    barCode: {
                        validators: {
                            notEmpty: {
                                message: '请输入产品BarCode'
                            }
                        }
                    },
                    productName: {
                        validators: {
                            notEmpty: {
                                message: '请输入产品名'
                            }
                        }
                    },
                    greaterthan: {
                        validators: {
                            notEmpty: {
                                message: '请输入产品过期时间'
                            },
                            greaterThan: {
                                inclusive: false,
                                //If true, the input value must be greater than or equal to the comparison one.
                                //If false, the input value must be greater than the comparison one
                                value: 0,
                                message: '请输入大于1的正整数'
                            }
                        }
                    }
                }
            }).on('success.field.bv', function (e, data) {
                // $(e.target)  --> The field element
                // data.bv      --> The BootstrapValidator instance
                // data.field   --> The field name
                // data.element --> The field element

                var $parent = data.element.parents('.form-group');

                // Remove the has-success class
                $parent.removeClass('has-success');
            });
        }, 0);

    }]);
})();