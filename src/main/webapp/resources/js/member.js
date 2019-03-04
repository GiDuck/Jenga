/**
 * 회원 정보에 관한 자바스크립트 함수들을 모아놓는 파일
 */

const COLOR_VALID = "#BEEFFF";
const COLOR_NOT_VALID = "#FFE6E6";


function validEmail(email) {

    return REGEX_EMAIL.test(email);
}

function validPassword(password) {

    return REGEX_PASSWORD.test(password);
}

function checkEmailFieldValid($email) {


    if (REGEX_EMAIL.test($email.val())) {
        $email.css("background-color", COLOR_VALID);
    } else {
        $email.css("background-color", COLOR_NOT_VALID);
    }
}

function checkPasswordFieldValid($password) {

    if (REGEX_PASSWORD.test($password.val())) {
        $password.css("background-color", COLOR_VALID);
    } else {
        $password.css("background-color", COLOR_NOT_VALID);
    }

}

function confirmPasswordField($password, $checkPwd) {

    if ($password.val() === $checkPwd.val()) {
        $password.css("background-color", COLOR_VALID);
        $checkPwd.css("background-color", COLOR_VALID);
    } else {
        $password.css("background-color", COLOR_NOT_VALID);
        $checkPwd.css("background-color", COLOR_NOT_VALID);

    }

}

//선택된 카드를 가져오는 함수
function getSelectedCard() {


    //카드 목록을 가져와서
    let $cards = $("#selectFavorField").find(".card");
    let selCards = new Array();

    $cards.each(function (index, item) {

        let temp = $(item).css('opacity');

        //만약 투명도가 1이 아니면 (카드 선택 시 투명도가 1 미만으로 설정되어있음)
        if (temp < 1) {

            //html값을 가져와서 배열에 넣어준다.
            selCards.push($(item).find("h3").html());

        }
    });

    //배열을 리턴
    return selCards;


}


//이메일 로그인 검증 함수
function validCheckAuth(id, pwd) {

    let passport = true;
    let idIsVoid = !id || id.replace(REGEX_TRIM_VOID, "").length === 0;
    let pwdIsVoid = !pwd || pwd.replace(REGEX_TRIM_VOID, "").length === 0;
    let vaildPwd = REGEX_PASSWORD.test(pwd);
    let vaildId = REGEX_EMAIL.test(id);


    if (idIsVoid || pwdIsVoid || !vaildId || !vaildPwd) {
        passport = false;
    }

    return passport;


}
  
  
 