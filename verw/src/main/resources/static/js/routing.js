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
		  state('balances', {
				url: "/balances",
				views :{
			  		"@" :  {
			  			templateUrl : 'modbalances/start.html',
			  			controller: 'BalancesMainController'	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modbalances/menu.html',
			  		}
				} 
		  }).
		  state('invoices', {
				url: "/invoices",
				views :{
			  		"@" :  {
			  			templateUrl : 'modinvoices/start.html',
			  			controller: 'InvoicesMainController'	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modinvoices/menu.html',
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
