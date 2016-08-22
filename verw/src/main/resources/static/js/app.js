(function(angular) {

  angular.module("verwApp.coupon", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm"]);
  angular.module("verwApp.balances", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"ngPrint"]);
  angular.module("verwApp.invoices", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"ngPrint"]);

  angular.module("verwApp", ["ui.router", "ui.select", "ui.bootstrap","verwApp.coupon", "verwApp.balances", "verwApp.invoices"]);


}(angular));
