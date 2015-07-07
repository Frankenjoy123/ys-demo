;
(function () {
  var app = angular.module('root');

  app.factory('orgService', ['$http', function ($http) {
    return {
      getOrgList: function (dataTable, fnSuccess, fnError) {
        $http.get('/api/organization/list?' + dataTable.toString()).success(fnSuccess);

        return this;
      },
      createOrg: function (org, fnSuccess, fnError) {
        $http.post("/api/organization", org).success(fnSuccess).error(fnError);

        return this;
      }
    };
  }]);

  app.controller('orgCtrl', [
    '$scope',
    '$timeout',
    'orgService',
    function ($scope, $timeout, orgService) {

      var orgObj = $scope.orgObj = {
        name: '',
        type_code: '',
        status_code: 'available',
        description: '',
        details: ''
      };

      $scope.spinnerShow = false;

      $timeout(function () {

        $('#orgForm').bootstrapValidator({
          message: '输入不合法',
          feedbackIcons: {
            valid: 'fa fa-check-circle fa-lg text-success',
            invalid: 'fa fa-times-circle fa-lg',
            validating: 'fa fa-refresh'
          },
          fields: {
            name: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入组织名称'
                }
              }
            },
            type_code: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入组织类型'
                }
              }
            },
            description: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入组织描述'
                }
              }
            },
            details: {
              container: 'tooltip',
              validators: {
                notEmpty: {
                  message: '请输入组织详情'
                }
              }
            }
          }
        }).on('success.form.#orgForm', function (e, data) {

          var $parent = data.element.parents('.form-group');
          // Remove the has-success class
          $parent.removeClass('has-success');
        });

      }, 0);

      $scope.createOrganization = function () {

        if (orgObj.name == '' || orgObj.type_code == '' || orgObj.description == '' || orgObj.details == '')
          return;

        $scope.spinnerShow = true;

        orgService.createOrg(orgObj, function (data) {
          $scope.spinnerShow = false;

          $('#myModal').modal('hide');

          $scope.utils.alert('info', '组织创建成功');

          $scope.orgTable = new $scope.utils.DataTable(dataTable);
        });
      };

      $scope.showModal = function () {
        $('#myModal').modal({backdrop: 'static', keyboard: false}).on("hidden.bs.modal", function () {
          $(this).removeData("bs.modal");
        });
      };

      var dataTable = {
        sortable: {
          target: '#sort-bar',
          sort: 'createdDateTime,desc'
        }
        ,
        pageable: {
          page: 0,
          size: 20
        }
        ,
        flush: function (callback) {
          orgService.getOrgList(this, function (data, status, headers) {
            callback({data: data, headers: headers});
          });
        }
      };

      $scope.orgTable = new $scope.utils.DataTable(dataTable);
    }]);
})();