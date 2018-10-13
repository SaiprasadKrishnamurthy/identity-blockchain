"use strict";

const REST_BASE_PATH = "/api/v1";
const GET_YOS_PATH = `${REST_BASE_PATH}/identities`;
const SEND_YO_PATH = `${REST_BASE_PATH}/capture`;
const STOMP_SUBSCRIBE_PATH = "/stomp";
const STOMP_RESPONSE_PATH1 = "/stompresponse1";
const STOMP_RESPONSE_PATH2 = "/stompresponse2";
const STOMP_RESPONSE_PATH3 = "/stompresponse3";

const app = angular.module("yoAppModule", ["ui.bootstrap"]);

app.controller("YoAppController", function($scope, $http, $location, $uibModal) {
    const yoApp = this;
    let peers = [];
    $scope.yos1 = [];
    $scope.yos2 = [];
    $scope.yos3 = [];

    // Starts streaming new Yo's from the websocket.
    (function connectAndStartStreamingYos() {
        let socket = new SockJS(STOMP_SUBSCRIBE_PATH);
        let stompClient = Stomp.over(socket);
        stompClient.connect({}, function startStreamingYos(frame) {
            stompClient.subscribe(STOMP_RESPONSE_PATH1, function updateYos(update) {
                let yoState = JSON.parse(update.body);
                $scope.yos1.push(yoState);
                // Forces the view to refresh, showing the new Yo.
                $scope.$apply();
            });

            stompClient.subscribe(STOMP_RESPONSE_PATH2, function updateYos(update) {
                            let yoState = JSON.parse(update.body);
                            $scope.yos2.push(yoState);
                            // Forces the view to refresh, showing the new Yo.
                            $scope.$apply();
                        });

            stompClient.subscribe(STOMP_RESPONSE_PATH3, function updateYos(update) {
                let yoState = JSON.parse(update.body);
                    $scope.yos3.push(yoState);
                    $scope.$apply();
                });
        });
    })();

    // Opens the send-Yo modal.
    yoApp.openSendYoModal = function openSendYoModal() {
        $uibModal.open({
            templateUrl: "yoAppModal.html",
            controller: "SendYoModalController",
            controllerAs: "sendYoModal",
            resolve: {
                peers: () => peers
            }
        });
    };

    // Gets a list of existing Yo's.
    function getYos() {
        return this.yos;
    }
});

// Controller for success/fail modal dialogue.
app.controller('ShowMessageController', function ($uibModalInstance, message) {
    const modalInstanceTwo = this;
    modalInstanceTwo.message = message.data;
});

// Intercepts unhandled-rejection errors.
app.config(["$qProvider", function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);