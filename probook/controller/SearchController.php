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

    public function searchByQuery() {
        $query = $this->request->param("query");
        $client = new SoapClient('http://localhost:5000/api/books?wsdl');
        $param = array("arg0"=>$query);
        echo json_encode($client->searchBook($param)->return);
    }

    public function landing()
    {
        $vars = [
            "navbar" => "browse"
        ];
        View::render("search_angular", $vars);

    }
}
