var Words;
var TalkWords;
var TalkSub;

async function InputPress() {
	if (event.keyCode == 13) { 
		chatRoom();
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
		if (TalkWords.value == "hi" || TalkWords.value == "hello") {
			botMessage = '<div class="atalk"><span id="asay">'+ TalkWords.value +'</span></div>';
			Words.innerHTML += botMessage;
			
		} else {
			toChat2();
		}
	} 
}

async function toChat2(){
 var cardLength = 6;
 var url = "http://localhost:8080/SearcHair/ChatbotServlet"; 
 var input = document.getElementById('talkwords').value.split(" ");
 if(input.length != 2){
	 botMessage = '<div class="atalk"><span id="asay">請問您是要找設計師還是店家呢？</span></div>';
	Words.innerHTML += botMessage;
	return;
}
 const result = await
FetchData.post(url, {
	func : input[0],
	keyword : input[1]
});

const resultJson = await result.json();
if(resultJson.length == 0) {
	botMessage = '<div class="atalk"><span id="asay">找不到相關資訊</span></div>';
	Words.innerHTML += botMessage;
}
else {
	botMessage = '<div class="atalk"><span id="asay">以下是我找到的資訊</span></div>';
	Words.innerHTML += botMessage;
	let out = null;
	if(resultJson.length < 5) {
		cardLength = resultJson.length;
	}
	if(resultJson[0].func == "salon") {
		for (let i = 0; i < cardLength; i += 1) {
			let count = Math.floor(Math.random()*(resultJson.length));
		    out = `<div class="card">
		                  <div class="card-body">
		                    <h5 class="card-title"><b>${resultJson[count].name}</b></h5>
		                    <p class="card-text">地址: ${resultJson[count].address}</p>
		                    <p class="card-text">電話: ${resultJson[count].phone}</p>
		                    <p class="card-text">營業時間: ${resultJson[count].businessTime}</p>
		                    <img src="${resultJson[count].picture}">
		                
		                  </div>`;
		    Words.innerHTML = Words.innerHTML + out;
		 }
	}
	else {
		for(let i = 0; i < cardLength; i+= 1) {
			let count = Math.floor(Math.random()*(resultJson.length));
		    out = `<div class="card">
		                  <div class="card-body">
		                    <h5 class="card-title"><b>${resultJson[count].name}</b></h5>
		                    <p class="card-text">職業名稱: ${resultJson[count].jobTitle}</p>
		                    <p class="card-text">所屬店家: ${resultJson[count].salon}</p>
		                    <p class="card-text">店家地址: ${resultJson[count].address}</p>
		                    <img src="${resultJson[count].picture}">
		                  </div>`;
		    Words.innerHTML = Words.innerHTML + out;
		}
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