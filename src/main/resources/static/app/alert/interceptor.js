(function() {
    'use strict';

    var mod = angular.module('chatty.alert');

    mod.service('AlertInterceptor', [ '$q', 'AlertSrv', Service ]);
    mod.config([ '$httpProvider', Config ]);

    function Service($q, AlertSrv) {
        this.request = function(config) {
            return config;
        }
        this.response = function(response) {
            return response;
        }
        this.responseError = function(response) {
            AlerSrv.showError(response);

            return $q.reject(response);
        }
    }

    function Config($httpProvider) {
        $httpProvider.interceptors.push('AlertInterceptor')
    }
})();