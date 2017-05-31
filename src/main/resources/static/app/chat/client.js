(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.service('ChatClientSrv', [ 'StompSrv', Service ]);

    function Service(StompSrv) {

        this.joinChat = function(chatId, user, handler) {
            return StompSrv.subscribe('/chat', '/topic/messages/' + chatId + '/' + user, handler);
        };

        this.leaveChat = function(chatId) {
            return StompSrv.unsubscribe('/chat', '/topic/messages/' + chatId);
        };

        this.sendMessage = function(chatId, messageContent) {
            StompSrv.send('/app/messages/' + chatId, messageContent);
        };
    }
})();