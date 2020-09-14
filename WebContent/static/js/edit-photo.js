const hairMatchAPI = {
	hairstyleAPI: 'api-hairMatch?func=hairstyle',
	hairColorAPI: 'api-hairMatch?func=hairColor',
	hairAPI: 'api-hairMatch'
	
};

//draggable to mobile
var moveFlag = 0;
// /iPad|iPhone|Android/.test( navigator.userAgent ) &&
(function ($) {
    var proto = $.ui.mouse.prototype, _mouseInit = proto._mouseInit;
    $.extend(proto, {
        _mouseInit: function () {
            this.element.bind("touchstart." + this.widgetName, $.proxy(this, "_touchStart"));
            _mouseInit.apply(this, arguments);
        }, _touchStart: function (event) {
            this.element.bind("touchmove." + this.widgetName, $.proxy(this, "_touchMove")).bind("touchend." + this.widgetName, $.proxy(this, "_touchEnd"));
            this._modifyEvent(event);
            $(document).trigger($.Event("mouseup"));
            //reset mouseHandled flag in ui.mouse
            this._mouseDown(event);

            //--------------------touchStart do something--------------------

        }, _touchMove: function (event) {
            moveFlag = 1;
            this._modifyEvent(event);
            this._mouseMove(event);

            //--------------------touchMove do something--------------------

        }, _touchEnd: function (event) {
            if (moveFlag == 0) {
                var evt = document.createEvent('Event');
                evt.initEvent('click', true, true);
                this.handleElement[0].dispatchEvent(evt);
            }
            this.element.unbind("touchmove." + this.widgetName).unbind("touchend." + this.widgetName);
            this._mouseUp(event);
            moveFlag = 0;

            //--------------------touchEnd do something--------------------

        }, _modifyEvent: function (event) {
            event.which = 1;
            var target = event.originalEvent.targetTouches[0];
            event.pageX = target.clientX;
            event.pageY = target.clientY;
        }
    });
})(jQuery);


async function getHairstyle(type){
	const result = await FetchData.get(`${hairMatchAPI.hairstyleAPI}&type=${type}`);
    const allHairstyle = await result.json();
    window.urlPrefix = allHairstyle.urlPrefix;
    console.log(window.urlPrefix);
    const hairstyleImg = document.getElementById('hairstyle-img');
    hairstyleImg.innerHTML = "";
    let tmp = "";
    for(i in allHairstyle.photos){
    	let hairstyleId = allHairstyle.photos[i].split('.')[0];
    	tmp += `<a href="#" id="${hairstyleId}" onclick="addToPhoto(this)">
    			<div class="hairstyle-div">
    				<img src="${urlPrefix}/${allHairstyle.photos[i]}">
    				<a href="#" data-toggle="modal" class="detail-img-link" onclick="showDetailModal('${hairstyleId}', '${type}')">
    					<img src="img/hairstyle_detail.png" class="detail-img">
    				</a>
    	</div></a>`;
    }
    hairstyleImg.innerHTML = tmp;
}

function showDetailModal(hairstyle, type){
	window.randomPhoto = `${hairstyle}.png`;
	window.randomType = type;
	randomGetPhoto();
	$('#hairstyle-detail-modal').modal('show');
}

async function randomGetPhoto(){
	const result = await FetchData.post(`${hairMatchAPI.hairAPI}`,{
		func: "userPhoto",
		hairstyle: window.randomPhoto,
	});
	const resultJson = await result.json();
	if(resultJson != null){
		let userName
		if(resultJson.userName != "null") userName = `使用者${resultJson.userName}`;
		else userName = "匿名者";
		document.getElementById('random-user-photo').src = resultJson.url; 
		document.getElementById('thanks-content').innerHTML = `感謝${userName}分享！`;
		window.randomColor = resultJson.color;
	}
	else {
		document.getElementById('random-user-photo').src = 'img/blank.png'; 
		document.getElementById('thanks-content').innerHTML = "哦喔，還沒有人分享照片，趕快去當第一個提供者吧！";
		window.randomColor = -1;
	};
}

