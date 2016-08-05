(function(angular) {
	angular.module("verwApp").config( [ '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
		// For any unmatched url, redirect to /state1
		$urlRouterProvider.otherwise("/couponDashboard");
		//
		// Now set up the states
		$stateProvider
		.state('couponInput', {
			url: "/couponInput",
			templateUrl: "coupon/input.html",
			controller: 'CouponInputController'	
		})
		.state('couponDashboard', {
			url: "/couponDashboard",
			templateUrl: "coupon/dashboard.html",
			controller: function($scope) {
				$scope.items = ["A", "List", "Of", "Items"];
			}
		});
	}]  
	);
}(angular));
