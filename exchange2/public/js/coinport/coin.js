var app = angular.module('coinport.coin', ['ui.bootstrap','navbar', 'coinport.app']);

app.controller('CoinCtrl', function ($scope, $http, $window) {
    $scope.coin = $window.location.pathname.replace("/coin/", "");
    $scope.addressUrl = COINPORT.addressUrl[$scope.coin];
    $scope.hotWallets = [];
    $scope.coldWallets = [];

    $scope.page = 1;
    $scope.limit = 10;
    $scope.filesPage = 1;
    $scope.filesLimit= 10;

    $http.get('/api/account/-1000')
        .success(function (data, status, headers, config) {
        $scope.accounts = data.data.accounts;
        });

    $http.get('/api/open/reserve/' + $scope.coin )
        .success(function(data, status, headers, config) {
            $scope.reserve = data.data;
        });

    $http.get('/api/open/wallet/' + $scope.coin + '/hot')
        .success(function(data, status, headers, config) {
            $scope.hotWallets = data.data.reverse();
        });

    $http.get('/api/open/wallet/' + $scope.coin + '/cold')
        .success(function(data, status, headers, config) {
            $scope.coldWallets = data.data.reverse();
        });

    $scope.cursor = 2000000000000;
    $scope.count = 1;
    $scope.reloadTransfers = function () {
        $http.get('/api/' + $scope.coin + '/transfer/-1', {params: {limit: $scope.limit, page: $scope.page}})
            .success(function (data, status, headers, config) {
                $scope.transfers = data.data.items;
                newCursor = $scope.cursor;
                $scope.transfers.forEach(function(item){
                    item.txlink =  COINPORT.txUrl[item.amount.currency]+item.txid;
                    if (newCursor > item.id)
                      newCursor = item.id;
                });
                if (newCursor < $scope.cursor) {
                  $scope.count = $scope.count + $scope.transfers.length
                  $scope.cursor = newCursor
                }
            });
    };

    $scope.reloadTransfers();

    $scope.loadFiles = function() {
        $http.get('/api/open/data/csv/asset/'+$scope.coin.toLowerCase(), {params: {limit: $scope.filesLimit, page: $scope.filesPage}})
        .success(function(data, status, headers, config) {
            $scope.files = data.data;
        });
    };

    $scope.loadFiles();

    $scope.toggleSignMessage = function(wallet) {
        wallet.showSign = !wallet.showSign;
    };
});
