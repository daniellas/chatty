(function() {
    'use strict';

    var mod = angular.module('chatty.login', []);

    mod.config([ '$stateProvider', Routing ]);

    function Routing($stateProvider) {
        $stateProvider.state('login', {
            url : '/',
            templateUrl : 'app/login/template.html',
            controller : 'LoginCtrl'
        });
    }

})();