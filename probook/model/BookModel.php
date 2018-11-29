<?php

require_once "model/BaseModel.php";

class BookModel extends BaseModel {
    protected $id;
    protected $name;
    protected $author;
    protected $rating;
    protected $vote;
    protected $description;
    protected $imgsrc;

    public function __construct()
    {
        parent::__construct("book");
    }
    
    public function searchByKeyword($query) {
        $stmt = $this->conn->prepare("select * from book where name like :query;");
        $query = '%' . $query . '%';
        $stmt->bindParam("query", $query);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        return $result;
    }

    public function loadById() {
        $stmt = $this->conn->prepare("select * from book where id like :id;");
        $stmt->bindParam("id", $this->id);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        return $result[0];
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
        $stmt = $this->conn->prepare("select * from `order` inner join review on `order`.review_id = review.id inner join user on order.user_id = user.id where book_id like :id;");
        $stmt->bindParam("id", $this->id);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        return $result;
    }

    /**
     * @return mixed
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * @param mixed $name
     */
    public function setName($name)
    {
        $this->name = $name;
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
    public function getAuthor()
    {
        return $this->author;
    }

    /**
     * @return mixed
     */
    public function getRating()
    {
        return $this->rating;
    }

    /**
     * @return mixed
     */
    public function getVote()
    {
        return $this->vote;
    }

    /**
     * @return mixed
     */
    public function getDescription()
    {
        return $this->description;
    }

    /**
     * @return mixed
     */
    public function getImgsrc()
    {
        return $this->imgsrc;
    }

    /**
     * @param mixed $author
     */
    public function setAuthor($author)
    {
        $this->author = $author;
    }

    /**
     * @param mixed $rating
     */
    public function setRating($rating)
    {
        $this->rating = $rating;
    }

    /**
     * @param mixed $vote
     */
    public function setVote($vote)
    {
        $this->vote = $vote;
    }

    /**
     * @param mixed $description
     */
    public function setDescription($description)
    {
        $this->description = $description;
    }

    /**
     * @param mixed $imgsrc
     */
    public function setImgsrc($imgsrc)
    {
        $this->imgsrc = $imgsrc;
    }

}