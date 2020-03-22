var month_name = ["January","Febrary","March","April","May","June","July","Auguest","September","October","November","December"];

var my_date = new Date();
var my_year = my_date.getFullYear();
var my_month = my_date.getMonth();

function refreshDate(){
    var ctitle = document.getElementById("calendar-title");
    var cyear = document.getElementById("calendar-year");
	
	ctitle.innerHTML = month_name[my_month]; //设置英文月份显示
	cyear.innerHTML = my_year; //设置年份显示
}

function init() {
    refreshDate();
    var prev = document.getElementById("prev");
    var next = document.getElementById("next");
    
    prev.addEventListener('click', function(){
        my_month--;
        if(my_month<0){
            my_year--;
            my_month = 11;
        }
        refreshDate();
    });
    next.addEventListener('click', function(){
        my_month++;
        if(my_month>11){
            my_year++;
            my_month = 0;
        }
        refreshDate();
    });
}

window.addEventListener('load', init);