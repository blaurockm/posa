(function(angular) {
  var InvoiceController = function($scope, $http, InvoiceDao, NgTableParams, $uibModal) {
	    $scope.showInvoice = function (i) {
	    	$uibModal.open({
	    	      animation: true,
	    	      templateUrl: '/templates/invoiceReport.html',
	    	      controller: function($scope) {
	    	    	  $scope.inv = i;
	    	      },
	    	      size: 'lg'
	    	    });
	    }

	    $scope.createInvoiceExport = function(pointOfSale) {
	    	if (!pointOfSale) {
	    		pointOfSale = 1;
	    		$scope.pointofsale = pointOfSale;
	    	}
	        var params = {pointid: pointOfSale};
	    	$http.get('/invoices/createExport', {params: params})
	        .then(function(response) {
	          alert("Rechnungsexport erzeugt" + response.data);
	        }, function(response) {
	          alert("Rechnungsexport konnte nicht erzeugt werden " + response.statusText);
	        } );
	    }

	    $scope.reloadData = function () {
	    	$scope.tableParams.reload();
	    }

	    $scope.updateMapping = function (i) {
	        var params = {pointid : i.pointid, customerId : i.customerId, debitorId : i.debitorId };
	    	$http.get('/invoices/updateMapping', {params: params});
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
	            if ($scope.hasOwnProperty('pointofsale')) {
	            	queryParams['pointid'] = $scope.pointofsale;
	            }
	            if ($scope.hasOwnProperty('exported')) {
	            	queryParams['exported'] = $scope.exported;
	            }
	            if ($scope.hasOwnProperty('mapped')) {
	            	if ($scope.mapped == false) {
	            		queryParams['debitorId'] = '0';
	            	}
	            }
	            InvoiceDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  var InvoicesExportController = function($scope, $http, ExportDao, NgTableParams, $uibModal, $window) {
	$scope.deleteExport = function(expId) {
		ExportDao.delete({id : expId }, function () {
	    	$scope.tableParams.reload();
		})
    };

    $scope.showExport = function (expId) {
        var params = {id : expId};
    	$http.get('/invoices/exportreport', {params: params}).
    	then( function (data) {
    		$uibModal.open({
    			templateUrl: '/templates/reportInvoiceExport.html',
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
		window.open("/invoices/exportfile?id="+expId);
	}

	$scope.showDetailsOfExport = function(expId) {
		ExportDao.get({id : expId}, function(bal) {
			$http.get(bal._links.invoices.href, {})
			.then(function(data) {
    			$scope.detailTable = new NgTableParams({},{dataset:data.data._embedded.invoices});
			})
		});
	}

    $scope.updateMapping = function (i) {
        var params = {pointid : i.pointid, customerId : i.customerId, debitorId : i.debitorId };
    	$http.get('/invoices/updateMapping', {params: params});
    }
    
    $scope.showInvoice = function (i) {
    	$uibModal.open({
    	      animation: true,
    	      templateUrl: '/templates/invoiceReport.html',
    	      controller: function($scope) {
    	    	  $scope.inv = i;
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
  
  InvoiceController.$inject = ['$scope', '$http', 'InvoiceDAO', 'NgTableParams', '$uibModal'];
  InvoicesExportController.$inject = ['$scope', '$http', 'ExportDAO', 'NgTableParams', '$uibModal', '$window'];

  var InvoiceFactory = function($resource) {
	    return $resource('/api/invoice/:id', { id: '@id' },
	    		{ 'query':  {method:'GET', isArray:false},
	    		  'search' : { method:'GET', isArray :false, url:'/invoices/invoicesDyn' } 
  		        } );
	  };
	  
  InvoiceFactory.$inject = ['$resource'];

  var ExportFactory = function($resource) {
	    return $resource('/api/invexport/:id', { id: '@id' },
	    		{ 'query':  {method:'GET', isArray:false},
	    		  'search' : { method:'GET', isArray :false, url:'/invoices/exportsDyn' }, 
	        	  'delete' : { method:'DELETE', parms: {id:'@id'}, url: '/invoices/deleteExport' }
		        } );
	  };
	  
  ExportFactory.$inject = ['$resource'];

  angular.module("verwApp.invoices").
   factory("InvoiceDAO", InvoiceFactory).
   factory("ExportDAO", ExportFactory).
   controller("InvoicesExportController", InvoicesExportController).
   controller("InvoiceController", InvoiceController).
   controller("InvoicesMainController", function($scope) {}).
   config(function ($stateProvider) {
    $stateProvider.
		state('invoices.invoices', {
			url: "/invoices",
			templateUrl: "modinvoices/invoices.html",
			controller: 'InvoiceController'	
		}).
		state('invoices.exports', {
			url: "/exports",
			templateUrl: "modinvoices/exports.html",
			controller: 'InvoicesExportController'	
		}).
		state('invoices.dashboard', {
			url: "/dashboard",
			templateUrl: "modinvoices/dashboard.html"
		});
   });

  angular.module("verwApp.invoices").
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
	        var d = new Date(input);
	        return $filter('date')(d, (optional ? optional : 'mediumDate'));
	    };
	}).
	run(function (amMoment, editableOptions) {
		amMoment.changeLocale('de');
		editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
	});

}(angular));
