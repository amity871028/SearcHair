function delayURL(url, time) {
  setTimeout(() => { window.location.href = `${url}`; }, time);
}

/*
$(document).ready(function () {
    $('#dismiss, .overlay').on('click', function () {
        $('#sidebar').removeClass('active');
@@ -15,6 +15,77 @@ $(document).ready(function () {
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });
    alert("尚未動工");
    delayURL('./index.html', 200);
}); 
    //alert("尚未動工");
    //delayURL('./index.html', 200);
});*/

window.onload = function () {

    var userinput = document.getElementById("message");
    var chatRoomForm = document.getElementById("send-form");
    var messageDisplay = document.getElementById("chatbox");
    var userNameInput = document.getElementById("user-name");
    var onlineUser = document.getElementById("online");

    var webSocket;
    var isConnectSuccess = false;


    if (userNameInput !== null) {
        setWebSocket();  // 設置WebSocket連接
    } else {
    	alert("請先登入");
        delayURL('./index.html', 200);
    }
    // Submit Form時送出訊息
    chatRoomForm.addEventListener("submit", function () {
        sendMessage();
        return false;
    });

    // 使用webSocket擁有的function, send(), 送出訊息
    function sendMessage() {
        // 檢查WebSocket連接狀態
        if (webSocket && isConnectSuccess) {
            var messageInfo = {
                userName: userNameInput.innerHTML,
                message: userinput.value
            }
            webSocket.send(JSON.stringify(messageInfo));
        }
    }

    // 設置WebSocket
    function setWebSocket() {
        // 開始WebSocket連線
        webSocket = new WebSocket('ws://localhost:8080/SearcHair/websocket');
        // 以下開始偵測WebSocket的各種事件

        // onerror , 連線錯誤時觸發
        /*
        webSocket.onerror = function (event) {
            loginBtn.disabled = false;
            userNameInput.disabled = false;
            infoWindow.innerHTML = "登入失敗";
        };*/

        // onopen , 連線成功時觸發
        webSocket.onopen = function (event) {
            isConnectSuccess = true;

            // 送一個登入聊天室的訊息
            var firstLoginInfo = {
                userName : "系統",
                message : userNameInput.innerHTML + " 登入了聊天室"
            };
            webSocket.send(JSON.stringify(firstLoginInfo));
        };

        // onmessage , 接收到來自Server的訊息時觸發
        webSocket.onmessage = function (event) {
            var messageObject = JSON.parse(event.data);
            messageDisplay.innerHTML += messageObject.userName + ": " + messageObject.message + "<br>";
            onlineUser.innerHTML = messageObject.count;
        };
    }
}; 