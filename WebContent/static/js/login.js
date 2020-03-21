var header = '<div class="modal fade" id="login-modal" tabindex="-1" role="dialog"> \
<div class="modal-dialog" role="document"> \
    <div class="modal-content"> \
        <div class="modal-header"> \
            <h5 class="modal-title">登入</h5> \
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"> \
            <span aria-hidden="true">&times;</span> \
            </button> \
        </div> \
        <div class="modal-body"> \
            <form id="login-form"> \
                <div class="form-group"> \
                    <label for="login-user-name" class="col-form-label">帳號(信箱)：</label> \
                    <input type="text" class="form-control" id="login-user-name" required> \
                </div> \
                <div class="form-group"> \
                    <label for="login-password" class="col-form-label">密碼：</label> \
                    <input type="password" class="form-control" id="login-password" required> \
                    <small id="login-wrong" class="form-text wrong-msg">　</small> \
                </div> \
                <button type="button" class="btn btn-primary" id="login">登入</button> \
            </form> \
        </div> \
        <div class="modal-footer"> \
            <a href="forget-password.html">忘記密碼</a> \
        </div> \
    </div> \
</div> \
</div>';

document.write(header);