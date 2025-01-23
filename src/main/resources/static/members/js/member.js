console.log("멤버 자바스크립트 연결")

/* 약관동의 내용 토글 버튼 */
document.querySelector(".use_chk_btn").addEventListener("click",function(e){
    e.preventDefault();
    const usechk = document.querySelector(".use_chk");
    if (usechk.style.display === 'none' || usechk.style.display === '') {
        usechk.style.display = 'block';
    } else {
        usechk.style.display = 'none';
    }
})

document.querySelector(".personal_chk_btn").addEventListener("click",function(e){
    e.preventDefault();
    const personalchk = document.querySelector(".personal_chk");
    if (personalchk.style.display === 'none' || personalchk.style.display === '') {
        personalchk.style.display = 'block';
    } else {
        personalchk.style.display = 'none';
    }
})
document.querySelector(".shopping_chk_btn").addEventListener("click",function(e){
    e.preventDefault();
    const shopping_chk = document.querySelector(".shopping_chk");
    if (shopping_chk.style.display === 'none' || shopping_chk.style.display === '') {
        shopping_chk.style.display = 'block';
    } else {
        shopping_chk.style.display = 'none';
    }
})
document.querySelector(".sms_chk_btn").addEventListener("click",function(e){
    e.preventDefault();
    const sms_chk = document.querySelector(".sms_chk");
    if (sms_chk.style.display === 'none' || sms_chk.style.display === '') {
        sms_chk.style.display = 'block';
    } else {
        sms_chk.style.display = 'none';
    }
})
document.querySelector(".email_chk_btn").addEventListener("click",function(e){
    e.preventDefault();
    const email_chk = document.querySelector(".email_chk");
    if (email_chk.style.display === 'none' || email_chk.style.display === '') {
        email_chk.style.display = 'block';
    } else {
        email_chk.style.display = 'none';
    }
})

/* 체크박스 모두 선택 */
document.querySelector("#all_chk").addEventListener("change",function(e){
    // all_chk 체크박스 상태
    var isChecked = this.checked;

    // 모든 체크박스 선택
    var checkboxes = document.querySelectorAll(".chkbox");

    // 각 체크박스 상태를 all_chk와 동일하게 설정
    checkboxes.forEach(function(checkbox){
        checkbox.checked = isChecked;
    });
});