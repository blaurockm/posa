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
	    $scope.cust = data;
	  });
	  
	  $scope.updateSubscrProduct = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
  };

  var SubscrProductDispoController = function($scope, $stateParams, $http, SubscrProductDao, NgTableParams, $filter ) {
	  SubscrProductDao.get({id : $stateParams.id}, function(data) { 
	    $scope.cust = data
	    $http.get(data._links.subscriptions.href).
	    then(function(data2) { 
	    	$scope.subscriptions = data2.data._embedded.subscriptions; 
	    });
	    $http.get(data._links.intervals.href).
	    then(function(data3) { 
			$scope.tableParams = new NgTableParams({sorting: { startDate: "asc" }},{counts:[],dataset:data3.data._embedded.subscrIntervals});
	    });
	  });

	  $scope.selectInterval = function(intvl) {
		  $scope.interval = intvl;
		  $http.get(intvl._links.deliveries.href).
		    then(function(data2) { 
		    	$scope.deliveries = data2.data._embedded.subscrIntervalDeliveries; 
		    });
	  };

	  $scope.createNextInterval = function(cust) {
		  var nextIntvl = {};
		  var pattern = cust.intervalPattern ? cust.intervalPattern : cust.namePattern;
		  var beginMom = moment(cust.lastInterval).add(1,'d')
		  var beginStr = beginMom.format("MM/YYYY");
		  nextIntvl.startDate = beginMom.valueOf();
		  
		  var endMom = $scope.computeIntervalEndmom(cust, beginMom);
		  var endStr = endMom.format("MM/YYYY");
		  nextIntvl.endDate = endMom.valueOf();
		  nextIntvl.productId=cust.id;
		  // create next Interval Name
		  nextIntvl.name = pattern.replace('#', $scope.nextIntervalName(cust));
		  nextIntvl.intervalType = cust.intervalType;
		  if ($scope.interval != null) {
			  nextIntvl.halfPercentage = $scope.interval.halfPercentage;
			  nextIntvl.bruttoFull = $scope.interval.bruttoFull;
			  nextIntvl.bruttoHalf = $scope.interval.bruttoHalf;
			  nextIntvl.brutto = $scope.interval.brutto;
		  }
		  $http.post('/subscrproducts/createinterval', nextIntvl).then( function() {
			  $http.get(cust._links.intervals.href).
			  then(function(data3) { 
				  $scope.tableParams = new NgTableParams({sorting: { startDate: "asc" }},{counts:[],dataset:data3.data._embedded.subscrIntervals});
			  });
		  });
		  cust.lastInterval = endMom.format("YYYY-MM-DD");
		  $scope.updateSubscrProduct();
	  }

	  $scope.createIntervalDelivery = function(subscription) {
		  var intvlDeliv = {}
		  intvlDeliv.subscriptionId=subscription.id; 
		  intvlDeliv.subscriberId=subscription.subscriberId; 
		  intvlDeliv.intervalId=$scope.interval.id; 
		  intvlDeliv.deliveryDate=moment().valueOf();
		  intvlDeliv.quantity=subscription.quantity;
		  intvlDeliv.intervalName=$scope.interval.name;
		  intvlDeliv.total=$scope.interval.brutto;
		  intvlDeliv.totalFull=$scope.interval.brutto_full;
		  intvlDeliv.totalHalf=$scope.interval.brutto_half;
		  intvlDeliv.shipmentCost=0;
		  intvlDeliv.creationDate=moment().valueOf();
		  $http.post('/subscrproducts/createintervaldelivery', intvlDeliv).then( function() {
			  $http.get($scope.interval._links.deliveries.href).
			    then(function(data2) { 
			    	$scope.deliveries = data2.data._embedded.subscrIntervalDeliveries; 
			    });
		  });
	  }
	  
	  $scope.nextIntervalName = function(cust) {
		  if (!cust) return "--";
		  var beginMom = moment(cust.lastInterval).add(1,'d')
		  var beginStr = beginMom.format("MM/YYYY");
		  var endMom = $scope.computeIntervalEndmom(cust, beginMom);
		  var endStr = endMom.format("MM/YYYY");
		  return beginStr +"-" + endStr;
//			String beginStr = startDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
//			String endStr = endDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
//			String intervalStr = beginStr + "-" + endStr;
//			String name = namePattern.replace("#", intervalStr);
//			name = name.replace("{start}", beginStr);
//			name = name.replace("{end}", endStr);
//			name = name.replace("{interval}", intervalStr);
//			switch (intervalType) {
//			case YEARLY : name = name.replace("{type}", "Jahr"); break;
//			case HALFYEARLY : name = name.replace("{type}", "Halbjahr"); break;
//			case MONTHLY : name = name.replace("{type}", "Monat"); break;
//			default: name = name.replace("{type}", ""); break;
//			}
//			name = name.replace("{type}", intervalStr);
//			String dp = "\\{date:(.+)\\}";
//			Pattern p = Pattern.compile(dp);
//			Matcher m = p.matcher(name);
//			if (m.find()) {
//				String datePattern = m.group(1);
//				String dateString = startDate.format(DateTimeFormatter.ofPattern(datePattern));
//				name = name.replaceFirst(dp, dateString);
//				dateString = endDate.format(DateTimeFormatter.ofPattern(datePattern));
//				name = name.replaceFirst(dp, dateString);
//			}
//			return name;
	  }

	  $scope.isDelivered = function(subscription) {
		  return $scope.deliveries != null && $filter('filter')($scope.deliveries, {subscriberId: subscription.subscriberId}).length > 0;
	  }
	  
	  $scope.computeIntervalEndmom = function(cust, startMom) {
		  var endMom = null;
			switch (cust.intervalType) {
			case 'YEARLY': endMom = startMom.add(1,'y').subtract(1,'d'); break;
			case 'HALFYEARLY': endMom = startMom.add(6,'M').subtract(1,'d'); break;
			case 'QUARTERLY': endMom = startMom.add(3,'M').subtract(1,'d'); break;
			case 'MONTHLY': endMom = startMom.add(1,'M').subtract(1,'d'); break;
			default: endMom = startMom.add(1,'d');
			}
		  return endMom;	
	  }
	  
	  
	  $scope.updateBrutto = function(newVal) {
		  $scope.interval.brutto  = newVal;
		  $scope.interval.brutto_half = Math.round(newVal * $scope.interval.halfPercentage);
		  $scope.interval.brutto_full = $scope.interval.brutto - $scope.interval.brutto_half;
		  $scope.updateInterval();
	  };
	  $scope.updateBruttoHalf = function(newVal) {
		  $scope.interval.brutto_half = newVal;
		  $scope.interval.brutto_full = $scope.interval.brutto - newVal;
		  $scope.interval.halfPercentage = $scope.interval.brutto_half / $scope.interval.brutto;
		  $scope.updateInterval();
	  };
	  $scope.updateBruttoFull = function(newVal) {
		  $scope.interval.brutto_full = newVal;
		  $scope.interval.brutto_half = $scope.interval.brutto - newVal;
		  $scope.interval.halfPercentage = $scope.interval.brutto_half / $scope.interval.brutto;
		  $scope.updateInterval();
	  };
	  
	  $scope.updateInterval = function() {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put($scope.interval._links.self.href,$scope.interval).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }
	  $scope.updateSubscrProduct = function(cust) {
	      $scope.error = "";
	      $scope.success = "";
	      $http.put(cust._links.self.href,cust).then(function(req) { $scope.success = req; })
	      .catch(function(req) { $scope.error = req; });
	  }

  };

  SubscrProductController.$inject = ['$scope', '$http', 'SubscrProductDAO', 'NgTableParams'];
  SubscrProductDetailController.$inject = ['$scope', '$stateParams', '$http', 'SubscrProductDAO'];
  SubscrProductDispoController.$inject = ['$scope', '$stateParams', '$http', 'SubscrProductDAO',
                                          'NgTableParams', '$filter'];

  var SubscrProductFactory = function($resource) {
    return $resource('/api/subscrproduct/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/subscrproducts/subscrproductsDyn' } 
    		} );
  };
  
  SubscrProductFactory.$inject = ['$resource'];

  angular.module("verwApp.subscrproducts").
   factory("SubscrProductDAO", SubscrProductFactory).
   controller("SubscrProductController", SubscrProductController).
   controller("SubscrProductDetailController", SubscrProductDetailController).
   controller("SubscrProductDispoController", SubscrProductDispoController).
   config(['$stateProvider', 'eehNavigationProvider', function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('subscrproducts.subscrproducts', {
			url: "/subscrproducts",
			templateUrl: "modsubscrproducts/subscrproducts.html",
			controller: 'SubscrProductController'	
		}).
		state('subscrproducts.dispo', {
			url: "/subscrproductdispo/{id:int}",
			templateUrl: "modsubscrproducts/dispo.html",
			controller: 'SubscrProductDispoController'	
		}).
		state('subscrproducts.subscrproductdetail', {
			url: "/subscrproductdetail/{id:int}",
			templateUrl: "modsubscrproducts/subscrproductdetail.html",
			controller: 'SubscrProductDetailController'	
		});
	    eehNavigationProvider.
	    menuItem('navside.subscrproducts', {
	 	     text : 'Abonnements', isCollapsed : true,
	 	     iconClass : 'fa fa-clock-o'
	    }).
	  	menuItem('navside.subscrproducts.subscrproducts', {
	       	 text : 'Abonnement-Suche',
	       	 state : 'subscrproducts.subscrproducts'
	    });

   }]);

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
  