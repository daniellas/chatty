(function() {
    'use strict';

    var mod = angular.module('chatty.date', []);

    mod.filter('chattyFullDate', [ '$filter', FullDate ]);

    function FullDate($filter) {
        return function(date) {
            return $filter('date')(date, 'EEEE d LLLL HH:mm:ss');
        };
    }
})();