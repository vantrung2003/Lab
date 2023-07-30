const mysql = require('mysql')
require('dotenv').config();
const connect1 = mysql.createConnection({
     host:'localhost',
     user:'root',
     password:'',
     database:'imagedatabase'
})

connect1.connect(function(err){
     if(err){
          console.log('Ket noi voi co so du lieu khong thanh cong');
     }
})

module.exports = connect1;