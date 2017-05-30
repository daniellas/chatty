(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.controller('ChatSessionCtrl', [ '$scope', '$stateParams', '$state', '$document', '$timeout', 'ChatClientSrv', 'RestSrv', Controller ]);

    function Controller($scope, $stateParams, $state, $document, $timeout, ChatClientSrv, RestSrv) {

        function scrollBottom() {
            $timeout(function() {
                var elem = $document[0].getElementById('messages');

                if (elem) {
                    elem.scrollTop = elem.scrollHeight + 1000;
                }
            }, 0, false);
        }

        function onMessage(message) {
            $scope.messages.push(message);
            $scope.$evalAsync();
            scrollBottom();
        }

        $scope.messages = [];

        if ($stateParams.id) {
            RestSrv.one('chats', $stateParams.id).get().then(function(response) {
                $scope.chat = response.data;

                ChatClientSrv.joinChat($stateParams.id, onMessage).then(function() {
                    $scope.initialized = true;
                    $scope.$on('$destroy', function() {
                        ChatClientSrv.leaveChat($stateParams.id);
                    });
                }, function() {
                });
            });
        } else {
            $scope.chat = {};
            $scope.initialized = true;
        }

        $scope.message = {};

        $scope.sendMessage = function(message) {
            if (message && message.content) {
                ChatClientSrv.sendMessage($stateParams.id, message.content);
                message.content = null;
            }
        };

        $scope.sendMessageByKey = function($event, message) {
            if ($event.charCode == 13) {
                $scope.sendMessage(message);
            }
        };

        $scope.chatTitle = function(chat) {
            if (chat && chat.id) {
                return chat.title;
            }

            return 'New chat';
        };

        $scope.createChat = function(title) {
            RestSrv.all('chats').customPOST(title).then(function(response) {
                $state.go('chat/open', {
                    id : response.data.id
                });
            });
        };

        scrollBottom();
    }
})();