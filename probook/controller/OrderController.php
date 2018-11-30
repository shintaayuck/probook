<?php

require_once "BaseController.php";
require_once "core/View.php";
require_once "core/Session.php";
require_once "model/OrderModel.php";
require_once "model/UserModel.php";

class OrderController extends BaseController
{
    public function __construct($request)
    {
        parent::__construct($request);
    }

    public function new()
    {
        $order_count = (int)$this->request->post("order_count");
        $book_id = (string)$this->request->post("book_id");

        error_log(print_r($order_count, TRUE));
        error_log(print_r($book_id, TRUE));

        $session = new Session();
        $user_id = (int)$session->inSession();
        $user = new UserModel();
        $user->setId($user_id);
        $user->load();

        $client = new SoapClient('http://localhost:5000/api/buy-books?wsdl');

        $param = array("arg0"=>$book_id, "arg1"=>$order_count, "arg2"=>$user->getCardnumber());
        $result = $client->buyBook($param)->return;

        header('Content-type: text/html');
        header_remove('Content-type');
        header("Content-type: application/json");

        $statusCode = $result->statusCode;
        if ($statusCode == 2) {
            //not enough amount
            echo json_encode(array("statusCode" => 2, "order_id" => 0));
        } else if ($statusCode > 2){
            //failed
            echo json_encode(array("statusCode" => 3, "order_id" => 0));
        } else {
            //success
            $order = new OrderModel();
            $order->setAmount($order_count);
            $order->setBookId($book_id);
            $order->setUserId($user_id);
            $order->setCreatedAt(date("Y/m/d H:i:s"));
            $order->insert();

            $result = array("statusCode" => 1, "order_id" => (int)$order->getLastInsertId());
            error_log(print_r(json_encode($result), TRUE));
            echo json_encode($result);
        }
    }
}