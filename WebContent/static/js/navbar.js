var header = '<!--Navbar--> \
<nav class="navbar navbar-expand-md fixed-top"> \
    <!-- Navbar brand --> \
    <a class="navbar-brand" href="index.html">SearcHair</a> \
 \
    <!-- Collapse button --> \
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#basicExampleNav" \
      aria-controls="basicExampleNav" aria-expanded="false" aria-label="Toggle navigation"> \
      <span class="navbar-toggler-icon"></span> \
    </button> \
    <!-- Collapsible content --> \
    <div class="collapse navbar-collapse" id="basicExampleNav"> \
   \
    <!-- Links --> \
    <ul class="navbar-nav mr-auto"> \
        <li class="nav-item dropdown"> \
            <a class="nav-link dropdown-toggle active" href="#"  id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">搜尋</a> \
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink"> \
                <a class="dropdown-item" href="search-salon.html">店家</a> \
                <a class="dropdown-item" href="search-stylist.html">設計師</a> \
                <a class="dropdown-item" href="search-stylist-work.html">髮型</a> \
            </div> \
        </li> \
        <li class="nav-item"> \
            <a class="nav-link" href="recommend-product.html">推薦商品</a> \
        </li> \
        <li class="nav-item"> \
            <a class="nav-link" href="hair-match.html">Hair Match</a> \
        </li> \
        <!--<li class="nav-item"> \
            <a class="nav-link" href="map.html">美髮地圖</a> \
        </li>--> \
        <li class="nav-item"> \
            <a class="nav-link" href="chatroom.html">聊天室</a> \
        </li> \
    </ul> \
     \
    <ul class="navbar-nav ml-auto">';
if(localStorage.getItem("account")){
	
	header += `<li class="nav-item dropdown"> \
		<a class="nav-link dropdown-toggle active" href="#"  id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><!--<img src="#" id="user-photo">--> <span id="user-name">${localStorage.getItem("name")}</span></a> \
        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink"> \
	            <a class="dropdown-item" href="calendar.html">月曆</a> \
	            <a class="dropdown-item" href="favorite.html">我的最愛</a> \
				<a class="dropdown-item" href="push.html">推播通知</a> \
	            <a class="dropdown-item" href="setting.html">設定</a> \
	        </div> \
    	</li> \
		\
		<li class="nav-item active"> \
	        <a class="nav-link" href="#" id="logout" onclick="logout();">登出</a> \
	    </li> \
	</ul> \
</nav> \
<!--/.Navbar-->` ;
		
 }
    else{
    	header+='<li class="nav-item active"> \
            <a class="nav-link" href="#login-modal" data-toggle="modal">登入</a> \
        </li> \
        <li class="nav-item active"> \
            <a class="nav-link" href="#register-modal" data-toggle="modal">註冊</a> \
        </li> \
    </ul> \
</nav> \
<!--/.Navbar-->';
    }
document.write(header);
document.write('<script src="./static/js/login.js"></script>');
document.write('<script src="./static/js/register.js"></script>');