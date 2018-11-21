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

//getter
app.get("/", function(req,res) {
    //create json
    json = {"nama": "shinta", "umur" : 19};
    res.send(json);

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
