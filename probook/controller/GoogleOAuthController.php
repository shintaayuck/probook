<?php
/**
 * Created by PhpStorm.
 * User: shintaayuck
 * Date: 30/11/18
 * Time: 14.43
 */

require "controller/BaseController.php";
require "core/View.php";
require_once "model/UserModel.php";
require_once "core/Session.php";

class GoogleOAuthController extends BaseController
{
    public function __construct($request)
    {
        parent::__construct($request);
    }

    public function oauth() {
        $id = $this->request->post("id");
        $name = $this->request->post("name");
        $email = $this->request->post("email");
        $img = $this->request->post("img");
        $arr = explode("@", $email);

        $user = new UserModel();
        $user->setName($name);
        $user->setUsername($arr[0]);
        $user->setEmail($email);
        $user->setPassword($id);
        $user->setAvatar($img);

        if ($user->checkUserExists()) {
            //login
            $user->loadFromUserPass();
        } else {
            $user->setAddress("Null");
            $user->setPhone("0811111111111");
            $user->setCardnumber('1000000000000000');
            $user->insert();
        }

        $session = new Session();
        $session->setSession($user->getId(), $user->getUsername());

        View::redirect("/search");
    }
}