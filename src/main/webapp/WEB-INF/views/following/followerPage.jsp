<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: gdtbg
  Date: 2019-01-03
  Time: 오전 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- End Navbar -->
<div class="wrapper">
    <div class="page-header page-header-small"
         style="background-image: url('${pageContext.request.contextPath}/resources/assets/img/default/followBackground.jpg')">
        <div class="filter"></div>
    </div>
    <div class="profile-content section-white-gray">
        <div class="container">
            <div class="row owner">
                <div class="col-md-2 col-sm-4 col-6 ml-auto mr-auto text-center">
                    <div class="avatar">
                        <img src="" alt="Circle Image" class="img-circle img-responsive"
                             onerror="this.src='${pageContext.request.contextPath}/resources/assets/img/placeholder.jpg'">
                        <div class="following">
                            <button name="profileBtn" class="btn btn-sm btn-info btn-just-icon" rel="tooltip"
                                    title="Follow"><i class="nc-icon nc-simple-add"></i></button>
                        </div>
                    </div>
                    <div class="name">
                        <h4 id="profile_title">Rihanna</h4>
                        <h4 id="profile_nick">
                            <small>@rihanna</small>
                        </h4>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 ml-auto mr-auto text-center">
                    <p name="profile_description"></p>
                    <div class="description-details">
                        <ul class="list-unstyled">
                            <li><i class="fa fa-map-marker"></i> ANTI</li>
                            <li><i class="fa fa-link"></i>
                                <a id="profile_link" href="javascript:void(0);">rihanna.com</a>
                            </li>
                            <li><i class="fa fa-calendar"></i> <span id="profile_joinDate">Joined</span></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="profile-tabs">
                <div class="nav-tabs-navigation">
                    <div class="nav-tabs-wrapper">
                        <ul id="tabs" class="nav nav-tabs" role="tablist">
                            <li class="nav-item ">
                                <a class="nav-link active" href="#tweets" data-toggle="tab" role="tab">Recent
                                    Writing</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#connections" data-toggle="tab" role="tab">Active Log</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="my-tab-content" class="tab-content">
                    <div class="tab-pane active" id="tweets" role="tabpanel">
                        <div class="row">
                            <div class="col-md-8">
                                <div class="tweets">
                                    <div class="media">
                                        <a class="pull-left" href="#paper-kit">
                                            <div class="avatar">
                                                <img class="media-object"
                                                     src="${pageContext.request.contextPath}/resources/assets/img/rihanna.jpg"
                                                     alt="..."/>
                                            </div>
                                        </a>
                                        <div class="media-body">
                                            <strong>Rihanna</strong>
                                            <h5 class="media-heading">
                                                <small>@rihanna &middot; 1h</small>
                                            </h5>
                                            <p>It's just beyond the vault. Discover room 7 of the
                                                <a href="javascript: void(0);" class="link-danger">#ANTIdiaRy</a> at
                                                <a href="" class="link-info">http://smarturl.it/AntidiaRyTW</a>
                                            </p>
                                            <div class="media-footer">
                                                <a href="#paper-kit" class="btn btn-link">
                                                    <i class="fa fa-reply"></i>
                                                </a>
                                                <a href="#paper-kit" class="btn btn-success btn-link">
                                                    <i class="fa fa-retweet"></i> 2.1k
                                                </a>
                                                <a href="#paper-kit" class="btn btn-danger btn-link">
                                                    <i class="fa fa-heart"></i> 3.2k
                                                </a>
                                                <div class="dropdown">
                                                    <button id="dLabel" type="button"
                                                            class="btn btn-just-icon btn-link btn-lg"
                                                            data-toggle="dropdown" aria-haspopup="true"
                                                            aria-expanded="false">
                                                        <i class="fa fa-ellipsis-h"></i>
                                                    </button>
                                                    <ul class="dropdown-menu dropdown-menu-right">
                                                        <li class="dropdown-item">
                                                            <a href="#paper-kit">
                                                                <div class="row">
                                                                    <div class="col-sm-2">
                                                                        <span class="icon-simple"><i
                                                                                class="fa fa-envelope"></i></span>
                                                                    </div>
                                                                    <div class="col-sm-9">Direct Message</div>
                                                                </div>
                                                            </a>
                                                        </li>
                                                        <div class="dropdown-divider"></div>
                                                        <li class="dropdown-item">
                                                            <a href="#paper-kit">
                                                                <div class="row">
                                                                    <div class="col-sm-2">
                                                                        <span class="icon-simple"><i
                                                                                class="fa fa-microphone-slash"></i></span>
                                                                    </div>
                                                                    <div class="col-sm-9">Mute</div>
                                                                </div>
                                                            </a>
                                                        </li>
                                                        <div class="dropdown-divider"></div>
                                                        <li class="dropdown-item">
                                                            <a href="#paper-kit">
                                                                <div class="row">
                                                                    <div class="col-sm-2">
                                                                        <span class="icon-simple"><i
                                                                                class="fa fa-exclamation-circle"></i></span>
                                                                    </div>
                                                                    <div class="col-sm-9">Report</div>
                                                                </div>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- end media -->

                                    <!-- end media -->

                                    <!-- end media -->

                                    <!-- end media -->
                                    <br/>
                                    <div class="text-center">
                                        <button class="btn btn-outline-info btn-round">Load more tweets</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 col-sm-6">
                                <div class="card card-with-shadow">
                                    <div class="card-body">
                                        <h5 class="card-title">Recommend People</h5>
                                        <h5>
                                            <small>
                                                <a href="javascript: void(0);" class="link-info">View all</a>
                                            </small>
                                        </h5>
                                        <br>

                                        <div class="accounts-suggestion">
                                            <ul class="list-unstyled">
                                                <c:forEach items="${recommend}" var="recommend">
                                                    <li class="account"> <%--여기부터--%>
                                                        <div class="row">
                                                            <div class="col-md-3">
                                                                <div class="avatar">
                                                                    <img src="${pageContext.request.contextPath}/resources/assets/img/${recommend.get("MEM_PROFILE")}.jpg"
                                                                         alt="Circle Image"
                                                                         class="img-circle img-no-padding img-responsive">
                                                                </div>
                                                            </div>
                                                            <div class="col-md-7 description-section">

                                <span>${recommend.get("MEM_NICK")}
                                    <br/>
                                  <a href="#paper-kit" class="text-muted">@${recommend.get("MEM_NICK")}</a>
                                </span>
                                                                <br/>
                                                                <span class="text-muted">
                                  <small>LEVEL
                                          ${recommend.get("MEM_LEVEL")}
                                  </small>

                                </span>
                                                            </div>
                                                            <div class="col-md-2 follow">
                                                                <button class="btn btn-sm btn-outline-info btn-just-icon">
                                                                    <i class="fa fa-plus"></i></button>
                                                            </div>
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <!-- end card -->
                                <div class="card card-with-shadow">
                                    <div class="card-body">
                                        <h5 class="card-title"><b>Trends</b> &middot;
                                            <small>
                                                <a href="javascript: void(0);" class="link-info">Change</a>
                                            </small>
                                        </h5>
                                        <br>
                                        <div class="hashtag-suggestions">
                                            <ul class="list-unstyled" style="padding : 5px">
                                                <li>
                                                    <a href="#paper-kit" class="link-danger">#JeSuisToujoursCharlie</a>
                                                </li>
                                                <li>
                                                    <a href="#paper-kit">Oculus Rift</a>
                                                </li>
                                                <li>
                                                    <a href="#paper-kit"
                                                       class="link-danger">#CarenAndLarryAreNotReal</a>
                                                </li>
                                                <li>
                                                    <a href="#paper-kit" class="link-danger">#Twitter10k</a>
                                                </li>
                                                <li>
                                                    <a href="#paper-kit">EXCLUSIVE MOVE WITHINGTON</a>
                                                </li>
                                                <li>
                                                    <a href="#paper-kit">London</a>
                                                <li>
                                                    <a href="#paper-kit">DJ Khaled Snapchat</a>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <!-- end card -->
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane text-center" id="connections" role="tabpanel"></div>
                    <div class="tab-pane" id="media" role="tabpanel"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    function renderProfile() {

        /*  $.ajax({
              url : "/recommendFollower",
              type : "post",
              success(response){
                  console.log(response);
              },
              error : function (xhs, status, error) {
                  swal({
                      text: "팔로우 추천에 실패하였습니다.",
                      type: "error"
                  });
              }
          })*/

        //TODO ajax로 데이터 로드
        /* $.ajax({
             url : "/followcheck",
             type: "post",
             data: {
                 //"follow_iuid": 이 페이지닝겐 iuid
             },
             success(response){
                 if (response.indexOf("myIuid") != -1){
                     $("button[name='profileBtn']").removeClass("btn-info").addClass("bg-danger").find("i").removeClass().addClass("nc-icon nc-ruler-pencil");
                 } else if(response.indexOf("notFollow") != -1){
                     //팔로아님 모양
                 } else{
                     //팔로상태 모양
                 }
             },
             error: function(xhs, status, error) {

                 swal({
                     text: "팔로우에 실패하였습니다.",
                     type: "error"
                 });
             }
         })*/
        //TODO follower uid 확인 후 만약 session에 있는 uid와 동일하면 페이지 수정 버튼 활성화 (현재 보고있는 사람 == 팔로워 페이지 주인)
        /* if(false){
             $("button[name='profileBtn']").removeClass("btn-info").addClass("bg-danger").find("i").removeClass().addClass("nc-icon nc-ruler-pencil");
         }*/


    }

    $(document).ready(function () {

        navbarObj.setType("bg-danger");

        $("button[name='profileBtn']").on("click", function () {

        })


    });

</script>