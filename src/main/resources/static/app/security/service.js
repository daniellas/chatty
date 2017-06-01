(function() {
    'use strict';

    var mod = angular.module('chatty.security');

    mod.service('SecuritySrv', [ '$rootScope', '$state', 'RestSrv', 'AlertSrv', Service ]);

    function setAuthenticated($rootScope, authenticated) {
        $rootScope.authenticated = authenticated;
    }

    function setUserDetails($rootScope, userDetails) {
        $rootScope.userDetails = userDetails;
    }

    function Service($rootScope, $state, RestSrv, AlertSrv) {
        this.login = function(username, password) {
            RestSrv.all('sec/login').customPOST({
                username : username,
                password : password
            }).then(function(response) {
                setAuthenticated($rootScope, true);
                setUserDetails($rootScope, response.data.plain());
                $state.go('chat/list');
                AlertSrv.showSuccess('You are signed in');
            }, function() {
                $rootScope.authenticated = false;
                $rootScope.userDetails = null;
            });
        };

        this.logout = function() {
            RestSrv.all('sec/logout').customPOST().then(function(response) {
                setAuthenticated($rootScope, false);
                $state.go('login');
                AlertSrv.showSuccess('You are signed out');
            });
        };

        this.userDetails = function() {
            return RestSrv.all('sec/userdetails').customPOST();
        };

        this.isAuthenticated = function() {
            return $rootScope.authenticated;
        };

        this.setAuthenticated = function(authenticated) {
            setAuthenticated($rootScope, authenticated);
        };

        this.setUserDetails = function(userDetails) {
            setUserDetails($rootScope, userDetails);
        };

        this.getCurrentUsername = function() {
            if ($rootScope.userDetails) {
                return $rootScope.userDetails.username;
            }
        };

    }

})();