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
        var_dump($result);

        $book["name"] = $result->title;
        $book["author"] = $result->authors;
        $book["description"] = $result->description;
        $book["imgsrc"] = $result->imgsrc;
        $book["rating"] = 4;
        $book["price"] = $result->bookPrice;

//         var_dump($book);

//        $model->setId($this->request->param("id"));
//        $model->setName($result->name);
//        $model->setDescription($result->description);
//        $model->setImgsrc($result->imgsrc);

//        $model->load();
//
        $vars["book"] = $book;
        $vars["review"] = $model->getBookReviews();
        $vars["recommend"] = $model->getRecommenderBooks($result->categories, $this->request->param("id"));

//         var_dump($vars);

        View::render("detail", $vars);
    }
}