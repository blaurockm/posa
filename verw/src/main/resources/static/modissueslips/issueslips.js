(function(angular) {
  var IssueslipController = function($scope, $http, InvoiceDao, NgTableParams, $uibModal) {
	    $scope.showIssueslip = function (i) {
	    	$uibModal.open({
	    	      animation: true,
	    	      templateUrl: '/templates/invoiceReport.html',
	    	      controller: function($scope) {
	    	    	  $scope.inv = i;
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
	    	    maxDate: new Date()+30,
	    	    minDate: new Date()-30,
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

	    $scope.reloadData = function () {
	    	$scope.tableParams.reload();
	    }

	    $scope.updateInclude = function (i) {
	        var params = {pointid : i.pointid, customerId : i.customerId, debitorId : i.debitorId };
	    	$http.get('/issueslips/updateInclude', {params: params});
	    }

	    $scope.tableParams = new NgTableParams({ count: 10 }, {
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
	            if ($scope.hasOwnProperty('includeOnInvoice')) {
	            	queryParams['includeOnInvoice'] = $scope.includeOnInvoice;
	            }
	            if ($scope.hasOwnProperty('reDatumVon') || $scope.hasOwnProperty('reDatumBis')) {
	            	queryParams['date'] = [];
	            	if ($scope.hasOwnProperty('reDatumVon') && $scope.reDatumVon != '' && $scope.reDatumVon != null ) {
	            		queryParams['date'].push(moment($scope.reDatumVon).format('DD.MM.YY'));
//	            		queryParams['dateFrom'] = moment($scope.reDatumVon).format('DD.MM.YYYY');
	            	} 
	            	if ($scope.hasOwnProperty('reDatumBis') && $scope.reDatumBis != '' && $scope.reDatumBis != null ) {
	            		queryParams['date'].push(moment($scope.reDatumBis).format('DD.MM.YY'));
//	            		queryParams['dateTill'] = moment($scope.reDatumVon).format('DD.MM.YYYY');
	            	} 
	            }
	            if ($scope.hasOwnProperty('payed')) {
	            	queryParams['payed'] = $scope.payed;
	            }
	            InvoiceDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };

  IssueslipController.$inject = ['$scope', '$http', 'IssueslipDAO', 'NgTableParams', '$uibModal'];

  var IssueslipFactory = function($resource) {
	    return $resource('/api/issueslip/:id', { id: '@id' },
	    		{ 'query':  {method:'GET', isArray:false},
	    		  'search' : { method:'GET', isArray :false, url:'/issueslips/issueslipsDyn' } 
  		        } );
	  };
	  
  IssueslipFactory.$inject = ['$resource'];


  angular.module("verwApp.issueslips").
   factory("IssueslipDAO", IssueslipFactory).
   controller("IssueslipController", IssueslipController).
   controller("IssueslipMainController", function($scope) {}).
   config(['$stateProvider','eehNavigationProvider', function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('issueslips.issueslips', {
			url: "/issueslips",
			templateUrl: "modissueslips/issueslips.html",
			controller: 'IssueslipController'	
		});
    eehNavigationProvider.
    menuItem('navside.issueslips', {
 	     text : 'Lieferscheine',
 	    isCollapsed: true, iconClass : 'fa fa-files-o', weight :100
    }).
  	menuItem('navside.issueslips.issueslips', {
       	 text : 'Lieferschein Suche',
       	 state : 'issueslips.issueslips'
     });
   }]);

  angular.module("verwApp.issueslips").
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
