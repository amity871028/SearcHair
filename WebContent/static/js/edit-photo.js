
const TYPE_DATA = {}; // {type1:{"id":[], "category":"item"}}

const hairMatchAPI = {
	hairstyleAPI: 'getAllhairstyle',
	hairColorAPI: 'hairColor'
};

/*async function getAllHairstyle(){
	const result = await FetchData.get(hairMatchAPI.hairstyleAPI);
    const allHairstyle = await result.json();
    
    let totalType = 0;
    allHairstyle.forEach((type, idx) => {
      TYPE_DATA[`type${idx + 1}`] = { id: [], category: type.category };
      type.content.forEach((i) => {
        TYPE_DATA[`type${idx + 1}`].id.push(i._id);
        ID_TO_NAME[i._id] = i.name;
      });
      totalType = idx + 1;
    });

}*/

function displayPhoto(uri){
	document.getElementById('stored-img').src = uri;
}

function downloadIamge(){
	var frame = document.getElementById("frame");
	document.getElementById('face-frame').src = "";
	html2canvas(document.getElementById("frame")).then(canvas => {
		canvas.setAttribute('crossOrigin', "anonymous");
		let uri = canvas.toDataURL("./images");
		saveFile(uri);
		displayPhoto(uri);
	});
	document.getElementById('face-frame').src = "./static//img/frame.png";
	localStorage.removeItem('user-img');
}

function saveFile(data){
	var save_link = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
	   save_link.href = data;
	   save_link.download = 'photo';
	  
	   var event = document.createEvent('MouseEvents');
	   event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	   save_link.dispatchEvent(event);
}

function addToPhoto(){
	var hairstyle = document.createElement('img');
	hairstyle.src="./static/img/test髮型2.png";
	hairstyle.setAttribute('class', 'hairstylePos');
	hairstyle.setAttribute('id', 'hairstylePos');
	document.getElementById('frame').appendChild(hairstyle);
	$("#hairstylePos").draggable();
}

function testHairstyleAdd(){
	document.getElementById('test').addEventListener('click', addToPhoto);
}

	
function zoom(func){
  var editedHairstyle = document.getElementsByClassName('hairstylePos')[0];
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
    //getAllHairstyle();
    document.getElementById('user-edited-photo').src = localStorage.getItem('user-img');

    document.getElementById('plus-btn').addEventListener('click', function(){zoom('plus')});
    document.getElementById('minus-btn').addEventListener('click', function(){zoom('minus')});
    
    var store_btn = document.getElementById('store-photo');
    store_btn.addEventListener('click', downloadIamge);

    $('#colorpicker').farbtastic('#color_name');
    
    testHairstyleAdd();
}

window.addEventListener('load', init);