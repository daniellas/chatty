(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.controller('ChatListCtrl', [ '$scope', 'RestSrv', Controller ]);

    function Controller($scope, RestSrv) {
        RestSrv.all('chats').getList().then(function(response) {
            $scope.chats = response.data;
            $scope.initialized = true;
        });
    }
})();