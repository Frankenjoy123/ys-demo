var express = require('express');
var router = express.Router();

/* GET home page. */

router.get('/', function (req, res, next) {
  res.render('default', {});
});

router.get('/usercontract', function (req, res, next) {
  res.render('user_contact', {});
});

router.get('/:key', function (req, res, next) {
  console.log('key:', req.params.key);
  res.render('home', {key: req.params.key});
});

module.exports = router;
