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
      getDeviceAuthToken: function (accountId, fnSuccess) {
        var url = '/api/auth/logintoken?account_id=' + accountId;
        $http.get(url).success(fnSuccess);

        return this;
      }
    };
  }]);

  app.controller("deviceCtrl", ["$scope", "deviceService", "$timeout", function ($scope, deviceService, $timeout) {

    $scope.curOrgAccounts = '';
    $scope.deviceComment = '';
    $scope.deviceName = '';
    $scope.selectAccount = '';

    (function newDataTable() {
      if ($scope.context.account) {
        $scope.deviceTable = new $scope.utils.DataTable({
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
        });
      } else {
        $timeout(newDataTable, 1000);
      }
    })();


    deviceService.getCurrentOrgAccounts(function (data) {
      $scope.curOrgAccounts = data;
    });

    var myTime;
    $scope.deviceAuth = function () {

      if ($scope.selectAccount == '') {
        $('#divSelectAccount').addClass('has-error').addClass('has-feedback');
        return;
      }

      if ($scope.deviceName == '') {
        $('#divDeviceName').addClass('has-error').addClass('has-feedback');
        return;
      }

      if ($scope.deviceComment.length > 20) {
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
        postObject.dn = $scope.deviceName;
        postObject.dc = $scope.deviceComment;

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

      $scope.time_distance = 120;
      if (myTime == undefined)
        myTime = setInterval(function () {
          $scope.time_distance--;

          var int_minute = Math.floor($scope.time_distance / 60);
          var int_second = Math.floor($scope.time_distance - int_minute * 60);

          if (int_minute < 10) {
            int_minute = "0" + int_minute;
          }
          if (int_second < 10) {
            int_second = "0" + int_second;
          }

          $('#time_refresh').html("设备授权码将在" + int_minute + "分" + int_second + "秒内刷新");

          if ($scope.time_distance == 0) {
            $scope.time_distance = 120;
            deviceService.getDeviceAuthToken($scope.selectAccount, getAuthToken);
          }
        }, 1000);

      setTimeout(function () {
        clearInterval(myTime);
      }, 10 * 120 * 1000);

      deviceService.getDeviceAuthToken($scope.selectAccount, getAuthToken);
    };

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

    $scope.cancelDeviceAuth = function () {
      $scope.utils.alert('info', '设备取消授权正在开发中');
    };

  }]);
})();