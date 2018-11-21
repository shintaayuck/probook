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
        $model->load();

        $vars["book"] = $model->loadById();
        $vars["review"] = $model->getBookReviews();

        // var_dump($vars);

        View::render("detail", $vars);
    }
}