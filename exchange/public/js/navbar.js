app = angular.module('navbar', ['ngCookies'])
app.controller('NotificationCtrl', function($scope, $http, $cookieStore) {
    $http.get('/notifications')
        .success(function(result, status, headers, config) {
            cookieAlerts = $cookieStore.get('alerts') || {};
            $scope.alerts = [];
            result.data.forEach(function(alert) {
            if(!cookieAlerts[alert.id])
                $scope.alerts.push(alert);
            });
        });

    $scope.closeAlert = function(index) {
        alert = $scope.alerts[index];
        $scope.alerts.splice(index, 1);
        cookieAlerts = $cookieStore.get('alerts') || {};
        cookieAlerts[alert.id] = new Date().getTime();
        $cookieStore.put('alerts', cookieAlerts);
    };
});

app.directive("repeatInput", function() {
  return {
    require: "ngModel",
    link: function(scope, elem, attrs, ctrl) {
      var otherInput = elem.inheritedData("$formController")[attrs.repeatInput];

      ctrl.$parsers.push(function(value) {
        if(value === otherInput.$viewValue) {
          ctrl.$setValidity("repeat", true);
          return value;
        }
        ctrl.$setValidity("repeat", false);
      });

      otherInput.$parsers.push(function(value) {
        ctrl.$setValidity("repeat", value === ctrl.$viewValue);
        return value;
      });
    }
  };
});
