function search() {
  let box = document.getElementsByName("query")[0];
  let query = box.value;

  if (query) {
    document.getElementById("book-search").submit();
  } else {
    box.setAttribute("invalid", "");
  }
}

App.controller('searchController', function($scope, $http, $timeout) {
    $scope.searchBook = null;
    $scope.change = function(text) {
        valtosend = $scope.searchText;
        $http.get('http://website/getdatafunction/' + valtosend).then(function(result){
            $scope.entries = result.data;
        });
    };
});
