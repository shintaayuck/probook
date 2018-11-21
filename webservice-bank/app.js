//Setup express
var express = require('express');
var app = express();

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


app.get("/api/validate", function(req,res) {
    card_no = req.query.card_no;
    if (card_no) {
        var sql = 'select * from customer where cardnumber = ?';
        con.query(sql, card_no, function(err, result) {
            // if (err) throw err;
            if (result[0]) {
                message = card_no + ' is found';
                res.status(200).send({"message" : message}); //OK
            } else {
                message = card_no + ' is not found';
                res.status(404).send({"message": message}); //Not Found
            }
        })
    } else {
        res.sendStatus(400);
    }

});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
