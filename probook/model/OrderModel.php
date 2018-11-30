<?php

require_once "model/BaseModel.php";
require_once "model/BookModel.php";

class OrderModel extends BaseModel {
    protected $user_id;
    protected $book_id;
    protected $amount;
    protected $review_id;
    protected $created_at;

    public function __construct()
    {
        parent::__construct("orders");
    }

    public function searchByUserId() {
        $stmt = $this->conn->prepare("select * from `orders` where user_id = :user_id");
        $stmt->bindParam(":user_id", $this->user_id);

        $stmt->execute();
        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        $orders = [];
        foreach ($result as $item) {
            $order = new OrderModel();
            $order->id = $item["id"];
            $order->load();
            array_push($orders, $order);
        }

        return $orders;
    }

    public function book() {
        $book = new BookModel();
        $book->setId($this->book_id);
        $book->load();

        return $book;
    }

    public function setBookId($book_id)
    {
        $this->book_id = $book_id;
    }

    public function setUserId($user)
    {
        $this->user_id = $user;
    }

    public function setAmount($amount)
    {
        $this->amount = $amount;
    }

    /**
     * @return mixed
     */
    public function getUserId()
    {
        return $this->user_id;
    }

    /**
     * @return mixed
     */
    public function getBookId()
    {
        return $this->book_id;
    }

    /**
     * @return mixed
     */
    public function getAmount()
    {
        return $this->amount;
    }

    /**
     * @return mixed
     */
    public function getReviewId()
    {
        return $this->review_id;
    }

    /**
     * @param mixed $review_id
     */
    public function setReviewId($review_id)
    {
        $this->review_id = $review_id;
    }

    /**
     * @return mixed
     */
    public function getCreatedAt()
    {
        return $this->created_at;
    }

    /**
     * @param mixed $created_at
     */
    public function setCreatedAt($created_at)
    {
        $this->created_at = $created_at;
    }


}