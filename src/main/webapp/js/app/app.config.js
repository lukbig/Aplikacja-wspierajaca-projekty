'use strict';

angular.module('awp').
	config(
		function($locationProvider, $routeProvider, $httpProvider) {
			$locationProvider.html5Mode({enabled:true})

			$routeProvider.
				when("/", {
					templateUrl : '/templates/home.html'
				}).
				when("/about", {
					templateUrl : '/templates/about.html'
				}).
				when("/awplogin", {
					templateUrl : '/templates/login.html',
					controller : 'loginController'
				}).
				when("/awplogin/:logout/:error", {
					templateUrl : '/templates/login.html',
					controller: 'basicNav'
				}).
				when("/admin", {
					template: "<admin-panel></admin-panel"
				}).
				when("/manager", {
					template: "<manager-panel></manager-panel"
				}).
				when("/user", {
					template: "<user-panel></user-panel"
				});

				//The custom "X-Requested-With" is a conventional header sent by browser clients,
        		//and it used to be the default in Angular but they took it out in 1.3.0.
        		//Spring Security responds to it by not sending a "WWW-Authenticate" header in a 401 response,
        		//and thus the browser will not pop up an authentication dialog (which is desirable in our app
        		//since we want to control the authentication).
        		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		});