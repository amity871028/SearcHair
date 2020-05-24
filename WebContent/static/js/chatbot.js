let Words;
let TalkWords;
let TalkSub;


function randomNumber(length){
	let arr = [];
	for(let i = 0;i < length; i += 1){
			arr.push(i);
	}
	
	// shuffle array
	arr.sort(function(){
		return Math.random()-0.5;
	})
	arr.length = length;
	return arr;
}

async function inputPress() {
	if (event.keyCode == 13) { 
		chatRoom();
		document.getElementById('talkwords').value = "";
	}	
} 

async function chatRoom(){
	let botMessage = "";
	if(TalkWords.value == ""){
		alert("請輸入訊息");
		return;
	} else{
		userMessage = '<div class="btalk"><span>' + TalkWords.value +'</span></div>' ;  
		Words.innerHTML += userMessage;
		if (TalkWords.value.toLowerCase() == "hi" || TalkWords.value.toLowerCase() == "hello" || TalkWords.value == "你好") {
			botMessage = `<div class="atalk">
								<img class="chatbot-icon" src="img/chatbot.jpg">
								<span class="asay">${TalkWords.value}</span>							
						  </div>`;
			Words.innerHTML += botMessage;
			
		} else {
			toChat();
		}
	} 
}

async function toChat(){
 let cardLength = 6;
 let url = "http://localhost:8080/SearcHair/ChatbotServlet"; 
// split user input
 let input = document.getElementById('talkwords').value.split(" "); 
 // input [0] is what to ask , input[1] to input[input.length] are keyword
 if(input.length < 2 || input[1] == ""){
	 if(input[0] == "店家") {
		 botMessage = `<div class="atalk">
			 				<img class="chatbot-icon" src="img/chatbot.jpg">
			 				<span class="asay">請輸入「店家 關鍵字」！</span>
			 		  </div>`;
	 } else if (input[0] == "設計師") {
		 botMessage = `<div class="atalk">
			 				<img class="chatbot-icon" src="img/chatbot.jpg">
			 				<span class="asay">請輸入「設計師 關鍵字」！</span>	
			 		   </div>`;
	 } else if (input[0] == "詢問") {
		 botMessage = `<div class="atalk">
				<img class="chatbot-icon" src="img/chatbot.jpg">
				<span class="asay">請輸入「詢問 關鍵字」！</span>	
		   </div>`;
	 }else {
		botMessage = `<div class="atalk">
							<img class="chatbot-icon" src="img/chatbot.jpg">
							<span class="asay">蝦米？請您再輸入一次！</span>
					  </div>`;
	 }
	Words.innerHTML += botMessage;
	return;
}
 
 // make keyword value to string
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
						<span class="asay">找不到相關資訊QAQ</span>
				  </div>`;
	Words.innerHTML += botMessage;
}
else {
	botMessage = `<div class="atalk">
						<img class="chatbot-icon" src="img/chatbot.jpg">
						<span class="asay">以下是我找到的資訊 > < </span>
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
	    out = `<div class="card">
	    			<p class="card-text">問題:<br> ${resultJson[0].question}</p>
                    <p class="card-text">建議:<br> ${resultJson[0].answer}</p>
                    來源網址:<br><a href="${resultJson[0].answerWeb}" target="_blank">${resultJson[0].answerWeb}</a>
	           </div>`;
	    Words.innerHTML = Words.innerHTML + out;
	}
}
} 

function displayChatbot(){
	const chatbot = document.getElementById('talk-con-id');
	const chatbotBtn = document.getElementsByClassName('chatbot-btn')[0];
	if(chatbot.getAttribute('style')=='display: none;'){
		chatbot.setAttribute('style', 'display: initial;');
		chatbotBtn.innerHTML = `<i class="fas fa-times close-icon"></i>`;
	}
	else {
		chatbot.setAttribute('style', 'display: none;');
		chatbotBtn.innerHTML = `<i class="far fa-comment-dots show-icon"></i>`;
	}
	
	// chatbot.setAttribute('style', 'display: initial;');
}

window.onload = function(){  
	Words = document.getElementById("words"); 
	TalkWords = document.getElementById("talkwords");
	TalkSub = document.getElementById("talksub"); 
    TalkSub.onclick = function(){ 
		chatRoom();			
    }   
}