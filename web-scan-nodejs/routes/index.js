var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/:key', function (req, res, next) {
  console.log('key:', req.params.key);
  res.render('home', {key: req.params.key});
});

module.exports = router;
