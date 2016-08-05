(function(angular) {
  
  var CouponFactory = function($resource) {
    return $resource('/coupons/:id', { id: '@id' }, {
    	  update: {method:'PUT'}
    });
  };
  
  CouponFactory.$inject = ['$resource'];
  angular.module("myApp.services").factory("Coupon", CouponFactory);
}(angular));
