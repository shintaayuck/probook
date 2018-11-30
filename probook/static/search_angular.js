angular.module('searchApp', [])
    .controller('searchController', function($scope, $http) {
        // message.style.display = 'none';
        $scope.query = null;

        $scope.change = function () {
            // showLoader()
            $scope.result = null;
            valtosend = $scope.query.replace(/\s+/g, '_');

            url = "http://localhost:8000/result/" + valtosend;
            $http.get(url)
                .then(function (response) {
                    if (response.data=="404 Not Found" || response.data=="null") {
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