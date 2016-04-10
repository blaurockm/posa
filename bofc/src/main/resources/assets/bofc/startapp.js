// create the module and name it phpro
// also include ngRoute for all our routing needs
var phpro = angular.module('phpro', ['ngRoute']);

// configure our routes
phpro.config(function($routeProvider) {

$routeProvider
        // route for the index page
        .when('/', {
                templateUrl : 'pages/index',
                controller  : 'mainCtrl'
        })

        // route for the FAQ page
        .when('/export', {
		        templateUrl : 'pages/export',
		        controller  : 'faqCtrl'
        })

        // route for the contact page
        .when('/rechnungen', {
                templateUrl : 'pages/rechnungen',
                controller  : 'contactCtrl'
        })
		// route for the contact page
		.when('/technics', {
		        templateUrl : 'pages/technics',
		        controller  : 'contactCtrl'
		})
		.when('/mappings', {
		        templateUrl : 'pages/mappings',
		        controller  : 'contactCtrl'
		})
		// route for the contact page
		.when('/commands', {
		        templateUrl : 'pages/commands',
		        controller  : 'contactCtrl'
		});
});

// create the controller and inject Angular's $scope
phpro.controller('mainCtrl', function($scope) {
        // create a message to display in our view
        $scope.heading = 'Welcome to PHPRO.ORG';
        $scope.message = 'Here you will find the meaning of life.';
});

phpro.controller('faqCtrl', function($scope) {
        $scope.heading = 'PHPRO.ORG FAQ';
        $scope.message = 'This is where you will find the accumulated knowledge of the world.';
});

phpro.controller('contactCtrl', function($scope) {
        $scope.heading = 'Contact PHPRO.ORG';
        $scope.message = 'Contact Kevin Waterson:';
});