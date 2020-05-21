var Words;
var TalkWords;
var TalkSub;


function randomNumber(length){
	var arr = [];
	for(let i = 0;i < length; i += 1){//一個從0到100的陣列
			arr.push(i);
	}
	arr.sort(function(){//隨機打亂這個陣列
		return Math.random()-0.5;
	})
	arr.length = length;//改寫長度
	console.log(arr);
	return arr;
}

async function inputPress() {
	if (event.keyCode == 13) { 
		chatRoom();
		document.getElementById('talkwords').value = "";
	}	
} 

async function chatRoom(){
	var botMessage = "";
	if(TalkWords.value == ""){
		alert("請輸入訊息");
		return;
	} else{
		userMessage = '<div class="btalk"><span>' + TalkWords.value +'</span></div>' ;  
		Words.innerHTML += userMessage;
		if (TalkWords.value == "hi" || TalkWords.value == "hello" || TalkWords.value == "你好") {
			botMessage = `<div class="atalk">
								<img class="chatbot-picture" src="img/chatbot.jpg">
								<span id="asay">'+ TalkWords.value +'</span>							
						  </div>`;
			Words.innerHTML += botMessage;
			
		} else {
			toChat();
		}
	} 
}

async function toChat(){
 var cardLength = 6;
 var url = "http://localhost:8080/SearcHair/ChatbotServlet"; 
 var input = document.getElementById('talkwords').value.split(" ");
 if(input.length < 2 || input[1] == ""){
	 if(input[0] == "店家") {
		 botMessage = `<div class="atalk">
			 				<img class="chatbot-icon" src="img/chatbot.jpg">
			 				<span id="asay">請輸入「店家 關鍵字」</span>
			 		  </div>`;
		 Words.innerHTML += botMessage;
	 } else if (input[0] == "設計師") {
		 botMessage = `<div class="atalk">
			 				<img class="chatbot-icon" src="img/chatbot.jpg">
			 				<span id="asay">請輸入「設計師 關鍵字」</span>	
			 		   </div>`;
		 Words.innerHTML += botMessage;
	 } else {
		botMessage = `<div class="atalk">
							<img class="chatbot-icon" src="img/chatbot.jpg">
							<span id="asay">我看不懂，請您在重新輸入一次</span>
					  </div>`;
		Words.innerHTML += botMessage;
	 }
	return;
}
 
 let keywordValue = "";
 for(let i = 1; i < input.length; i+=1){
	 keywordValue += input[i] + " ";
 }
 const result = await
FetchData.post(url, {
	func : input[0],
	keyword : keywordValue.substring(0,keywordValue.length-1)
});

const resultJson = await result.json();
if(resultJson.length == 0) {
	botMessage = `<div class="atalk">
						<img class="chatbot-icon" src="img/chatbot.jpg">
						<span id="asay">找不到相關資訊</span>
				  </div>`;
	Words.innerHTML += botMessage;
}
else {
	botMessage = `<div class="atalk">
						<img class="chatbot-icon" src="img/chatbot.jpg">
						<span id="asay">以下是我找到的資訊</span>
				  </div>`;
	Words.innerHTML += botMessage;
	let out = null;
	if(resultJson.length < 5) {
		cardLength = resultJson.length;
	}
	if(resultJson[0].func == "salon") {
		const number = randomNumber(resultJson.length);
		for (let i = 0; i < cardLength; i += 1) {
		    out = `<a href="./salon-detail.html?id=${resultJson[number[i]].id}" style="text-decoration:none; color:black;" target="_blank">
		    			<div class="card">
		                  <div class="card-body">
		                    <h5 class="card-title"><b>${resultJson[number[i]].name}</b></h5>
		                    <p class="card-text">地址: ${resultJson[number[i]].address}</p>
		                    <p class="card-text">電話: ${resultJson[number[i]].phone}</p>
		                    <p class="card-text">營業時間: ${resultJson[number[i]].businessTime}</p>
		                    <img src="${resultJson[number[i]].picture}">
		                    </div>
		                 </div>
		            </a>`;
		    Words.innerHTML = Words.innerHTML + out;
		 }
	}
	else if(resultJson[0].func == "stylist"){
		const number = randomNumber(resultJson.length);
		for(let i = 0; i < cardLength; i+= 1) {
		    out = `<a href="./stylist-detail.html?id=${resultJson[number[i]].id}" style="text-decoration:none; color:black;" target="_blank">
		    			<div class="card">
		                  <div class="card-body">
		                    <h5 class="card-title"><b>${resultJson[number[i]].name}</b></h5>
		                    <p class="card-text">職業名稱: ${resultJson[number[i]].jobTitle}</p>
		                    <p class="card-text">所屬店家: ${resultJson[number[i]].salon}</p>
		                    <p class="card-text">店家地址: ${resultJson[number[i]].address}</p>
		                    <img src="${resultJson[number[i]].picture}">
		                    </div>
		                </div>
		            </a>`;
		    Words.innerHTML = Words.innerHTML + out;
		}
	}
	else {
		// const number = randomNumber(resultJson.length);
		//for(let i = 0; i < 1; i+= 1) {
		    out = `<div class="card">
		    			<p class="card-text">問題:<br> ${resultJson[0].question}</p>
	                    <p class="card-text">建議:<br> ${resultJson[0].answer}</p>
	                    來源網址:<br><a href="${resultJson[0].answerWeb}" target="_blank">${resultJson[0].answerWeb}</a>
		           </div>`;
		    Words.innerHTML = Words.innerHTML + out;
		//}
	}
}
} 

window.onload = function(){  
	Words = document.getElementById("words"); 
	TalkWords = document.getElementById("talkwords");
	TalkSub = document.getElementById("talksub"); 
    TalkSub.onclick = function(){ 
		chatRoom();			
    }   
}