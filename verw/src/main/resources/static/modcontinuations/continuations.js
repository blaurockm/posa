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
	                queryParams["sort"] = sortingProp[0] + ',' + params.sorting()[sortingProp[0]];
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

  var ContinuationDispoController = function($scope, $stateParams, $http, ContinuationDao, NgTableParams, $filter ) {
	  ContinuationDao.get({id : $stateParams.id}, function(data) { 
	    $scope.cust = data
	    $http.get(data._links.subscriptions.href).
	    then(function(data2) { 
	    	$scope.subscriptions = data2.data._embedded.subscriptions; 
	    });
	    $http.get(data._links.articles.href).
	    then(function(data3) { 
			$scope.tableParams = new NgTableParams({sorting: { startDate: "asc" }},{counts:[],dataset:data3.data._embedded.subscrArticles});
	    });
	  });

	  $scope.selectArticle = function(intvl) {
		  $scope.article = intvl;
		  $http.get(intvl._links.deliveries.href).
		    then(function(data2) { 
		    	$scope.deliveries = data2.data._embedded.subscrDeliveries; 
		    });
	  };

	  $scope.createNextArticle = function(cust) {
		  var nextIntvl = {};
		  var pattern = cust.namePattern;
		  nextIntvl.productId=cust.id;
		  // create next Interval Name
		  nextIntvl.name = pattern.replace('#', $scope.nextArticleName(cust));
		  nextIntvl.issueNo = cust.count +1;
		  if ($scope.article != null) {
			  nextIntvl.halfPercentage = $scope.article.halfPercentage;
			  nextIntvl.bruttoFull = $scope.article.bruttoFull;
			  nextIntvl.bruttoHalf = $scope.article.bruttoHalf;
			  nextIntvl.brutto = $scope.article.brutto;
		  }
		  $http.post('/subscrproducts/createarticle', nextIntvl).then( function() {
			  $http.get(cust._links.articles.href).
			  then(function(data3) { 
				  $scope.tableParams = new NgTableParams({sorting: { startDate: "asc" }},{counts:[],dataset:data3.data._embedded.subscrArticles});
			  });
		  });
		  cust.count = cust.count +1;
		  $scope.updateContinuation(cust);
	  }

	  $scope.createArticleDelivery = function(subscription) {
		  var intvlDeliv = {}
		  intvlDeliv.subscriptionId=subscription.id; 
		  intvlDeliv.subscriberId=subscription.subscriberId; 
		  intvlDeliv.articleId=$scope.article.id; 
		  intvlDeliv.deliveryDate=moment().valueOf();
		  intvlDeliv.quantity=subscription.quantity;
		  intvlDeliv.articleName=$scope.article.name;
		  intvlDeliv.total=$scope.article.brutto;
		  intvlDeliv.totalFull=$scope.article.brutto_full;
		  intvlDeliv.totalHalf=$scope.article.brutto_half;
		  intvlDeliv.shipmentCost=0;
		  intvlDeliv.creationDate=moment().valueOf();
		  $http.post('/subscrproducts/createarticledelivery', intvlDeliv).then( function() {
			  $http.get($scope.interval._links.deliveries.href).
			    then(function(data2) { 
			    	$scope.deliveries = data2.data._embedded.subscrDeliveries; 
			    });
		  });
	  }
	  
	  $scope.nextArticleName = function(cust) {
		  if (!cust) return "--";
		  var nLief = cust.count +1;
		  return nLief +". Lieferung";
	  }

	  $scope.isDelivered = function(subscription) {
		  return $scope.deliveries != null && $filter('filter')($scope.deliveries, {subscriberId: subscription.subscriberId}).length > 0;
	  }
	  
	  $scope.updateBrutto = function(newVal) {
		  $scope.article.brutto  = newVal;
		  $scope.article.brutto_half = Math.round(newVal * $scope.article.halfPercentage);
		  $scope.article.brutto_full = $scope.article.brutto - $scope.article.brutto_half;
		  $scope.updateArticle();
	  };
	  $scope.updateBruttoHalf = function(newVal) {
		  $scope.article.brutto_half = newVal;
		  $scope.article.brutto_full = $scope.article.brutto - newVal;
		  $scope.article.halfPercentage = $scope.article.brutto_half / $scope.article.brutto;
		  $scope.updateArticle();
	  };
	  $scope.updateBruttoFull = function(newVal) {
		  $scope.article.brutto_full = newVal;
		  $scope.article.brutto_half = $scope.article.brutto - newVal;
		  $scope.article.halfPercentage = $scope.article.brutto_half / $scope.article.brutto;
		  $scope.updateArticle();
	  };
	  
	  $scope.updateArticle = function() {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put($scope.article._links.self.href,$scope.article).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
	  
	  $scope.updateContinuation = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }

  };
  
  ContinuationController.$inject = ['$scope', '$http', 'ContinuationDAO', 'NgTableParams'];
  ContinuationDetailController.$inject = ['$scope', '$stateParams', '$http', 'ContinuationDAO'];
  ContinuationDispoController.$inject = ['$scope', '$stateParams', '$http', 'ContinuationDAO',
                                           'NgTableParams', '$filter'];

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
   controller("ContinuationDispoController", ContinuationDispoController).
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
		}).
		state('continuations.dispo', {
			url: "/continuationdispo/{id:int}",
			templateUrl: "modcontinuations/dispo.html",
			controller: 'ContinuationDispoController'	
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
  