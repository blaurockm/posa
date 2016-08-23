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
		  state('customers', {
				url: "/customers",
				views :{
			  		"@" :  {
			  			templateUrl : 'modcustomers/start.html',
			  			controller: function($scope) { 
			  				$scope.laeden = [{value: 1, text: 'Dornhan'},{value: 2, text: 'Sulz'} ,{value: 3, text: 'Schramberg'}];
			  				$scope.versandarten = [{value:'DELIVERY', text: 'Belieferung d. Buchlese'},{value:'PICKUP', text: 'Abholer'},{value:'PUBLISHER', text: 'durch den Verlag'},{value:'MAILCOST', text: 'per Post mit Porto'},{value:'MAIL', text: 'per Post'}];
			  				$scope.sammelnDerRechnungen =[{value:true, text: 'Sammelrechnung erwünscht'},{value:false,text:'keine Sammelrechnung'}];
			  				$scope.liefernMitSchein=[{value:true, text: 'Lieferschein notwendig'},{value:false,text:'ohne Dok'}];
			  			}	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modcustomers/menu.html',
			  		}
				} 
		  }).
		  state('continuations', {
				url: "/continuations",
				views :{
			  		"@" :  {
			  			templateUrl : 'modcontinuations/start.html',
			  			controller: function($scope) { 
			  				$scope.versandarten = [{value:'DELIVERY', text: 'Belieferung d. Buchlese'},{value:'PICKUP', text: 'Abholer'},{value:'PUBLISHER', text: 'durch den Verlag'},{value:'MAILCOST', text: 'per Post mit Porto'},{value:'MAIL', text: 'per Post'}];
			  			}	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modcontinuations/menu.html',
			  		}
				} 
		  }).
		  state('subscrproducts', {
				url: "/subscrproducts",
				views :{
			  		"@" :  {
			  			templateUrl : 'modsubscrproducts/start.html',
			  			controller: function($scope) { 
			  				$scope.versandarten = [{value:'DELIVERY', text: 'Belieferung d. Buchlese'},{value:'PICKUP', text: 'Abholer'},{value:'PUBLISHER', text: 'durch den Verlag'},{value:'MAILCOST', text: 'per Post mit Porto'},{value:'MAIL', text: 'per Post'}];
			  			}	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modsubscrproducts/menu.html',
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
