const settingAPI = {
	password: 'api-user-password-update',
	hairAnaysis: 'api-setting',
	remind: 'api-user-remind'
};
const PASSWORD = ['old-password', 'new-password', 'confirm-new-password'];
const account = localStorage.getItem('account');
const hairAnalysis = {
		oily: "油性髮質",
		dry: "乾性髮質",
		normal: "中性髮質",
		combination: "混和性髮質",
		damaged: "受損髮質"
}

/* ----check the fields of the password---- */
function validatePassword() {
  const newPassword = document.getElementById('new-password');
  const comfirmNewPassword = document.getElementById('confirm-new-password');
  /* --validate all password-- */
  PASSWORD.forEach((element) => {
    if (document.getElementById(element).value === '') {
      document.getElementById(element).setCustomValidity('需填寫');
    } else {
      document.getElementById(element).setCustomValidity('');
    }
  });

  if ((newPassword.value).length < 7) {
    newPassword.setCustomValidity('密碼需設長度大於等於8個字母長度');
  } else if (newPassword.value !== comfirmNewPassword.value) {
    comfirmNewPassword.setCustomValidity('無法和密碼匹配');
  } else {
    comfirmNewPassword.setCustomValidity('');
  }
}

/* ----update the password---- */
async function updatePassword() {
  validatePassword();
  document.getElementById('update-password-wrong').innerHTML = '';
  if (document.forms['update-password-form'].reportValidity()) {
    const newPassword = document.getElementById('new-password').value;
    if (newPassword.length >= 8) {
      const result = await FetchData.post(settingAPI.password, {
    	account: account,
        oldPassword: document.getElementById('old-password').value,
        newPassword: newPassword,
      });
      if (result.status === 401) {
        document.getElementById('update-password-wrong').innerHTML = '舊密碼錯誤或是未登入。';
      } else if (result.status === 200) {
        $('#update-password-modal').modal('hide');
        $('#update-success-modal').modal('show');
        PASSWORD.forEach((element) => { // clear password field
          document.getElementById(element).value = '';
        });
      }
    }
  }
}

async function updateRemindFrequency(){
	const frequency = document.getElementById('day');
	const noticeSwitch = document.getElementById('notice-switch');
	if(noticeSwitch.checked == true && frequency.value == ""){
		alert("請輸入天數！");
	}
	else if(frequency.value <= 0){
		alert("天數輸入不對！請重新輸入");
	}
	else {
		let remindFrequency = 0;
		if(noticeSwitch.checked == false) remindFrequency = -1;
		else remindFrequency = frequency.value;
		const result = await FetchData.post(settingAPI.remind, {
			account: account,
	    	remindFrequency: parseInt(remindFrequency),
		});
		if(result.status === 200) alert('更新成功！');
		else alert('請再試一次！');
	}
}

async function updateHairAnalysis(){
	if (document.forms['hair-analysis-form'].reportValidity()) {
		const result = await FetchData.post(settingAPI.hairAnaysis, {
			account: account,
			greasy: $('input:radio[name="greasy"]:checked')[0].value === "true",
			frizzy: $('input:radio[name="frizzy"]:checked')[0].value === "true",
			sleek: $('input:radio[name="sleek"]:checked')[0].value === "true",
			tangled: $('input:radio[name="tangled"]:checked')[0].value === "true",
			perm: $('input:radio[name="perm"]:checked')[0].value === "true"
		});
		const json = await result.json();
		if(result.status == 200){
			localStorage.setItem('hair-analysis', json.hairAnalysis);
			updateHairResult();
		}
	}
}

function updateHairResult(){

	document.getElementById('result').setAttribute('style', 'display: initial;');
	document.getElementById('hair-analysis-form').setAttribute('style', 'display: none;');
	
	let hair = hairAnalysis[localStorage.getItem('hair-analysis')];
	document.getElementById('hair-result').innerHTML = hair;
}

function sidebarSetting(){
    document.getElementById('dismiss').addEventListener('click', function(){
        document.getElementById('sidebar').classList.remove('active');
    });
    document.getElementById('sidebarCollapse').addEventListener('click', function(){
        document.getElementById('sidebar').classList.add('active');
    });
    $('#myList a').on('click', function () {
        document.getElementsByClassName('btn btn-outline-primary active')[0].classList.remove('active');
    })
}

function init(){
	// to block illegal users
	if(account == null){
		alert("無法瀏覽此頁面！請登入後再查看！");
	    delayURL('./index.html', 200);
	}
    sidebarSetting();

    document.getElementById('profile-name').value = localStorage.getItem('name');
    document.getElementById('profile-account').value = localStorage.getItem('account');
    
    document.getElementById('update-btn').addEventListener('click', updatePassword);
    
    document.getElementById('notice-switch').addEventListener('change', function(){
    	const frequency = document.getElementById('day');
    	if(this.checked == true) frequency.disabled = false;
    	else frequency.disabled = true;
    });
    document.getElementById('update-notice-btn').addEventListener('click', updateRemindFrequency);
    
    
    if(localStorage.getItem('hair-analysis')) updateHairResult();
    document.getElementById('hair-analysis-btn').addEventListener('click', updateHairAnalysis);
    document.getElementById('update-form-btn').addEventListener('click', function(){
    	document.getElementById('result').setAttribute('style', 'display: none;');
    	document.getElementById('hair-analysis-form').setAttribute('style', 'display: initial;');
    });
    
}

window.addEventListener('load', init);