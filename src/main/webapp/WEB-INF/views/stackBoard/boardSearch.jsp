<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<div class="wrapper">
    <div class="main">
        <div class="section">
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
                <div class="row items-row">

                    <div class="col-md-3 col-sm-4 ml-auto" id="bkCard" style="display : none">
                        <div class="card card-plain text-center">
                            <div class="card-image">
                                <a>
                                    <img name="bk_image"
                                         onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/sections/pavel-kosov.jpg'"
                                         alt="Rounded Image" class="img-rounded img-responsive">
                                </a>
                                <br><br>
                                <div class="text"><p name="bk_title" class="name"></p></div>

                                <div class="card-body details-center">
                                    <a>
                                        <div class="author">
                                            <img
                                                 onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/faces/joe-gardner-2.jpg'"
                                                 alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                            <div class="text">
                                                <span class="name"></span>
                                                <div class="meta"><i class="fas fa-heart" name="likeIcon">0</i></div>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="w-100 text-center" id="loaderContainer" style="visibility: hidden;">
                            <div class="preloader">
                                <div class='uil-reload-css' style=''>
                                    <div></div>
                                </div>
                                <h5>Loading More </h5>
                            </div>
                    </div>


                </div>
            </div>
        </div>
    </div>
</div>


<script>

    function PreLoader(){

        this.preloader = $("#loaderContainer");

    }




    PreLoader.prototype.show = function(){

        this.preloader.css("visibility", "visible");

    };

    PreLoader.prototype.hide = function(){

        this.preloader.css("visibility", "hidden");

    }

    let preLoader = new PreLoader();



    $(document).ready(function () {

        setNavType("blue");
        selectDropdown();
        $("button[name='bs_searchBtn']").on("click", function(e){

            e.stopPropagation();
            searchAction();

        });

    });


    function selectDropdown(){

        $("#search_option").find(".dropdown-menu").children().each(function(){

            $(this).on("click", function(e){
               e.stopPropagation();
               let selected = $(this).html();
               let selectedVal = $(this).val();
               let $bs_dropdown = $("#bs_dropdown");
                $bs_dropdown.html(selected);
                $bs_dropdown.dropdown("toggle");
                $bs_dropdown.find("input[name='bs_dropdownSelected']").val(selectedVal);

            });

        });

    }

    //ajax를 통해 검색된 결과를 가져옴
    function searchAction() {

        preLoader.show();

        let key = $("#bs_dropdown").find("input[name='bs_dropdownSelected']").val();
        let keyword = $("input[name='bs_keyword']").val();

        console.log($.trim(keyword));

        if($.trim(keyword).length < 2){

            swal({

                text : "공백이거나 검색어가 너무 짧습니다. 검색어는 두 자리 이상 입력하십시오.",
                type : "warning"

            });

            preLoader.hide();
            return;

        }


        $.ajax({

            type: "GET",
            url: "/board/searchAction",
            data: {

                search : keyword,
                search_check : key

            },
            success: function (response) {

                console.log("받아온거..");
                console.log(response);

                new Promise(function() {

                    renderItems(response);

                }).then(function(){

                    setTimeout(function(){

                        preLoader.hide();

                    }, 1000);

                });

            },
            error: function (xhs, status, error) {

                swal({

                    text : "검색에 실패하였습니다.",
                    type : "error"

                });

                preLoader.hide();
                console.log(error + " " + status);

            }

        });


    }


    //ajax 호출을 통해 가져온 데이터를 화면에 전시함
    function renderItems(items) {

        let $cardForm = $(document).find("#bkCard");

        for (let i = 0; i < items.length; ++i) {

            let blockItem = items[i].block; //boardDTO
            let blockImg = items[i].blockImg; //block image
            let writerInfo = items[i].writer; //글쓴이 정보

            let $dummy = $cardForm.clone();
            $dummy.attr("id", null);
            $dummy.find("img[name='bk_image']").attr("src", blockImg);
            $dummy.find("p[name='bk_title']").attr("src", blockItem.bl_title);
            $dummy.find(".author > img").attr("src", writerInfo.writerProfile);
            $dummy.find(".author > .name").attr("src", writerInfo.writerName);

            //클릭시 Datail 보기로 이동
            $dummy.on("click", function (e) {

                location.href="/board/boardView?bl_uid="+blockItem.bl_uid;

            });


        }


    }


</script>