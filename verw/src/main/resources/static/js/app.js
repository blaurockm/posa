(function(angular) {

  angular.module("verwApp.coupon", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm"]);
  angular.module("verwApp.commands", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable"]);
  angular.module("verwApp.issueslips", ["ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable"]);
  angular.module("verwApp.balances", ["eehNavigation", "ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"ngPrint"]);
  angular.module("verwApp.invoices", ["eehNavigation","ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"ngPrint","xeditable"]);
  angular.module("verwApp.customers", ["eehNavigation","ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"xeditable"]);
  angular.module("verwApp.continuations", ["eehNavigation","ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"xeditable"]);
  angular.module("verwApp.subscrproducts", ["eehNavigation","ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"xeditable"]);
  angular.module("verwApp.subscriptions", ["eehNavigation","ngResource", "ui.router", "ui.select", "ui.bootstrap", "ngSanitize", "angular-confirm","ngTable", 'angularMoment',"xeditable"]);

  angular.module("verwApp", ["eehNavigation","ui.router", "ui.select", "ui.bootstrap",
                             "verwApp.coupon", "verwApp.balances", "verwApp.invoices",
                             "verwApp.commands", "verwApp.issueslips",
                             "verwApp.customers","verwApp.continuations","verwApp.subscrproducts","verwApp.subscriptions"]);


}(angular));
