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

    public function searchByQuery($query) {
        $client = new SoapClient('http://localhost:5000/api/books?wsdl');
        $param = array("arg0"=>$query);
//        var_dump(json_encode($client->searchBook($param)->return));
       return json_decode(json_encode($client->searchBook($param)->return));
    }

    public function landing()
    {
        $vars = [
            "navbar" => "browse"
        ];

        if($this->request->get("query") === NULL) {
            View::render("search_angular", $vars);
        } else {
            $query = $this->request->get("query");

            $vars["query"] = $query;
            $model = new BookModel();
//            var_dump($this->searchByQuery($query));
//            $vars["result"] = $this->searchByQuery($query);
//            var_dump($this->searchByQuery($query));
//            var_dump($model->searchByKeyword($query));
            $vars["result"] = $model->searchByKeyword($query);


            View::render("search_angular", $vars);
        }

    }
}
