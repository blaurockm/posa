(function(angular) {
  var BalanceController = function($scope, $http, BalanceDao, NgTableParams, $uibModal) {
	    $scope.showBalance = function (bal) {
	    	$uibModal.open({
	    	      animation: true,
	    	      templateUrl: '/templates/balanceReport.html',
	    	      controller: function($scope) {
	    	    	  $scope.balance = bal;
	    	      },
	    	      size: 'lg'
	    	    });
	    }

	    $scope.createBalanceExport = function(pointOfSale) {
	    	if (!pointOfSale) {
	    		pointOfSale = 1;
	    		$scope.pointofsale = pointOfSale;
	    	}
	        var params = {pointid: pointOfSale};
	    	$http.get('/balances/createExport', {params: params})
	        .then(function(response) {
	          alert("Kassenberichtexport erzeugt" + response.data);
	        }, function(response) {
	          alert("Kassenberichtexport konnte nicht erzeugt werden " + response.statusText);
	        } );
	    }
	    
	    $scope.reloadData = function () {
	    	$scope.tableParams.reload();
	    }

	    $scope.tableParams = new NgTableParams({ count: 7 }, {
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
	            if ($scope.hasOwnProperty('exported')) {
	            	queryParams['exported'] = $scope.exported;
	            }
	            BalanceDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var BalancesExportController = function($scope, $http, ExportDao, NgTableParams, $uibModal, $window) {
	$scope.deleteExport = function(expId) {
		ExportDao.delete({id : expId }, function () {
	    	$scope.tableParams.reload();
		})
    };

    $scope.showExport = function (expId) {
        var params = {id : expId};
    	$http.get('/balances/exportreport', {params: params}).
    	then( function (data) {
    		$uibModal.open({
    			templateUrl: '/templates/reportBalanceExport.html',
    			scope : $scope,
    			controller: function($scope) {
    				$scope.exp = data.data;
    				$scope.exp.id  = expId;
    			},
    			size: 'lg'
    		})
    	});
    }
    
	$scope.download = function(expId) {
		window.open("/balances/exportfile?id="+expId);
	}

	$scope.showDetailsOfExport = function(expId) {
		ExportDao.get({id : expId}, function(bal) {
			$http.get(bal._links.balances.href, {})
			.then(function(data) {
    			$scope.detailTable = new NgTableParams({},{dataset:data.data._embedded.cashbalances});
			})
		});
	}
	
    $scope.showBalance = function (bal) {
    	$uibModal.open({
    	      animation: true,
    	      templateUrl: '/templates/balanceReport.html',
    	      controller: function($scope) {
    	    	  $scope.balance = bal;
    	      },
    	      size: 'lg'
    	    });
    }
    
    $scope.tableParams = new NgTableParams({ count: 10 }, {
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
            ExportDao.search(queryParams, function(data) {
                params.total(data.totalElements);
                $defer.resolve( data.content);
            })
        }
    });

    
  };
  
  BalanceController.$inject = ['$scope', '$http', 'CashbalanceDAO', 'NgTableParams', '$uibModal'];
  BalancesExportController.$inject = ['$scope', '$http', 'BalExportDAO', 'NgTableParams', '$uibModal', '$window'];

  var CashbalanceFactory = function($resource) {
    return $resource('/api/cashbalance/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/balances/balancesDyn' } 
    		} );
  };
  
  CashbalanceFactory.$inject = ['$resource'];

  var BalExportFactory = function($resource) {
	    return $resource('/api/balexport/:id', { id: '@id' },
	    		{ 'query':  {method:'GET', isArray:false},
	    		  'search' : { method:'GET', isArray :false, url:'/balances/exportsDyn' }, 
	        	  'delete' : { method:'DELETE', parms: {id:'@id'}, url: '/balances/deleteExport' }
		        } );
	  };
	  
	  BalExportFactory.$inject = ['$resource'];

  angular.module("verwApp.balances").
   factory("CashbalanceDAO", CashbalanceFactory).
   factory("BalExportDAO", BalExportFactory).
   controller("BalancesExportController", BalancesExportController).
   controller("BalanceController", BalanceController).
   controller("BalancesMainController", function($scope) {}).
   config(['$stateProvider','eehNavigationProvider',function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('balances.balances', {
			url: "/balances",
			templateUrl: "modbalances/balances.html",
			controller: 'BalanceController'	
		}).
		state('balances.exports', {
			url: "/exports",
			templateUrl: "modbalances/exports.html",
			controller: 'BalancesExportController'	
		}).
		state('balances.dashboard', {
			url: "/dashboard",
			templateUrl: "modbalances/dashboard.html"
		});
      eehNavigationProvider.
	    menuItem('navside.balances', {
	 	     text : 'Kassenberichte',isCollapsed: true, iconClass: 'fa fa-money', weight:200
	 	}).
      	menuItem('navside.balances.dashboard', {
        	 text : 'Kassenberichte Übersicht',
        	 state : 'balances.dashboard'
	    }).
      	menuItem('navside.balances.balances', {
	       	 text : 'Kassenberichte Suche',
	       	 state : 'balances.balances'
	    }).
      	menuItem('navside.balances.export', {
        	 text : 'Kassenbericht Exporte',
        	 state : 'balances.exports'
         });
   }]);

  angular.module("verwApp.balances").
   filter('abschlussdate', function($filter) {
	    return function(input, optional) {
	        return new moment(input).format("dd DD.MM. / [KW] w / [Q] Q");
	    };
	}).
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
	run(function (amMoment, $state, eehNavigation) {
		amMoment.changeLocale('de');
	});

}(angular));
