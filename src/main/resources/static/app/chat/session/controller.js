(function() {
    'use strict';

    var mod = angular.module('chatty.chat');

    mod.controller('ChatSessionCtrl', [ '$scope', '$stateParams', '$state', '$document', '$timeout', 'MessengerSrv', Controller ]);

    function Controller($scope, $stateParams, $state, $document, $timeout, MessengerSrv) {

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
            $scope.chat = {
                id : '1',
                createdBy : 'daniel',
                createDate : '2017-01-01 12:00:01',
                title : 'Conversation 1'
            };
            MessengerSrv.joinChat($stateParams.id, onMessage).then(function() {
                console.log('Joined chat ' + $stateParams.id);
            }, function() {
            });
        } else {
            $scope.chat = {};
        }

        $scope.message = {
            content : 'Test'
        };

        $scope.sendMessage = function(message) {
            if (message && message.content) {
                MessengerSrv.sendMessage($stateParams.id, message.content);
                message.content = null;
            }
        };

        $scope.sendMessageByKey = function($event, message) {
            if ($event.charCode == 13) {
                $scope.sendMessage(message);
            }
        };

        $scope.chatTitle = function(chat) {
            if (chat.id) {
                return chat.title;
            }

            return 'New chat';
        };

        $scope.createChat = function(chat) {
            $state.go('chat/open', {
                id : 1
            });
        };

        scrollBottom();
    }
})();