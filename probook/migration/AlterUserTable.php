<?php
/**
 * Created by PhpStorm.
 * User: shintaayuck
 * Date: 30/11/18
 * Time: 18.40
 */

require "core/config.php";

$conn  = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USERNAME, DB_PASS);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$stmt = $conn->prepare("alter table `user` modify column `avatar` varchar(100);");

$stmt->execute();

?>