<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>

    .followerCard {

        border-radius: 5px;
        padding: 10px;

    }

    .followerCard:hover {

        background-color: #efa2a9;
        cursor: pointer;

    }

    .followingBtn{

        margin: 3px;

    }



</style>


<div class="wrapper">
    <div class="main">
        <div class="section section-white section-search">
            <div class="container">
                <div class="row">
                    <div class="col-md-6 col-12 ml-auto mr-auto text-center">
                        <form role="search" class="form-inline search-form">
                            <div class="input-group no-border">
                                <span class="input-group-addon addon-xtreme no-border" id="basic-addon1"><i
                                        class="fa fa-search"></i></span>
                                <input type="text" class="form-control input-xtreme no-border"
                                       placeholder="추가한 팔로워 검색.." aria-describedby="basic-addon1">
                            </div>
                        </form>

                        <div class="row">
                            <div class="col-12">
                          <button class="followingBtn btn btn-outline-danger pull-right" id="showFollowingBtn" value="following">Following</button>
                          <button class="followingBtn btn btn-outline-success pull-right" id="showFollowerBtn" value="follower">Follower</button>

                            </div>
                        </div>
                        <br>

                        <h6 name="description" class="text-muted"></h6>
                        <br>
                        <ul class="list-unstyled follows" id="cardField">
                            <li>
                                <div id="dummyCard" class="row followerCard" style="display: none;">
                                    <div class="col-md-2 col-3">
                                        <img name="userProfile" src="" onerror = "this.src='${pageContext.request.contextPath}/resources/assets/img/placeholder.jpg'"
                                             alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                    </div>
                                    <div class="col-md-6 col-4 description">
                                        <h5 name="userName"></h5>
                                        <br>
                                        <h5><small name="userDescription"></small></h5>
                                    </div>
                                    <div class="col-md-2 col-2">
                                        <button name="unFollow"
                                                class="btn btn-just-icon btn-round btn-outline-danger btn-tooltip"
                                                rel="tooltip" title="unfollow"><i class="fa fa-minus"></i></button>
                                    </div>
                                </div>
                            </li>

                        </ul>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    //현재 문서에서 사용할 Button 객체를 OOP 방식으로 정의한다.
    class Button{

        constructor(_DOMElement){

            if(!_DOMElement || !_DOMElement.length){
                throw new Error("현재 요소 중 지정된 요소가 존재하지 않습니다.");
            }

            const methods = {

                on(){

                    _DOMElement.addClass("active");
                },

                off(){

                    _DOMElement.removeClass("active");

                },

                setOnClick(action){

                    _DOMElement.on("click", function(e){

                        action();

                    });

                },

                toggle(others){


                    if(!(others instanceof Array) && others instanceof Button){

                        others.off();
                    }

                    if(!others){

                        throw new Error("지정된 배열이 null입니다.");
                    }

                    for(let i = 0 ; i<others.length; ++i){
                        if(!others[i] instanceof Button){
                            console.log("지정된 배열 요소가 Button 타입이 아닙니다. ignore..");
                            continue;
                        }
                        others[i].off();

                    }

                }

            }


            Object.assign(this, methods);


        }



    }

    class FollowingBtn extends Button{

        constructor(_DOMElement){
            super(_DOMElement);

        };
    }

    class FollowerBtn extends Button{

        constructor(_DOMElement){
            super(_DOMElement);

        };
    }


    function renderItem(items){

        let $dummy = $("#dummyCard");
        let $field = $("#cardField");

        for(let i = 0; i<items.length; ++i){

            let $copyedDummy = $dummy.clone();
            $copyedDummy.css("display", "block");
            $copyedDummy.attr("id", "");
            $copyedDummy.find("img[name='userProfile']").attr("src", "");
            $copyedDummy.find("h5[name='userName']").text("");
            $copyedDummy.find("small[name='userDescription']").text("");
            $copyedDummy.find("button[name='unFollow']").on("click", function(e){

                e.stopPropagation();
                //TODO ajax로 unfollow 하는 로직 작성

            });


            $field.append($("<li>").append($copyedDummy));


        }

    }



    $(document).ready(function () {

        let followingStr = "내가 팔로잉 하는 사람들";
        let followerStr = "나를 팔로잉 하는 사람들";
        let btnFollow =  new FollowingBtn($("#showFollowingBtn"));
        let btnFollower =  new FollowerBtn($("#showFollowerBtn"));

        navbarObj.setType("bg-info");
        navbarObj.addHeadBlock();
        setAutomaticResizeWindow($(".section"));

        let $description = $("h6[name='description']");
        $(".followerCard").each(function () {

            $(this).find("button[name='unFollow']").on("click", function (e) {

                e.preventDefault();
                e.stopPropagation();
                alert("언팔로 되었습니다.");

            });

            $(this).on("click", function () {

                location.href = "/getFollowerPage";

            });

        });


        btnFollow.setOnClick(function(){

            $description.text(followingStr);
            btnFollow.on();
            btnFollow.toggle(btnFollower);

            //TODO Ajax로 팔로잉 리스트 받아오는 로직 작성
        });


        btnFollower.setOnClick(function(){

            $description.text(followerStr);
            btnFollower.on();
            btnFollower.toggle(btnFollow);
            //TODO Ajax로 팔로워 리스트 받아오는 로직 작성

        });

        btnFollow.on();
        $description.text(followerStr);

    });


</script>
