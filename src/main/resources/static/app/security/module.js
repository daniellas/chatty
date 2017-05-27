(function() {
    'use strict';

    var mod = angular.module('chatty.security', []);

    mod.run([ '$transitions', '$state', 'SecuritySrv', Initialize ]);

    function Initialize($transitions, $state, SecuritySrv) {
        SecuritySrv.userDetails().then(function(response) {
            SecuritySrv.setAuthenticated(true);
            SecuritySrv.setUserDetails(response.data.plain());
            $transitions.onStart({
                to : '**'
            }, function(trans) {
                if (trans.targetState().identifier() != 'login' && !SecuritySrv.isAuthenticated()) {
                    return trans.router.stateService.target('login');
                }
            });
        }, function() {
            $state.go('login');
        });
    }

})();