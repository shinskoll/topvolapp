var topVolApp = angular.module("topVolApp", ['angular-growl']);

//Gestion des URL des différentes vues
topVolApp.config(function($routeProvider){
	$routeProvider
	.when("/",{
		controller: "accueilCtrl",
		templateUrl: "static/accueil.html"
	})
	.when("/login",{
		controller: "loginCtrl",
		templateUrl: "static/login.html"
	})
	.when("/vols",{
		controller: "volCtrl",
		templateUrl: "static/vols.html"
	})
	.when("/nouveaucompte",{
		controller: "nouveauCompteCtrl",
		templateUrl: "static/compte.html"
	});
	$routeProvider.otherwise({"redirectTo": "/"});
});

//Pour gérer les notification (erreurs par exemple). Le message reste affiché 5 secondes
topVolApp.config(['growlProvider', function(growlProvider) {
    growlProvider.globalTimeToLive(5000);
}]);

//Controlleur pour la home page
topVolApp.controller("accueilCtrl", function ($scope, $http, $window, growl) {
	if($window.sessionStorage.token) {
		$scope.connecte = {"ok" : true, "enTantQue":$window.sessionStorage.token};
	}
});


//Controlleur pour la création d'un nouveau compte
topVolApp.controller("nouveauCompteCtrl", function ($scope, $http, $window, growl) {
	if($window.sessionStorage.token) {
		$scope.connecte = {"ok" : true, "enTantQue":$window.sessionStorage.token};
	}
	$scope.creerCompte = function() {
		var utilisateur = $scope.utilisateur;
		if(utilisateur && utilisateur.nom && utilisateur.identifiant && utilisateur.motDePasse && utilisateur.mail) {
			$http.post('/utilisateur/ajouter', utilisateur)
			.then(function(results){
				console.log("OK: " + results.status);
				$scope.compteCree = true;
			}, function(results){
				console.log("KO: " + results.status);
				growl.addErrorMessage("La création du compte "+utilisateur.identifiant+" a échoué - code d'erreur: "+results.status);
			})
		} else {
			growl.addErrorMessage("Merci de renseigner tous les champs pour créer un compte");	
		}
	}
});

//Controlleur pour le login
topVolApp.controller("loginCtrl", function ($scope, $http, $window, growl){
	if($window.sessionStorage.token) {
		$scope.connecte = {"ok" : true, "enTantQue":$window.sessionStorage.token};
	}
	$scope.login = function() {
		var utilisateur = $scope.utilisateur;
		if(utilisateur && utilisateur.identifiant && utilisateur.motDePasse) {
			$http.post('/utilisateur/login', utilisateur)
			.then(function(results){
				console.log("OK: " + results.status);
				$window.sessionStorage.token = utilisateur.identifiant;
				$scope.connecte = {"ok" : true, "enTantQue":$window.sessionStorage.token};
			}, function(results){
				console.log("KO: " + results.status);
				delete $window.sessionStorage.token;
				growl.addErrorMessage("La connexion a échoué - code d'erreur: "+results.status);
			})
		} else {
			growl.addErrorMessage("Merci de renseigner tous les champs pour se connecter");	
		}
	}
	
	$scope.logout = function() {
		delete $scope.connecte;
		delete $window.sessionStorage.token;
	}
});


//Controlleur pour la recherche de vols
topVolApp.controller("volCtrl", function ($scope, $http, $window, growl){
	if($window.sessionStorage.token) {
		$scope.connecte = {"ok" : true, "enTantQue":$window.sessionStorage.token};
	}
	
	
});