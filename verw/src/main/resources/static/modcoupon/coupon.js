(function(angular) {
  var CouponInputController = function($scope, $http, Coupon) {
	Coupon.query(function(response) {
      $scope.coupons = response ? response : [];
    });
    
    $scope.addCoupon = function(coup) {
      $scope.error = "";
      $scope.success = "";
      new Coupon(coup).$save(function(coupon) {
        $scope.coupons.push(coupon);
      }).then(function(req) { $scope.success = req; })
      .catch(function(req) { $scope.error = req; });
      $scope.newcoup = "";
    };

    $scope.filtering = function(filter) {
    	Coupon.query({pupil : filter}, function(response) {
   	      $scope.coupons = response ? response : [];
   	    });
    }

    $scope.updateCoupon = function(coupon) {
    	coupon.amount = coupon.amount+1;
      coupon.$update();
    };
    
    $scope.deleteCoupon = function(coupon) {
      coupon.$remove(function() {
        $scope.coupons.splice($scope.coupons.indexOf(coupon), 1);
      });
    };
    
    $scope.customers = [];
    $scope.refreshCustomers = function(cust) {
      var params = {name: cust};
      return $http.get('/customer', {params: params})
        .then(function(response) {
          $scope.customers = response.data._embedded.customer
        });
    };    
    
  };
  

  var CouponFactory = function($resource) {
    return $resource('/coupons/:id', { id: '@id' }, {
    	  update: {method:'PUT'}
    });
  };
  
  
  angular.module("verwApp.coupon").
   factory("CouponDAO", CouponFactory).
   controller("CouponInputController", CouponInputController).
   controller("CouponMainController", function($scope) {}).
   config(function ($stateProvider) {
    $stateProvider.
		state('coupon.input', {
			url: "/input",
			templateUrl: "modcoupon/input.html",
			controller: 'CouponInputController'	
		}).
		state('coupon.dashboard', {
			url: "/dashboard",
			templateUrl: "modcoupon/dashboard.html"
		});
   });

  CouponInputController.$inject = ['$scope', '$http', 'CouponDAO'];
  CouponFactory.$inject = ['$resource'];

}(angular));
