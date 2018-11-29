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
        $result = json_decode(json_encode($client->getBook($param)->return));
        var_dump($result);
        $model->setId($this->request->param("id"));
        $model->setName($result->name);
        $model->setDescription($result->description);
        $model->setImgsrc($result->imgsrc);

//        $model->load();
//
        $vars["book"] = [$model];
        $vars["review"] = $model->getBookReviews();

//         var_dump($vars);

        View::render("detail", $vars);
    }
}