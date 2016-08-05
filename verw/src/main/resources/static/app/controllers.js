(function(angular) {
  var AppController = function($scope, Coupon) {
	Coupon.query(function(response) {
      $scope.coupons = response ? response : [];
    });
    
    $scope.addCoupon = function(description) {
      new Coupon({
        pupilsname: description,
        pupilclass : '1984',
      }).$save(function(coupon) {
        $scope.coupons.push(coupon);
      });
      $scope.newCoupon = "";
    };

    $scope.filtering = function(filter) {
    	Coupon.query({pupil : filter}, function(response) {
   	      $scope.coupons = response ? response : [];
   	    });
    }

    $scope.updateCoupon = function(coupon) {
    	coupon.amount = coupon.amount+1;
      coupon.$update();
    };
    
    $scope.deleteCoupon = function(coupon) {
      coupon.$remove(function() {
        $scope.coupons.splice($scope.coupons.indexOf(coupon), 1);
      });
    };
  };
  
  AppController.$inject = ['$scope', 'Coupon'];
  angular.module("myApp.controllers").controller("AppController", AppController);
}(angular));
