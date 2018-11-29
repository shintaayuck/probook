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
                    $scope.result = response.data;
                    if (!$scope.result) {
                        var message = document.getElementById('search-no-result');
                        message.style.display = 'none';
                    }

                });
        }
    });