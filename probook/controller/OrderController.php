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
        $book_id = (int)($this->request->post("book_id"));

        $session = new Session();
        $user_id = (int)$session->inSession();

        $client = new SoapClient('http://localhost:5000/api/buy-books?wsdl');
        $param = array("arg0"=>$book_id, "arg1"=>$order_count, "arg2"=>$model->getCardnumber());
//        var_dump(json_encode($client->searchBook($param)->return));
        $result = json_decode(json_encode($client->buyBook($param)->return));

        if ($result) {
            if ()
        } else {
            header('Content-Type: application/json');
            echo json_encode(['status' => 'err', 'order_id' => NULL;
        }



        $order = new OrderModel();
        $order->setAmount($order_count);
        $order->setBookId($book_id);
        $order->setUserId($user_id);
        $order->setCreatedAt(date("Y/m/d H:i:s"));
        $order->insert();

        header('Content-Type: application/json');
        echo json_encode(['status' => 'ok', 'order_id' => (int)$order->getLastInsertId()]);
    }
}