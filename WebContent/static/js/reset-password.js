/* global FetchData */
const resetPasswordAPI = 'api-user-password-reset';


function delayURL(url, time) {
  setTimeout(() => { window.location.href = `${url}`; }, time);
}

function validateNewPassword() {
  const confirmPassword = document.getElementById('confirm-new-password').value;
  const password = document.getElementById('new-password').value;
  if (confirmPassword !== password) {
    document.getElementById('confirm-new-password').setCustomValidity('無法和密碼匹配');
  } else {
    document.getElementById('confirm-new-password').setCustomValidity('');
  }
}

async function resetPassword() {
  // validate field and show hint
  if (document.forms['reset-password-form'].reportValidity()) {
    // start post
    const result = await FetchData.post(resetPasswordAPI, {
      password: document.getElementById('new-password').value,
    });
    if (result.status === 401) {
      // show wrong msg
      document.getElementById('reset-password-form').innerHTML = '';
      document.getElementById('txt').innerText = '網頁已過期，自動跳轉主頁面。';
      delayURL('./index.html', 1800);
    } else {
      // show successful msg
      document.getElementById('reset-password-form').innerHTML = '';
      document.getElementById('txt').innerText = '重設密碼成功！移轉到主頁面。';
      delayURL('./index.html', 1800);
    }
  }
}


function clickOnEnter(event) {
  if (event.keyCode === 13) {
    event.preventDefault();
    document.getElementById('reset-password-btn').click();
  }
}

function checkToken(){
	var decodedCookie = decodeURIComponent(document.cookie);
	var cookieList = decodedCookie.split(';');
	for(var i in cookieList){
		if(cookieList[i].match("token=")) return;
	}
	console.log("denied!");
	//denied!
}

function init() {
  checkToken();
  // add event listener
  document.getElementById('reset-password-btn').addEventListener('click', resetPassword);
  // validate password when password or confirm password change
  document.getElementById('new-password').addEventListener('keyup', validateNewPassword);
  document.getElementById('confirm-new-password').addEventListener('keyup', validateNewPassword);
  document.getElementById('confirm-new-password').addEventListener('keyup', clickOnEnter);
}

window.addEventListener('load', init);
