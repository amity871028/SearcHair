const settingAPI = 
	password: '/api/user/password/update'
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
console.log("??");
  if (document.forms['update-password-form'].reportValidity()) {
    const newPassword = document.getElementById('new-password').value;
    if (newPassword.length >= 8) {
      const result = await FetchData.post(settingAPI.password, {
        oldPassword: document.getElementById('old-password').value,
        newPassword: document.getElementById('new-password').value,
      });

      if (result.status === 401) {
        document.getElementById('update-password-wrong').innerHTML = '舊密碼錯誤或是未登入。';
        console.log("?!!!?");
      } else if (result.status === 200) {
        $('#update-password-modal').modal('hide');
        $('#update-success-modal').modal('show');
        console.log("?????");
        PASSWORD.forEach((element) => { // clear password field
          document.getElementById(element).value = '';
        });
      }
    }
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
}

window.addEventListener('load', init);