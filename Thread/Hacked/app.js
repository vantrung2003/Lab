const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;
const db = require('./mysql');
// thư viện đọc body request
const bodyParser = require('body-parser');
// thư viện quản lý resource
const cors = require('cors');
app.use(cors({ origin: '*' }));
app.set('view engine', 'ejs');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.post('/upload', (req, res) => {
     // kiểm tra xem dữ liệu gửi lên đủ không
     // if (!req.body.image
     //      || !req.body.title
     //      || !req.body.copyright
     //      || !req.body.date
     //      || !req.body.explaination) {
     //      res.json({ status: false, message: "Lack of information!" })
     // } else {
     //      try {
     //           const image = req.body.image;
     //           const title = req.body.title;
     //           const copyright = req.body.copyright;
     //           const date = req.body.date;
     //           const explaination = req.body.explaination;

     //           // quert insert dữ liệu
     //           const sql = `INSERT INTO 
     //         imagetable (title, image, copyright, date, explaination)
     //     VALUES
     //     ('${title}', '${image}', '${copyright}', '${date}', '${explaination}')`;
     //           db.query(sql, (err, result) => {
     //                if (err) throw err;
     //                console.log("1 record has been inserted");
     //                res.json({ status: true, message: "1 record has been inserted" });
     //           });
     //      } catch (error) {
     //           res.json({ status: false, message: error });
     //      }
     // }
    let sql = 'INSERT INTO imagetable SET ?';
    db.query(sql, req.body,(err, data) =>{
     if(err){
          res.status(500).json('Loi khong the them!! Xin hay xem lai')
     }else{
          res.status(200).json('Them thanh cong!!')
     }
    })

});



app.get('/get-image', (req, res) => {
     db.query('SELECT * FROM imagetable', (err, data) => {
          if (err) {
               console.error('Error executing query:', err);
               return res.status(500).json({ error: 'Error executing query' });
          }
          res.json(data);
     });
});

app.delete('/delete-image/:id', (req, res) => {
     // TODO: tự làm
     const id = req.params.id;

     db.query('DELETE FROM imagetable WHERE id =?', [id], (err) => {
          if (err) {
               res.status(500).json({ error: 'Loi khong the xoa' })
          }

          res.json({ message: 'Xoa thanh cong' });

     })

})
// TODO: api /update-image
app.put('/update-image/:id', (req, res) => {
     // TODO: tự làm
     const id = req.params.id;
     const {title, image, copyright, date, explaination} = req.body;
     db.query('UPDATE imagetable SET title = ?, image = ?, copyright = ?, date = ?, explanation =?',
     [title, image, copyright, date, explaination,id],(err)=>{
          if(err){
               return res.status(500).json({error:'Khong the update!! xin hay xem lai'})
          }
          res.status(200).json('update thanh cong')
     })
 })

app.listen(PORT, () => {
     console.log(`Server is running on port http://localhost:${PORT}`);
});
