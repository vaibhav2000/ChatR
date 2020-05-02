const { RTCPeerConnection, RTCSessionDescription } = window;
var connections = new Object();
var isAlreadyCalling = new Object();

function newConnection(socketId){
  isAlreadyCalling[socketId] = false;

  const peerConnection = new RTCPeerConnection();
  peerConnection.ontrack = function({ streams: [stream] }) {
    const remoteVideo = document.getElementById("remote-video");
    if (remoteVideo) {
      remoteVideo.srcObject = stream;
    }
  };

  navigator.getUserMedia(
      { video: true, audio: true },
      stream => {
        const localVideo = document.getElementById("local-video");
        if (localVideo) {
          localVideo.srcObject = stream;
        }

        stream.getTracks().forEach(track => peerConnection.addTrack(track, stream));
      },
      error => {
        console.warn(error.message);
      }
  );
  connections[socketId] = peerConnection;
}

const socket = io.connect("https://prprtc.herokuapp.com/");

socket.on("update-user-list", ({ users }) => {
  updateUserList(users);
 });
  
socket.on("remove-user", ({ socketId }) => {
  const elToRemove = document.getElementById(socketId);
  
  if (elToRemove) {
    elToRemove.remove();
  }
 });

socket.on("call-made", async data => {
  await connections[data.socket].setRemoteDescription(
    new RTCSessionDescription(data.offer)
  );
  const answer = await connections[data.socket].createAnswer();
  await connections[data.socket].setLocalDescription(new RTCSessionDescription(answer));
  
  socket.emit("make-answer", {
    answer,
    to: data.socket
  });
 });

socket.on("answer-made", async data => {
    await connections[data.socket].setRemoteDescription(
      new RTCSessionDescription(data.answer)
    );
    
    if (!isAlreadyCalling[data.socket]) {
      callUser(data.socket);
      isAlreadyCalling[data.socket] = true;
    }
 });

 function updateUserList(socketIds) {
  const activeUserContainer = document.getElementById("active-user-container");
  
  socketIds.forEach(socketId => {
    const alreadyExistingUser = document.getElementById(socketId);
    if (!alreadyExistingUser) {
      const userContainerEl = createUserItemContainer(socketId);
      activeUserContainer.appendChild(userContainerEl);
      newConnection(socketId);
    }
  });
 }

 function createUserItemContainer(socketId) {
  const userContainerEl = document.createElement("div");
  
  const usernameEl = document.createElement("p");
  
  userContainerEl.setAttribute("class", "active-user");
  userContainerEl.setAttribute("id", socketId);
  usernameEl.setAttribute("class", "username");
  usernameEl.innerHTML = `Socket: ${socketId}`;
  
  userContainerEl.appendChild(usernameEl);
  
  userContainerEl.addEventListener("click", () => {
    unselectUsersFromList();
    userContainerEl.setAttribute("class", "active-user active-user--selected");
    const talkingWithInfo = document.getElementById("talking-with-info");
    talkingWithInfo.innerHTML = `Talking with: "Socket: ${socketId}"`;
    callUser(socketId);
    listenTo = socketId;
    console.log(listenTo);
  }); 
  return userContainerEl;
 }

 async function callUser(socketId) {
  const offer = await connections[socketId].createOffer();
  await connections[socketId].setLocalDescription(new RTCSessionDescription(offer));
  
  socket.emit("call-user", {
    offer,
    to: socketId
  });
 }
 
 function unselectUsersFromList() {
  const alreadySelectedUser = document.querySelectorAll(
    ".active-user.active-user--selected"
  );

  alreadySelectedUser.forEach(el => {
    el.setAttribute("class", "active-user");
  });
}
