'use strict';

angular.module('awp', [
	//external
	'ngResource',
    'ngRoute',
    'ui.bootstrap',
    'angularUtils.directives.dirPagination',

    //internal
    'basicNav',
    'adminNav',
    'adminPanel',
    'managerNav',
    'managerPanel',
    'userNav',
    'userPanel',
    'login'
	]);