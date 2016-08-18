(function(angular) {

  angular.module("verwApp.coupon", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm"]);
  angular.module("verwApp.accounting", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment']);

  angular.module("verwApp", ["ui.router", "ui.select", "ui.bootstrap","verwApp.coupon", "verwApp.accounting"]);


}(angular));
