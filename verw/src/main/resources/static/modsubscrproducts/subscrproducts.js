(function(angular) {
  var SubscrProductController = function($scope, $http, SubscrProductDao, NgTableParams) {
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
	            SubscrProductDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var SubscrProductDetailController = function($scope, $stateParams, $http, SubscrProductDao ) {
	  SubscrProductDao.get({id : $stateParams.id}, function(data) { 
	    $scope.cust = data
	  });
	  
	  $scope.updateSubscrProduct = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
  };

  SubscrProductController.$inject = ['$scope', '$http', 'SubscrProductDAO', 'NgTableParams'];
  SubscrProductDetailController.$inject = ['$scope', '$stateParams', '$http', 'SubscrProductDAO'];

  var SubscrProductFactory = function($resource) {
    return $resource('/api/subscrproduct/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/subscrproduct/subscrproductsDyn' } 
    		} );
  };
  
  SubscrProductFactory.$inject = ['$resource'];

  angular.module("verwApp.subscrproducts").
   factory("SubscrProductDAO", SubscrProductFactory).
   controller("SubscrProductController", SubscrProductController).
   controller("SubscrProductDetailController", SubscrProductDetailController).
   controller("SubscrProductMainController", function($scope) {}).
   config(function ($stateProvider) {
    $stateProvider.
		state('subscrproducts.subscrproducts', {
			url: "/subscrproducts",
			templateUrl: "modsubscrproducts/subscrproducts.html",
			controller: 'SubscrProductController'	
		}).
		state('subscrproducts.subscrproductdetail', {
			url: "/subscrproductdetail/{id:int}",
			templateUrl: "modsubscrproducts/subscrproductdetail.html",
			controller: 'SubscrProductDetailController'	
		});
   });

  angular.module("verwApp.subscrproducts").
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
  