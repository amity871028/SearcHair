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
	return arr;
}

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
 if(input.length < 2){
	 botMessage = '<div class="atalk"><span id="asay">請問您是要找設計師還是店家呢？</span></div>';
	Words.innerHTML += botMessage;
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
		const number = randomNumber(cardLength);
		for (let i = 0; i < cardLength; i += 1) {
		    out = `<div class="card">
		                  <div class="card-body">
		                    <h5 class="card-title"><b>${resultJson[number[i]].name}</b></h5>
		                    <p class="card-text">地址: ${resultJson[number[i]].address}</p>
		                    <p class="card-text">電話: ${resultJson[number[i]].phone}</p>
		                    <p class="card-text">營業時間: ${resultJson[number[i]].businessTime}</p>
		                    <img src="${resultJson[number[i]].picture}">
		                
		                  </div>`;
		    Words.innerHTML = Words.innerHTML + out;
		 }
		if(cardLength%2!=0){
			for(let i = 0; i < 24; i+=1){
				Words.innerHTML += `<br>`;
			}
		}
	}
	else {
		const number = randomNumber(cardLength);
		for(let i = 0; i < cardLength; i+= 1) {
		    out = `<div class="card">
		                  <div class="card-body">
		                    <h5 class="card-title"><b>${resultJson[number[i]].name}</b></h5>
		                    <p class="card-text">職業名稱: ${resultJson[number[i]].jobTitle}</p>
		                    <p class="card-text">所屬店家: ${resultJson[number[i]].salon}</p>
		                    <p class="card-text">店家地址: ${resultJson[number[i]].address}</p>
		                    <img src="${resultJson[number[i]].picture}">
		                  </div>`;
		    Words.innerHTML = Words.innerHTML + out;
		}
		if(cardLength%2!=0){
			for(let i = 0; i < 24; i+=1){
				Words.innerHTML += `<br>`;
			}
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