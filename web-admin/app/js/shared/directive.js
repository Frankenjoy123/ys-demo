(function () {
  'use strict';

  var utils = angular.module('utils');

  utils.directive('dtPageable', function () {
    return {
      restrict: 'A',
      scope: {
        pageable: '=dtPageable'
      },
      templateUrl: 'partials/widgets/pageable.html'
    };
  });

  utils.directive('keyType', function () {
    return {
      restrict: 'A',
      scope:{
        keyTypes: '=keyType'
      },
      templateUrl: 'partials/widgets/keytype.html'
    };
  });

})();