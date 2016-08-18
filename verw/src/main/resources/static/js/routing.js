(function(angular) {
	angular.module("verwApp").config( 
           [ '$stateProvider', '$urlRouterProvider', 
     function($stateProvider ,  $urlRouterProvider) {
		// For any unmatched url, redirect to /state1
		$urlRouterProvider.otherwise("/menu");
		//
		// Now set up the states
		$stateProvider.
		  state('menu', {
			  	url: "/menu",
			  	views : {
			  		"@" :  {
			  			template : '<div class="container"> Startseite </div>'
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'menu.html',
			  		}
			  	}
		  }).
		  state('accounting', {
				url: "/accounting",
				views :{
			  		"@" :  {
			  			templateUrl : 'modaccounting/start.html',
			  			controller: 'AccountingMainController'	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modaccounting/menu.html',
			  		}
				} 
		  }).
		  state('coupon', {
			    url: "/coupon",
			    views : {
			  		"@" :  {
			  			templateUrl : 'modcoupon/start.html',
			  			controller: 'CouponMainController'	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modcoupon/menu.html',
			  		}
			    }
		  });
	  }]  
	);
}(angular));
