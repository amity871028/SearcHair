const settingAPI = {
	password: 'api-user-password-update',
	remind: 'api-user-remind'
};
const PASSWORD = ['old-password', 'new-password', 'confirm-new-password'];

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
    	account: localStorage.getItem('account'),
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
			account: localStorage.getItem('account'),
	    	remindFrequency: parseInt(remindFrequency),
		});
		alert('更新成功！');
	}
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
    sidebarSetting();
    document.getElementById('update-btn').addEventListener('click', updatePassword);
    
    document.getElementById('notice-switch').addEventListener('change', function(){
    	const frequency = document.getElementById('day');
    	if(this.checked == true) frequency.disabled = false;
    	else frequency.disabled = true;
    });
    document.getElementById('update-notice-btn').addEventListener('click', updateRemindFrequency);
}

window.addEventListener('load', init);