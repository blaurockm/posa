(function(angular) {
  var SubscriptionController = function($scope, $http, SubscriptionDao, NgTableParams) {
	    $scope.reloadData = function () {
	    	$scope.tableParams.reload();
	    }

	    $scope.tableParams = new NgTableParams({ count: 15 }, {
	        getData: function($defer, params) {
	            var queryParams = {
	                page:params.page() - 1, 
	                size:params.count()
	            };
	            var sortingProp = Object.keys(params.sorting());
	            if(sortingProp.length == 1){
	                queryParams["sort"] = sortingProp[0];
	                queryParams["sortDir"] = params.sorting()[sortingProp[0]];
	            }
	            if (params.hasFilter()) {
	            	angular.extend(queryParams, params.filter());
	            }
	            if ($scope.hasOwnProperty('pointofsale')) {
	            	queryParams['pointid'] = $scope.pointofsale;
	            }
	            SubscriptionDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var SubscriptionDetailController = function($scope, $stateParams, $http, SubscriptionDao, NgTableParams ) {
	  SubscriptionDao.get({id : $stateParams.id}, function(data) { 
	    $scope.cust = data
	    $http.get(data._links.subscriber.href).
	    then(function(data2) { 
	    	$scope.subscriber = data2.data; 
	    });
	    $http.get(data._links.product.href).
	    then(function(data3) { 
			$scope.product = data3.data;
			$http.get(data._links.articleDeliveries.href).
			then(function(data3) { 
				$scope.articleDeliveries = new NgTableParams({sorting: { deliveryDate: "asc" }},{counts:[],dataset:data3.data._embedded.subscrDeliveries});
			});
			$http.get(data._links.intervalDeliveries.href).
			then(function(data3) { 
				$scope.intervalDeliveries = new NgTableParams({sorting: { deliveryDate: "asc" }},{counts:[],dataset:data3.data._embedded.subscrIntervalDeliveries});
			});
	    });

	  });
	  
	  $scope.updateSubscription = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
  };

  SubscriptionController.$inject = ['$scope', '$http', 'SubscriptionDAO', 'NgTableParams'];
  SubscriptionDetailController.$inject = ['$scope', '$stateParams', '$http', 'SubscriptionDAO','NgTableParams'];

  var SubscriptionFactory = function($resource) {
    return $resource('/api/subscription/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/subscriptions/subscriptionsDyn' } 
    		} );
  };
  
  SubscriptionFactory.$inject = ['$resource'];

  angular.module("verwApp.subscriptions").
   factory("SubscriptionDAO", SubscriptionFactory).
   controller("SubscriptionController", SubscriptionController).
   controller("SubscriptionDetailController", SubscriptionDetailController).
   controller("SubscriptionMainController", function($scope) {}).
   config(['$stateProvider','eehNavigationProvider',function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('subscriptions.subscriptions', {
			url: "/subscriptions",
			templateUrl: "modsubscriptions/subscriptions.html",
			controller: 'SubscriptionController'	
		}).
		state('subscriptions.subscriptiondetail', {
			url: "/subscriptiondetail/{id:int}",
			templateUrl: "modsubscriptions/subscriptiondetail.html",
			controller: 'SubscriptionDetailController'	
		});
    eehNavigationProvider.
    menuItem('navside.subscriptions', {
 	     text : 'Kunden-Abos', isCollapsed: true, iconClass: 'fa fa-newspaper-o'
    }).
  	menuItem('navside.subscriptions.subscriptions', {
       	 text : 'Kundenabo Suche',
       	 state : 'subscriptions.subscriptions'
    });

   }]);

  angular.module("verwApp.subscriptions").
   filter('localdatetime', function($filter) {
	    return function(input, optional) {
	    	if (input == null) {
	    		return "";
	    	}
	        var d = new Date(input);
	        return $filter('date')(d,(optional ? optional : 'medium'));
	    };
	}).
   filter('localdate', function($filter) {
	    return function(input, optional) {
	    	if (input == null) {
	    		return "";
	    	}
	        var d = new Date(input);
	        return $filter('date')(d, (optional ? optional : 'fullDate'));
	    };
	}).
	run(function (amMoment, editableOptions) {
		amMoment.changeLocale('de');
		editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
	});

}(angular));
  