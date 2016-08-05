(function(angular) {
  
  var CouponFactory = function($resource) {
    return $resource('/coupons/:id', { id: '@id' }, {
    	  update: {method:'PUT'}
    });
  };
  
  CouponFactory.$inject = ['$resource'];
  angular.module("verwApp.services").factory("CouponDAO", CouponFactory);
}(angular));
