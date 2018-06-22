app = angular.module('ids', [ 'ngSanitize', 'mgcrea.ngStrap', 'ngRoute' ]);

app.controller('resultDisplay', [
		'$scope',
		'$http',
		function($scope, $http) {
			$scope.results = "high";
			$scope.resultType = "search";
			$scope.currentFav = "";
			$scope.popover = {
				"title" : "Title",
				"content" : "Hello Popover<br />This is a multiline message!"
			};
			$scope.addToFavorite = function(favName, objId) {
				$http.get(
						'/addToFavoriteGroup?favorite=' + favName
								+ "&objectId=" + objId).success()
			};
			$scope.removeFromFavoriteGroup = function(favName, objId) {
				$http.get(
						'/removeFromFavoriteGroup?favorite=' + favName
								+ "&objectId=" + objId).success(
						function() {
							$http.get('/getFavorites?favorite=' + favName)
									.success(function(data) {
										$scope.results = format(data);
									});
						});
			}

		} ]);

app.controller('search', [
		'$scope',
		'$http',
		function($scope, $http) {
			$scope.search = function() {
				$http.get('/search?search=' + $scope.searchText).success(
						function(data) {
							$scope.$parent.results = format(data);
							$scope.$parent.resultType = "search";
						})
			}
		} ]);

app.controller('favorites', [
		'$scope',
		'$http',
		function($scope, $http) {
			$http.get('/getFavoriteGroups').success(function(data) {
				$scope.favorites = data;
			})
			$scope.addFavorite = function(favName) {
				$http.get('/addFavoriteGroup?favorite=' + $scope.favoriteName)
						.success(function(data) {
							$scope.favorites = data;
						})

			}
			$scope.getFavorites = function(favName) {
				$http.get('/getFavorites?favorite=' + favName).success(
						function(data) {
							$scope.$parent.results = format(data);
							$scope.$parent.resultType = "favorite";
							$scope.$parent.currentFav = favName;
						})
			}
			$scope.removeFavoriteGroup = function(favName) {
				$http.get('/removeFavoriteGroup?favorite=' + favName).success(
						function(data) {
							$http.get('/getFavoriteGroups').success(
									function(data) {
										$scope.favorites = data;
									})
						})
			}
		} ]);

app.controller('user', [ '$scope', '$http', function($scope, $http) {
	$scope.register = function() {
		$http.get('/addUser').success(function(data) {
			$scope.newUser = data;
			$scope.newUser.password = $scope.user.password;
			$scope.newUser.username = $scope.user.username;
			$scope.newUser.role = "user";
			$http.post('/addUser', $scope.newUser).success(function() {
				console.log("success");
				alert("User has been created")
			})
		})
	}
} ]);
function alert(message) {
	alertDiv = angular.element(document.getElementById('alertDiv'));
	alertDiv
			.html('<div class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>'
					+ message + '</span></div>')
}
function format(results) {
	results = formatIndentation(results);
	return results;
}
function formatIndentation(results) {
	alphabet = 'abcdefghijklmnopqrstuvwxyz'.split('');
	for (i = 0; i < results.length; i++) {
		textBody = results[i].text;
		if (typeof textBody == 'undefined')
			return;
		listsFound = false;
		lines = textBody.split("\n");
		formattedText = "";
		currentTier = 0;
		index2 = 0;
		for (j = 0; j < lines.length; j++) {
			line = lines[j];
			if (line.startsWith("(")) {
				listsFound = true;
				close = line.indexOf(")")
				pos = line.substring(1, close);
				switch (currentTier) {
				case 0:
					if (pos == "1") {
						line = "<ul id=\"indentT1\"><li>" + line;
					} else if (isNaN(pos)) {
						line = "<ul id=\"indentT2\"><li>" + line;
						currentTier++;
						index2++;
					} else {
						line = "<li>" + line;
					}
					break;
				case 1:
					if (isNaN(pos)) {
						if (pos == alphabet[index2 + 1]) {
							index2++;
							line = "<li>" + line;
						} else {
							line = "<ul id=\"indentT3\"><li>" + line;
							currentTier++;
						}
					} else {
						line = "</ul><li>" + line;
						currentTier--;
						index2 = 0;
					}
					break;
				case 2:
					if (pos == alphabet[index2]) {
						line = "</ul><li>" + line;
						currentTier--;
					} else if (!isNaN(pos)) {
						line = "</ul></ul><li>" + line;
						currentTier = 0;
						index2 = 0;
					} else {
						line = "<li>" + line;
					}
					break;

				}
				formattedText += line;
			} else {
				if (listsFound == true) {
					line = "</ul>" + line;
					listsFound = false;
				}
				formattedText += line + "<br>";
			}
		}
		results[i].text = formattedText;

	}

	return results;

}