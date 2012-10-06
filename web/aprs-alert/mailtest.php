<?php session_start();
if ( !isset($_SESSION['userid'] ) ) {
    header("location:login.php");
}
include "db_setup.php";

// Connect to server and select databse.
//pg_connect("$host", "$username", "$password")or die("cannot connect");
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
//mysql_select_db("$db_name")or die("cannot select DB");

// Define $myusername and $mypassword
$na_id=strtoupper($_GET['addr']);

$sql="SELECT * FROM notification_addresses WHERE na_id=$na_id and user_id=".$_SESSION['userid'];
$result=pg_exec($sql);

$count=pg_numrows($result);
if ( $count > 0 ) {
    $row = pg_fetch_assoc($result, 0);
    $email_address = $row['address'];
    $hash = md5(mt_rand());
    $subject = "APRS-Alert Test Mail!";
    $body = "Hi,\n\nThis is a test email notification from APRS-Alert\n";
    $body .=" de John, AB0OO\n";
    $headers = "From: alert@aprs-alert.net\r\n" .  "X-Mailer: php";
    session_register('error');
    if (mail($email_address, $subject, $body, $headers)) {
        $_SESSION['error'] = "Email sent to $email_address";
    } else {
        $_SESSION['error'] = "Email FAILED during send to $email_address";
    }
    header("location:notification_addresses.php");
    exit;
}
?>
