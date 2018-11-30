<?php

require_once "model/BaseModel.php";

class BookModel extends BaseModel {
    protected $id;
    protected $rating;
    protected $vote;

    public function __construct()
    {
        parent::__construct("book_vr");
    }

    public function loadById() {
        $stmt = $this->conn->prepare("select * from book_vr where id like :id;");
        $stmt->bindParam("id", $this->id);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        return $result;
    }

    public function recalculateRating() {
        $count = 0;
        $total = 0;
        $this->load();
        $reviews = $this->getBookReviews();
        foreach($reviews as $r) {
            $count += 1;
            $total += (int)$r["star"];
        }
        $rating = 0;
        if($count > 0) {
            $rating = $total / $count;
        }
        $this->setRating($rating);
        $this->save();
    }

    public function getBookReviews() {
        $stmt = $this->conn->prepare("select * from `orders` inner join review on `orders`.review_id = review.id inner join user on orders.user_id = user.id where book_id like :id;");
        $stmt->bindParam("id", $this->id);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        return $result;
    }

    public function setId($id) {
        $this->id = $id;
    }

    public function getId() {
        return $this->id;
    }

    /**
     * @return mixed
     */
    public function getRating()
    {
        return $this->rating;
    }

    /**
     * @param mixed $rating
     */
    public function setRating($rating)
    {
        $this->rating = $rating;
    }

    /**
     * @return mixed
     */
    public function getVote()
    {
        return $this->vote;
    }

    /**
     * @param mixed $vote
     */
    public function setVote($vote)
    {
        $this->vote = $vote;
    }

}