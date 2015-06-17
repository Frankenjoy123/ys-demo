(function () {
    var app = angular.module('root');

    app.factory('accountManageService', ['$http', function ($http) {
        return {
            getAccounts: function (dataTable, fnSuccess, fnError) {
                $http.get('/api/account?' + dataTable.toString()).success(fnSuccess);

                return this;
            },
            createAccount: function (account, fnSuccess, fnError) {
                $http.post("/api/account", account).success(fnSuccess).error(fnError);

                return this;
            }
        };
    }]);

    app.controller('AccountManageCtrl', [
        '$scope',
        '$timeout',
        'accountManageService',
        'dataFilterService',
        function ($scope, $timeout, accountManageService, dataFilterService) {

            $scope.accountIdentifier = '';
            $scope.lastName = '';
            $scope.firstName = '';
            $scope.email = '';
            $scope.phone = '';
            $scope.password = '';
            $scope.passwordConfirm = '';

            $scope.dashBoardRead = '';
            $scope.productKeyRead = '';
            $scope.productKeyMng = '';
            $scope.packageRead = '';
            $scope.packageMng = '';
            $scope.logisticsRead = '';
            $scope.logisticsMng = '';
            $scope.tieMaRead = '';
            $scope.saoMaRead = '';
            $scope.monMaRead = '';
            $scope.placeMaRead = '';
            $scope.productRead = '';
            $scope.productMng = '';
            $scope.msgRead = '';
            $scope.deviceRead = '';
            $scope.deviceMng = '';
            $scope.accountRead = '';
            $scope.accountMng = '';
            $scope.passwordRead = '';

            $scope.authAccount = function(){

            };

            $scope.accountTable = new $scope.utils.DataTable({
                sortable: {
                    target: '#sort-bar',
                    sort: 'createdDateTime,desc'
                },
                pageable: {
                    page: 0,
                    size: 20
                },
                flush: function (callback) {
                    accountManageService.getAccounts(this, function (data, status, headers) {
                        callback({data: data, headers: headers});
                    });
                }
            });

            $timeout(function () {

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

                $('#accountForm').bootstrapValidator({
                    message: '输入不合法',
                    feedbackIcons: {
                        valid: 'fa fa-check-circle fa-lg text-success',
                        invalid: 'fa fa-times-circle fa-lg',
                        validating: 'fa fa-refresh'
                    },
                    fields: {
                        accountIdentifier: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入账号标识'
                                }
                            }
                        },
                        lastName: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入姓'
                                }
                            }
                        },
                        firstName: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入名'
                                }
                            }
                        },
                        email: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入邮箱'
                                },
                                emailAddress: {
                                    message: '邮箱输入不合法'
                                }
                            }
                        },
                        phone: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入电话'
                                },
                                digits: {
                                    message: '电话只能是数字'
                                }
                            }
                        },
                        password: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入密码'
                                }
                            }
                        },
                        passwordConfirm: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: '请输入确认密码'
                                },
                                identical: {
                                    field: 'password',
                                    message: '确认密码不一致'
                                }
                            }
                        }
                    }
                }).on('success.form.#accountForm', function (e, data) {

                    var $parent = data.element.parents('.form-group');
                    // Remove the has-success class
                    $parent.removeClass('has-success');
                });

            }, 0);

            $scope.createAccount = function () {

                if ($scope.accountIdentifier == '')
                    return;

                if ($scope.firstName == '')
                    return;

                if ($scope.lastName == '')
                    return;

                if ($scope.email == '')
                    return;

                if ($scope.phone == '')
                    return;

                if ($scope.password == '')
                    return;

                var account = {};

                account.org_id = $scope.context.organization.id;
                account.identifier = $scope.accountIdentifier;
                account.first_name = $scope.firstName;
                account.last_name = $scope.lastName;
                account.email = $scope.email;
                account.phone = $scope.phone;
                account.password = $scope.password;

                accountManageService.createAccount(account, function (data) {
                    $scope.utils.alert('success', '创建账号成功');
                }, function (data) {
                    $scope.utils.alert('info', '创建账号失败');
                });
            };

        }]);
})();