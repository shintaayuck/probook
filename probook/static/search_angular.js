angular.module('searchApp', [])
    .controller('searchController', function($scope, $http) {
        var loader = document.getElementById('lds-roller');
        $scope.query = null;

        $scope.change = function () {
            loader.style.display = 'block';
            $scope.result = null;
            valtosend = $scope.query.replace(/\s+/g, '_');

            url = "http://localhost:8000/result/" + valtosend;
            $http.get(url)
                .then(function (response) {
                    loader.style.display = 'none';
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