(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.service('ChatClientSrv', [ 'StompSrv', Service ]);

    function Service(StompSrv) {
        var destinationPrefix = '/user/queue/messages/';

        this.joinChat = function(chatId, user, handler) {
            return StompSrv.subscribe('/chat', destinationPrefix + chatId, handler);
        };

        this.leaveChat = function(chatId) {
            return StompSrv.unsubscribe('/chat', destinationPrefix + chatId);
        };

        this.sendMessage = function(chatId, messageContent) {
            StompSrv.send('/app/messages/' + chatId, messageContent);
        };
    }
})();