(function () {
    angular.module('utils', ['YUNSOO_CONFIG'])
        .factory('utils', ['YUNSOO_CONFIG', function (YUNSOO_CONFIG) {
            return {
                formatDateString: function (value) {
                    return new DateTime(value).toString('yyyy-MM-dd HH:mm:ss');
                },
                logout: function () {
                    console.log('[logout]');
                    $.removeCookie(YUNSOO_CONFIG.NAME_ACCESS_TOKEN, {path: '/'});
                    window.location.href = 'login.html';
                },
                lock: function () {
                    console.log('[lock screen]');
                    this.notification('warning', '锁屏界面开发中...');
                },
                /**
                 * shortcut of $.niftyNoty, show floating notification on the top right
                 * @param type string ["info", "primary", "success", "warning", "danger", "mint", "purple", "pink", "dark"]
                 * @param message string
                 * @param title string
                 */
                notification: function (type, message, title) {
                    $.niftyNoty && $.niftyNoty({
                        type: type,
                        container: 'floating',
                        title: title,
                        message: message,
                        timer: 3000
                    });
                },
                /**
                 * shortcut of $.niftyNoty, show alert, default in page top
                 * @param type string ["info", "primary", "success", "warning", "danger", "mint", "purple", "pink", "dark"]
                 * @param message string
                 * @param container string ["floating", "page"] | "jQuery selector"
                 */
                alert: function (type, message, container) {
                    $.niftyNoty && $.niftyNoty({
                        type: type,
                        container: container || 'page',
                        message: message,
                        timer: 3000
                    });
                },
                arrayToMap: function (array, keyName) {
                    var map = {};
                    if (Array.isArray(array) && keyName) {
                        $.each(array, function (i, item) {
                            if (item.hasOwnProperty(keyName)) {
                                map[item[keyName]] = item;
                            }
                        });
                    }
                    return map;
                }

            };
        }]);
})();