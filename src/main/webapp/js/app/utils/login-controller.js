'use strict';

angular.module('login', []).
controller('loginController', function($http, $rootScope, $scope, $location){

	init();

	$scope.clickSubmitLogin = function() {
		login();
	}

	function init() {
		$scope.username = null;
		$scope.password = null;
	}

	function login() {
		if ($scope.username == null || $scope.password == null) {
			showDangerDialog('Username or password is empty!',1);
			return;
		}

		$http.post('http://localhost:8080/login?username=' + $scope.username + '&password=' + $scope.password).then(function successCalback(response) {
			getUser();
		}, function errorCallback(response) {
			showDangerDialog('Login failed!', 1);
		});
	}

	function getUser() {
		$http.get('http://localhost:8080/rest/user/login?nickName=' + $scope.username).then(function successCalback(response) {
			$rootScope.loggedUser = response.data;

			var path = '';
			var position = response.data.permissions;
			if (position == 'USER') {
				path = '/user';
			} else if (position == 'MANAGER') {
				path = '/manager';
			} else if (position == 'ADMIN') {
				path = '/admin';
			} else {
				showDangerDialog('Incorrect permissions!',1);
				return;
			}

			$location.path(path);

		}, function errorCallback(response) {
			showDangerDialog('Incorrect login data!', 1);
		});
	}

	function showDangerDialog(givenMessage, bootstrapdialogtype) {
		if (givenMessage == null) {
			return;
		}
		var bd = BootstrapDialog.TYPE_INFO;
		var btitle = 'Information';
		if (bootstrapdialogtype == 1) {
			bd = BootstrapDialog.TYPE_DANGER;
			btitle = 'Danger';
		}
		BootstrapDialog.show({
			type: bd,
            title: btitle,
            message: givenMessage,
            buttons: [{
				label: 'Ok',
				cssClass: 'btn-default',
				action: function(dialogItself){
					dialogItself.close();
				}
			}]
        });
	}
});