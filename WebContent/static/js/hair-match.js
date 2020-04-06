function drag(){
  $("#face-frame").bind("click", function (event) { 
    proxy(event); 
   }); 

   $("#face-frame").bind("mousedown", function (event) { 
    proxy(event); 
   }); 

   $("#face-frame").bind("mouseup", function (event) { 
    proxy(event); 
   }); 

   $("#face-frame").bind("mousemove", function (event) { 
    proxy(event); 
   }); 
  $("#edited-photo").draggable({ obstacle:".obstacle", preventCollision: true });
}

function proxy(event) { 
  $("#edited-photo").trigger(event); 
} 

function zoom(func){
  var editedPhoto = document.getElementById('edited-photo');
  let afterWidth;
  if(func == 'plus') afterWidth = editedPhoto.width + 20;
  else afterWidth = editedPhoto.width - 20;
  editedPhoto.width = afterWidth;
}

// let picture can show instantly
function readURL(input) {
    if (input.files && input.files[0]) {
      const reader = new FileReader();
  
      reader.onload = function loadPicture(e) {
        document.getElementById('edited-photo').setAttribute('src', e.target.result);
      };
  
      reader.readAsDataURL(input.files[0]);
    }
}

function save(){
	var frame = document.getElementById("frame");
	document.getElementById('face-frame').src = "";
	html2canvas(document.getElementById("frame")).then(canvas => {
		canvas.setAttribute('crossOrigin', "anonymous");
		localStorage.setItem('user-img',canvas.toDataURL("./images"));
		window.location.href = "edit-photo.html";
  });
}

function init(){
	localStorage.removeItem('user-img');
    document.getElementById('face-align').setAttribute('style', 'display: none;');
    document.getElementById('upload-photo').addEventListener('change',  function () {
        document.getElementById('content').setAttribute('style', 'display: none;');
        document.getElementById('face-align').setAttribute('style', '');
        readURL(this);
    });
    drag();
    document.getElementById('plus-btn').addEventListener('click', function(){zoom('plus')});
    document.getElementById('minus-btn').addEventListener('click', function(){zoom('minus')});

    document.getElementById('ok-btn').addEventListener('click', save);
}

window.addEventListener('load', init);
window.scrollTo(0, 0);