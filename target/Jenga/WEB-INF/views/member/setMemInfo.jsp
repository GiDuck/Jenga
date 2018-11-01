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

            <form class="settings-form" method="POST" enctype="multipart/form-data" action="/regMemInfo" onsubmit = "return onFormReq();">

                <div class="row">
                    <div class="profile-picture">
                        <div class="fileinput fileinput-new" data-provides="fileinput">
                            <div class="fileinput-new img-no-padding">
                                <img src="${pageContext.request.contextPath}/resources/assets/img/faces/clem-onojeghuo-2.jpg"
                                     alt="...">
                            </div>
                            <div class="fileinput-preview fileinput-exists img-no-padding"></div>
                            <div>
                <span class="btn btn-outline-default btn-file btn-round">
                  <span class="fileinput-new">Change Photo</span>
                  <span class="fileinput-exists">Change</span>
                  <input type="file" name="mem_profile" id="mem_profile">
                </span>
                                <br/>
                                <a href="#paper-kit" class="btn btn-link btn-danger fileinput-exists"
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
                            <div class="col-md-6 col-12">
                                <div class="form-group">
                                    <label>NickName</label>
                                    <input type="text" class="form-control border-input" placeholder="NickName" name="mem_nick" id="mem_nick">
                                </div>
                            </div>
                            <%--<div class="col-md-6 col-12">
                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" class="form-control border-input" placeholder="Email" id="mem_email">
                                </div>
                            </div>--%>
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

                <%-- 이메일로 가입에서 넘어온 이메일과 비밀번호--%>
                <input type="hidden" value="${emailMemberDTO.getEm_id()}" name="em_id">
                <input type="hidden" value="${emailMemberDTO.getEm_pwd()}" name="em_pwd">

                <%-- 소셜로그인에서 넘어온 이메일--%>
                <input type="hidden" value="${socialMemberDTO.getSm_id()}" name="sm_id">
                <input type="hidden" value="${socialMemberDTO.getSm_type()}" name="sm_type">


                <%--<input type="hidden" value="" name="mem_iuid">--%>
                <%--<input type="hidden" value="" name="mem_level">--%>
                <%--<input type="hidden" value="" name="mem_joinDate">--%>



                <%--=======
                                  <input name="userProfile" type="file" name="...">
                                </span>
                                <br />
                                <a href="#" class="btn btn-link btn-danger fileinput-exists" data-dismiss="fileinput"><i class="fa fa-times"></i> Remove</a>
                              </div>
                            </div>
                          </div>
                        </div>

                        <br><br>

                        <div class="row">
                          <div class="col-md-6 ml-auto mr-auto">

                            <form class="settings-form" action="/endPoint..." method="POST" onsubmit = "return onFormReq();">
                              <div class="row">
                                <div class="col-md-6 col-12">
                                  <div class="form-group">
                                    <label>NickName</label>
                                    <input name ="nickname" type="text" class="form-control border-input" placeholder="NickName">
                                  </div>
                                </div>
                                <div class="col-md-6 col-12">
                                  <div class="form-group">
                                    <label>Email</label>
                                    <input name ="email"type="email" class="form-control border-input" placeholder="Email">
                                  </div>
                                </div>
                              </div>

                              <div class="col-12 text-center" style="padding-bottom : 40px"><h3 style="font-weight : bold">당신의 취향을 선택해 주세요!</h3></div>


                              <div id="selectFavorField" class="row text-center" style="margin : 0"></div>

                              <div class="col-12 mr-auto ml-auto" style="margin-top : 20px">
                              <label style="font-weight : bold">Notifications</label>
                              <ul class="notifications">
                                <li class="notification-item">
                                     푸쉬 알림을 통해서 나에게 맞는 정보를 받아볼래요?
                                  <input name ="configure" type="checkbox" data-toggle="switch" checked="true" data-on-color="info" data-off-color="info">
                                  <span class="toggle"></span>
                                </li>
                                <li class="notification-item">
                                      팔로워가 새로운 글을 올리면 알려줄까요?
                                  <input name ="configure" type="checkbox" data-toggle="switch" checked="true" data-on-color="info" data-off-color="info">
                                  <span class="toggle"></span>
                                </li>

                              </ul>

                              </div>
                              <div class="text-center">
                                <button type="submit" id="saveBtn" class="btn btn-wd btn-info btn-round">Save</button>
                              </div>
                >>>>>>> f82263e0e8653aa1b1004d15bb9bed7a059d4040--%>
            </form>

        </div>
    </div>
</div>

<script>


    /* ------------ 뷰 초기화 작업 ------------ */
    $(document).ready( _ => {
        //네비바 색상 초기화
        setNavType("blue");
        initFavorForm();

        /*  $("#saveBtn").on('click', _ => {
             //alert("asdfsadfsadf");
           //  getSelectedCard();

          });*/

    });


    // ---------- Submit시에 Hidden 값을 넣어주는 함수 -----------

    function onFormReq(){


        //사용자가 선택한 취향 카드 목록을 들고온다.
        let selectCard = getSelectedCard();

        console.log(selectCard);

        //Hidden 태그를 만들어 value를 사용자가 선택한 카테고리 이름으로 초기화 시킨다. 그리고 form 태그 안에 추가시킴.
        for(let i =0 ; i < selectCard.length ; ++i){
            $inputNode = $("<input>").attr("type", "hidden").attr("name", "favor").val(selectCard[i]);
            $(".settings-form").append($inputNode);

        }

        //초기화 절차가 끝나면 true를 리턴하여 form submit 수행
        return true;



    }



    /*  //------------- 선택된 카드를 가져오는 함수 --------------

      function getSelectedCard() {

         //카드 목록을 가져와서
         let $cards = $("#selectFavorField").find(".card");
         let selCards = new Array();

         $cards.each(function(index, item) {


            let temp = $(item).css('opacity');

              //만약 투명도가 1이 아니면 (카드 선택 시 투명도가 1 미만으로 설정되어있음)
            if(temp < 1){

               //html값을 가져와서 배열에 넣어준다.
               selCards.push($(item).find("h3").html());

            }
         });


         //배열을 리턴
         return selCards;


      }*/





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

                    console.log(index);
                    let $cardItem = $("#cardItem").clone();

                    //display : none 처리 되어있는 카드를 show 해준다.
                    $cardItem.css('display', 'block');
                    $cardItem.find(".card").css("background-image", "url('"+ index.image +"')" );
                    $cardItem.find("h3").html(index.name);

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