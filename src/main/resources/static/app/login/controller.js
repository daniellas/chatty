(function() {
    'use strict';

    var mod = angular.module('chatty.login');

    mod.controller('LoginCtrl', [ '$scope', 'SecuritySrv', Controller ]);

    function Controller($scope, SecuritySrv) {
        $scope.login = function(username, password) {
            SecuritySrv.login(username, password);
        };
    }
})();