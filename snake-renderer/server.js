var express = require('express'); 
var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
const Kafka = require('node-rdkafka');

var consumer = new Kafka.KafkaConsumer({
  'group.id': 'kafka',
  'metadata.broker.list': 'localhost:9092',
}, {});

io.on('connection', function (socket) {
  consumer.connect();
  consumer
    .on('ready', function() {
      consumer.subscribe(['keyboard-input']);
      consumer.consume();
    })
    .on('data', function(data) {
      console.log(data.value.toString());
      socket.emit("keys", data.value.toString());
    })
    .on('event.error', function(err) {
      console.log(err);
    });
});

app.use(express.static(__dirname + '/public'));

app.get('/', function(req, res, next){
  res.render('./public/index.html');
});

server.listen(8888);