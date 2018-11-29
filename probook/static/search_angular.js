angular.module('searchApp', [])
    .controller('searchController', function($scope) {
        var probook = this;
        probook.books = []
        probook.searchTerm = "";
        probook.details = "";

        $scope.printAuthor = function (obj) {
            if (typeof obj === "string") {
                return obj;
            } else {
                return obj.join(", ");
                ;
            }
        }

        probook.search = function () {
            while (probook.books.length > 0) {
                probook.books.pop();
            }
            document.getElementById("loader").style.display = "block";
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    console.log(this.responseText);
                    json = JSON.parse(this.responseText);
                    angular.forEach(json.item, function (book) {
                        probook.books.push(book);
                    });
                    console.log(probook.searchTerm);
                    console.log('done');
                    console.log(probook.books);
                    document.getElementById("loader").style.display = "none";
                    $scope.$apply();
                }
            };
            xhttp.open("POST", "./SearchContoller.php", true);
            xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xhttp.send("query=" + probook.searchTerm);
        }
    })