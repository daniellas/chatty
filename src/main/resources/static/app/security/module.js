(function() {
    'use strict';

    var mod = angular.module('chatty.security', []);

    mod.run([ '$transitions', '$state', 'SecuritySrv', Initialize ]);

    function Initialize($transitions, $state, SecuritySrv) {
        SecuritySrv.userDetails().then(function(response) {
            SecuritySrv.setAuthenticated(true);
            SecuritySrv.setUserDetails(response.data.plain());
            $state.go('chat/list');
        }, function() {
            $state.go('login');
        });
        
        $transitions.onStart({
            to : '**'
        }, function(trans) {
            if (trans.targetState().identifier() != 'login') {
                if (!SecuritySrv.isAuthenticated()) {
                    return trans.router.stateService.target('login');
                }
            } else {
                if (SecuritySrv.isAuthenticated()) {
                    return trans.router.stateService.target('chat/list');
                }
            }
        });

    }

})();