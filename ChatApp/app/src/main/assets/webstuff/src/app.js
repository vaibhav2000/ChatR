var express = require('express');
var socket = require('socket.io');
var path = require('path');
// App setup
var app = express();
var server = app.listen(parseInt(process.env.PORT || "5000"), function(){
  console.log('listening to port 5000');
});

// Static files
// app.use('/public', express.static('public'));
app.use(express.static(path.join(__dirname, "../public")));
//Socket setup
var activeSockets =[];
var io = socket(server);

io.on("connection", socket => {
    console.log("Socket connected.");
    const existingSocket = activeSockets.find(
      existingSocket => existingSocket === socket.id
    );

    if (!existingSocket) {
      activeSockets.push(socket.id);

      socket.emit("update-user-list", {
        users: activeSockets.filter(
          existingSocket => existingSocket !== socket.id
        )
      });

      socket.broadcast.emit("update-user-list", {
        users: [socket.id]
      });
    }

    socket.on("disconnect", () => {
        activeSockets = activeSockets.filter(
          existingSocket => existingSocket !== socket.id
        );
        socket.broadcast.emit("remove-user", {
          socketId: socket.id
        });
      });
      
    socket.on("call-user", data => {
        socket.to(data.to).emit("call-made", {
          offer: data.offer,
          socket: socket.id
        });
      });
      
    socket.on("make-answer", data => {
        socket.to(data.to).emit("answer-made", {
          socket: socket.id,
          answer: data.answer
        });
      });

  });