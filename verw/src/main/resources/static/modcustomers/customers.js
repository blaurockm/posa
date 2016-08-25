(function(angular) {
  var CustomerController = function($scope, $http, CustomerDao, NgTableParams) {
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
	            CustomerDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
	    
  };

  var CustomerDetailController = function($scope, $stateParams, $http, CustomerDao ) {
	  CustomerDao.get({id : $stateParams.id}, function(data) { 
	    $scope.cust = data
	  });
	  
	  $scope.updateCustomer = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
  };

  CustomerController.$inject = ['$scope', '$http', 'CustomerDAO', 'NgTableParams'];
  CustomerDetailController.$inject = ['$scope', '$stateParams', '$http', 'CustomerDAO'];

  var CustomerFactory = function($resource) {
    return $resource('/api/customer/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/customers/customersDyn' } 
    		} );
  };
  
  CustomerFactory.$inject = ['$resource'];

  angular.module("verwApp.customers").
   factory("CustomerDAO", CustomerFactory).
   controller("CustomerController", CustomerController).
   controller("CustomerDetailController", CustomerDetailController).
   controller("CustomerMainController", function($scope) {}).
   config(['$stateProvider', 'eehNavigationProvider',function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('customers.customers', {
			url: "/customers",
			templateUrl: "modcustomers/customers.html",
			controller: 'CustomerController'	
		}).
		state('customers.customerdetail', {
			url: "/customerdetail/{id:int}",
			templateUrl: "modcustomers/customerdetail.html",
			controller: 'CustomerDetailController'	
		});
    eehNavigationProvider.
    menuItem('navside.customers', {
 	     text : 'Kunden',
 	     iconClass : 'fa fa-female', weight:-10
    }).
  	menuItem('navside.customers.customers', {
       	 text : 'Kunden Suche',
       	 state : 'customers.customers'
    });
    
   }]);

  angular.module("verwApp.customers").
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
  