<?php
/**
 * Created by PhpStorm.
 * User: Gabriel B.R
 * Email: gabriel.bentara@gmail.com
 * Date: 23/10/18
 * Time: 8:39 AM
 */

require "core/config.php";

$conn  = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USERNAME, DB_PASS);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$stmt = $conn->prepare("create table session (id int AUTO_INCREMENT, session_id varchar(30) not null, user_id int not null, primary key (id), expire timestamp default current_timestamp, browser varchar(25) not null, ip varchar(16) not null);");

$stmt->execute();