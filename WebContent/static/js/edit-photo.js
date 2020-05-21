const hairMatchAPI = {
	hairstyleAPI: 'api-hairMatch?func=hairstyle',
	hairColorAPI: 'api-hairMatch?func=hairColor',
	hairstoreAPI: 'api-hairMatch'
	
};

async function getHairstyle(type){
	const result = await FetchData.get(`${hairMatchAPI.hairstyleAPI}&type=${type}`);
    const allHairstyle = await result.json();
    window.urlPrefix = allHairstyle.urlPrefix;
    const hairstyleImg = document.getElementById('hairstyle-img');
    hairstyleImg.innerHTML = "";
    let tmp = "";
    for(i in allHairstyle.photos){
    	let hairstyleId = allHairstyle.photos[i].split('.')[0];
    	tmp += `<a href="#" id="${hairstyleId}" onclick="addToPhoto(this)"><img src="${urlPrefix}/${allHairstyle.photos[i]}"></a>`;
    }
    hairstyleImg.innerHTML = tmp;
}

async function changeHairColor(device){
	let color = "";
	if(device == "pc") color = document.getElementById('color_name').value.replace('#', '');
	else color = document.getElementById('color-value').value.replace('#', '');
    const addedHair = document.getElementById('hairstylePos');
    const folder = window.folder;
    const picture = window.picture;
    
    //check whether user selects hairstyle
    if(!folder || !picture){
    	alert("請先選擇髮型！");
    }
    else{
		const result = await FetchData.get(`${hairMatchAPI.hairColorAPI}&userName=a&folder=${folder}&picture=${picture}&color=${color}`);
	    const newColor = await result.json();
    	addedHair.src = newColor.url;	   
	    
    }
}

function displayPhoto(uri){
	document.getElementById('stored-img').src = uri;
}

async function downloadIamge(){
	var frame = document.getElementById("frame");
	document.getElementById('face-frame').src = "";
	let canvas = await html2canvas(document.getElementById("frame"));
	canvas.setAttribute('crossOrigin', "anonymous");
	let uri = canvas.toDataURL("./images");
	saveFile(uri);
	displayPhoto(uri);
	console.log(uri);

	// to prevent facebook's link haven't generated yet 
    document.getElementById('loadingDiv').style.display = 'block';
    document.getElementById('loadingImg').style.display = 'block';
    
	const result = await FetchData.post(`${hairMatchAPI.hairstoreAPI}`,{
		img: uri,
	});
	const resultJson = await result.json();
	console.log(resultJson.url);
	document.getElementById('fb-link').href = `https://www.facebook.com/sharer/sharer.php?u=${resultJson.url}`;;
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

	var hairstyle = document.createElement('img');
	hairstyle.src=`${urlPrefix}/${e.id}.png`;
	
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
  if(func == 'plus') afterWidth = editedHairstyle.width + 20;
  else afterWidth = editedHairstyle.width - 20;
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

/*function favoriteSetting(){
    var favorite_list = document.getElementsByClassName('favorite');
    for(let i = 0; i < favorite_list.length; i++){
        favorite_list[i].addEventListener('mouseover', function(){
            this.src = "../static/img/favorite.png";
        });
        favorite_list[i].addEventListener('mouseout', function(){
            this.src = "../static/img/favorite_undo.png";
        });
    }
}*/

function init(){
    sidebarSetting();
    //favoriteSetting();
    getHairstyle('girl-long'); //initial hairstyle type
    document.getElementById('user-edited-photo').src = localStorage.getItem('user-img');

    document.getElementById('plus-btn').addEventListener('click', function(){zoom('plus')});
    document.getElementById('minus-btn').addEventListener('click', function(){zoom('minus')});
    
    document.getElementById('hairstyle-type').addEventListener('click', function(){
    	let type = this.options[this.selectedIndex].id;
    	getHairstyle(type);
    });
    
    var store_btn = document.getElementById('store-photo');
    store_btn.addEventListener('click', downloadIamge);
    
    $('#colorpicker').farbtastic('#color_name');
    document.getElementById('colorpicker').addEventListener('click', function(){changeHairColor("pc");});
    document.getElementById('color-value').addEventListener('change', function(){changeHairColor("mobile");});
}

window.addEventListener('load', init);