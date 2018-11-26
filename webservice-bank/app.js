//Setup express
var express = require('express');
var app = express();
var bodyParser = require('body-parser');

//Setup database
const mysql = require('mysql');

// Connect to database
const con = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'bank'
});

con.connect((err) => {
  if(err){
    console.log('Error connecting to DB');
    return;
  }
  console.log('Connection established');
});

//Setup connection to localhost
app.listen(3000, () => {
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

app.get("/api/validate", function(req,res) {
    card_no = req.query.card_no;
    if (card_no) {
        var sql = 'SELECT * FROM customer WHERE cardnumber = ?';
        con.query(sql, card_no, function(err, result) {
            // if (err) throw err;
            if (result[0]) {
                message = card_no + ' is found';
                res.header("Access-Control-Allow-Origin", "*");
                res.status(200).send({"message" : message}); //OK
            } else {
                message = card_no + ' is not found';
                res.header("Access-Control-Allow-Origin", "*");
                res.status(404).send({"message": message}); //Not Found
            }
        })
    } else {
        res.status(400).send({"message": "Bad request"});
    }

});

app.post("/api/transfer", function(req,res) {
  card_no_sender = req.body.card_no_sender;
  card_no_receiver = req.body.card_no_receiver;
  amount = req.body.amount;
  if ((card_no_sender) && (card_no_receiver) && (amount)) {
    var sql = 'SELECT balance FROM customer WHERE cardnumber = ?';
    con.query(sql, card_no_sender, function(err, result){
      if (err) res.status(400).send({"message": "Bad request", "code" : 1});
      if (result[0].balance < amount) {
        res.status(400).send({"message": "Not enough amount", "code" : 2});
      }
      else {
        var sql = 'UPDATE customer SET balance = balance - ? WHERE cardnumber = ?';
        var val = [amount, card_no_sender];
        con.query(sql, val, function(err, result){
          if (err) res.status(500).send({"message": "Error updating sender balance", "code" : 3});
          console.log("1 record updated");
        })
        var sql = 'UPDATE customer SET balance = balance + ? WHERE cardnumber = ?';
        var val = [amount, card_no_receiver];
        con.query(sql, val, function(err, result){
          if (err) res.status(500).send({"message": "Error updating receiver balance", "code" : 3});
          console.log("1 record updated");
        })
        var sql = 'INSERT INTO transaction(sender,recipient,amount) VALUES (?,?,?)';
        var val = [card_no_sender, card_no_receiver, amount];
        con.query(sql, val, function(err, result){
          if (err) res.status(500).send({"message": "Error inserting transaction to DB", "code" : 3});
          console.log("1 record inserted")
        })
        res.status(200).send({"message": "Transaction success", "code" : 0});
      }
    })
  }
});

// module.exports = app;
