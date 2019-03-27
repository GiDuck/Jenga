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
                                        <button id="searchOptionBtn" class="dropdown-toggle btn btn-round btn-info w-100" href="#" role="button" id="select_searchOpt" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">검색 옵션</button>

                                        <div id="searchOption" class="dropdown-menu" aria-labelledby="bs_dropdown">
                                            <a class="dropdown-item" value="keyword">키워드</a>
                                            <a class="dropdown-item" value="name">글쓴이</a>
                                            <a class="dropdown-item" value="tag">태그</a>
                                        </div>

                                        <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                    </div>
                                </div>



                                <div class="row">
                                    <div class="dropdown col-12">
                                        <button class="dropdown-toggle btn btn-round btn-danger w-100" href="#"
                                                role="button"
                                                id="mainCategory"
                                                data-toggle="dropdown" aria-haspopup="true"
                                                aria-expanded="false">메인 카테고리</button>

                                        <div id="mainCategoryItem" class="dropdown-menu" aria-labelledby="bs_dropdown"></div>

                                        <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                    </div>


                                    <div class="dropdown col-12">
                                        <button class="dropdown-toggle btn btn-round btn-danger w-100" href="#" role="button"
                                                id="subCategory"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">서브 카테고리</button>

                                        <div id="subCategoryItem" class="dropdown-menu" aria-labelledby="bs_dropdown">
                                        </div>

                                        <input type="hidden" name="bs_dropdownSelected" value="keyword">

                                    </div>
                                </div>


                            </div>


                        </div>


                    </div>

                    <div class="col-md-9 col-sm-8">
                        <div class="row items-row text-left" id="block_field">
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
                                                <div class="col-12 w-100">
                                                    <p name="user_nick" class="name w-100"></p>
                                                </div>
                                                <div class="col-12 w-100">
                                                    <div class="meta w-100"><i class="fa fa-heart" name="likeIcon" style="color: red"></i><p id="likeCount">0</p></div>
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


    let preLoader = new PreLoader();
    let categories = JSON.parse('${categories}');

    $(document).ready(function () {

        navbarObj.addHeadBlock("gray");
        navbarObj.setType("bg-info");
        setWindowSize($(document));
        preLoader.init();
        selectDropdown();
        getData();
        setCategory(categories);

        $("button[name='bs_searchBtn']").on("click", function (e) {

            e.stopPropagation();
            searchAction();


        });

        $("#searchOption").find(".dropdown-item").on("click", function(e){
            $("#searchOptionBtn").html($(e.target).html());
        });

        $(window).on('keyup', function (e) {

            e.stopPropagation();

            if (e.keyCode == 13) {

                searchAction();

            }


        });

    });


        function getData() {


            $.ajax({
                url: "/board/getMyFavoriteBlock",
                type: "get",
                success: function (response) {
                    renderItems(response);
                },
                error: function (xhs) {
                    console.log(xhs.status);
                }


            });

        }

        function selectDropdown() {

            $("#search_option").find(".dropdown-menu").children().each(function () {

                $(this).on("click", function (e) {
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

        //ajax 호출을 통해 가져온 데이터를 화면에 전시함
        function renderItems(items, removeField) {

            let $cardForm = $("#bkCard");
            let $field = $("#block_field");

            if (removeField) {
                $field.empty();
            }

            for (let i = 0; i < items.length; ++i) {

                let block = items[i];
                let $dummy = $cardForm.clone();
                $dummy.attr("id", null);
                $dummy.css("display", "block");
                $dummy.css("cursor", "pointer");
                $dummy.find("p[name='bk_title']").html(block.bl_title);
                $dummy.find("img[name='bk_image']").attr("src", block.bl_img);
                $dummy.find("#likeCount").text(block.bl_count);


                $.ajax({

                    url: "/getUserInfo",
                    data: {profile: true, nick: true, introduce: null, uid: block.bl_writer},
                    type: "GET",
                    success: function (response) {

                        console.log(JSON.stringify(response));
                        $dummy.find(".author").find(".text").find("p[name='user_nick']").html(response.nick);
                        $dummy.find(".author").find("img[name='user_profile']").attr("src", response.profile);

                    },
                    error: function (xhs, status, error) {


                    }


                });

                //클릭시 Datail 보기로 이동
                $dummy.on("click", function (e) {
                    location.href = "/board/boardView?bl_uid=" + block.bl_uid;
                });

                $field.append($dummy);


            }


        }

    //카테고리를 설정하는 함수
    function setCategory(categoryObj){

        let $mainField = $("#mainCategoryItem");
        let mainCategory = Object.keys(categoryObj);


        let $subField = $("#subCategoryItem");
        let $childForm = $("<a>").addClass("dropdown-item");

        for(let i = 0; i<mainCategory.length; ++i){

            let $mainChild = $childForm.clone();
            $mainChild.html(mainCategory[i]);

            $mainChild.on('click', function(e){
                e.preventDefault();
                let selected = $(this).html();

                //메인 카테고리 선택시 드롭다운 메뉴가 선택한 카테고리로 초기화
                $("#mainCategory").html(selected);
                let subCategory = categoryObj[selected];

                $subField.empty();
                $("#subCategory").html(subCategory[0]);

                for(let j=0; j<subCategory.length; ++j){
                    let $subChild = $childForm.clone();
                    $subChild.html(subCategory[j]);
                    $subField.append($subChild);

                    $subChild.on('click', function(e){
                        e.preventDefault();
                        $("#subCategory").html($(this).html());

                    });


                }


            });

            $mainField.append($mainChild);



        }

    }


</script>