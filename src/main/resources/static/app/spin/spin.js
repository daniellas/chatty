(function() {
    'use strict';

    var mod = angular.module('chatty.spin', []);

    mod.service('SpinSrv', [ '$timeout', Service ]);
    mod.service('SpinInterceptor', [ '$q', '$rootScope', 'SpinSrv', SpinInterceptor ]);
    mod.config([ '$httpProvider', HttpConfig ]);

    function Service($timeout) {
        var spinner = new Spinner({
            className : 'spinner'
        });

        this.start = function() {
            spinner.spin(document.getElementById('body'));
        };

        this.stop = function() {
            $timeout(function() {
                spinner.stop();
            }, 100);
        };
    }

    function SpinInterceptor($q, $rootScope, SpinSrv) {
        this.request = function(config) {
            SpinSrv.start();

            return config;
        }

        this.response = function(response) {
            SpinSrv.stop();

            return response;
        }

        this.responseError = function(response) {
            SpinSrv.stop();

            return $q.reject(response);
        }
    }

    function HttpConfig($httpProvider) {
        $httpProvider.interceptors.push('SpinInterceptor');
    }

})();