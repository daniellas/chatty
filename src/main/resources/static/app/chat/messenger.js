(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.service('MessengerSrv', [ 'StompSrv', Service ]);

    function Service(StompSrv) {

        this.joinChat = function(chatId, handler) {
            return StompSrv.subscribe('/chat', '/topic/messages/' + chatId, handler);
        };

        this.sendMessage = function(chatId, messageContent) {
            StompSrv.send('/app/chat/' + chatId, messageContent);
        };
    }
})();