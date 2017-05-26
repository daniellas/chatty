(function() {
    'use strict';

    var mod = angular.module('chatty.security', []);

    mod.run([ '$transitions', 'SecuritySrv', Initialize ]);

    function Initialize($transitions, SecuritySrv) {
        $transitions.onStart({
            to : '**'
        }, function(trans) {
            if (trans.targetState().identifier() != 'login' && !SecuritySrv.isAuthenticated()) {
                return trans.router.stateService.target('login');
            }
        });
    }

})();