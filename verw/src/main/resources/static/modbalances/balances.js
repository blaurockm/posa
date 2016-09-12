(function(angular) {
  var BalanceController = function($scope, $http, BalanceDao, NgTableParams, $uibModal) {
   	    $scope.pfilter = {pointid : '1' };
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
	    
	    $scope.dateOptions1 = {
	    	    formatYear: 'yy',
	    	    startingDay: 1
	    };
	    $scope.dateOptions = {
	    	    formatYear: 'yy',
//	    	    minDate: new Date()+30,
//	    	    maxDate: new Date()-30,
	    	    startingDay: 1
	    };
	    $scope.open1 = function() {
	        $scope.popup1.opened = true;
	      };
	    $scope.popup1 = {
    		opened: false
	    };
	    $scope.open2 = function() {
	        $scope.popup2.opened = true;
	      };
	    $scope.popup2 = {
    		opened: false
	    };
	    $scope.open3 = function() {
	        $scope.popup3.opened = true;
	      };
	    $scope.popup3 = {
    		opened: false
	    };

	    $scope.createBalanceExport = function(expLim) {
	        var params = {pointid: $scope.pfilter.pointid};
	    	if (expLim) {
	    		params.exportLimit = expLim;
	    	}
	    	$http.get('/balances/createExport', {params: params})
	        .then(function(response) {
	        	if (response.status == 200) {
	        		$scope.success = response;
	        	} else {
	        		$scope.error = response;
	        		$scope.error.data = { exception : "Keine Daten gefunden!" };
	        	}
	        }, function(response) {
	          $scope.error = response;
	        } );
	    }
	    
	    $scope.reloadData = function () {
	    	$scope.tableParams.reload();
	    }

	    $scope.tableParams = new NgTableParams({ count: 7, filter: $scope.pfilter, sorting : { abschlussId : 'desc' } }, {
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
	            if ($scope.hasOwnProperty('exported')) {
	            	queryParams['exported'] = $scope.exported;
	            }
	            if ($scope.hasOwnProperty('reDatumVon') || $scope.hasOwnProperty('reDatumBis')) {
	            	queryParams['firstCovered'] = [];
	            	if ($scope.hasOwnProperty('reDatumVon') && $scope.reDatumVon != '' && $scope.reDatumVon != null ) {
	            		queryParams['firstCovered'].push(moment($scope.reDatumVon).format('DD.MM.YY HH:mm'));
//	            		queryParams['dateFrom'] = moment($scope.reDatumVon).format('DD.MM.YYYY');
	            	} 
	            	if ($scope.hasOwnProperty('reDatumBis') && $scope.reDatumBis != '' && $scope.reDatumBis != null ) {
	            		queryParams['firstCovered'].push(moment($scope.reDatumBis).format('DD.MM.YY HH:mm'));
//	            		queryParams['dateTill'] = moment($scope.reDatumVon).format('DD.MM.YYYY');
	            	} 
	            }
	            BalanceDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var BalancesExportController = function($scope, $http, ExportDao, NgTableParams, $uibModal, $window) {
 	    $scope.pfilter = {pointId : '1' };
 	    
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
    
    $scope.tableParams = new NgTableParams({ count: 10, filter: $scope.pfilter, sorting : { till : 'desc' } }, {
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
