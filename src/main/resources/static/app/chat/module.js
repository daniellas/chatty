(function() {
    'use strict';

    var mod = angular.module('chatty.chat', []);

    mod.config([ '$stateProvider', Routing ]);

    function Routing($stateProvider) {
        $stateProvider.state('chat/list', {
            url : '/chats',
            templateUrl : 'app/chat/list/template.html',
            controller : 'ChatListCtrl'
        }).state('chat/create', {
            url : '/chat/create',
            templateUrl : 'app/chat/session/template.html',
            controller : 'ChatSessionCtrl'
        }).state('chat/open', {
            url : '/chat/:id/open',
            templateUrl : 'app/chat/session/template.html',
            controller : 'ChatSessionCtrl'
        });
    }

})();