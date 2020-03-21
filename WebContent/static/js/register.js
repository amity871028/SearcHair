var header = '<div class="modal fade" id="register-modal" tabindex="-1" role="dialog"> \
<div class="modal-dialog" role="document"> \
    <div class="modal-content"> \
        <div class="modal-header"> \
            <h5 class="modal-title">註冊</h5> \
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"> \
            <span aria-hidden="true">&times;</span> \
            </button> \
        </div> \
        <div class="modal-body"> \
            <form id="register-form"> \
                <div class="form-group"> \
                    <label for="register-account">帳號(信箱)：</label> \
                    <input type="email" class="form-control" id="register-account" required> \
                </div> \
                <div class="form-group"> \
                    <label for="register-name">使用者名稱：</label> \
                    <input type="text" class="form-control" id="register-name" required> \
                </div> \
                <div class="form-group"> \
                    <label for="register-password">密碼:</label> \
                    <input type="password" class="form-control" id="register-password" required minlength="8"> \
                </div> \
                <div class="form-group"> \
                    <label for="register-confirm-password">確認密碼:</label> \
                    <input type="password" class="form-control" id="register-confirm-password" required minlength="8"> \
                </div> \
                <button type="button" class="btn btn-primary" id="register">註冊</button> \
            </form> \
        </div> \
    </div> \
</div> \
</div> \
<div class="modal fade" id="register-success-modal" tabindex="-1" role="dialog"> \
<div class="modal-dialog" role="document"> \
    <div class="modal-content"> \
        <div class="modal-header"> \
            <h5 class="modal-title">註冊成功</h5> \
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"> \
            <span aria-hidden="true">&times;</span> \
            </button> \
        </div> \
        <div class="modal-body"> \
            <p>恭喜您註冊成功</p> \
        </div> \
    </div> \
</div> \
</div>';

document.write(header);