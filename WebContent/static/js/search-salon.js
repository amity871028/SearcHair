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

    $('.favorite').mouseover(function(){
        console.log(this.src);
        var a = this.src;
        this.src = "../static/img/favorite.png";
    });
    $('.favorite').mouseout(function(){
        console.log(this.src);
        var a = this.src;
        this.src = "../static/img/favorite_undo.png";
    });
});