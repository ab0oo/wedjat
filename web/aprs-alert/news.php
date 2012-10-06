<?php session_start();?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title>APRS-Alert News</title>
        <meta name="generator" content="Bluefish 2.0.2" >
        <meta name="author" content="John Gorkos" >
        <meta name="date" content="2011-04-27T19:20:41-0400" >
        <meta name="copyright" content="">
        <meta name="keywords" content="">
        <meta name="description" content="">
        <meta name="ROBOTS" content="NOINDEX, NOFOLLOW">
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8">
        <meta http-equiv="content-style-type" content="text/css">
        <meta http-equiv="expires" content="0">
        <link href="main.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
            <h3>16-Jun-2011 08:48</h3>
            <p>Well, the memory troubles continue.  The VPS server I have only gives me 512MB of RAM.  The funny part is, they
            also give me 16 cores and 10GB of disk space.  Java is a memory pig, and it's possible I have a leak in one of the
            threaded parts of the code, so the JVM just stops running every 36 hours or so.  I'm torn as to whether I should
            pack up and leave the VPS and just host it from home (I have several far more powerful machines at the house, and a
            comcast cable connection), or move to a new VPS, or just keep restarting the core data logger every X hours...</p>

            <h3>11-May-2011 12:06</h3>
            <p>I'm having some stability problems with the Java backend, mainly related to lack of memory on the VPS.  I'm going to
            shrink things down as much as possible and then switch to a different VPS provider in a few months, when my initial
            contract runs out.  Please bear with me while I make these changes.
            </p>
            <h3>11-May-2011 12:06</h3>
            <p>Wedjat has become APRS-Alert, and the code is just about done.  It's taken much longer than I'd hoped to get this
            site up and running, but it's not "50 lines of Perl and a prayer."  I have a VPS Ubuntu host through a company 
            called <a href="http://www.a2hosting.com/services/vps-hosting/">A2 Hosting</a>.  They've been exactly what I hoped for,
            and nothing more or less.  It is costing me $14/month to run this site, so if you find it useful, I'm not above
            asking for donations.</p>
            Anyway, I'm going to turn it lose to the public tomorrow, so we'll see what happens.</p>
            John Gorkos, AB0OO<br>
        </div>
        <div id="footer"</div><?php include "footer.php"?></div>
    </body>
</html>
