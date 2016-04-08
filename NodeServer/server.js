/**
 * Created by Thomas on 21.03.2016.
 */

//todo******************
//todo****** INIT ******
//todo******************
var express = require('express');
var https = require('https');
var http = require('http');
var mysql     =    require('mysql');
var fs = require('fs');
var bodyParser     =        require("body-parser");
var app = express();

// This line is from the Node.js HTTPS documentation.
var options = {
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.crt')
};

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

//app.use('/', express.static(__dirname + '/public'));
//__dirname is the same as "./" and means starting with current directory.
//app.set('views', __dirname + 'public/views');

app.use('/', express.static(__dirname + '/'));

// Create an HTTP service.
http.createServer(app).listen(8080);
// Create an HTTPS service identical to the HTTP service.
//https.createServer(options, app).listen(443);


//todo****************************
//todo****** RequestHandler ******
//todo****************************

app.get('/', function(req, res){
    res.sendFile(__dirname + '/public/views/index.html');
});



app.post('/*', function(request, res){
    console.log("got postRequest: " + request.param('action'));
    handle_database(request,res)
});




//todo*****************************
//todo****** DatabaseHandler ******
//todo*****************************

var pool      =    mysql.createPool({
    connectionLimit : 100, //important
    host     : 'localhost',
    user     : 'tjomie',
    password : 'YOLO',
    database : 'topptur',
    debug    :  false,
    port: 3307
});

function handle_database(req,res) {
    console.log("running method 'handle_database'");
    pool.getConnection(function(err,connection){
        if (err) {
            connection.release();
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
        }

        console.log('connected as id ' + connection.threadId);

        var action = req.param("action");
        console.log("action = " + action);
        switch(action) {
            case "StartGame":
                console.log("entering case: 'StartGame'");
                connection.query("select * from tbl_line",function(err,rows){
                    connection.release();
                    if(!err) {
                        var objs = [];
                        for (var i = 0;i < rows.length; i++) {
                            //objs.push({name: rows[i].NAME,coordinates: rows[i].COORDINATES});
                            objs.push(rows[i]);
                        }

                        res.json(objs);
//                      res.send(objs);


                        //res.json(JSON.stringify(rows, null, 2));
                        //return()
                    }else{
                        console.log("******ERROR**********" + err);
                    }
                });
                break;

            default:
            //default code block
        }


        connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
        });
    });
}