(function(angular) {
  var ContinuationController = function($scope, $http, ContinuationDao, NgTableParams) {
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
	            ContinuationDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var ContinuationDetailController = function($scope, $stateParams, $http, ContinuationDao ) {
	  ContinuationDao.get({id : $stateParams.id}, function(data) { 
	    $scope.cust = data
	  });
	  
	  $scope.updateContinuation = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
  };

  ContinuationController.$inject = ['$scope', '$http', 'ContinuationDAO', 'NgTableParams'];
  ContinuationDetailController.$inject = ['$scope', '$stateParams', '$http', 'ContinuationDAO'];

  var ContinuationFactory = function($resource) {
    return $resource('/api/subscrproduct/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/subscrproducts/continuationsDyn' } 
    		} );
  };
  
  ContinuationFactory.$inject = ['$resource'];

  angular.module("verwApp.continuations").
   factory("ContinuationDAO", ContinuationFactory).
   controller("ContinuationController", ContinuationController).
   controller("ContinuationDetailController", ContinuationDetailController).
   controller("ContinuationMainController", function($scope) {}).
   config(['$stateProvider','eehNavigationProvider', function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('continuations.continuations', {
			url: "/continuations",
			templateUrl: "modcontinuations/continuations.html",
			controller: 'ContinuationController'	
		}).
		state('continuations.continuationdetail', {
			url: "/continuationdetail/{id:int}",
			templateUrl: "modcontinuations/continuationdetail.html",
			controller: 'ContinuationDetailController'	
		});
    eehNavigationProvider.
    menuItem('navside.continuations', {
 	     text : 'Fortsetzungen', isCollapsed: true, iconClass: 'fa fa-file-text-o'
    }).
  	menuItem('navside.continuations.continuations', {
       	 text : 'Fortsetzungs-Suche',
       	 state : 'continuations.continuations'
    });

   }]);

  angular.module("verwApp.continuations").
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
  