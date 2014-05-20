var app = angular.module('coinport.open', ['ui.bootstrap', 'ngResource', 'navbar', 'ngRoute', 'coinport.app']);

function routeConfig($routeProvider) {
    $routeProvider.
    when('/', {
        redirectTo: '/about/en-US'
    }).
    when('/about/en-US', {
        templateUrl: 'views/transparency.en-US.html'
    }).
    when('/about/zh-CN', {
        templateUrl: 'views/transparency.zh-CN.html'
    }).
    when('/opendata', {
        controller: 'DownCtrl',
        templateUrl: 'views/opendata.html'
    }).
    when('/reserve', {
        controller: 'ReserveCtrl',
        templateUrl: 'views/reserve.html'
    }).
    when('/opensource', {
        templateUrl: 'views/opensource.html'
    }).
    otherwise({
        redirectTo: '/'
    });
}

app.config(routeConfig);

app.controller('DownCtrl', function ($scope, $http) {
    $scope.messagesPage = 1;
    $scope.snapshotsPage = 1;

    $scope.loadSnapshots = function() {
        $http.get('/api/open/data/snapshot', {params: {limit: 10, page: $scope.snapshotsPage}})
        .success(function(data, status, headers, config) {
            $scope.snapshots = data.data;
        });
    }

    $scope.loadMessages = function() {
        $http.get('/api/open/data/messages', {params: {limit: 10, page: $scope.messagesPage}})
        .success(function(data, status, headers, config) {
            $scope.messages = data.data;
        });
    }

    $scope.loadSnapshots();
    $scope.loadMessages();
});

app.controller('ReserveCtrl', function ($scope, $http) {
    $http.get('/api/account/-1')
        .success(function(data, status, headers, config) {
            $scope.accounts = data.data.accounts;
    });
});
