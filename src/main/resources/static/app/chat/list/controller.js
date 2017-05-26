(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.controller('ChatListCtrl', [ '$scope', Controller ]);

    function Controller($scope) {
        $scope.chats = [ {
            id : '1',
            createdBy : 'daniel',
            createDate : '2017-01-01 12:00:01',
            title : 'Conversation 1'
        }, {
            id : '2',
            createdBy : 'daniel',
            createDate : '2017-01-01 12:00:01',
            title : 'Conversation 2'
        }, {
            id : '3',
            createdBy : 'daniel',
            createDate : '2017-01-01 12:00:01',
            title : 'Conversation 3'
        } ];
    }
})();