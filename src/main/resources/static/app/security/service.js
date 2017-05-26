(function() {
    'use strict';

    var mod = angular.module('chatty.security');

    mod.service('SecuritySrv', [ '$rootScope', '$state', Service ]);

    function Service($rootScope, $state) {

        this.login = function(username, password) {
            $rootScope.authenticated = true;
            $state.go('chat/list');
        };

        this.logout = function() {
            $rootScope.authenticated = false;
            $state.go('login');
        };

        this.isAuthenticated = function() {
            $rootScope.authenticated = true;
            return $rootScope.authenticated;
        };
    }

})();