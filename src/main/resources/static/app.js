"use strict";

const REST_BASE_PATH = "/api/v1";
const GET_YOS_PATH = `${REST_BASE_PATH}/identities`;
const SEND_YO_PATH = `${REST_BASE_PATH}/capture`;
const STOMP_SUBSCRIBE_PATH = "/stomp";
const STOMP_RESPONSE_PATH1 = "/stompresponse1";
const STOMP_RESPONSE_PATH2 = "/stompresponse2";
const STOMP_RESPONSE_PATH3 = "/stompresponse3";

const STATE_A = "/states?party=PartyA";
const STATE_B = "/states?party=PartyB";
const STATE_C = "/states?party=WatchDog";

const app = angular.module("yoAppModule", ["ui.bootstrap"]);

app.controller("YoAppController", function($scope, $http, $location, $uibModal) {
    const yoApp = this;
    let peers = [];
    $scope.yos1 = [];
    $scope.yos2 = [];
    $scope.yos3 = [];

    $scope.img1;
    $scope.img2;
    $scope.img3;

    $scope.e1;
    $scope.e2;
    $scope.e3;


    // Starts streaming new Yo's from the websocket.
    (function connectAndStartStreamingYos() {
        let socket = new SockJS(STOMP_SUBSCRIBE_PATH);
        let stompClient = Stomp.over(socket);
        stompClient.connect({}, function startStreamingYos(frame) {
            stompClient.subscribe(STOMP_RESPONSE_PATH1, function updateYos(update) {
                let yoState = JSON.parse(update.body);
                yoState.img=JSON.parse(yoState.data1).image;
                yoState.e=JSON.stringify(JSON.parse(yoState.data1).events);
                $scope.yos1.push(yoState);
                $scope.img1 = JSON.parse(yoState.data1).image;
                $scope.e1 = JSON.stringify(JSON.parse(yoState.data1).events);
                debugger;

                // Forces the view to refresh, showing the new Yo.
                $scope.$apply();
            });

            stompClient.subscribe(STOMP_RESPONSE_PATH2, function updateYos(update) {
                            let yoState = JSON.parse(update.body);
                            yoState.img=JSON.parse(yoState.data2).image;
                            yoState.e=JSON.stringify(JSON.parse(yoState.data2).events);
                            $scope.yos2.push(yoState);
                            $scope.img2 = JSON.parse(yoState.data2).image;
                            $scope.e2 = JSON.stringify(JSON.parse(yoState.data2).events);

                            // Forces the view to refresh, showing the new Yo.
                            $scope.$apply();
                        });

            stompClient.subscribe(STOMP_RESPONSE_PATH3, function updateYos(update) {
                let yoState = JSON.parse(update.body);
                    yoState.img=JSON.parse(yoState.data3).image;
                    yoState.e=JSON.stringify(JSON.parse(yoState.data3).events);
                    $scope.yos3.push(yoState);
                    $scope.img3 = JSON.parse(yoState.data3).image;
                    $scope.e3 = JSON.stringify(JSON.parse(yoState.data3).events);
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

app.controller("StatesController", function($scope, $http, $location, $uibModal) {
    const yoApp = this;
    let peers = [];
    $scope.yos1 = [];
    $scope.yos2 = [];
    $scope.yos3 = [];

    // Starts streaming new Yo's from the websocket.
    (function allStates() {
        $http.get(REST_BASE_PATH+STATE_A)
            .then(function(response) {
                debugger;
                $scope.yos1 = $scope.yos1.concat(response.data);
            });

             $http.get(REST_BASE_PATH+STATE_B)
                .then(function(response) {
                     $scope.yos2 = $scope.yos2.concat(response.data);
              });
              $http.get(REST_BASE_PATH+STATE_C)
                .then(function(response) {
                    $scope.yos3 = $scope.yos3.concat(response.data);
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


// Intercepts unhandled-rejection errors.
app.config(["$qProvider", function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);