<?php
/**
 * Created by PhpStorm.
 * User: shintaayuck
 * Date: 24/10/18
 * Time: 17.46
 */

require "controller/BaseController.php";
require "core/View.php";
require "model/UserModel.php";

class ProfileController extends BaseController
{
    public function __construct($request)
    {
        parent::__construct($request);
    }

    public function view()
    {
        $model = new UserModel();
        $session = new Session();
        $model->setId((int)$session->inSession());
        $model->load();

        $vars = [
            "navbar" => "profile",
            "name" => $model->getName(),
            "username" => $model->getUsername(),
            "email" => $model->getEmail(),
            "address" => $model->getAddress(),
            "phone" => $model->getPhone(),
            "cardnumber" => $model->getCardnumber()
        ];

        if (strpos($model->getAvatar(), 'https') !== false) {
            $vars["avatar"] = $model->getAvatar();
        } else {
            $vars["avatar"] = "/static/" . $model->getAvatar();
        }

        View::render("profile", $vars);
    }
}