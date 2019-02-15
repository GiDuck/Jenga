<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<style>

    .dropdown{
        margin-bottom : 10px;
        position: center;
    }

    .card-refine{
        padding : 30px;
    }

</style>


<div class="wrapper">
    <div class="main">
        <div class="section section-gray">
            <div class="container">

                <div class="row">

                    <div class="col-md-3 col-sm-4">

                        <div class="card card-refine">
                            <div class="row text-center panel-group w-100 h-100">

                                <div class="row" style="display: flex">

                                    <div class="col-md-10"><input type="text" class="form-control border-input w-100" name="bs_keyword" placeholder="Search..." >&nbsp;&nbsp;
                                    </div>
                                    <div class="col-md-2">
                                        <button name="bs_searchBtn" class="btn btn-just-icon"><i class="fa fa-search w-90 h-90"></i>
                                        </button>
                                    </div>

                                    <div class="dropdown col-12">
                                        <button class="dropdown-toggle btn btn-round btn-info w-100" href="#" role="button" id="select_searchOpt" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">검색 옵션</button>

                                        <div class="dropdown-menu" aria-labelledby="bs_dropdown">
                                            <a class="dropdown-item" value="keyword">키워드</a>
                                            <a class="dropdown-item" value="name">글쓴이</a>
                                            <a class="dropdown-item" value="tag">태그</a>
                                        </div>

                                        <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                    </div>
                                </div>



                                <div class="row">
                                    <div class="dropdown col-12">
                                        <button class="dropdown-toggle btn btn-round btn-danger w-100" href="#" role="button" id="select_mainCtg" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">메인 카테고리</button>

                                        <div class="dropdown-menu" aria-labelledby="bs_dropdown">
                                            <a class="dropdown-item" value="keyword">키워드</a>
                                            <a class="dropdown-item" value="name">글쓴이</a>
                                            <a class="dropdown-item" value="tag">태그</a>
                                        </div>

                                        <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                    </div>


                                    <div class="dropdown col-12">
                                        <button class="dropdown-toggle btn btn-round btn-danger w-100" href="#" role="button" id="select_subCtg" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">서브 카테고리</button>

                                        <div class="dropdown-menu" aria-labelledby="bs_dropdown">
                                            <a class="dropdown-item" value="keyword">키워드</a>
                                            <a class="dropdown-item" value="name">글쓴이</a>
                                            <a class="dropdown-item" value="tag">태그</a>
                                        </div>

                                        <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                    </div>
                                </div>


                            </div>


                        </div>

                        <div class="col-md-9 col-sm-8">

                            <div class="row items-row text-left" id="block_field">
                            </div>

                        </div>
                    </div>


                    <br><br>


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
                                                    <div class="meta w-100"><i class="fa fa-heart" name="likeIcon" style="color: red"></i>0</div>
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
</div>


<script>

    let pageNum = 1;
    let key;
    let keyword;
    let isEndPage = false;
    let preLoader = new PreLoader();

    $(document).ready(function () {

        let timeChecker = new TimeChecker();
        navbarObj.addHeadBlock("gray");
        navbarObj.setType("bg-info");
        preLoader.init();
        selectDropdown();
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

            let isTouched = parseInt($(window).scrollTop()) == $(document).height() - ($(window).height() + 1);

            if(isTouched){

                let flag = timeChecker.validateOverInterval();
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




        $.ajax({

            type: "GET",
            url: "/board/searchAction",
            data: {

                search : keyword,
                search_check : key,
                pageNum : parseInt(pageNum)

            },
            success: function (response) {

                if(response.length == 0 || !response){

                    swal({

                        text : "검색 결과가 존재하지 않습니다!",
                        type : "warning"

                    });

                    preLoader.hide();
                    return;

                }

                (function(){

                    renderItems(response["board"], isFirstRequest);

                    if(response["count"]/(pageNum*10) > 1){
                        pageNum++;
                        isEndPage = false;

                    }else{
                        isEndPage = true;
                    }


                    return (function(){

                        setTimeout(function(){

                            preLoader.hide();

                        }, 2000);

                    })()

                })();



            },
            error: function (xhs, status, error) {

                swal({

                    text : "검색에 실패하였습니다.",
                    type : "error"

                });

                preLoader.hide();
                return;

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




            $.ajax({

                url : "/getUserInfo",
                data : {profile : null, nick : null, introduce : null, uid : block.bl_writer},
                type: "GET",
                success : function(response){

                    console.log(JSON.stringify(response));
                    $dummy.find(".author").find(".text").find("span[name='user_nick']").html(response.nick);
                    $dummy.find(".author").find("img[name='user_profile']").attr("src", response.profile);

                },
                error : function(xhs, status, error){


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