function applyPhoto(){
	//addToPhoto();
	//delete origin hairstyle photo
	if(document.getElementById('hairstylePos')){
		const addedPhoto = document.getElementById('hairstylePos');
		addedPhoto.parentNode.removeChild(addedPhoto);
	}
	const urlPrefixSplit = window.urlPrefix.split('/');
    window.folder = window.randomType;
    window.picture = window.randomPhoto;
    window.color = window.randomColor;
	var hairstyle = document.createElement('img');
	hairstyle.src=`${window.urlPrefix}/${window.picture}`;
	
	// to resolve that imgs' size are different  
	if(window.picture.match("long")){
		hairstyle.setAttribute('class', 'hairstyleLongPos');
	}
	else if(window.picture.match("short")){
		hairstyle.setAttribute('class', 'hairstyleShortPos');
	}
	else {
		hairstyle.setAttribute('class', 'hairstyleBoyPos');
	}
	
	hairstyle.setAttribute('id', 'hairstylePos');
	document.getElementById('frame').appendChild(hairstyle);
	$("#hairstylePos").draggable();
	
	if(color != -1){
		document.getElementById('color-value').value = color;
		changeHairColor();
	}

	$('#hairstyle-detail-modal').modal('hide');
}

async function changeHairColor(device){
	let color = "";
	if(device == "pc") color = document.getElementById('color_name').value.replace('#', '');
	else color = document.getElementById('color-value').value.replace('#', '');
    const addedHair = document.getElementById('hairstylePos');
    const folder = window.folder;
    const picture = window.picture;
    
    //check whether user selects hairstyle
    if(!folder || (picture == -1)){
    	alert("請先選擇髮型！");
    }
    else{
		const result = await FetchData.get(`${hairMatchAPI.hairColorAPI}&userName=a&folder=${folder}&picture=${picture}&color=${color}`);
	    const newColor = await result.json();
    	addedHair.src = newColor.url;	   
	    window.color= `#${color}`;
    }
}

function displayPhoto(uri){
	document.getElementById('stored-img').src = uri;
}

function translateBase64ImgToFile(base64,filename,contentType){ // base64轉file
    var arr = base64.split(',');
    var bstr = atob(arr[1]);
    var leng = bstr.length;
    var u8arr = new Uint8Array(leng);
    while(leng--){
       u8arr[leng] =  bstr.charCodeAt(leng);
    }
    return new File([u8arr],filename,{type:contentType});
}

async function downloadIamge(){
	var frame = document.getElementById("frame");
	document.getElementById('face-frame').src = "";
	let canvas = await html2canvas(document.getElementById("frame"));
	canvas.setAttribute('crossOrigin', "anonymous");
	let url = canvas.toDataURL("./images");
	saveFile(url);
	displayPhoto(url);
	window.url = url;

	// to prevent facebook's link haven't generated yet 
	document.getElementById('loadingDiv').style.display = 'block';
    document.getElementById('loadingImg').style.display = 'block';
    
	var file = translateBase64ImgToFile(url,"testImg.png","image/png")
	var CLOUDINARY_URL = 'https://api.cloudinary.com/v1_1/dszvkufl8/upload';
	var CLOUDINARY_UPLOAD_PRESET = 'z62aocfi';
	var formData = new FormData();
    formData.append('file', file);
    formData.append('upload_preset', CLOUDINARY_UPLOAD_PRESET);
    let res = await axios({
        url: CLOUDINARY_URL,
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: formData
    });
    window.imgur = res.data.secure_url; //imgur's url
    console.log(window.imgur);
    document.getElementById('fb-link').href = `https://www.facebook.com/sharer/sharer.php?u=${window.imgur}`;;
	document.getElementById('face-frame').src = "img/frame.png";
	localStorage.removeItem('user-img');
	
	document.getElementById('loadingDiv').style.display = 'none';
    document.getElementById('loadingImg').style.display = 'none';
}

function saveFile(data){
	var save_link = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
	   save_link.href = data;
	   save_link.download = 'photo';
	  
	   var event = document.createEvent('MouseEvents');
	   event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	   save_link.dispatchEvent(event);
}

