<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--
본 페이지는 사용자가 가입 후에 개인 정보를 수정할 수 있는 페이지임.
Form-data parameter
닉네임 - nickname
이메일 - email
프로필 사진 - userProfile
사용자 환경 설정 (배열) - configure
사용자 취향 설정 (String 배열) - favor
-->

<script src="${pageContext.request.contextPath}/resources/js/mem_js.js"></script>
<jsp:include page="./mem_components.jsp"/>

<div class="wrapper">
    <div class="profile-content section">
        <div class="container">

            <form class="settings-form" enctype="multipart/form-data" action="/modMemInfo" method="POST" onsubmit="return onFormReq();">

                <div class="row">
                    <div class="col-12 text-center"><h2>회원 정보 수정</h2><br><br></div>

                    <div class="profile-picture">
                        <div class="fileinput fileinput-new" data-provides="fileinput">
                            <div class="fileinput-new img-no-padding">
                                <img name="profile" id="profile" src="img/${DTO.mem_profile}" alt="프로필 사진">
                            </div>
                            <div class="fileinput-preview fileinput-exists img-no-padding"></div>
                            <div>
                                <span class="btn btn-outline-default btn-file btn-round">
                                  <span class="fileinput-new">Change Photo</span>
                                  <span class="fileinput-exists">Change</span>
                                  <input type="file" name="mem_profile" id="mem_profile">
                                </span>
                                <br/>
                                <a href="#" class="btn btn-link btn-danger fileinput-exists" data-dismiss="fileinput"><i class="fa fa-times"></i> Remove</a>
                            </div>
                        </div>
                    </div>
                </div>

                <br><br>

                <div class="row">
                    <div class="col-md-6 ml-auto mr-auto">

                        <div class="row">
                            <div class="col-md-6 col-12">
                                <div class="form-group">
                                    <label>NickName</label>
                                    <input type="text" name="mem_nick" id="mem_nick" class="form-control border-input" placeholder="NickName" value="${DTO.mem_nick}" onchange="nickChange()">
                                </div>
                            </div>
                            <div class="col-md-6 col-12">
                                <div class="form-group">
                                    <label>PassWord</label>
                                    <input type="password" id = "em_pwd" name="em_pwd" class="form-control border-input" placeholder="Password" value="">
                                </div>
                            </div>
                        </div>

                        <div class="col-12 text-center" style="padding-bottom : 40px"><h3 style="font-weight : bold">당신의
                            취향을 선택해 주세요!</h3></div>


                        <div id="selectFavorField" class="row text-center" style="margin : 0"></div>

                        <div class="col-12 mr-auto ml-auto" style="margin-top : 20px">
                            <label style="font-weight : bold">Notifications</label>
                            <ul class="notifications">
                                <li class="notification-item">
                                    푸쉬 알림을 통해서 나에게 맞는 정보를 받아볼래요?
                                    <input name="configure" type="checkbox" data-toggle="switch" checked="true"
                                           data-on-color="info" data-off-color="info">
                                    <span class="toggle"></span>
                                </li>
                                <li class="notification-item">
                                    팔로워가 새로운 글을 올리면 알려줄까요?
                                    <input name="configure" type="checkbox" data-toggle="switch" checked="true"
                                           data-on-color="info" data-off-color="info">
                                    <span class="toggle"></span>
                                </li>

                            </ul>

                        </div>
                        <br><br>
                        <div class="row text-center" style="padding : 8px">
                            <button type="submit" id="saveBtn" class="col-sm-6 btn btn-info btn-round">Save</button>
                            <button id="retireBtn" class="col-sm-6 btn btn-danger btn-round">회원 탈퇴</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script>

    var userFavor = new Array();

</script>

<c:forEach var="favor" items="${favor}">
    <script>
        userFavor.push('${favor}');
    </script>
</c:forEach>

<!-- 사용자가 선택한 취향 정보를 JS에서 사용하기 위해, EL을 통해 받아온 리스트를 JS의 배열로 변환하는 소스 -->

<<<<<<< HEAD
=======
<script>
    //사용자가 선택한 취향 정보
    console.log(userFavor);

</script>


>>>>>>> f0699b9c9c2e716763e992421c03f1738a99f282
<script>
    //사용자가 선택한 취향 정보
    console.log(userFavor);

</script>


