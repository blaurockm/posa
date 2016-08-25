(function(angular) {
	angular.module("verwApp").config( 
           [ '$stateProvider', '$urlRouterProvider', 'eehNavigationProvider',
     function($stateProvider ,  $urlRouterProvider, eehNavigationProvider) {
	   
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
				abstract : true,
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
				abstract : true,
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
			  				$scope.zahlintervalle = [{value:'EACHDELIVERY', text: 'pro Lieferung '}];
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
			  				$scope.zahlintervalle = [{value:'EACHDELIVERY', text: 'pro Lieferung '},{value:'MONTHLY', text: 'Monatlich'},{value:'QUARTERLY', text: 'Vierteljährlich'},{value:'HALFYEARLY', text: 'Halbjährlich'},{value:'YEARLY', text: 'Jährlich'}];
			  			}	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modsubscrproducts/menu.html',
			  		}
				} 
		  }).
		  state('subscriptions', {
				url: "/subscriptions",
				abstract : true,
				views :{
			  		"@" :  {
			  			templateUrl : 'modsubscriptions/start.html',
			  			controller: function($scope) { 
			  				$scope.laeden = [{value: 1, text: 'Dornhan'},{value: 2, text: 'Sulz'} ,{value: 3, text: 'Schramberg'}];
			  				$scope.versandarten = [{value:'DELIVERY', text: 'Belieferung d. Buchlese'},{value:'PICKUP', text: 'Abholer'},{value:'PUBLISHER', text: 'durch den Verlag'},{value:'MAILCOST', text: 'per Post mit Porto'},{value:'MAIL', text: 'per Post'}];
			  				$scope.zahlintervalle = [{value:'EACHDELIVERY', text: 'pro Lieferung '},{value:'MONTHLY', text: 'Monatlich'},{value:'QUARTERLY', text: 'Vierteljährlich'},{value:'HALFYEARLY', text: 'Halbjährlich'},{value:'YEARLY', text: 'Jährlich'}];
			  				$scope.sammelnDerRechnungen =[{value:true, text: 'Sammelrechnung erwünscht'},{value:false,text:'keine Sammelrechnung'}];
			  				$scope.liefernMitSchein=[{value:true, text: 'Lieferschein notwendig'},{value:false,text:'ohne Dok'}];
			  			}	
			  		},
			  		"mainmenunav" :  {
			  			templateUrl : 'modsubscriptions/menu.html',
			  		}
				} 
		  }).
		  state('invoices', {
				url: "/invoices",
				abstract : true,
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
		eehNavigationProvider
		.menuItem('navtop.home', {
			text: 'Home',
			iconClass: 'glyphicon-home',
			weight: 1,
			state: 'menu'
		});
	  }]  
	);
	
}(angular));
