'use strict';

angular.module('managerPanel').
	component('managerPanel', {
		templateUrl: '/templates/manager.html',
		controller: function($http, $scope, $routeParams, $location, $rootScope) {

			$scope.loggedUser = $rootScope.loggedUser;
			// $scope.loggedUser = {
			// 	"userId": 7 //anakin skywalker
			// };
			refreshData();

			function refreshData() {
				$scope.editTaskMode = false;
				$scope.createTaskMode = false;
				$scope.searchTaskMode = false;
				$scope.searchUserMode = false;
				$scope.viewCommentsMode = false;

				$scope.searchTaskQuery = "";
				$scope.selectedTask = null;
				$scope.tasksFound = null;
				$scope.usersFound = null;

				$scope.searchTaskPRFT = "";
				$scope.searchTaskProgrammer = "";
				$scope.searchTaskTester = "";
				$scope.searchTaskEditor = "";
				$scope.selectedTaskUser = null;

				$scope.searchTask = null;

				getSortedTasks('taskId', 'asc');
			}

			// Advanced search <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Advanced search>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.submitAdvancedTaskSearch = function (task) {
				if ((!task || angular.equals({}, task)) &&
						$scope.searchTaskPRFT.length < 1 &&
						$scope.searchTaskProgrammer.length < 1 &&
						$scope.searchTaskTester.length < 1 &&
						$scope.searchTaskEditor.length < 1
					) {
					showDangerDialog('Empty search data!', 0);
					return;
				}
				getSearchedTasks(task);
			}

			$scope.clickAvancedTaskSearch = function() {
				refreshData();
				getSortedUsers('nickName', 'asc');
				$scope.searchTask = {};
				$scope.searchTaskMode = true;
			}

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<advanced users search>>>>>>>>>>>>>>>>>>>>>>>>>>>

			$scope.clickAvancedUserSearch = function() {
				refreshData();
				$scope.searchUser = {};
				$scope.searchUserMode = true;
			}

			$scope.submitAdvancedUserSearch = function(searchUser) {
				if (!searchUser || angular.equals({}, searchUser)) {
					showDangerDialog('Empty search data!', 0);
					return;
				}
				getCustomUsers(searchUser);
			}

			$scope.closeTableWithFoundUsers = function() {
				$scope.usersFound = null;
			}

			// actions in search table <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<actions in search table>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.
			$scope.clickOkAfterTasksFound = function(task) {
				$scope.editTaskMode = false;
				$scope.searchTaskMode = false;
				$scope.selectedTask = task;
			}

			$scope.clearTasksFound = function() {
				$scope.tasksFound = null;
			}

			$scope.clickEditAfterTasksFound = function(task) {
				$scope.editTaskMode = true;
				$scope.searchTaskMode = false;
				$scope.selectedTask = task;
				$scope.searchTaskPRFT = task.personResponsibleForTask.nickName;
				$scope.searchTaskProgrammer = task.programmer.nickName;
				$scope.searchTaskTester = task.tester.nickName;
			}

			$scope.changeTaskStatus = function(task) {
				changeStatus(task.taskId, $scope.loggedUser.userId);
			}

			$scope.clickDeleteTask = function(task) {
				if (!task) {
					return;
				}
				var dialog = new BootstrapDialog({
		            title: 'Delete Danger!',
		            message: 'Do you really want to delete task with id ' + task.taskId + ' ?',
		            draggable: true,
		            buttons: [
		            
		             	{
		             		type: BootstrapDialog.TYPE_DANGER,
							label: 'Delete',
    						cssClass: 'btn-danger',
    						action: function(d) {
								deleteTask(task);
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

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<view comments>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			$scope.clickViewComments = function(task) {
				if (task.comments.length < 1) {
					showDangerDialog("There are no comments!", 1);
					return;
				}
				$scope.editTaskMode = false;
				$scope.viewCommentsMode = true;
			}

			$scope.backFromViewComments = function() {
				$scope.editTaskMode = true;
				$scope.viewCommentsMode = false;	
			}

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


			$scope.clickViewTask = function(task) {
				$scope.searchTaskMode = false;
			}

			$scope.convertDate = function(ms) {
				var d = new Date(ms);
				var minutes = d.getMinutes();
				if (minutes < 10) {
					minutes = '0' + minutes;
				}
				return d.getDate() + "-" + (d.getMonth() + 1) + "-" + d.getFullYear() + " " + d.getHours() + ":" + minutes;
			}

			$scope.setEditTaskMode = function(task) {
				refreshData();
				$scope.selectedTask = task;
				$scope.editTaskMode = true;
				$scope.searchTaskPRFT = task.personResponsibleForTask.nickName;
				$scope.searchTaskProgrammer = task.programmer.nickName;
				$scope.searchTaskTester = task.tester.nickName;
			}

			$scope.selectTask = function(task) {
				refreshData();
				$scope.selectedTask = task;
			}

			$scope.clickAddTask = function() {
				refreshData();
				$scope.createTaskMode = true;
				$scope.selectedTask = {};
				getSortedUsers('nickName', 'asc');
			}

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<ADD/EDIT VIEW MODE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.submitAddTask = function(task) {
				if ($scope.createTaskMode == true) {
					task.editor = $scope.loggedUser;
					task.status = "CREATED";
					if (task.personResponsibleForTask == null) {
						searchUser($scope.searchTaskPRFT);
						task.personResponsibleForTask = $scope.selectedTaskUser;
					}
					if (task.programmer == null) {
						searchUser($scope.searchTaskProgrammer);
						task.programmer = $scope.selectedTaskUser;
					}
					if (task.tester == null) {
						searchUser($scope.searchTaskTester);
						task.tester = $scope.selectedTaskUser;
					}
				}
				if (task.tester == null || task.programmer == null || task.personResponsibleForTask == null) {
					showDangerDialog('Tester, programmer or person responsible for task is null!',1);
					return;
				}
				saveTask(task);
			}

			$scope.deleteTaskInEditMode = function(task) {
				$scope.clickDeleteTask(task);
				refreshData();
			}

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			$scope.selectTaskPRFT = function(task, $item) {
				task.personResponsibleForTask = {userId: $item.userId};
			}

			$scope.selectTaskProgrammer = function(task, $item) {
				task.programmer = {userId: $item.userId};
			}

			$scope.selectTaskTester = function(task, $item) {
				task.tester = {userId: $item.userId};
			}

			$scope.selectTaskEditor = function(task, $item) {
				task.editor = {userId: $item.userId};
			}

			$scope.searchTask = function() {
				$scope.selectedTask = null;
				if ($scope.searchTaskQuery.length < 1) {
					return;
				}
				getSortedTasks('taskId', 'asc');
				$scope.sortedTasks.every(function(entry) {
					if (entry.taskId == $scope.searchTaskQuery) {
						$scope.selectedTask = entry;
						return false;
					}
					return true;
				});
				if ($scope.selectedTask == null) {
					$scope.searchTaskQuery = "";
				}
			}

			$scope.refreshData = function() {
				refreshData();
			}

			$scope.searchUser = function(query) {
				searchUser(query);
			}

			function searchUser(query) {
				if (query.length < 1) {
					return;
				}
				$scope.sortedTaskUsers.every(function(entry) {
					if (entry.nickName.startsWith(query)) {
						$scope.selectedTaskUser = entry;
						return false;
					}
					return true;
				});
			}

			function getSortedTasks(attribute, direction) {
				$http.get('http://localhost:8080/rest/task/' + attribute + '?direction=' + direction).then(function successCalback(response) {
					$scope.sortedTasks = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getSearchedTasks(task) {
				var url = "";
				if(task.editor != null && task.editor.userId != null) {
					url = 'http://localhost:8080/rest/task/custom/editor?userId=' + task.editor.userId;
				} else if(task.personResponsibleForTask != null && task.personResponsibleForTask.userId != null) {
					url = 'http://localhost:8080/rest/task/custom/prft?userId=' + task.personResponsibleForTask.userId;
				} else if(task.programmer != null && task.programmer.userId != null) {
					url = 'http://localhost:8080/rest/task/custom/programmer?userId=' + task.programmer.userId;
				} else if(task.tester != null && task.tester.userId != null) {
					url = 'http://localhost:8080/rest/task/custom/tester?userId=' + task.tester.userId;
				} else if(task.status != null && task.status.length > 0) {
					url = 'http://localhost:8080/rest/task/custom/status?userId=0&status=' + task.status;
				}

				// autselect user
				if (url.length < 1) {
					if ($scope.searchTaskEditor.length > 0) {
						searchUser($scope.searchTaskEditor);
						if ($scope.selectedTaskUser != null) {
							url = 'http://localhost:8080/rest/task/custom/editor?userId=' + $scope.selectedTaskUser.userId;
						} else {
							showDangerDialog('Incorrect search data.', 1);
							return;
						}
					} else if ($scope.searchTaskPRFT.length > 0) {
						searchUser($scope.searchTaskPRFT );
						if ($scope.selectedTaskUser != null) {
							url = 'http://localhost:8080/rest/task/custom/prft?userId=' + $scope.selectedTaskUser.userId;
						} else {
							showDangerDialog('Incorrect search data.', 1);
							return;
						}
					} else if ($scope.searchTaskProgrammer.length > 0) {
						searchUser($scope.searchTaskProgrammer);
						if ($scope.selectedTaskUser != null) {
							url = 'http://localhost:8080/rest/task/custom/programmer?userId=' + $scope.selectedTaskUser.userId;
						} else {
							showDangerDialog('Incorrect search data.', 1);
							return;
						}
					} else if ($scope.selectedTaskUser.length > 0) {
						if ($scope.selectedTaskUser != null) {
							url = 'http://localhost:8080/rest/task/custom/tester?userId=' + $scope.selectedTaskUser.userId;
						} else {
							showDangerDialog('Incorrect search data.', 1);
							return;
						}
					}  else {
						showDangerDialog('Incorrect search data.', 1);
						return;
					}
				}

				$http.get(url).then(function successCalback(response) {
					$scope.tasksFound = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function changeStatus(taskId, userId) {
				$http.post('http://localhost:8080/rest/task/UP?taskId=' + taskId + '&userId=' + userId).then(function successCalback(response) {
					showDangerDialog('Status successfully changed!', 0);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function deleteTask(task) {
				var comments = task.comments;
				for (var i = 0; i < comments.length; i++) {
					deleteComment(comments[i].commentId);
				}
				$http.delete('http://localhost:8080/rest/task/' + task.taskId).success(function(){
					showDangerDialog('Task successfully deleted!', 0);
				}).error(function(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function deleteComment(commentId) {
				$http.delete('http://localhost:8080/rest/comment/' + commentId).error(function(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getSortedUsers(attribute, direction) {
				$http.get('http://localhost:8080/rest/user/' + attribute + '?direction=' + direction).then(function successCalback(response) {
					$scope.sortedTaskUsers = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getTask(taskId) {
				$http.get('http://localhost:8080/rest/task?taskId=' + taskId).then(function successCalback(response) {
					$scope.selectedTask = response.data;
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});	
			}

			function saveTask(task) {
				$http.put('http://localhost:8080/rest/task', task).
					success(function(data, status, headers, config) {
				    // this callback will be called asynchronously
				    // when the response is available
				    refreshData();
				    showDangerDialog('Task successfully created.', 0);
					})
				.error(function(data, status, headers, config) {
					if (data) {
						showDangerDialog(data.ExceptionCause, 1);
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
			// end <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.
		}
	});