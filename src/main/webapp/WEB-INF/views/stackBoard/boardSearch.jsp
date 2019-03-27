<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<div class="wrapper">
    <div class="main">
        <div class="section section-gray">
            <div class="container">
                <div class="row">
                    <div class="col-md-6 col-12 ml-auto mr-auto text-center">

                        <div class="row text-center">

                            <div class="col-md-3">
                                <div id="search_option" class="dropdown w-100">
                                    <button class="dropdown-toggle btn btn-lg w-100" href="#" role="button" id="bs_dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">키워드</button>

                                        <div class="dropdown-menu" aria-labelledby="bs_dropdown">
                                            <a class="dropdown-item" value="keyword">키워드</a>
                                            <a class="dropdown-item" value="name">글쓴이</a>
                                            <a class="dropdown-item" value="tag">태그</a>
                                        </div>

                                    <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                </div>

                            </div>
                            <div class="col-md-7"><input type="text" class="form-control border-input w-100 h-100" name="bs_keyword" placeholder="Search...">&nbsp;&nbsp;
                            </div>
                            <div class="col-md-2">
                                <button name="bs_searchBtn" class="btn btn-just-icon w-100 h-100"><i class="fa fa-search w-90 h-90"></i>
                                </button>
                            </div>

                        </div>
                        <br>
                    </div>

                </div>
                <br><br>
                <div class="row items-row text-left" id="block_field">


                </div>
                <div id="preLoaderContainer"></div>

                <div class="col-md-3 col-sm-4" id="bkCard" style="display : none">
                    <div class="card card-blog text-center">
                        <div class="card-image">
                                <img name="bk_image" onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'"
                                     src="" alt="Rounded Image" class="img-rounded img-responsive">
                            <br><br>
                            <div class="text"><p name="bk_title" class="name"></p></div>

                            <div class="card-body details-center" style="padding : 10px">
                                    <div class="author w-50 text-center">
                                        <img name ="user_profile" src="" onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/placeholder.jpg'"
                                             alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                        <div class="text">
                                            <div class="row">
                                                <div class="col-8 w-100">
                                            <span name="user_nick" class="name w-100"></span>
                                                </div>
                                                <div class="col-4 w-100">
                                                    <div class="meta w-100"><i class="fa fa-heart" name="likeIcon" style="color: red"></i><p name="likeCount"></p></div>
                                                </div>
                                                </div>
                                            </div>
                                    </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>


<script>

    let pageNum = 1;
    let key;
    let keyword;
    let isEndPage = false;


    let preLoader = new PreLoader();
    preLoader.init();

    $(document).ready(function () {

        let timeChecker = new TimeChecker();
            navbarObj.setType("bg-info");
            navbarObj.addHeadBlock("gray");
        selectDropdown();
        setAutomaticResizeWindow($(".section"));
        $("button[name='bs_searchBtn']").on("click", function(e){

            e.stopPropagation();
            searchAction(true);
            isEndPage = false;


        });

        $(window).on('keyup', function(e){

            e.stopPropagation();

           if(e.keyCode == 13){

               if(Swal.isVisible()){

                   Swal.close();
               }else{

                   searchAction(true);
                   isEndPage = false;


               }

           }

        }).on("scroll", function(){

            let isTouched = parseInt($(window).scrollTop()) <=  (parseInt($(document).height()) - parseInt($(window).height()) + 1);


            if(isTouched){

                let flag = timeChecker.validateOverInterval();
                console.log("flag" + flag);
                if(flag){
                    searchAction(false);
                }

            }



        });


    });


    function selectDropdown(){

        $("#search_option").find(".dropdown-menu").children().each(function(){

            $(this).on("click", function(e){
               e.stopPropagation();
               let selected = $(this).html();
               let selectedVal = $(this).attr("value");
               let $bs_dropdown = $("#bs_dropdown");
                $bs_dropdown.html(selected);
                $bs_dropdown.dropdown("toggle");
                $bs_dropdown.find("input[name='bs_dropdownSelected']").val(selectedVal);

            });

        });

    }


    //ajax를 통해 검색된 결과를 가져옴
    function searchAction(isFirstRequest) {

        if(isEndPage) return;
        preLoader.show();


        if(isFirstRequest){
        pageNum = 0;
             key = $("input[name='bs_dropdownSelected']").attr("value");
             keyword = $("input[name='bs_keyword']").val();


            if($.trim(keyword).length < 2){

                swal({

                    text : "공백이거나 검색어가 너무 짧습니다. 검색어는 두 자리 이상 입력하십시오.",
                    type : "warning"

                });

                preLoader.hide();
                return;

            }

        }


        pageNum++;

        $.ajax({

            type: "get",
            url: "/board/searchAction",
            data: {

                search : keyword,
                search_check : key,
                pageNum : parseInt(pageNum)

            },
            success: function (response) {
                if(!response){
                    preLoader.hide(2000);
                    return;
                }
                (function(){

                    renderItems(response, isFirstRequest);

                    return (function(){
                        preLoader.hide(2000);
                    })()

                })();



            },
            error: function (xhs) {
                preLoader.hide(2000);
            }

        });


    }


    //ajax 호출을 통해 가져온 데이터를 화면에 전시함
    function renderItems(items, removeField) {

        let $cardForm = $("#bkCard");
        let $field = $("#block_field");

        if(removeField){
            $field.empty();
        }
        for (let i = 0; i < items.length; ++i) {

            let block = items[i];

            let $dummy = $cardForm.clone();
            $dummy.attr("id", null);
            $dummy.css("display", "block");
            $dummy.css("cursor", "pointer");
            $dummy.find("p[name='bk_title']").html(block.bl_title);
            $dummy.find("p[name='likeCount']").html(block.bl_count);
            console.log(block);
            if(block.hasOwnProperty("bl_img"))
            $dummy.find("img[name='bk_image']").attr("src", block.bl_img);
            $.ajax({

               url : "/getUserInfo",
               data : {profile : true, nick : true, introduce : null, uid : block.bl_writer},
               type: "get",
               success : function(response){

                    $dummy.find(".author").find(".text").find("span[name='user_nick']").html(response.nick);
                    $dummy.find(".author").find("img[name='user_profile']").attr("src", response.profile);

                },
                error : function(xhs, status, error){
                   console.log(error);
                }


            });
            //클릭시 Datail 보기로 이동
                $dummy.on("click", function (e) {

                    location.href="/board/boardView?bl_uid="+block.bl_uid;

                });


            $field.append($dummy);


        }


    }

</script>