<?php
include_once '../GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$customer_username = $_POST['customer_username'];
$clientTime = $_POST['time'];
$shopID = $_POST['shop_id'];
$awardID = $_POST['award_id'];
$quantity = $_POST['quantity'];


// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found"}';
    die();
}

$query = "select * from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token"}';
	die();
}

$userFullName = $row['name'];

// Tìm trong bảng award thỏa điều kiện
// award.awardID == $awardID && award.shopID = $shopID
$query = "select * from award where id='".$awardID."' and shopID = '" . $shopID . "'";
$query_exec = mysqli_query($localhost, $query);
$awardRow = mysqli_fetch_array($query_exec);
$awardID = $awardRow['id'];

if($awardID == "") {
    echo '{"error":"this shop does not have this award"}';
    die();
}

$awardName = $awardRow['name'];
$awardImage = $awardRow['image'];
$awardPoint = $awardRow['point'];


// Lấy thông tin của shop
$query = "select * from shop where id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$shopRow = mysqli_fetch_array($query_exec);
$shopID = $shopRow['id'];

if($shopID == "") {
    echo '{"error":"shop does not exist"}';
    die();
}

// Lấy thông tin của customer
$query = "select * from customer_users where username='".$customer_username."'";
$query_exec = mysqli_query($localhost, $query);
$customer = mysqli_fetch_array($query_exec);
$customer_username = $customer['username'];
$customer_fullname = $customer['name'];

if($customer_username == "") {
    echo '{"error":"shop does not exist"}';
    die();
}


// Trừ điểm tích lũy của user trong bảng customer_shop
$point = $quantity * $awardPoint;

$query = "select * from customer_shop where username='".$customer_username."' and shop_id = '" . $shopID . "'";
$query_exec = mysqli_query($localhost, $query);
$folllowRow = mysqli_fetch_array($query_exec);
$currentPoint = $folllowRow['point'];

$newPoint = $currentPoint - $point;
if($folllowRow){
    if($newPoint >= 0){
        // update lại award quantity
        $newQuantity = $awardRow['quantity'] - $quantity;
        $query = "update award set quantity = '" . $newQuantity . "' where id = '" . $awardID . "'";
        $query_exec = mysqli_query($localhost, $query);

        $query = "update customer_shop set point = '".$newPoint."' where username='".$customer_username."' and shop_id ='".$shopID."'";
        $query_exec = mysqli_query($localhost, $query);
        $id = uniqid();

        $query = "insert into history values ('"
                                .$id."','0','"
                                .$customer_username."','"
                                .$customer_fullname."','"
                                .$customer['phone_number']."','"
                                .$shopID."','"
                                .$point."','"
                                .$clientTime."','')";
        $query_exec = mysqli_query($localhost, $query);
        $query = "insert into buy_award_history values ('"
                                .$id."','"
                                .$shopID."','"
                                .$awardID."','"
                                .$quantity."')";
        $query_exec = mysqli_query($localhost, $query);

        echo '{"error":""}';

        // Gửi notification cho user
        // Từ $customerName -> regID (bảng customer_registration)
        $query = "select * from customer_registration where username='".$customer_username."'";
        $query_exec = mysqli_query($localhost, $query);
        $row = mysqli_fetch_array($query_exec);
        $regID = $row['regID'];

            // Gửi thông báo đến regID
        if($regID != "") {

            $regID = array($regID);
            $message = "trade successfully";
            $message = array("message" => $message, "shopID" => $shopID, "historyID" => $id);

            $gcm = new GCM();

            $result = $gcm->send_notification($regID, $message);

        }
    }else{
        echo '{"error":"Not enough point"}';
    }
}else{
    echo '{"error":"cannot update point"}';
}

mysqli_close($localhost);
?>