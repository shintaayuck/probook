angular.module('searchApp', [])
    .controller('searchController', function($scope, $http) {
        $scope.query = null;

        $scope.change = function () {
            // showLoader()
            $scope.result = null;
            valtosend = $scope.query.replace(/\s+/g, '_');

            url = "http://localhost:8000/result/" + valtosend;
            $http.get(url)
                .then(function (response) {
                    console.log(response);
                    $scope.result = response.data;

                });
        }
    });