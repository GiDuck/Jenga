<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div class="wrapper">
    <div class="main">
        <div id="mainSection" class="section">
            <div class="container">
                <div class="row">

                    <div class="col-8 ml-auto mr-auto text-center">
                        <div class="card card-raised page-carousel">
                            <div id="carousel-Indicators-Wrapper" class="carousel slide" data-ride="carousel">
                                <ol id="carousel-indicators-Inner" class="carousel-indicators"></ol>
                                <div id="carousel-inner" class="carousel-inner" role="listbox"></div>

                                <a class="left carousel-control carousel-control-prev" href="#carousel-Indicators-Wrapper" role="button" data-slide="prev">
                                    <span class="fa fa-angle-left"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="right carousel-control carousel-control-next" href="#carousel-Indicators-Wrapper" role="button" data-slide="next">
                                    <span class="fa fa-angle-right"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </div>
                        <br><br>
                    </div>
                </div>


            <div class="row">
               <div class="col-12 ml-auto mr-auto"><h2>인기 Blocks</h2>
                   <div class="dropdown">
                       <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true" style="border : none">
                           Dropdown
                           <span class="caret"></span>
                       </button>
                       <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                           <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Action</a></li>
                           <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Another action</a></li>
                           <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Something else here</a></li>
                           <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Separated link</a></li>
                       </ul>
                   </div>
                   <br><br>
               </div>
            </div>

                <div class="row items-row">
                    <div class="col-md-3 col-sm-4">
                        <div class="card card-plain text-center">
                            <div class="card-image">
                                <a href="#paper-kit">
                                    <img src="${pageContext.request.contextPath}/resources/assets/img/sections/pavel-kosov.jpg" alt="Rounded Image" class="img-rounded img-responsive">
                                </a>
                                <br><br>
                                <div class="text"><span class="name">제목입니다... 제목...</span></div>

                                <div class="card-body details-center">
                                    <a href="#paper-kit">
                                        <div class="author">
                                            <img src="${pageContext.request.contextPath}/resources/assets/img/faces/joe-gardner-2.jpg" alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                            <div class="text">
                                                <span class="name">Jane Doe</span>
                                                <div class="meta">Drawn on 23 Nov</div>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 text-center"><br><br><button class="btn btn-round btn-outline-danger">더 보기</button></div>

                </div>


                <br>
                <hr>
                <br>

                <div class="row">
                    <div class="col-12 ml-auto mr-auto"><h2>실시간 Blocks</h2><br><br>
                    </div>
                </div>

                <div class="row items-row">
                    <div class="col-md-3 col-sm-4">
                        <div class="card card-plain text-center">
                            <div class="card-image">
                                <a href="#paper-kit">
                                    <img src="${pageContext.request.contextPath}/resources/assets/img/sections/pavel-kosov.jpg" alt="Rounded Image" class="img-rounded img-responsive">
                                </a>
                                <br><br>
                                <div class="text"><span class="name">제목입니다... 제목...</span></div>

                                <div class="card-body details-center">
                                    <a href="#paper-kit">
                                        <div class="author">
                                            <img src="${pageContext.request.contextPath}/resources/assets/img/faces/joe-gardner-2.jpg" alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                            <div class="text">
                                                <span class="name">Jane Doe</span>
                                                <div class="meta">Drawn on 23 Nov</div>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 text-center"><br><br><button class="btn btn-round btn-outline-danger">더 보기</button></div>

                </div>


                <div class="row" style="display:none">
                    <div class="col-md-4 text-center ml-auto mr-auto">
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

    function initCarousel(){

        let $innerIndicator = $("#carousel-indicators-Inner");
        let $innerCarousel = $("#carousel-inner");

        //ajax를 통해 carousel 슬라이더에 이미지와 내용, url을 받아와 넣는다.
        $.ajax({

            url : "",
            type : "GET",
            data : null,
            dataType : "application/json",
            success : function(response){

                let pages = JSON.parse(response);


                for(let i = 0 ; i < pages.length; ++i){
                    let nowPage = pages[i];
                    let $indicatorChild = $("<li>").attr("data-target", "#carousel-Indicators-Wrapper").attr("data-slide-to", i);

                    $innerIndicator.append($indicatorChild);

                    let $carouselChild = $("<div>").addClass("carousel-item text-center");
                    let $img = $("<img>").addClass("d-block img-fluid w-100 h-75").attr("src", nowPage.src).attr("onerror", 'this.src="${pageContext.request.contextPath}/resources/assets/img/default/no_image4.png"');
                    let $text;

                    if(nowPage.introduce){

                        $text = $("<div>").addClass("carousel-caption d-none d-md-block").append($("<span>").html(nowPage.introduce));
                        $img.append($text);
                    }

                    $carouselChild.append($img);
                    $innerCarousel.append($carouselChild);

                    if(i==0){
                        $indicatorChild.addClass("active");
                        $carouselChild.addClass("active");
                    }


                }

                let $carousel = $(".carousel").carousel({
                    interval : 2000
                });



            },
            error : function(error){

                let $indicatorChild = $("<li>").attr("data-target", "#carousel-Indicators-Wrapper").attr("data-slide-to", 0).addClass("active");
                $innerIndicator.append($indicatorChild);
                let $carouselChild = $("<div>").addClass("carousel-item active text-center");
                let $img = $("<img>").addClass("d-block img-fluid w-100 h-75").attr("src", "${pageContext.request.contextPath}/resources/assets/img/default/no_image4.png");
                $carouselChild.append($img);
                $innerCarousel.append($carouselChild);

                console.log("carousel image loading fail...");

            }

        });

    }


    $(document).ready(function(){

        navbarObj.setType("bg-info");
        navbarObj.addHeadBlock();
        initCarousel();

        setAutomaticResizeWindow($("#mainSection"));


    });



</script>