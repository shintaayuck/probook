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
        $model->setId($this->request->param("id"));
        $model->loadById();
        $client = new SoapClient('http://localhost:5000/api/books?wsdl');
        $param = array("arg0"=>$this->request->param("id"));
        $result = $client->getBook($param)->return;

        $book["name"] = $result->title;
        if (is_array($result->authors)) {
            $book["author"] = $result->authors[0];
            for ($i=1; $i < sizeof($result->authors); $i++) {
                $book["author"] .= ", " . $result->authors[$i];
            }
        } else {
            $book["author"] = $result->authors;
        }
        $book["description"] = $result->description;
        $book["imgsrc"] = $result->imgsrc;
        $book["rating"] = $model->getRating();
        if (!$book["rating"]) {
            $book["rating"] = 0;
        }
        $book["vote"] = $model->getVote();
        if (!$book["vote"]) {
            $book["vote"] = 0;
        }
        $book["price"] = $result->bookPrice;

        $client_recomm = new SoapClient('http://localhost:5000/api/recommender?wsdl');
        $param = array("arg0"=>$result->categories, "arg1"=>$result->bookID);
        $result_recomm = $client_recomm->getRecommendedBook($param)->return;

        $recommend_model = new BookModel();
        $recommend_model->setId($result_recomm->bookID);
        $recommend_model->loadById();

        $recommend["name"] = $result_recomm->title;
        if (is_array($result_recomm->authors)) {
            $recommend["author"] = $result_recomm->authors[0];
            for ($i=1; $i < sizeof($result_recomm->authors); $i++) {
                $recommend["author"] .= ", " . $result_recomm->authors[$i];
            }
        } else {
            $recommend["author"] = $result_recomm->authors;
        }
        $recommend["description"] = $result_recomm->description;
        $recommend["imgsrc"] = $result_recomm->imgsrc;
//        $recommend["rating"] = 3;
        $recommend["rating"] = $recommend_model->getRating();
        if (!$recommend["rating"]) {
            $recommend["rating"] = 0;
        }
        $recommend["vote"] = $recommend_model->getVote();
        if (!$recommend["vote"]) {
            $recommend["vote"] = 0;
        }
        $recommend["price"] = $result_recomm->bookPrice;
        $recommend["id"] = $result_recomm->bookID;

        $vars["book"] = $book;
        $vars["review"] = $model->getBookReviews();
        $vars["recommend"] = $recommend;

        View::render("detail", $vars);
    }
}