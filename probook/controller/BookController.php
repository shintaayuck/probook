<?php

require "BaseController.php";
require "core/View.php";
require "model/BookModel.php";

class BookController extends BaseController
{
    public function __construct($request)
    {
        parent::__construct($request);
    }

    public function detail()
    {
        $vars = [
            "navbar" => "browse"
        ];

        $model = new BookModel();
        $client = new SoapClient('http://localhost:5000/api/books?wsdl');
        $param = array("arg0"=>$this->request->param("id"));
        $result = $client->getBook($param)->return;

        $book["name"] = $result->title;
        if (sizeof($result->authors) > 1) {
            $book["author"] = $result->authors[0];
            for ($i=1; $i < sizeof($result->authors); $i++) {
                $book["author"] .= ", " . $result->authors[$i];
            }
        } else {
            $book["author"] = $result->authors;
        }
        $book["description"] = $result->description;
        $book["imgsrc"] = $result->imgsrc;
        $book["rating"] = 4;
        $book["price"] = $result->bookPrice;

        $client_recomm = new SoapClient('http://localhost:5000/api/recommender?wsdl');
        $param = array("arg0"=>$result->categories, "arg1"=>$result->bookID);
        $result_recomm = $client_recomm->getRecommendedBook($param)->return;
        
        // Rekomendasi didapat dari webservice
        //var_dump($result->categories);
        //var_dump($result_recomm->categories);
        $recommend["name"] = $result_recomm->title;
        $recommend["author"] = $result_recomm->authors;
        $recommend["description"] = $result_recomm->description;
        $recommend["imgsrc"] = $result_recomm->imgsrc;
        $recommend["rating"] = 4;
        $recommend["price"] = $result_recomm->bookPrice;

        $vars["book"] = $book;
        $vars["review"] = $model->getBookReviews();
        $vars["recommend"] = $recommend;

        View::render("detail", $vars);
    }
}