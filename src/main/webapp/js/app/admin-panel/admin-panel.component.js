'use strict';

angular.module('adminPanel').
	component('adminPanel', {
		templateUrl: '/templates/admin.html',
		controller: function($http, $scope, $routeParams, $location) {
			
			refreshData();

			function refreshData() {
				$scope.editMode = false;
				$scope.createMode = false;
				$scope.searchMode = false;
				$scope.selectedUser = null;
				$scope.newpassword = null;
				$scope.repeatnewpassword = null;
				$scope.searchUser = null;
				$scope.searchUserQuery = "";
				$scope.usersFound = null;
				getSortedUsers('nickName', 'asc');
			}

			$scope.advancedSearch = function() {
				refreshData();
				$scope.searchMode = true;
				initSearchUser();
			}

			$scope.closeTableWithFoundUsers = function() {
				$scope.usersFound = null;
			}

			function initSearchUser() {
				$scope.searchUser = {};
			}

			$scope.cancelAdvancedSearch = function() {
				refreshData();
			}

			$scope.submitAdvancedSearch = function() {
				if (!$scope.searchUser || angular.equals({}, $scope.searchUser)) {
					showDangerDialog('Empty search data!', 0);
					return;
				}
				getCustomUsers($scope.searchUser);
			}

			$scope.clickSearchViewOk = function(user) {
				refreshData();
				$scope.selectedUser = user;
			}

			$scope.clickDelete = function(user) {
				if (!user) {
					return;
				}
				var dialog = new BootstrapDialog({
		            title: 'Delete Danger!',
		            message: 'Do you really want to delete user with name ' + user.nickName + ' ?',
		            draggable: true,
		            buttons: [
		            
		             	{
		             		type: BootstrapDialog.TYPE_DANGER,
							label: 'Delete',
    						cssClass: 'btn-danger',
    						action: function(d) {
    							deleteUser(user.userId);
    							d.close();
    						}
    					},
    					{
    						label: 'Cancel',
    						cssClass: 'btn-default',
    						action: function(dialogItself){
        						dialogItself.close();
    						}
    					}
					]
		        });
		        dialog.realize();
		        dialog.open();
			}

			$scope.addUser = function() {
				refreshData();
				$scope.createMode = true;
				$scope.selectedUser = {};
			}

			$scope.submitAddUser = function() {
				if ($scope.createMode == true) {
					saveUser($scope.selectedUser);
				} else if ($scope.editMode == true && $scope.selectedUser.userId != null) {
					if ($scope.newpassword && $scope.newpassword.length > 0) {
						if ($scope.newpassword == $scope.repeatnewpassword) {
							$scope.selectedUser.password = $scope.newpassword;
						} else {
							BootstrapDialog.alert('Passwords do not match!');
							$scope.newpassword = null;
							$scope.repeatnewpassword = null;
							return;
						}
					} else {
						delete $scope.selectedUser.password;
					}
					updateUser($scope.selectedUser);
				}
			}

			$scope.setEditMode = function(user) {
				refreshData();
				$scope.selectedUser = user;
				$scope.editMode = true;
			}

			$scope.clearSelectedUser = function() {
				$scope.searchUserQuery = "";
				$scope.selectedUser = null;
				$scope.editMode = false;
			}

			$scope.selectUser = function($item) {
				$scope.selectedUser = $item;
			}

			$scope.searchUser = function() {
				$scope.selectedUser = null;
				if ($scope.searchUserQuery.length < 1) {
					return;
				}
				getSortedUsers('nickName', 'asc');
				$scope.sortedUsers.every(function(entry) {
					if (entry.nickName.startsWith($scope.searchUserQuery)) {
						$scope.selectedUser = entry;
						return false;
					}
					return true;
				});
				if ($scope.selectedUser == null) {
					$scope.searchUserQuery = "";
				}
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
			
			function getUser(userId) {
				$http.get('http://localhost:8080/rest/user?userId=' + userId).then(function successCalback(response) {
					$scope.user = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getUsers(page, size) {
				$http.get('http://localhost:8080/rest/user?page=' + page + '&size=' + size).then(function successCalback(response) {
					$scope.users = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getCustomUsers(user) {
				var isFirst = true;
				var url = 'http://localhost:8080/rest/user/custom?';
				if (user.nickName != null && user.nickName.length > 0) {
					if (isFirst) {
						isFirst = false;
						url = url + 'nickName=' + user.nickName;
					} else {
						url = url + '&nickName=' + user.nickName;
					}
				}
				if (user.firstName != null && user.firstName.length > 0) {
					if (isFirst) {
						isFirst = false;
						url = url + 'firstName=' + user.firstName;
					} else {
						url = url + '&firstName=' + user.firstName;
					}
				}
				if (user.secondName != null && user.secondName.length > 0) {
					if (isFirst) {
						isFirst = false;
						url = url + 'secondName=' + user.secondName;
					} else {
						url = url + '&secondName=' + user.secondName;
					}
				}
				if (user.email != null && user.email.length > 0) {
					if (isFirst) {
						isFirst = false;
						url = url + 'email=' + user.email;
					} else {
						url = url + '&email=' + user.email;
					}
				}
				if (user.position != null && user.position.length > 0) {
					if (isFirst) {
						isFirst = false;
						url = url + 'position=' + user.position;
					} else {
						url = url + '&position=' + user.position;
					}
				}
				if (user.permissions != null && user.permissions.length > 0) {
					if (isFirst) {
						isFirst = false;
						url = url + 'permissions=' + user.permissions;
					} else {
						url = url + '&permissions=' + user.permissions;
					}
				}
				// 'http://localhost:8080/rest/user/custom?nickName=' + user.nickName + '&firstName=' + user.firstName + '&secondName=' + 
				// 	user.secondName + '&email=' + user.email + '&permissions=' + user.permissions + '&position=' + user.position
				$http.get(url).then(function successCalback(response) {
					$scope.usersFound = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getSortedUsers(attribute, direction) {
				$http.get('http://localhost:8080/rest/user/' + attribute + '?direction=' + direction).then(function successCalback(response) {
					$scope.sortedUsers = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function deleteUser(userId) {
				$http.delete('http://localhost:8080/rest/user/'+userId).then(function successCalback(response) {
					refreshData();
					showDangerDialog('User successfully deleted.', 0);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function saveUser(user) {
				$http.post('http://localhost:8080/rest/user', user).
					success(function(data, status, headers, config) {
				    // this callback will be called asynchronously
				    // when the response is available
				    refreshData();
				    showDangerDialog('User successfully added.', 0);
					})
				.error(function(data, status, headers, config) {
					if (data) {
						showDangerDialog(data.ExceptionCause, 1);
					}
				});
			}

			function updateUser(user) {
				$http.put('http://localhost:8080/rest/user', user).then(function successCalback(response) {
					refreshData();
					showDangerDialog('User successfully updated.', 0);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}
		}
	});