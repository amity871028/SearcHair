var header = '<a href="javascript:void(0);" onclick="displayChatbot(this);"><div class="chatbot-btn"><i class="far fa-comment-dots show-icon"></i></div></a> \
        <div class="talk-con" id="talk-con-id" style="display: none;"> \
	        <div class="talk-show" id="words"> \
	            <div class="atalk"> \
	            	<img class="chatbot-icon" src="img/chatbot.jpg"> \
	            	<span class="asay">嘿！我是魔髮小聊ヽ( ´∀`)ﾉ</span> \
	            </div> \
	        </div> \
	        <div class="talk-input"  id="talk-input-id"> \
	            <input type="text" class="talk-word" id="talkwords" placeholder = "請輸入訊息" onkeypress="inputPress()"> \
	            <input type="button" value="發送" class="talk-sub" id="talksub"> \
	        </div> \
	    </div>';
document.write(header);