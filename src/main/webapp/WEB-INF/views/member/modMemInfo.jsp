<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<script src="${pageContext.request.contextPath}/resources/js/member.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/modal/bookmarkModal.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/modal/memberModal.js"></script>

<jsp:include page="memComponent.jsp"/>

<style>

    #em_pwd:hover{
        background-color: #9A9A9A;
    }

</style>
<div class="wrapper">
    <div class="profile-content section">
        <form class="settings-form" onsubmit="return false;">

            <div class="row">
                <div class="col-12 text-center"><h2>회원 정보 수정</h2><br><br></div>

                <div class="profile-picture">
                    <div class="fileinput fileinput-new" data-provides="fileinput">
                        <div class="fileinput-new img-no-padding">
                            <img src="${DTO.mem_profile}" name="profile" alt="프로필 사진" onerror="this.src='${pageContext.request.contextPath}/resources/assets/img/default/no_image.png'">
                        </div>
                        <div class="fileinput-preview fileinput-exists img-no-padding"></div>
                        <div>
                                <span class="btn btn-outline-default btn-file btn-round">
                                  <span class="fileinput-new">Change Photo</span>
                                  <span class="fileinput-exists">Change</span>
                                  <input type="file" id="mem_profile">
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
                                <input type="text" name="mem_nick" id="mem_nick" class="form-control border-input" placeholder="NickName" value="${DTO.mem_nick}">
                            </div>
                        </div>
                        <div class="col-md-6 col-12">
                            <div class="form-group">
                                <label>Password</label>
                                <div id = "em_pwd" name="em_pwd" style="cursor : pointer" class="form-control border-input">비밀번호 수정</div>
                            </div>
                        </div>

                        <div class="col-12">

                            <label>Introduce</label>
                            <textarea name="mem_introduce" class="form-control border-input w-100" rows="5">${DTO.mem_introduce}</textarea>

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

                        <div class="row">

                            <div class="col-12" style="margin-top : 20px">
                                <label style="font-weight : bold">Bookmarks</label>
                            </div>
                            <div class="col-12 row w-100">

                                <div class="col-5">
                                    <div class="btn btn-danger w-100 text-center" id="btnSyncWithGoogleBK">
                                        <i class="fa fa-google-plus" aria-hidden="true"></i>구글 북마크 동기화</div>
                                </div>

                                <div class="col-7">
                                    <span>최근 동기화 : <p id="chromeSyncDate"></p></span>
                                </div>

                            </div>

                            <div class="col-12 row w-100" style="margin-top : 20px">

                                <div class="col-5">
                                    <div class="btn btn-primary w-100 text-center"  id="btnSyncWithExploreBK">익스플로러 북마크 동기화</div>
                                </div>

                                <div class="col-7">
                                    <span>최근 동기화 : <p id="exploreSyncDate"></p></span>
                                </div>
                            </div>
                        </div>
                    </div>

                        <br><br>
                        <div class="row text-center" style="padding : 8px">
                            <button id="saveBtn" class="col-sm-6 btn btn-info btn-round">Save</button>
                            <button id="retireBtn" class="col-sm-6 btn btn-danger btn-round">회원 탈퇴</button>
                        </div>
                    </div>
                </div>
        </form>
        <div class="container">
        </div>
    </div>

<script>
    var userFavor = new Array();

    $(function () {
        $('#btnSyncWithGoogleBK').click(function () {
            makeUploadBookMarkFileModal('chrome');
        });

        $('#btnSyncWithExploreBK').click(function () {
            makeUploadBookMarkFileModal('explorer');
        });
    });

</script>


<c:forEach var="favor" items="${favor}">
    <script>
        userFavor.push('${favor}');
    </script>
</c:forEach>

<!-- 사용자가 선택한 취향 정보를 JS에서 사용하기 위해, EL을 통해 받아온 리스트를 JS의 배열로 변환하는 소스 -->

