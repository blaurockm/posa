(function(angular) {
  var AccountingBalanceController = function($scope, $http, BalanceDao, NgTableParams, $uibModal) {
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
	    	$http.get('/accounting/createBalanceExport', {params: params})
	        .then(function(response) {
	          alert("Kassenberichtexport erzeugt" + response.data);
	        }, function(response) {
	          alert("Kassenberichtexport konnte nicht erzeugt werden " + response.statusText);
	        } );
	    }
	    
	    $scope.reloadData = function () {
//	        $scope.tableParams.settings({
//	          dataset: $scope.foundBalances
//	        });
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

  var AccountingInvoiceController = function($scope, $http, InvoiceDao, NgTableParams) {
	    $scope.showInvoice = function (id) {
	        alert("You selected balance ID: " + id);
	    }
	    
	    $scope.reloadData = function () {
//	        $scope.tableParams.settings({
//	          dataset: $scope.foundBalances
//	        });
	    	$scope.tableParams.reload();
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
	            InvoiceDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var AccountingExportController = function($scope, $http, ExportDao, NgTableParams) {
	$scope.deleteExport = function(accExport) {
    };

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
  
  AccountingBalanceController.$inject = ['$scope', '$http', 'CashbalanceDAO', 'NgTableParams', '$uibModal'];
  AccountingInvoiceController.$inject = ['$scope', '$http', 'InvoiceDAO', 'NgTableParams'];
  AccountingExportController.$inject = ['$scope', '$http', 'ExportDAO', 'NgTableParams'];

  var CashbalanceFactory = function($resource) {
    return $resource('/api/cashbalance/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    	      'search' : { method:'GET', isArray :false, url:'/accounting/balancesDyn' } 
    		} );
  };
  
  CashbalanceFactory.$inject = ['$resource'];

  var InvoiceFactory = function($resource) {
	    return $resource('/api/invoice/:id', { id: '@id' },
	    		{ 'query':  {method:'GET', isArray:false},
	    		  'search' : { method:'GET', isArray :false, url:'/accounting/invoicesDyn' } 
  		        } );
	  };
	  
  InvoiceFactory.$inject = ['$resource'];

  var ExportFactory = function($resource) {
	    return $resource('/api/export/:id', { id: '@id' },
	    		{ 'query':  {method:'GET', isArray:false},
	    		  'search' : { method:'GET', isArray :false, url:'/accounting/exportsDyn' } 
		        } );
	  };
	  
  ExportFactory.$inject = ['$resource'];

  angular.module("verwApp.accounting").
   factory("CashbalanceDAO", CashbalanceFactory).
   factory("InvoiceDAO", InvoiceFactory).
   factory("ExportDAO", ExportFactory).
   controller("AccountingExportController", AccountingExportController).
   controller("AccountingInvoiceController", AccountingInvoiceController).
   controller("AccountingBalanceController", AccountingBalanceController).
   controller("AccountingMainController", function($scope) {}).
   config(function ($stateProvider) {
    $stateProvider.
		state('accounting.invoices', {
			url: "/invoices",
			templateUrl: "modaccounting/invoices.html",
			controller: 'AccountingInvoiceController'	
		}).
		state('accounting.balances', {
			url: "/balances",
			templateUrl: "modaccounting/balances.html",
			controller: 'AccountingBalanceController'	
		}).
		state('accounting.exports', {
			url: "/exports",
			templateUrl: "modaccounting/exports.html",
			controller: 'AccountingExportController'	
		}).
		state('accounting.dashboard', {
			url: "/dashboard",
			templateUrl: "modaccounting/dashboard.html"
		});
   });

  angular.module("verwApp.accounting").
   filter('abschlussdate', function($filter) {
	    return function(input, optional) {
	        return new moment(input).format("dd DD.MM. / [KW] w / [Q] Q");
	    };
	}).
   filter('localdatetime', function($filter) {
	    return function(input, optional) {
	        var d = new Date(input);
	        return $filter('date')(d,(optional ? optional : 'medium'));
	    };
	}).
   filter('localdate', function($filter) {
	    return function(input, optional) {
	        var d = new Date(input);
	        return $filter('date')(d, (optional ? optional : 'fullDate'));
	    };
	}).
	run(function (amMoment) {
		amMoment.changeLocale('de');
	});

}(angular));