<script>




    /* ------------ 뷰 초기화 작업 ------------ */
    $(document).ready(function () {
        setNavType("blue");
        initFavorForm();

        /*$("#saveBtn").on('click', function (e) {
            e.preventDefault();
            getSelectedCard();

        });
*/
        // 회원 탈퇴
        $("#retireBtn").on('click', function (e) {


            e.preventDefault();
            e.stopPropagation();
            //OK 버튼 클릭시 수행 함수
            let okFunc = function () {

                $.ajax({

                    url: "/delMemInfo",
                    type: "post",
                    success: makeSimpleNotifyModal(null, "회원 탈퇴되었습니다. 감사합니다.", "닫기", null),
                    error: (xhs, status, error), function() {

                        console.log(status.code + "에러가 발생하였습니다.");

                    }

                });

            }

            //거절 시 수행 함수
            let refuseFunc = function () {
            }
            //모달 창 띄우기
            makeCheckableModal("회원 탈퇴", "복구용 계정을 설정 하지 않았다면 정보를 더이상 찾을 수 없습니다. 계속 진행하시겠습니까?", "예", "아니오", okFunc, refuseFunc);


        });

    });

/*    console.log("input file 원래 사진 "+$("#mem_profile").value); //요거 인듯
    $("#mem_profile").change(function () {
        alert("사진이 바뀌었습니다.");
        console.log("input file 원래 사진 "+ $("#mem_profile").value);

    });


    $("#mem_nick").change(function () {
        alert("닉이 바뀌었습니다.");
        console.log("input file 원래 사진 "+ $("#mem_nick").value);

    });

    function nickChange() {
        alert("Asdfasdf");
    }*/

    // ---------- Submit시에 Hidden 값을 넣어주는 함수 -----------

    function onFormReq() {

        //사용자가 선택한 취향 카드 목록을 들고온다.
        let selectCard = getSelectedCard();

        //Hidden 태그를 만들어 value를 사용자가 선택한 카테고리 이름으로 초기화 시킨다. 그리고 form 태그 안에 추가시킴.
        for (let i = 0; i < selectCard.length; ++i) {
            $inputNode = $("<input>").attr("type", "hidden").attr("name", "favor").val(selectCard[i]);
            $(".settings-form").append($inputNode);

        }
        alert(userFavor);           // 받아온 문학/예술, 경제/경영
        alert($("#profile").val());
        alert($("#profile").value);
        alert(document.getElementsByName('mem_nick')[0].value); // 냥
        alert(document.getElementsByName('mem_profile')[0].value); //  C:\fakepath\about2.jpg
        alert(document.getElementsByName('profile')[0].value);  // asdfasdf1

        //초기화 절차가 끝나면 true를 리턴하여 form submit 수행
        return true;


    }


    //선택된 카드를 가져오는 함수
    function getSelectedCard() {

        //카드 목록을 가져와서 -
        let $cards = $("#selectFavorField").find(".card");
        let selCards = new Array();

        $cards.each(function (index, item) {
            let temp = $(item).css('opacity');
            // alert("temp..." + temp);
            //만약 투명도가 1이 아니면 (카드 선택 시 투명도가 1 미만으로 설정되어있음)
            if (temp < 1) {

                //html값을 가져와서 배열에 넣어준다.
                selCards.push($(item).find("h3").html());

            }
        });

        //배열을 리턴
        return selCards;


    }


    //취향 카드 리스트 초기화
    let initFavorForm = function () {

        //Ajax로 DB에서 카테고리 리스트를 받아옴
        $.ajax({

            "type": "get",
            "url": "/getCategory",
            "data": null,
            "success": function (response) {

                //div 필드를 초기화
                let $selectFavorField = $("#selectFavorField");

                //Request Scope로 넘긴 배열 리스트들을
                let index;
                for (let i = 0; i < response.length; ++i) {
                    index = response[i];
                    let $cardItem = $("#cardItem").clone();

                    //display : none 처리 되어있는 카드를 show 해준다.
                    $cardItem.css('display', 'block');
                    $cardItem.find(".card").css("background-image", "url('" + index.image + "')");
                    $cardItem.find("h3").html(index.name);

                    if (userFavor.includes(index.name)) {

                        $cardItem.find(".card").css('opacity', '0.2');


                    }

                    $cardItem.on('click', function (e) {

                        //사용자가 클릭 시 투명도를 변경하여 눈에 띄게 처리한다.
                        e.preventDefault();
                        let opacity = $cardItem.find(".card").css('opacity');

                        //만약 선택된 카드가 선택되지 않았다면
                        if (opacity == 1) {

                            //투명도를 낮춰준다.
                            $cardItem.find(".card").css('opacity', '0.2');
                        } else {

                            //만약 카드가 선택되었다면 투명도를 높여준다.
                            $cardItem.find(".card").css('opacity', '1');
                        }

                    });

                    //지정된 태그에 자식으로 추가
                    $selectFavorField.append($cardItem);


                }

            },

            "error": function (xhs, request, error) {


                console.log("error code.. " + request.status + " message : " + request.responseText + "error : " + error);

            }


        });


    }


</script>