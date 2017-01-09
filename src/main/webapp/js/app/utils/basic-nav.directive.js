'use strict';

angular.module('basicNav').
	controller('basicNav', function ($scope, $routeParams) {
			$scope.param = {};
			$scope.param.error = $routeParams.error;
			$scope.param.logout = $routeParams.logout;
		}).
	directive('basicNav', function($location) {
		return {
			// When you create a directive, it is restricted to attribute and elements only by default.
			// In order to create directives that are triggered by class name, you need to use the restrict option.
			restrict: "E",
			templateUrl: "templates/basic-nav.html",
			link : function(scope, element, attr) {
				scope.about = function() {
					$location.url('/about');
				}
				scope.login = function() {
					$location.url('/awplogin');
				}
				scope.home = function() {
					$location.url('/');	
				}
			}
		}
	});