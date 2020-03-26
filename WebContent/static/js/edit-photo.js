function downloadIamge(selector, name) {
    // 通過選擇器獲取img元素
    var img = document.querySelector(selector);
    // 將圖片的src屬性作為URL地址
    var url = img.src;
    var a = document.createElement('a');
    var event = new MouseEvent('click');
    
    a.download = name || '下載圖片名稱';
    a.href = url;
    
    a.dispatchEvent(event);
}


function init(){
    var store_btn = document.getElementById('store-photo');
    store_btn.addEventListener('click', downloadIamge(this, '../static/img/test_photo2.png'));
}

window.addEventListener('load', init);

$(document).ready(function () {
    $('#dismiss, .overlay').on('click', function () {
        $('#sidebar').removeClass('active');
        $('.overlay').removeClass('active');
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').addClass('active');
        $('.overlay').addClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });
});