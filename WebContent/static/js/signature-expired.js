
function delayURL(url, time) {
  setTimeout(() => { window.location.href = `${url}`; }, time);
}

function init() {
  delayURL('/menu', 1800);
}

window.addEventListener('load', init);