function addToPhoto(e){
	//delete origin hairstyle photo
	if(document.getElementById('hairstylePos')){
		const addedPhoto = document.getElementById('hairstylePos');
		addedPhoto.parentNode.removeChild(addedPhoto);
	}
	const urlPrefixSplit = window.urlPrefix.split('/');
    window.folder = urlPrefixSplit[urlPrefixSplit.length - 1];
    window.picture = `${e.id}.png`;
    window.color = -1;
	var hairstyle = document.createElement('img');
	hairstyle.src=`${window.urlPrefix}/${e.id}.png`;
	// to resolve that imgs' size are different  
	if(e.id.match("long")){
		hairstyle.setAttribute('class', 'hairstyleLongPos');
	}
	else if(e.id.match("short")){
		hairstyle.setAttribute('class', 'hairstyleShortPos');
	}
	else {
		hairstyle.setAttribute('class', 'hairstyleBoyPos');
	}
	
	hairstyle.setAttribute('id', 'hairstylePos');
	document.getElementById('frame').appendChild(hairstyle);
	$("#hairstylePos").draggable();
}
	
function zoom(func){
  var editedHairstyle = document.getElementById('hairstylePos');
  let afterWidth;
  if(func == 'plus') afterWidth = editedHairstyle.width + 15;
  else afterWidth = editedHairstyle.width - 15;
  editedHairstyle.setAttribute('style', 'width:' + afterWidth);
}

function sidebarSetting(){
    document.getElementById('dismiss').addEventListener('click', function(){
        document.getElementById('sidebar').classList.remove('active');
    });
    document.getElementById('sidebarCollapse').addEventListener('click', function(){
        document.getElementById('sidebar').classList.add('active');
    });
}

async function sharePhoto(){
	let ans;
	$("input[type=radio]:checked").each(function () {
		  ans = $(this).val() ;
		});
	if(ans == "yes"){
		let userName;
		if(localStorage.getItem('name') != null) userName = localStorage.getItem('name');
		else userName = "null";
		const result = await FetchData.post(`${hairMatchAPI.hairAPI}`, {
			func: "share",
			userName: userName,
			hairstyle: window.picture,
			color: window.color,
			url: window.imgur,
		});
		if(result.status === 200 || result.status === 409){
			document.getElementById('share-msg').innerHTML = "謝謝你！";
		}

	}
	else {
		document.getElementById('share-msg').innerHTML = "";
	}
}

function shareToChatroom(){
	localStorage.setItem('picture', window.imgur);
	window.location.href = "chatroom.html";
}

function addToFavorite(){
	console.log("!");
	$('#add-favorite-modal').modal('show');
}

function init(){
	window.picture = -1;
	window.color = -1;
    sidebarSetting();
    getHairstyle('girl-long'); //initial hairstyle type
    document.getElementById('user-edited-photo').src = localStorage.getItem('user-img');
	if(localStorage.getItem('hairstyle')){
		window.randomPhoto = localStorage.getItem('hairstyle');
		window.urlPrefix = "http://localhost:8080/SearcHair/img/hair-match/hairstyle-source/girl-long";
		window.randomColor = -1;
		applyPhoto();
	}

    document.getElementById('plus-btn').addEventListener('click', function(){zoom('plus')});
    document.getElementById('minus-btn').addEventListener('click', function(){zoom('minus')});
    
    document.getElementById('hairstyle-type').addEventListener('click', function(){
    	let type = this.options[this.selectedIndex].id;
    	getHairstyle(type);
    });
    
    document.getElementById('random-btn').addEventListener('click', randomGetPhoto);
    const storeBtn = document.getElementById('store-photo');
    storeBtn.addEventListener('click', downloadIamge);
    
    document.getElementById('share-btn').addEventListener('click', sharePhoto);
    document.getElementById('apply-btn').addEventListener('click', applyPhoto);

    document.getElementById('share-chatroom-btn').addEventListener('click', shareToChatroom);
    document.getElementById('add-favorite-btn').addEventListener('click', addToFavorite);
    
    
    $('#colorpicker').farbtastic('#color_name');
    document.getElementById('colorpicker').addEventListener('click', function(){changeHairColor("pc");});
    document.getElementById('color-value').addEventListener('change', function(){changeHairColor("mobile");});
}

window.addEventListener('load', init);