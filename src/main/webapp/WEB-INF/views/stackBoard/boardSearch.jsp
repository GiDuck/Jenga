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
                <br><br>
                <div class="row items-row text-left" id="block_field">


                </div>
                <div class="w-100 text-center" id="loaderContainer" style="visibility: hidden;">
                    <div class="preloader">
                        <div class='uil-reload-css' style=''>
                            <div></div>
                        </div>
                        <h5>Loading More </h5>
                    </div>
                </div>

                <div class="col-md-3 col-sm-4" id="bkCard" style="display : none">
                    <div class="card card-plain text-center">
                        <div class="card-image">
                                <img name="bk_image" onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'"
                                     src="" alt="Rounded Image" class="img-rounded img-responsive">
                            <br><br>
                            <div class="text"><p name="bk_title" class="name"></p></div>

                            <div class="card-body details-center">
                                    <div class="author w-50 text-center">
                                        <img src="" onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/faces/joe-gardner-2.jpg'"
                                             alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                        <div class="text">
                                            <span class="name"></span>
                                            <div class="meta"><i class="fas fa-heart" name="likeIcon"></i>0</div>
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

        $(window).on('keyup', function(e){

            e.stopPropagation();

           if(e.keyCode == 13){


               if(Swal.isVisible()){

                   Swal.close();

               }else{

                   searchAction();

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
    function searchAction() {

        preLoader.show();

        let key = $("input[name='bs_dropdownSelected']").attr("value");
        let keyword = $("input[name='bs_keyword']").val();


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

                if(response.length == 0 || !response){

                    swal({

                        text : "검색 결과가 존재하지 않습니다!",
                        type : "warning"

                    });

                    preLoader.hide();
                    return;

                }

                (function(){

                    renderItems(response);

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
    function renderItems(items) {

        let $cardForm = $("#bkCard");
        let $field = $("#block_field");

        $field.empty();

        for (let i = 0; i < items.length; ++i) {

            let block = items[i];

            let $dummy = $cardForm.clone();
            $dummy.attr("id", null);
            $dummy.css("display", "block");
            //$dummy.find("img[name='bk_image']").attr("src", block.blockImg);
            $dummy.find("p[name='bk_title']").html(block.bl_title);
         /*   $dummy.find(".author > img").attr("src", block.writerProfile); */
            //$dummy.find(".author > .name").attr("src", block.bl_writer);

            //클릭시 Datail 보기로 이동
                $dummy.on("click", function (e) {

                    location.href="/board/boardView?bl_uid="+block.bl_uid;

                });

            $field.append($dummy);


        }


    }





</script>