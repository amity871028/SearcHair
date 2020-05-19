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
            console.log("i touchStart!");

        }, _touchMove: function (event) {
            moveFlag = 1;
            this._modifyEvent(event);
            this._mouseMove(event);

            //--------------------touchMove do something--------------------
            console.log("i touchMove!");

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
            console.log("i touchEnd!");

        }, _modifyEvent: function (event) {
            event.which = 1;
            var target = event.originalEvent.targetTouches[0];
            event.pageX = target.clientX;
            event.pageY = target.clientY;
        }
    });
})(jQuery);

// to resolve that frame overing edited-photo lead to cannot drag edited-photo
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

//to storage in localStorage
function storage(){
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

    document.getElementById('ok-btn').addEventListener('click', storage);
}

window.addEventListener('load', init);
window.scrollTo(0, 0);