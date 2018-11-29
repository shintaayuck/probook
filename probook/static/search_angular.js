angular.module('searchApp', [])
    .controller('searchController', function($scope) {
        $scope.books = []
        $scope.searchTerm = "";
        $scope.details = "";

        $scope.printAuthor = function (obj) {
            if (typeof obj === "string") {
                return obj;
            } else {
                return obj.join(", ");
                ;
            }
        }

        $scope.search = function (query) {
            console.log("MASHOK");
            while ($scope.books.length > 0) {
                $scope.books.pop();
            }
            console.log("BWAHAHA");
            document.getElementById("loader").style.display = "block";
            var xhttp = new XMLHttpRequest();
            console.log("HELAW");
            xhttp.onreadystatechange = function () {
                console.log("HELAW!!");
                // xhttp.open("GET", "http://localhost:8000/result/"+ query ,true);
                // xhttp.send();
                if (this.readyState == 4 && this.status == 200) {
                    // console.log(this.responseText);
                    console.log(JSON.parse(this.responseText));
                    json = JSON.parse(this.responseText);
                    // angular.forEach(json.item, function (book) {
                    //     $scope.books.push(book);
                    // });
                    // console.log($scope.searchTerm);
                    // console.log('done');
                    // console.log($scope.books);
                    // document.getElementById("loader").style.display = "none";

                    // $scope.$apply();

                    $scope.result = response.data;
                }
            };


            xhttp.open("GET", "http://localhost:8000/result/"+ query ,true);
            xhttp.send();
            // xhttp.open("POST", "./SearchContoller.php", true);
            // xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            // xhttp.send("query=" + $scope.searchTerm);
        }

        $scope.search($scope.query);
    });