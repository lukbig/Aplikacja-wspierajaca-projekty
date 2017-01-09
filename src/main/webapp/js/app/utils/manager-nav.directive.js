'use strict';

angular.module('managerNav').
	directive('managerNav', function($location) {
		return {
			// When you create a directive, it is restricted to attribute and elements only by default.
			// In order to create directives that are triggered by class name, you need to use the restrict option.
			restrict: "E",
			templateUrl: "templates/manager-nav.html",
			link : function(scope, element, attr) {
				scope.logout = function() {
					$location.url('/awplogin?logout=1');
				}
			}
		}
	});