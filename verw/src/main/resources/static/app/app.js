(function(angular) {
  angular.module("verwApp.controllers", []);
  angular.module("verwApp.services", []);
  angular.module("verwApp", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm", "verwApp.controllers", "verwApp.services"]);
}(angular));
