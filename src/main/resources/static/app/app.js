(function() {
    'use strict';

    var app = angular.module('chatty', [ 'ngAnimate', 'ngCookies', 'toaster', 'ui.router', 'ui.bootstrap', 'restangular', 'chatty.spin', 'chatty.validation',
            'chatty.date', 'chatty.alert', 'chatty.security', 'chatty.navbar', 'chatty.login', 'chatty.stomp', 'chatty.chat' ]);

    app.config([ '$stateProvider', '$urlRouterProvider', '$qProvider', Routing ]);
    app.config([ 'RestangularProvider', RestangularConfig ]);
    app.controller('MasterCtrl', [ '$scope', Controller ])
    app.service('RestSrv', [ 'Restangular', RestSrv ]);

    function Routing($stateProvider, $urlRouterProvider, $qProvider) {
        $qProvider.errorOnUnhandledRejections(false);
        $urlRouterProvider.otherwise('/');

    }

    function RestangularConfig(RestangularProvider) {
        RestangularProvider.setDefaultHeaders({
            'Content-Type' : 'application/json;charset=utf-8'
        }).setFullResponse(true);
    }

    function Controller($scope) {
    }

    function RestSrv(Restangular) {
        return Restangular.withConfig(function(RestangularConfigurer) {
        });
    }

})();