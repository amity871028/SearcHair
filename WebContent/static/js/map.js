
function delayURL(url, time) {
  setTimeout(() => { window.location.href = `${url}`; }, time);
}


alert("尚未動工");
delayURL('./index.html', 200);