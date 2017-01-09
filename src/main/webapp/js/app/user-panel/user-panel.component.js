'use strict';

angular.module('userPanel').
	component('userPanel', {
		templateUrl: '/templates/user.html',
		controller: function($http, $scope, $routeParams, $location, $rootScope) {

			$scope.loggedUser = $rootScope.loggedUser;
			// $scope.loggedUser = {
			// 	"userId": 11 //anakin skywalker
			// };
			refreshData();

			function refreshData() {
				$scope.searchTaskMode = false;
				$scope.searchUserMode = false;
				$scope.commentMode = false;
				$scope.viewTaskMode = false;
				$scope.myTaskViewMode = false;
				$scope.beforeMode = 0;

				$scope.searchMyTaskQuery = "";
				$scope.tasksFound = null;
				$scope.usersFound = null;

				$scope.searchTaskPRFT = "";
				$scope.searchTaskProgrammer = "";
				$scope.searchTaskTester = "";
				$scope.searchTaskEditor = "";

				$scope.searchUser = null;
				$scope.searchTask = null;
				$scope.selectedTask = null;

				$scope.newComment = null;

				getCustomTask('editor', $scope.loggedUser.userId);
				$scope.myEditorTasks = $scope.customUserTasks;
			}

			$scope.refreshData = function() {
				refreshData();
			}

			function setModeFalse() {
				$scope.searchTaskMode = false;
				$scope.searchUserMode = false;
				$scope.commentMode = false;
				$scope.viewTaskMode = false;
				$scope.myTaskViewMode = false;
			}

			// ADVANCED SEARCH TASK FORM FUNCTIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

			$scope.clickAvancedTaskSearch = function() {
				refreshData();
				getSortedUsers('nickName', 'asc');
				$scope.searchTask = {};
				$scope.searchTaskMode = true;
			}

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

			$scope.enableTaskActions = function(task) {
				if (!task) {
					return false;
				}
				if (task.editor.userId == $scope.loggedUser.userId) {
					return true;
				}
				return false;
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

			$scope.clearTasksFound = function() {
				$scope.tasksFound = null;
			}
			// ADVANCED SEARCH TASK ACTIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.clickViewTaskFromAS = function(task) {
				setModeFalse();
				$scope.selectedTask = task;
				$scope.viewTaskMode = true;
				$scope.beforeMode = 1;
			}

			$scope.clickAddCommentFromAs = function(task) {
				setModeFalse();
				$scope.selectedTask = task;
				$scope.commentMode = true;
				$scope.beforeMode = 1;

				$scope.newComment = {
					commentType : "REGULAR"
				};
			}

			// COMMENTS MODE function <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			// 1 - searchTaskMode
			// 2 - viewTaskMode
			// 3 - myTaskViewMode
			$scope.backFromTaskCommentView = function() {
				setModeFalse();
				switch($scope.beforeMode) {
					case 1:
						$scope.searchTaskMode = true;
					break;
					case 2:
						$scope.viewTaskMode = true;
					break;
					case 3:
						$scope.myTaskViewMode = true;
					break;
					default:
					refreshData();
				}
				$scope.beforeMode = 0;
			}

			

			// ADVANCED SEARCH USER FORM FUNCTIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

			// VIEW TASK MODE FUNCTIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.clickAddCommentFromVt = function(task) {
				setModeFalse();
				$scope.selectedTask = task;
				$scope.commentMode = true;
				$scope.beforeMode = 2;

				$scope.newComment = {
					commentType : "REGULAR"
				};
			}

			$scope.convertDate = function(ms) {
				var d = new Date(ms);
				var minutes = d.getMinutes();
				if (minutes < 10) {
					minutes = '0' + minutes;
				}
				return d.getDate() + "-" + (d.getMonth() + 1) + "-" + d.getFullYear() + " " + d.getHours() + ":" + minutes;
			}

			// TYPEAHEAD FUNCTIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.selectMyTask = function(task) {
				setModeFalse();
				$scope.selectedTask = task;
				$scope.viewTaskMode = true;
			}

			$scope.searchMyTask = function() {
				$scope.selectedTask = null;
				if ($scope.searchMyTaskQuery.length < 1) {
					return;
				}
				$scope.myEditorTasks.every(function(entry) {
					if (entry.taskId == $scope.searchMyTaskQuery) {
						$scope.selectedTask = entry;
						return false;
					}
					return true;
				});
				if ($scope.selectedTask == null) {
					$scope.searchMyTaskQuery = "";
				}
			}

			// MY TASKS VIEW <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.clickMyTaskView = function() {
				refreshData();
				$scope.myTaskViewMode = true;
			}

			$scope.clickViewTaskFromMTV = function(task) {
				setModeFalse();
				$scope.selectedTask = task;
				$scope.viewTaskMode = true;
				$scope.beforeMode = 3;
			}

			$scope.clickAddCommentFromMTV = function(task) {
				setModeFalse();
				$scope.selectedTask = task;
				$scope.commentMode = true;
				$scope.beforeMode = 3;

				$scope.newComment = {
					commentType : "REGULAR"
				};	
			}

			$scope.clickPRFT = function() {
				getCustomTask('prft', $scope.loggedUser.userId);
			}

			$scope.clickProgrammer = function() {
				getCustomTask('programmer', $scope.loggedUser.userId);
			}

			$scope.clickTester = function() {
				getCustomTask('tester', $scope.loggedUser.userId);
			}			

			// TASK FUNCTIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			$scope.changeTaskStatusForward = function(task) {
				changeStatusForward(task.taskId, $scope.loggedUser.userId);
			}

			$scope.changeTaskStatusBackward = function(task) {
				changeStatusBackward(task.taskId, $scope.loggedUser.userId);
			}

			// COMMENT FUNCTIONS
			$scope.saveComment = function(newComment, task) {
				if (newComment.commentType == null) {
					showDangerDialog('Comment type must be specified!', 1);
					return;
				}
				newComment.creator = {
					userId : $scope.loggedUser.userId
				};

				newComment.task = {
					taskId : task.taskId
				};

				putComment(newComment, task.taskId);
			}

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<UPDATES>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			function putComment(comment, taskId) {
				$http.put('http://localhost:8080/rest/comment', comment).then(function successCalback(response) {
					getTask(taskId);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function getTask(taskId) {
				$http.get('http://localhost:8080/rest/task?taskId=' + taskId).then(function successCalback(response) {
					$scope.selectedTask = response.data;
					$scope.newComment = {
						commentType : "REGULAR"
					};
					showDangerDialog('Comment added!', 0);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});	
			}

			function changeStatusForward(taskId, userId) {
				$http.post('http://localhost:8080/rest/task/UP?taskId=' + taskId + '&userId=' + userId).then(function successCalback(response) {
					showDangerDialog('Status successfully changed!', 0);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			function changeStatusBackward(taskId, userId) {
				$http.post('http://localhost:8080/rest/task/DOWN?taskId=' + taskId + '&userId=' + userId).then(function successCalback(response) {
					showDangerDialog('Status successfully changed!', 0);
				}, function errorCallback(response) {
					if (response.data) {
						showDangerDialog(response.data.ExceptionCause, 1);
					}
				});
			}

			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<QUERIES>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			function getCustomTask(attribute, userId) {
				$http.get('http://localhost:8080/rest/task/custom/' + attribute + '?userId=' + userId).then(function successCalback(response) {
					$scope.customUserTasks = response.data;
				}, function errorCallback(response) {
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

			// END
		}
	});