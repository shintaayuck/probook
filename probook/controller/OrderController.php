<?php

require_once "BaseController.php";
require_once "core/View.php";
require_once "core/Session.php";
require_once "model/OrderModel.php";

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