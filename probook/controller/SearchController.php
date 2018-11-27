<?php

require "BaseController.php";
require "core/View.php";
require "model/BookModel.php";

class SearchController extends BaseController
{
    public function __construct($request)
    {
        parent::__construct($request);

    }

    public function landing()
    {
        $vars = [
            "navbar" => "browse"
        ];

        if($this->request->get("query") === NULL) {
            View::render("search", $vars);
        } else {
            $query = $this->request->get("query");

            $vars["query"] = $query;
            $model = new BookModel();
            $vars["result"] = $model->searchByKeyword($query);

            View::render("result", $vars);
        }

         $client = new SoapClient('http://localhost:5000/api/books?wsdl', array('cache-wsdl' => WSDL_CACHE_NONE));
         $client->__getFunctions();
         echo("HOLAA");
         $resultinjson = $client->searchBook("SHINTA")->return[0];
         var_dump($resultinjson);
         $title = $resultinjson->title;
//         var_dump($title);
         echo("AUOOOOOOOOOOOOOOO");


    }
}
