<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<jsp:include page="./mem_components.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/mem_js.js"></script>

<!--

본 페이지는 사용자가 가입 후에 개인 정보를 설정할 수 있는 페이지임

Form-data parameter

닉네임 - nickname
이메일 - email
프로필 사진 - userProfile
사용자 환경 설정 (배열) - configure
사용자 취향 설정 (String 배열) - favor


-->

<div class="wrapper">
    <div class="profile-content section">
        <div class="container">

            <form class="settings-form" onsubmit = "return onFormReq();">

                <div class="row">
                    <div class="profile-picture">
                        <div class="fileinput fileinput-new" data-provides="fileinput">
                            <div class="fileinput-new img-no-padding">
                                <img src=""
                                     alt="..." onerror="this.src='${pageContext.request.contextPath}/resources/assets/img/faces/clem-onojeghuo-2.jpg'">
                            </div>
                            <div class="fileinput-preview fileinput-exists img-no-padding"></div>
                            <div>
                            <span class="btn btn-outline-default btn-file btn-round">
                              <span class="fileinput-new">Change Photo</span>
                              <span class="fileinput-exists">Change</span>
                              <input type="file" name="mem_profile" id="mem_profile">
                            </span>
                                <br/>
                                <a href="#" class="btn btn-link btn-danger fileinput-exists"
                                   data-dismiss="fileinput"><i class="fa fa-times"></i> Remove</a>
                            </div>
                        </div>
                    </div>
                </div>

                <br> <br>

                <div class="row">
                    <div class="col-md-6 ml-auto mr-auto">
                        <%--<form class="settings-form">--%>
                        <div class="row">
                            <div class="col-12">
                                <div class="form-group">
                                    <label>NickName</label>
                                    <input type="text" class="form-control border-input" placeholder="Nickname" placeholder="닉네임을 입력 해 주세요.." name="mem_nick">
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-group">
                                    <label>Introduce</label>
                                    <textarea type="text" class="form-control border-input" placeholder="소개를 입력 해 주세요.." name="mem_introduce" rows="3"></textarea>
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
                                    <input type="checkbox" data-toggle="switch" checked="" data-on-color="info" data-off-color="info">
                                    <span class="toggle"></span>
                                </li>
                                <li class="notification-item">
                                    팔로워가 새로운 글을 올리면 알려줄까요?
                                    <input type="checkbox" data-toggle="switch" checked="" data-on-color="info" data-off-color="info">
                                    <span class="toggle"></span>
                                </li>

                            </ul>

                        </div>
                        <div class="text-center">
                            <button type="submit" class="btn btn-wd btn-info btn-round">Save</button>
                        </div>
                        <%--</form>--%>
                    </div>
                </div>

            </form>

        </div>
    </div>
</div>

<script>


    /* ------------ 뷰 초기화 작업 ------------ */
    $(document).ready( function() {
        //네비바 색상 초기화
        setNavType("blue");
        initFavorForm();



        $("input[name='mem_profile']").on('change', function(e){

            checkImageFile(e);

        });

    });


    // ---------- Submit시에 Hidden 값을 넣어주는 함수 -----------

    function onFormReq(){


        //사용자가 선택한 취향 카드 목록을 들고온다.
        let selectCard = getSelectedCard();


        //Hidden 태그를 만들어 value를 사용자가 선택한 카테고리 이름으로 초기화 시킨다. 그리고 form 태그 안에 추가시킴.
        if(selectCard.length==0){
            alert("카드를 하나이상 선택해 주세요!")
            return false;
        }

        let nickname = $("input[name='mem_nick']").val();
        let introduce = $("textarea[name='mem_introduce']").val();
        let profile = $("input[name='mem_profile']").prop("files")[0];

        let formData = new FormData();
        formData.append("mem_nick", nickname);
        formData.append("mem_introduce", introduce);
        formData.append("mem_profile", profile);
        formData.append("favor", selectCard);
        formData.append("em_id", "${emailMemberDTO.em_id}");
        formData.append("em_pwd", "${emailMemberDTO.em_pwd}");
        formData.append("sm_id", "${socialMemberDTO.sm_id}");
        formData.append("sm_type", "${socialMemberDTO.sm_type}");

        $.ajax({

            type : "POST",
            url : "/regMemInfo",
            encrypt : "multipart/form-data",
            data : formData,
            contentType: false,
            cache: false,
            processData:false,
            success : function(response){
                swal({
                    text : "추가 정보를 성공적으로 등록하였습니다.",
                    type : "success"

                }).then(function(){
                    location.replace("/");
                });
            },error : function(xhs, status, error) {
                console.log(xhs);
                console.log(status);
                console.log(error);
                swal({
                    text: "추가 정보 등록 중 에러가 발생하였습니다. 문제가 지속되면 관리자에게 문의하십시오.",
                    type: "error"

                });

                return;

            }
        });


        return false;



    }


    //--------------- 취향 카드 리스트 초기화 ----------------
    let initFavorForm = function(){

        //Ajax로 DB에서 카테고리 리스트를 받아옴
        $.ajax({

            "type" : "get",
            "url" : "/getCategory",
            "data" : null,
            "success" : (response) => {

                //div 필드를 초기화
                let $selectFavorField =  $("#selectFavorField");
                let index;
                for(let i=0; i<response.length; ++i){

                    index = response[i];

                    // console.log(index);
                    console.log(index["MCTG_NAME"]);
                    console.log(index.MCTG_NAME);
                    let $cardItem = $("#cardItem").clone();
                    let imgUrl = "/categoryimg/" + index.MCTG_IMG;

                    //display : none 처리 되어있는 카드를 show 해준다.
                    $cardItem.css('display', 'block');
                    $cardItem.find(".card").css("background-image", "url('"+ imgUrl + "')");
                    $cardItem.find("h3").html(index["MCTG_NAME"]);

                    $cardItem.on('click', (e) => {

                        //사용자가 클릭 시 투명도를 변경하여 눈에 띄게 처리한다.
                        e.preventDefault();
                        let opacity = $cardItem.find(".card").css('opacity');

                        //만약 선택된 카드가 선택되지 않았다면
                        if(opacity == 1){
                            //투명도를 높여준다.
                            $cardItem.find(".card").css('opacity', '0.2');

                        }else{
                            //만약 카드가 선택되었다면 투명도를 낮춰준다.
                            $cardItem.find(".card").css('opacity', '1');
                        }


                    });

                    //지정된 태그에 자식으로 추가

                    $selectFavorField.append($cardItem);


                }

            },

            "error" : function(xhs, request, error){


                console.log("error code.. " + request.status + " message : " + request.responseText + "error : " + error );

            }


        });


    }








</script>