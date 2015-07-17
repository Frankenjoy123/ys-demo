(function () {
  var app = angular.module('root');

  app.factory("deviceService", ["$http", function ($http) {
    return {
      getCurrentOrgDevices: function (dataTable, fnSuccess) {
        var url = '/api/device?' + dataTable.toString();
        $http.get(url).success(fnSuccess);

        return this;
      },
      getCurrentOrgAccounts: function (fnSuccess) {
        var url = '/api/account';
        $http.get(url).success(fnSuccess);

        return this;
      },
      getDeviceAuthToken: function (accountId, fnSuccess, fnError) {
        var url = '/api/auth/logintoken?account_id=' + accountId;
        $http.get(url).success(fnSuccess).error(fnError);

        return this;
      },
      createAccount: function (account, fnSuccess, fnError) {
        $http.post("/api/account", account).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, deviceService, $timeout) {

    var myTime = null;

    deviceService.getCurrentOrgAccounts(function (data) {
      device.account.curOrgAccounts = data;
    });

    function utf16to8(str) {
      var out, i, len, c;

      out = "";
      len = str.length;
      for (i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
          out += str.charAt(i);
        } else if (c > 0x07FF) {
          out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
          out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
          out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
          out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
          out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
      }
      return out;
    }

    $timeout(function () {
      $('#createForm').bootstrapValidator({
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
          accountPassword: {
            container: 'tooltip',
            validators: {
              notEmpty: {
                message: '请输入账号密码'
              }
            }
          },
          accountPasswordConfirm: {
            container: 'tooltip',
            validators: {
              notEmpty: {
                message: '请输入确认密码'
              },
              identical: {
                field: 'accountPassword',
                message: '确认密码不一致'
              }
            }
          }
        }
      }).on('success.field.bv', function (e, data) {
        device.account.createEnabled = false;
        $scope.$apply();
      });

    }, 0);

    var device = $scope.device = {
      account: {
        curOrgAccounts: '',
        showCreateAccount: false,
        createEnabled: true,
        accountIdentifier: '',
        accountPassword: '',
        accountPasswordConfirm: '',
        spinnerShow: false,
        createAccount: function () {

          if (device.account.accountIdentifier == '') {
            return;
          }

          if (device.account.accountPassword == '') {
            return;
          }

          if (device.account.accountPasswordConfirm == '') {
            return;
          }

          device.account.spinnerShow = true;

          var accountObj = {};

          accountObj.org_id = $scope.context.organization.id;
          accountObj.identifier = device.account.accountIdentifier;
          accountObj.password = device.account.accountPassword;
          accountObj.first_name = '设备';
          accountObj.last_name = '设备';
          accountObj.email = 'device@device.com';
          accountObj.phone = '111';

          deviceService.createAccount(accountObj, function (data) {

            device.account.spinnerShow = false;

            $scope.utils.alert('success', '创建账号成功', '#myModal .modal-dialog', false);

            deviceService.getCurrentOrgAccounts(function (data) {
              device.account.curOrgAccounts = data;
            });

            device.account.showCreateAccount = false;

          }, function (data) {
            device.account.spinnerShow = false;
            $scope.utils.alert('danger', '创建账号失败', '#myModal .modal-dialog', false);
          });
        },
        cancelAccount: function () {
          device.account.showCreateAccount = false;
          device.account.spinnerShow = false;
        }
      },
      deviceTable: new $scope.utils.DataTable({
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        },
        pageable: {
          page: 0,
          size: 20
        },
        flush: function (callback) {
          deviceService.getCurrentOrgDevices(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      }),
      time_distance: 0,
      deviceComment: '',
      deviceName: '',
      selectAccount: '',
      deviceAuth: function () {

        if (device.selectAccount == '') {
          $('#divSelectAccount').addClass('has-error').addClass('has-feedback');
          return;
        }

        if (device.deviceName == '') {
          $('#divDeviceName').addClass('has-error').addClass('has-feedback');
          return;
        }

        if (device.deviceComment.length > 20) {
          $('#divDeviceComment').addClass('has-error').addClass('has-feedback');
          return;
        }

        $('#divDeviceName').addClass('has-success').addClass('has-feedback');
        $('#divSelectAccount').addClass('has-success').addClass('has-feedback');
        $('#divDeviceComment').addClass('has-success').addClass('has-feedback');

        var getAuthToken = function (data) {

          var postObject = {};
          postObject.a = $scope.context.account.id;
          postObject.t = data.token;
          postObject.dn = device.deviceName;
          postObject.dc = device.deviceComment;

          var qrCodeContent = JSON.stringify(postObject);

          $("#authQRCode").html('');

          $scope.qrcode = $("#authQRCode").qrcode({
            render: "canvas", //table方式
            width: 300, //宽度
            height: 300, //高度
            foreground: "#337ab7",//前景颜色
            correctLevel: 3,//纠错等级
            text: utf16to8(qrCodeContent) //任意内容
          });

        };

        device.time_distance = 120;
        if (myTime == null) {
          myTime = setInterval(function () {
            device.time_distance--;

            var int_minute = Math.floor(device.time_distance / 60);
            var int_second = Math.floor(device.time_distance - int_minute * 60);

            if (int_minute < 10) {
              int_minute = "0" + int_minute;
            }
            if (int_second < 10) {
              int_second = "0" + int_second;
            }

            $('#time_refresh').html("设备授权码将在" + int_minute + "分" + int_second + "秒内刷新");

            if (device.time_distance == 0) {
              device.time_distance = 120;
              deviceService.getDeviceAuthToken(device.selectAccount, getAuthToken, function () {
                $('#time_refresh').html("");
                clearInterval(myTime);
                myTime = null;
                $("#authQRCode").html('');
                $scope.utils.alert('danger', '获取账号Token失败', '#myModal .modal-dialog', false);
              });
            }
          }, 1000);
        }

        setTimeout(function () {
          clearInterval(myTime);
        }, 10 * 120 * 1000);

        deviceService.getDeviceAuthToken(device.selectAccount, getAuthToken, function () {
          $('#time_refresh').html("");
          clearInterval(myTime);
          myTime = null;
          $("#authQRCode").html('');
          $scope.utils.alert('danger', '获取账号Token失败', '#myModal .modal-dialog', false);
        });
      }

      ,
      cancelDeviceAuth: function () {
        $scope.utils.alert('info', '设备取消授权正在开发中');
      }
      ,
      showDeviceModal: function () {
        $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
          $(this).removeData("bs.modal");
        });
      }
      ,
      hideDeviceModal: function () {
        device.account.spinnerShow = false;
        $('#myModal').modal('hide');
      }
    };

  }])
  ;
})
();