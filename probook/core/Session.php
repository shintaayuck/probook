    <?php
/**
 * Created by PhpStorm.
 * User: Gabriel B.R
 * Email: gabriel.bentara@gmail.com
 * Date: 23/10/18
 * Time: 8:02 AM
 */

require "model/SessionModel.php";

class Session
{
    private $session;

    public function __construct()
    {
        $this->session = new SessionModel();
    }

    public function generateSessionId() {
        $sessionId = "";
        $chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        for ($i = 0; $i < 30; $i++) {
            $sessionId .= $chars[random_int(0, strlen($chars)-1)];
        }

        return $sessionId;
    }

    public function setSession($userId, $username) {
        if ($this->inSession()) {
            return;
        }

        $browserinfo = get_browser($_SERVER['HTTP_USER_AGENT'], true);
        $browsername = $browserinfo['browser'];

        $ip = $_SERVER['REMOTE_ADDR'];
        $sessionId = $this->generateSessionId();

        if ($this->session->checkSessionUnavailable($userId)) {
            session_start();

            $this->session->setSessionId($sessionId);
            $this->session->setUserId($userId);
            $this->session->setExpire(date('Y-m-d H:i:s', strtotime("+10 minutes")));
            $this->session->setBrowser($browsername);
            $this->session->setIp($ip);

            $this->session->insert();

            setcookie("session", $sessionId);
            setcookie("username", $username);
        } else {
            $this->session->loadByUserID($userId);
            if (($this->session->getBrowser() == $browsername) and ($this->session->getIp() == $ip)){
                setcookie("session", $this->session->getSessionId());
                setcookie("username", $username); 
            }           
        }
        return;
    }

    public function inSession() {
        if (!isset($_COOKIE["session"])) {
            return False;
        }

        $sessionId = $_COOKIE["session"];
        $this->session->setSessionId($sessionId);
        $this->session->load();

        if ($this->session->getUserId() == null) {
            $this->session->delete();
            return False;
        }

        if ($this->session->getExpire() < date('Y-m-d H:i:s')) {
            $this->session->delete();
            return False;
        }

        $browserinfo = get_browser($_SERVER['HTTP_USER_AGENT'], true);
        $browsername = $browserinfo['browser'];
        $ip = $_SERVER['REMOTE_ADDR'];

        if (($this->session->getBrowser() != $browsername) and ($this->session->getIp() == $ip)){
            $this->session->delete();
            return False;
        }

        if ($this->session->getIp() != $ip){
            $this->session->delete();
            return False;          
        }
        return $this->session->getUserId();
    }

    public function destroy() {
        if (!isset($_COOKIE["session"])) {
            return;
        }

        $sessionId = $_COOKIE["session"];
        $this->session->setSessionId($sessionId);
        $this->session->load();
        $this->session->delete();
        session_destroy();

        unset($_COOKIE["session"]);
    }
}