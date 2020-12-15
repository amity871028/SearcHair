
window.onload = function () {
	

    var userinput = document.getElementById("message");
    var messageDisplay = document.getElementById("chatbox");
    var userNameInput = localStorage.getItem("name");
    var onlineUser = document.getElementById("online");
 
    var webSocket;
    var isConnectSuccess = false;
    var havePicture = false;
    
    
    document.getElementById('basic-addon1').addEventListener('change', function(){
		//uploadPicture(this);
	});
 
    if (userNameInput == null) {
    	userNameInput = "匿名者" + Math.floor(Math.random()*10000); // 設置WebSocket連接
    }

    if (userNameInput !== null) {
        setWebSocket();  // 設置WebSocket連接
    }
    // Submit Form時送出訊息
    userinput.addEventListener("keypress", function (e) {
    	if(e.key === 'Enter'){
    		sendMessage();
    		document.getElementById('message').value = "";
    	}
    });
    
    // 使用webSocket擁有的function, send(), 送出訊息
    function sendMessage() {
    	if(userinput.value == "" && havePicture == false) {
    		alert("請輸入訊息");
    	}
        // 檢查WebSocket連接狀態
        if (webSocket && isConnectSuccess) {
            var messageInfo = {
                userName: userNameInput,
                message: userinput.value,
            }
            webSocket.send(JSON.stringify(messageInfo));
        }
    }
    
    function sendPicture(input) {
    	console.log("here");
    	// console.log(localStorage.getItem("picture"));
    	if(userinput.value == "" && havePicture == false) {
    		alert("請輸入訊息");
    	}
        // 檢查WebSocket連接狀態
        if (webSocket && isConnectSuccess) {
            var messageInfo = {
                userName: userNameInput,
                message: input,
            }
            console.log(input);
            webSocket.send(JSON.stringify(messageInfo));
        }
    }
    /*
    function uploadPicture(input){
    	if (input.files && input.files[0]) {
    	      const reader = new FileReader();
    	  
    	      reader.onload = function loadPicture(e) {
    	    	  localStorage.setItem('picture', e.target.result);
    	    	  havePicture = true;
    	    	  sendPicture(e.target.result);
    	      };
    	      reader.readAsDataURL(input.files[0]);
    	    }
    }*/
 
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
                message : userNameInput + " 進入了聊天室"
            };
            webSocket.send(JSON.stringify(firstLoginInfo));
        };
 
        // onmessage , 接收到來自Server的訊息時觸發
        webSocket.onmessage = function (event) {
        	setTimeout(function() {
        		var messageObject = JSON.parse(event.data);
                if (messageObject.message != ""){
                	messageDisplay.innerHTML += messageObject.userName + ": " + messageObject.message + "<br>";
                }
                onlineUser.innerHTML = messageObject.count;
                localStorage.removeItem("picture");
        	}, 10); 
            
        };
    }
};