<script>

    /* ------------ 뷰 초기화 작업 ------------ */
    $(document).ready(function () {
        navbarObj.setType("bg-info");
        navbarObj.addHeadBlock();
        initFavorForm();

        //썸네일 이미지가 업로드 될때마다 유효성 검사 실시 (1MB 이하만 업로드 가능, jpg, jpeg, png, gif 외 확장자 사용 불가)
        $("input[name='profile']").on('change', function(e){
            checkImageFile(e);
        });


        $("#em_pwd").on("click", function(e){

            makeModifyPWModal(authStatusCode);

        });



        let syncComDateStr = "미등록";
        $.ajax({

           type : "GET",
           data : null,
           url : "/getBookmarkUploadDate",
           success : function(response){;
               if(response.replace(REGEX_TRIM_VOID, "").length > 0){
                   syncComDateStr = new DateTimeFormatter().getFullDateTime(response);
                   renderBookmarkDateField(syncComDateStr, "chrome");
               }
           }

        });



        $("#saveBtn").on('click', function (e) {
            e.preventDefault();
            onFormReq();

        });

        // 회원 탈퇴
        $("#retireBtn").on('click', function (e) {


            e.preventDefault();
            e.stopPropagation();
            //OK 버튼 클릭시 수행 함수
            let okFunc = function () {

                $.ajax({

                    url: "/delMemInfo",
                    type: "DELETE",
                    success: function(response){

                        swal({

                           text  : "삭제가 성공적으로 마무리 되었습니다.",
                           type : "success"

                        }).then(function(){

                            location.replace("/");

                        });

                    },
                    error:  function(xhs, status, error) {

                        swal({

                            text  : "삭제 중 오류가 발생하였습니다.",
                            type : "error"

                        });

                        console.log(status.code + "에러가 발생하였습니다.");

                    }

                });

            }


            swal({

               title : "회원 탈퇴",
               text : "복구용 계정을 설정 하지 않았다면 정보를 더이상 찾을 수 없습니다. 계속 진행하시겠습니까?",
               type : "warning",
               showCancelButton : true,
               showConfirmButton: true

            }).then(function(result){

                if(result.value){

                    okFunc();

                }


            });


        });

    });


    function renderBookmarkDateField(syncComDateStr, type){

        let $syncDateField;
        if(type === 'chrome'){
            $syncDateField = $("#chromeSyncDate");
        }else if(type ==='explore'){
            $syncDateField = $("#exploreSyncDate");
        }
        $syncDateField.html(syncComDateStr);

    }

    // ---------- Submit시에 Hidden 값을 넣어주는 함수 -----------
    function onFormReq() {

        //사용자가 선택한 취향 카드 목록을 들고온다.
        let selectCard = getSelectedCard();

        let nickname = $("input[name='mem_nick']").val();
        let introduce = $("textarea[name='mem_introduce']").val();
        let profile = $("#mem_profile").prop("files");

        let params = new FormData();
        params.append("mem_nick", nickname);
        params.append("mem_profile", profile[0]);
        params.append("mem_introduce", introduce);
        params.append("favor", selectCard);
        $.ajax({

            url : "/modMemInfo",
            type : "POST",
            enctype: "multipart/form-data",
            data : params,
            contentType: false,
            cache: false,
            processData:false,
            success : function(response){

                let statusCode = parseInt(response);
                console.log(response);
                console.log(authStatusCode.MOD_SUCCESS);
                if(authStatusCode.MOD_SUCCESS == statusCode){
                    swal({
                        text : "회원정보 수정 성공하였습니다.",
                        type : "success"

                    }).then(function(){

                        window.location.replace("/modMemInfo");
                    });
                }else if(authStatusCode.MOD_FAIL == statusCode){

                    swal("수정 실패", "회원 정보 수정 실패 하였습니다.", "error");


                }


            },
            error : function(error){

                swal("수정 실패", "회원 정보 수정 중 에러가 발생 하였습니다.", "error");

            }


        });



    }

    function getSelectedCard() {

        let $cards = $("#selectFavorField").find(".card");
        let selCards = new Array();

        $cards.each(function (index, item) {
            let temp = $(item).css('opacity');
            if (temp < 1) {
                selCards.push($(item).find("h3").html());

            }
        });
        return selCards;


    }


    //취향 카드 리스트 초기화
    let initFavorForm = function () {

        $.ajax({

            type: "get",
            url: "/getCategory",
            data: null,
            success: function (response) {

                let $selectFavorField = $("#selectFavorField");
                let index;
                for (let i = 0; i < response.length; ++i) {
                    index = response[i];
                    let $cardItem = $("#cardItem").clone();

                    $cardItem.css('display', 'block');
                    $cardItem.find(".card").css("background-image", "url('/categoryimg/" + index.MCTG_IMG + "')");
                    $cardItem.find("h3").html(index.MCTG_NAME);

                    if (userFavor.includes(index.MCTG_NAME)) {
                        $cardItem.find(".card").css('opacity', '0.2');
                    }

                    $cardItem.on('click', function (e) {
                        e.preventDefault();
                        let opacity = $cardItem.find(".card").css('opacity');

                        if (opacity == 1) {
                            $cardItem.find(".card").css('opacity', '0.2');
                        } else {
                            $cardItem.find(".card").css('opacity', '1');
                        }
                    });

                    //지정된 태그에 자식으로 추가
                    $selectFavorField.append($cardItem);


                }

            },

            error: function (xhs, request, error) {


                console.log("error code.. " + request.status + " message : " + request.responseText + "error : " + error);

            }


        });


    }


</script>