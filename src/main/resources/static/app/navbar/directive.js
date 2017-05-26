(function() {
    'use strict';

    var mod = angular.module('chatty.navbar', []);

    mod.directive('chattyNavbar', [ 'SecuritySrv', Directive ]);

    function Directive(SecuritySrv) {
        return {
            restrict : 'E',
            replace : true,
            templateUrl : 'app/navbar/template.html',
            link : function($scope) {
                $scope.collapse = true;

                $scope.logout = function() {
                    $scope.collapse = true;
                    SecuritySrv.logout();
                };
            }
        };
    }

})();