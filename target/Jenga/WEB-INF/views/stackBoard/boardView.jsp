<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>


<div class="wrapper">
    <div class="main">
        <div class="section">
            <div class="container">
                <div class="row">
                    <div class="col-md-6 col-12 ml-auto mr-auto text-center">

                        <div class="row text-center">

                            <div class="col-md-10"><input type="text" class="form-control border-input w-100 h-100" placeholder="Search...">&nbsp;&nbsp;</div>
                            <div class="col-md-2"> <button class="btn btn-just-icon w-100 h-100" ><i class="fa fa-search w-90 h-90"></i></button></div>

                        </div>
                        <br>
                    </div>
                </div>
                <div class="row items-row">
                    <div class="col-md-3 col-sm-4 ml-auto">
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
                    <div class="col-md-4 col-sm-4">
                        <div class="card card-plain text-center">
                            <div class="card-image">
                                <a href="#paper-kit">
                                    <img src="${pageContext.request.contextPath}/resources/assets/img/sections/por7o.jpg" alt="Rounded Image" class="img-rounded img-responsive">
                                </a>
                                <div class="card-body details-center">
                                    <a href="#paper-kit">
                                        <div class="author">
                                            <img src="${pageContext.request.contextPath}/resources/assets/img/faces/erik-lucatero-2.jpg" alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                            <div class="text">
                                                <span class="name">Tom Hanks</span>
                                                <div class="meta">Drawn on 23 Jan</div>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-4 mr-auto">
                        <div class="card card-plain text-center">
                            <div class="card-image">
                                <a href="#paper-kit">
                                    <img src="${pageContext.request.contextPath}/resources/assets/img/sections/vincent-versluis.jpg" alt="Rounded Image" class="img-rounded img-responsive">
                                </a>
                                <div class="card-body details-center">
                                    <a href="#paper-kit">
                                        <div class="author">
                                            <img src="${pageContext.request.contextPath}/resources/assets/img/chet_faker_2.jpg" alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                            <div class="text">
                                                <span class="name">Chet Faker</span>
                                                <div class="meta">Drawn on 20 Jan</div>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="row">
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

    $(document).ready(function(){

        setNavType("blue");


    });



</script>