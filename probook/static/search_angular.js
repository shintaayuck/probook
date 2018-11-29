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
                    if (response.data=="404 Not Found") {
                        var message = document.getElementById('search-no-result');
                        message.style.display = 'block';
                    } else {
                        $scope.result = response.data;
                        var message = document.getElementById('search-no-result');
                        message.style.display = 'none';
                    }

                });
        }
    });