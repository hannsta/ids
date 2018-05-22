angular.module('ids', ['ngSanitize']).controller('home',['$scope','$http', function($scope, $http) {

	$scope.search = function () {
		
		$http.get('/search?search='+$scope.searchText).success(function(data) {
			$scope.results = format(data);
		})
		
    }
	
	
	
	
	

}]);

function format(results){
	results = formatIndentation(results);
	return results;
}
function formatIndentation(results){
	alphabet = 'abcdefghijklmnopqrstuvwxyz'.split('');	
	for (i=0;i<results.length;i++){
		textBody=results[i].text;
		listsFound = false;
		lines = textBody.split("\n");
		formattedText = "";
		currentTier = 0;
		index2 = 0;
		for (j=0;j<lines.length;j++){
			line=lines[j];
			if (line.startsWith("(")){
				listsFound = true;
				close = line.indexOf(")")
				pos = line.substring(1, close);
				console.log("--")
				switch(currentTier) {
					case 0:
						console.log("0")

						if (pos=="1"){
							line = "<ul id=\"indentT1\"><li>" +line;
						}else if (isNaN(pos)){
							line = "<ul id=\"indentT2\"><li>" +line;
							currentTier++;
							index2++;
						}else{
							line = "<li>"+line;
						}
						break;
					case 1:
						if (isNaN(pos)){
							if (pos==alphabet[index2+1]){
								index2++;
								line = "<li>"+line;
							}else{
								line = "<ul id=\"indentT3\"><li>" +line;
								currentTier++;
							}
						}else{
							line = "</ul><li>" + line;
							currentTier--;
							index2=0;
						}
						break;
					case 2:
						if (pos==alphabet[index2]){
							line = "</ul><li>" + line;
							currentTier--;
						}else if (!isNaN(pos)){
							line = "</ul></ul><li>" + line;
							currentTier=0;
							index2=0;
						}
						else{
							line = "<li>"+line;
						}
						break;

				}
				formattedText+=line;
			}else{
				if (listsFound == true){
					line="</ul>"+line;
					listsFound=false;
				}
				formattedText+=line+"<br>";
			}
		}
		results[i].text=formattedText;

	}

	return results;

}