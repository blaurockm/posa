(function(angular) {
  var InvoiceController = function($scope, $http, InvoiceDao, NgTableParams) {
	    $scope.showInvoice = function (id) {
	        alert("You selected balance ID: " + id);
	    }
	    
	    $scope.reloadData = function () {
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

  var InvoicesExportController = function($scope, $http, ExportDao, NgTableParams) {
	$scope.deleteExport = function(accExport) {
		
		ExportDao.delete({id : accExport.id }, function () {
	    	$scope.tableParams.reload();
		})
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
  
  InvoiceController.$inject = ['$scope', '$http', 'InvoiceDAO', 'NgTableParams'];
  InvoicesExportController.$inject = ['$scope', '$http', 'ExportDAO', 'NgTableParams'];

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
