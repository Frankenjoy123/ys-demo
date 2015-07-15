(function () {
  angular.module('YUNSOO_CONFIG', [])
      .factory('YUNSOO_CONFIG', function () {
        return {
          NAME_ACCESS_TOKEN: 'YS_ACCESS_TOKEN',
          NAME_PERMANENT_TOKEN: 'YS_PERMANENT_TOKEN',

          HEADER_ACCESS_TOKEN: 'X-YS-AccessToken',
          HEADER_APP_ID: 'X-YS-AppId',

          PARAMETER_ACCESS_TOKEN: 'access_token',

          APP_ID: '2k2juhux4z5d6eceh4v',

          PAGE_ACCESS: {
            RESOURCE: {
              DASHBOARD: 'page-enterprise-dashboard',
              PRODUCTKEY: 'page-enterprise-productkey',
              PACKAGE: 'page-enterprise-package',
              LOGISTICS: 'page-enterprise-logistics',
              REPORT: 'page-enterprise-report-*',
              PRODUCTBASE: 'page-enterprise-productbase',
              MESSAGE: 'page-enterprise-message',
              DEVICE: 'page-enterprise-device',
              ACCOUNT: 'page-enterprise-account',
              GROUP: 'page-enterprise-group',
              PROFILE: 'page-enterprise-profile'
            },
            ACTION: {
              READ: 'read',
              MANAGE: 'manage',
              ALL: '*'
            }
          }
        };
      });

  window.location.search.indexOf('debug') > 0 && (window._debug = {}); //for debug only, remove this line if
})();