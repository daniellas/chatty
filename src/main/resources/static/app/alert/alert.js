(function() {
    'use strict';

    var mod = angular.module('chatty.alert', []);

    mod.service('AlertSrv', [ 'toaster', AlertService ]);
    mod.service('AlertInterceptor', [ '$q', 'AlertSrv', AlertInterceptor ]);
    mod.config([ '$httpProvider', Config ]);

    function AlertService(toaster) {
        this.showSuccess = function(message) {
            toaster.pop({
                type : 'success',
                body : message
            });
        };
        this.showError = function(message) {
            toaster.pop({
                type : 'error',
                body : message
            });
        };
    }

    function AlertInterceptor($q, AlertSrv) {
        this.request = function(config) {
            return config;
        }

        this.response = function(response) {
            return response;
        }
        this.responseError = function(response) {
            if (response.data.path != '/sec/userdetails') {
                AlertSrv.showError(response.data.message);
            }

            return $q.reject(response);
        }
    }

    function Config($httpProvider) {
        $httpProvider.interceptors.push('AlertInterceptor')
    }

})();