/**
 * Created by Peter on 2016/10/22.
 */


angular.module('javaDashboard').config(['$stateProvider', function ($stateProvider) {
    $stateProvider
        .state('home', {
            url: '',
            templateUrl: 'admin/html/board/view/admin/homepage.html',
            controller: 'homepageCtrl'
        })
        .state('dashboard', {
            url: '/dashboard',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('mine', {
            url: '/mine',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('mine.view', {
            url: '/{id}',
            params: {id: null},
            templateUrl: 'admin/html/board/view/dashboard/view.html',
            controller: 'boardViewCtrl'
        })
        .state('dashboard.folder', {
            url: '/{folder}',
            params: {folder: null},
            abstract: true,
            template: '<div ui-view></div>',
        })
        .state('dashboard.folder.view', {
            url: '/{id}',
            params: {id: null},
            templateUrl: 'admin/html/board/view/dashboard/view.html',
            controller: 'boardViewCtrl'
        })
        .state('config', {
            url: '/config',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('config.board', {
            url: '/board/{boardId}',
            params: {boardId: null},
            templateUrl: 'admin/html/board/view/config/board.html',
            controller: 'boardCtrl'
        })
        .state('config.widget', {
            url: '/widget?id&datasetId',
            params: {id: null, datasetId: null},
            templateUrl: 'admin/html/board/view/config/widget.html',
            controller: 'widgetCtrl'
        })
        .state('config.datasource', {
            url: '/datasource/{id}',
            params: {id: null},
            templateUrl: 'admin/html/board/view/config/datasource.html',
            controller: 'datasourceCtrl'
        })
        .state('config.category', {
            url: '/category',
            templateUrl: 'admin/html/board/view/config/category.html',
            controller: 'categoryCtrl'
        })
        .state('config.dataset', {
            url: '/dataset/{id}',
            params: {id: null},
            templateUrl: 'admin/html/board/view/config/dataset.html',
            controller: 'datasetCtrl'
        })
        .state('config.job', {
            url: '/job',
            templateUrl: 'admin/html/board/view/config/job.html',
            controller: 'jobCtrl'
        })
        .state('config.role', {
            url: '/role',
            templateUrl: 'admin/html/board/view/config/shareResource.html',
            controller: 'shareResCtrl'
        })
        .state('admin', {
            url: '/admin',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('admin.user', {
            url: '/user',
            templateUrl: 'admin/html/board/view/admin/user.html',
            controller: 'userAdminCtrl'
        });

}]);

angular.module('javaDashboard').factory('sessionHelper', ["$rootScope","$q", function ($rootScope,$q) {
    var sessionHelper = {
        responseError: function (response) {
            if (response.code == 1) {
                if ($rootScope.alert) {
                    $rootScope.alert(response.msg);
                }
            }
            return $q.reject(response);
        }
    };
    return sessionHelper;
}]);


angular.module('javaDashboard').config(function ($httpProvider) {
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function (data) {
        /**
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
        var param = function (obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;

            for (name in obj) {
                value = obj[name];

                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(name) + '='
                        + encodeURIComponent(value) + '&';
                }
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };

        return angular.isObject(data) && String(data) !== '[object File]'
            ? param(data)
            : data;
    }];

    $httpProvider.interceptors.push('sessionHelper');

});


angular.module('javaDashboard').config(function ($translateProvider, $translatePartialLoaderProvider) {
    $translatePartialLoaderProvider.addPart('board');
    $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: 'i18n/{lang}/{part}.json'
    });

    $translateProvider.preferredLanguage(settings.preferredLanguage);
});