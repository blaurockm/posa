(function(angular) {
  var CommandController = function($scope, $http, CommandDao, NgTableParams, $uibModal) {
	  $scope.cmdfilter = {pointid : '1' };
	  
	  $scope.deleteCommand = function (oldCmd) {
	      $scope.error = "";
	      $scope.success = "";
	      new CommandDao(oldCmd).$remove().then(function(req) { $scope.tableParams.reload(); })
	      .catch(function(req) { $scope.error = req; });
	  }

	  var initNewCommand = function(oldCmd) {
		  $scope.newcommand.param1 = null;
	  }

	  $scope.addCommand = function (newCmd) {
	      $scope.error = "";
	      $scope.success = "";
	      newCmd.pointid = $scope.cmdfilter.pointid;
	      newCmd.creationtime = new Date();
	      new CommandDao(newCmd).$save().then(function(req) {$scope.tableParams.reload(); $scope.success = req; initNewCommand($scope.newcommand); })
	      .catch(function(req) { $scope.error = req; });
	  }
	  
	    $scope.tableParams = new NgTableParams({ count: 10, sorting : { creationtime : 'desc' }, filter : $scope.cmdfilter }, {
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
	            if ($scope.hasOwnProperty('reDatumVon') || $scope.hasOwnProperty('reDatumBis')) {
	            	queryParams['date'] = [];
	            	if ($scope.hasOwnProperty('reDatumVon') && $scope.reDatumVon != '' && $scope.reDatumVon != null ) {
	            		queryParams['date'].push(moment($scope.reDatumVon).format('DD.MM.YY hh:mm'));
//	            		queryParams['dateFrom'] = moment($scope.reDatumVon).format('DD.MM.YYYY');
	            	} 
	            	if ($scope.hasOwnProperty('reDatumBis') && $scope.reDatumBis != '' && $scope.reDatumBis != null ) {
	            		queryParams['date'].push(moment($scope.reDatumBis).format('DD.MM.YY hh:mm'));
//	            		queryParams['dateTill'] = moment($scope.reDatumVon).format('DD.MM.YYYY');
	            	} 
	            }
	            CommandDao.search(queryParams, function(data) {
	                params.total(data.totalElements);
	                $defer.resolve( data.content);
	            })
	        }
	    });
  };
  

  var CommandFactory = function($resource) {
    return $resource('/api/command/:id', { id: '@id' },
    		{ 'query':  {method:'GET', isArray:false},
    		  'search' : { method:'GET', isArray :false, url:'/commands/commandsDyn' } 
	        } );
  };
  
  
  angular.module("verwApp.commands").
   factory("CommandDAO", CommandFactory).
   controller("CommandController", CommandController).
   controller("CommandMainController", function($scope) {}).
   config(['$stateProvider','eehNavigationProvider', function ($stateProvider, eehNavigationProvider) {
    $stateProvider.
		state('commands.commands', {
			url: "/input",
			templateUrl: "modcommands/commands.html",
			controller: 'CommandController'	
		}).
		state('commands.dashboard', {
			url: "/dashboard",
			templateUrl: "modcommands/dashboard.html"
		});
    eehNavigationProvider.
    menuItem('navside.commands', {
 	     text : 'Kommandos',
 	    isCollapsed: true, iconClass : 'fa fa-terminal', weight :900
    }).
  	menuItem('navside.commands.commands', {
       	 text : 'Kommandozentrale',
       	 state : 'commands.commands'
     });
   }]);

  CommandController.$inject = ['$scope', '$http', 'CommandDAO', 'NgTableParams', '$uibModal'];
  CommandFactory.$inject = ['$resource'];

  angular.module("verwApp.commands").
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
	});

}(angular));
