angular.module('navbar', [])
  .controller('NaviCtrl', function ($scope, $modal, $log) {
    $scope.openLoginWindow = function (activeTab) {
      $scope.activeTab = activeTab;
      var modalInstance = $modal.open({
        templateUrl: 'register.html',
        controller: function ($scope, $http, $modalInstance) {
          $scope.login = {};
          $scope.register = {};

          $scope.activeTab = $scope.$parent.activeTab;

          $scope.isRegisterActive = ($scope.activeTab == 1);

          $scope.doLogin = function () {
            $http.post('login', $scope.login)
              .success(function(data, status, headers, config) {
                if (data.success) {
                  $scope.$parent.username = $scope.login.username;
                  $scope.$parent.isLogin = true;
                  $modalInstance.close();
                } else {
                  $scope.$parent.loginErrorMessage = data.message;
                  $scope.$parent.showLoginError = true;
                }
              });
          };

          $scope.doRegister = function () {
            $http.post('register', $scope.register)
              .success(function(data, status, headers, config) {
                console.log(data);
                if (data.success) {
                  $scope.$parent.username = $scope.register.username;
                  $scope.$parent.isLogin = true;
                  $modalInstance.close();
                } else {
                  $scope.$parent.registerErrorMessage = data.message;
                  $scope.$parent.showRegisterError = true;
                }
              });
          };

          $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
          };
        },
        scope: $scope
      });

      modalInstance.result.then(function (selectedItem) {
        $scope.selected = selectedItem;
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });
    };
  });