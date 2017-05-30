(function() {
    'use strict';

    var mod = angular.module('chatty.stomp', []);

    mod.service('StompSrv', [ '$q', Service ]);

    function Service($q) {
        var socket;
        var stompClient;
        var subscription;

        this.subscribe = function(url, channel, handler) {
            return $q(function(resolve, reject) {
                socket = new SockJS(url);
                stompClient = Stomp.over(socket);

                stompClient.connect(url, function() {
                    subscription = stompClient.subscribe(channel, function(data) {
                        handler(angular.fromJson(data.body));
                    });
                    resolve(subscription);
                }, function() {
                    reject();
                });
            });
        };
        
        this.unsubscribe = function() {
            if (subscription) {
                subscription.unsubscribe();
            }
        };

        this.send = function(channel, message) {
            stompClient.send(channel, {}, message);
        };
    }

})();