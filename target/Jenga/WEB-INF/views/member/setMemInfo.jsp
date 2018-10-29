<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<jsp:include page="./c_components.jsp"/>

<script>
    console.log(${emailMemberDTO.getEm_id()});

</script>
<div class="wrapper">

    <div class="profile-content section">
        <div class="container">

            <form class="settings-form" method="POST" enctype="multipart/form-data" action="/regMemInfo">

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



            </form>

        </div>
    </div>
</div>


<script>

    $(document).ready(_ => {
        setNavType("blue");
        initFavorForm();

    });


    let initFavorForm = _ => {

        $.ajax({

            "type": "get",
            "url": "/getCategory",
            "data": null,
            "success": (response) => {

                let $selectFavorField = $("#selectFavorField");
                let index;
                for (let i = 0; i < response.length; ++i) {

                    index = response[i];

                    console.log(response);
                    let $cardItem = $("#cardItem").clone();
                    $cardItem.css('display', 'block');
                    $cardItem.find(".card").css("background-image", "url('" + index.image + "')");
                    $cardItem.find("h3").html(index.name);

                    $cardItem.on('click', (e) => {

                        e.preventDefault();

                        let opacity = $cardItem.find(".card").css('opacity');

                        console.log('opacity ... ' + opacity);

                        if (opacity == 1) {

                            $cardItem.find(".card").css('opacity', '0.2');

                        } else {

                            $cardItem.find(".card").css('opacity', '1');
                        }


                    });

                    $selectFavorField.append($cardItem);

                }

            },

            "error": function (xhs, request, error) {

                console.log("error code.. " + request.status + " message : " + request.responseText + "error : " + error);
            }

        });

    }


    //선택된 카드를 가져오는 함수
    function getSelectedCard() {

        //카드 목록을 가져와서
        let $cards = $("#selectFavorField").find(".card");
        let selCards = new Array();

        $cards.each(function(index, item) {

            let temp = $(item).css('opacity');

            //만약 투명도가 1이 아니면 (카드 선택 시 투명도가 1 미만으로 설정되어있음)
            if(temp !== 1){

                //html값을 가져와서 배열에 넣어준다.
                selCards.push($(item).find("h3").html());

            }
        });

        //배열을 리턴
        return selCards;


    }


</script>
