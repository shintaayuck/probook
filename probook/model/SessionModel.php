<?php
/**
 * Created by PhpStorm.
 * User: Gabriel B.R
 * Email: gabriel.bentara@gmail.com
 * Date: 23/10/18
 * Time: 8:15 AM
 */

require_once "model/BaseModel.php";

class SessionModel extends BaseModel
{
    protected $session_id;
    protected $user_id;
    protected $expire;
    protected $browser;
    protected $ip;

    public function __construct()
    {
        parent::__construct("session");
    }

    public function load()
    {
        $stmt = $this->conn->prepare("select * from $this->tableName where session_id = :id limit 1");
        $stmt->bindParam(":id", $this->session_id);
        $stmt->execute();
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($result == []) {
            return;
        }
        foreach ($result as $column => $value) {
            $this->$column = $value;
        }
    }

    public function loadByUserID($userId)
    {
        $stmt = $this->conn->prepare("select * from $this->tableName where user_id = :id limit 1");
        $stmt->bindParam(":id", $userId);
        $stmt->execute();
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($result == []) {
            return;
        }
        foreach ($result as $column => $value) {
            $this->$column = $value;
        }
    }

    public function checkSessionUnavailable($userId)
    {
        $stmt = $this->conn->prepare("select * from $this->tableName where user_id = :id limit 1");
        $stmt->bindParam(":id", $userId);
        $stmt->execute();
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($result == []) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return mixed
     */
    public function getSessionId()
    {
        return $this->session_id;
    }

    /**
     * @param mixed $session_id
     */
    public function setSessionId($session_id)
    {
        $this->session_id = $session_id;
    }

    /**
     * @return mixed
     */
    public function getUserId()
    {
        return $this->user_id;
    }

    /**
     * @param mixed $user_id
     */
    public function setUserId($user_id)
    {
        $this->user_id = $user_id;
    }

    /**
     * @return mixed
     */
    public function getExpire()
    {
        return $this->expire;
    }

    /**
     * @param mixed $expire
     */
    public function setExpire($expire)
    {
        $this->expire = $expire;
    }

    /**
     * @return mixed
     */
    public function getBrowser()
    {
        return $this->browser;
    }

    /**
     * @param mixed $browser
     */
    public function setBrowser($browser)
    {
        $this->browser = $browser;
    }
        
    /**
     * @return mixed
     */
    public function getIp()
    {
        return $this->ip;
    }

    /**
     * @param mixed $ip
     */
    public function setIp($ip)
    {
        $this->ip = $ip;
    }
}