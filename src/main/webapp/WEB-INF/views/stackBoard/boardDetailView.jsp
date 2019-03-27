<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>


<!-- End Navbar -->
<div class="main">
    <div class="section">
        <div class="container">
            <form>
                <div class="row">
                    <div class="col-md-5 col-sm-5">
                        <div class="fileinput fileinput-new text-center" data-provides="fileinput">
                            <div class="fileinput-new thumbnail img-no-padding" style="max-width: 370px; max-height: 250px;">
                                <img id="thumbnail_image" src="" alt="..." onerror="this.src='${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'">
                            </div>
                            <div class="fileinput-preview fileinput-exists thumbnail img-no-padding" style="max-width: 370px; max-height: 250px;"></div>

                        </div>
                        <h6>Tags</h6>
                        <div id="tags">
                            <input id="tags_inputField" class="tagsinput" data-color="success" type="text" value="" data-role="tagsinput" disabled="disabled"/>
                        </div>
                        <br>
                        <h6>Categories</h6>
                        <div id="bd_category" class="form-control" style="border : 0"></div>
                        <br>
                        <h6>Like</h6>
                        <div class="row">
                        <div id="heartContainer" class="feed col-1" >
                             <div id="likeBtn" class="heart" rel="like"></div>

                        </div>
                        <div id="likeCount" class="likeCount" style="align-items: center; display: flex"></div>
                    </div>
                    </div>
                    <div class="col-md-7 col-sm-7">
                        <div class="form-group">
                            <h6>title</h6>
                            <div class="form-control border-input" id="bd_title"></div>
                        </div>
                        <div class="form-group">
                            <h6>introduce</h6>
                            <div class="form-control border-input" id="bd_introduce" style="width:100%; min-height : 100px;"></div>
                        </div>
                        <div class="form-group">
                            <h6>date</h6>
                            <div class="form-control border-input" id="bd_date"></div>
                        </div>

                        <div class="form-group">
                            <h6>북마크 가져오기</h6>
                            <div class="dropdown">
                            <button id="saveBookmark" class="btn btn-secondary dropdown-toggle w-100" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">북마크</button>

                            <div id="saveBookmarkType" class="dropdown-menu w-100" aria-labelledby="saveBookmark">
                                <a class="dropdown-item w-100" value="chrome">Chrome</a>
                                <a class="dropdown-item w-100" value="firefox">Firefox</a>
                                <a class="dropdown-item w-100" value="explore">IE</a>
                            </div>
                        </div>
                        </div>
                    </div>


                    <div class="form-group col-12 w-80">
                        <hr>
                        <div class="media">
                            <a class="pull-left" href="#paper-kit">
                                <div class="avatar big-avatar">
                                    <img id="writer_image" class="media-object" alt="user profile" src="" onerror="this.src='${pageContext.request.contextPath}/resources/assets/img/placeholder.jpg'">
                                </div>
                            </a>
                            <div class="media-body">
                                <h4 id="writer_name" class="media-heading"></h4>
                                <span id="writer_description"></span>

                                <div class="pull-right">
                                    <div id="writerPanel">
                                        <div id="thisBlockWriterPanel"  style="display : none">
                                        <a id="modifyBtn" href= "/board/modifyBlock?bl_uid=${bl_uid}" class="btn btn-info btn-round "> <i class="nc-icon nc-ruler-pencil"></i>&nbsp Modify</a>&nbsp
                                        <div id="removeBtn" class="btn btn-danger btn-round "> <i class="nc-icon nc-simple-remove"></i>&nbsp Remove</div>&nbsp
                                        </div>
                                        <a name="following" href="#" class="btn btn-success btn-round "> <i class="fa fa-reply"></i>&nbsp Follow</a>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="form-group col-sm-12">
                        <h6>북마크 목록</h6>
                        <div id="bd_bookmarks"></div>

                    </div>

                    <div class="form-group col-sm-12">
                        <h6>Description</h6>
                        <section id="editor">
                            <div id="bd_description" class="form-control border-input" style="width:100%; min-height : 380px;"></div>
                        </section>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<script>

    let blockObj;
    let blockJson;
    let $followBtn = $("a[name='following']");
    let isFollowing;
    let isEmptySession = ${empty sessionScope.Member};




    function setData(){


        //json으로 넘어온 map object를 js에서 사용할 수 있는 object 형식으로 파싱
        blockJson = ${map.bookmarks};
        blockObj = JSON.parse(blockJson['_value']);


        //작성자 이름
        $("#writer_name").html('${map.mem_nick}');
        //작성자 소개
        $("#writer_description").html('${map.mem_introduce}');
        //작성자 이미지
        $("#writer_image").attr("src", '${map.mem_profile}');
        //선택한 태그들
        $("#tags_inputField").val(${map.tag});
        $("#thumbnail_image").attr("src", '${map.bti_url}');
        //블록 제목
        $("#bd_title").html('${map.bl_title}');
        //블록 소개
        $("#bd_introduce").html('${map.bl_introduce}');
        //블록 내용
        $("#bd_description").html('${map.bl_description}');
        //블록 좋아요 개수
        $("#likeCount").html('${map.likes}');
        //카테고리
        let categoryStr = '${map.bl_mainCtg}' + " > "  + '${map.bl_smCtg}';
        $("#bd_category").html(categoryStr);


        //날짜
        let dateObj = new Date(parseInt('${map.bl_date}'));
        let dateStr = dateObj.getFullYear() + "년 " + (dateObj.getMonth()+1) + " 월" + dateObj.getDate() + " 일 "
            + dateObj.getHours() + ":" + dateObj.getMinutes();
        $("#bd_date").html(dateStr);


        //비동기로 북마크 로딩
        (function(){

            setBookmarks(blockObj);

        }());


        $("#removeBtn").on("click", function(e){

            e.stopPropagation();
            e.preventDefault();

            swal({
               title : "게시글 삭제",
               text : "게시글을 삭제 합니다. 삭제된 게시글은 복구할 수 없습니다. 정말 진행하시겠습니까?",
               type : "warning",
               showCancelButton : true,
               confirmButtonText : "삭제하기",
               cancelButtonText : "취소하기",
               reverseButtons : true

            }).then(function(result){
                if(!result.value) return;
                $.ajax({

                    url : "/board/delBlock",
                    type : "get",
                    data : {bl_uid : '${bl_uid}'},
                    success : function(statusCode){
                        if(blockStatusCode.BLOCK_DEL_SUCCESS == statusCode){
                            swal("삭제 성공", "게시글이 성공적으로 삭제되었습니다.", "success").then(function(){
                                window.location.replace("/");
                            });
                            return;
                        }

                        swal("삭제 실패", "게시글이 삭제되지 않았습니다.", "error");


                    },
                    error : function(xhs){
                        swal("삭제 실패", "게시글 삭제 중 오류가 발생하였습니다", "error");
                        console.log(xhs.error);
                    }

                })

            });

        });


        if(!isEmptySession){

            if(${map.bl_writer eq sessionScope.Member.mem_iuid}){
                $("#thisBlockWriterPanel").css("display", "inline");
            }
            $.ajax({

           url : "/board/followCheck",
           type : "GET",
           data : {bl_writer : encodeURI('${map.bl_writer}')},
           success : function(response){

               if(blockStatusCode.FOLLOW == response){
                   isFollowing = true;
                   convertToUnFollow();
               }else{
                   isFollowing = false;
                   convertToFollow();
               }


           },
            error : function(xhs){

               console.log("following check error...");
               console.log("status code... " + status);

            }


        });

        }

        $followBtn.on("click", function(e){

            e.stopPropagation();
            if(isEmptySession){
                swal("팔로우 실패", "로그인이 필요한 서비스 입니다. 먼저 로그인 해 주세요.", "warning");
                return;
            }
            let endPoint;
            if(isFollowing){
                endPoint = "/board/unFollow";
            }else{
                endPoint = "/board/follow";
            }

            $.ajax({

               type : "GET",
               data : { bl_writer : encodeURI('${map.bl_writer}')},
               url : endPoint,
               success : function(response){
                   if(response == blockStatusCode.FOLLOW_SUCCESS){
                      swal("팔로우 성공","이제부터 " + '${map.mem_nick}' + " 님의 소식을 받아 보실 수 있습니다.", "success");
                       convertToUnFollow();
                       isFollowing = true;

                   }else if(response == blockStatusCode.UNFOLLOW_SUCCESS){
                       swal("팔로우 취소", "이제 부터 " + '${map.mem_nick}' + " 님의 소식을 받아 보실 수 없습니다.","warning");
                       convertToFollow();
                       isFollowing = false;
                   }else if(response == blockStatusCode.FOLLOW_SAME_AUTH){
                       swal("팔로잉 실패", "내 글은 팔로우 할 수가 없습니다.","warning");
                   }else{
                       swal("팔로잉 실패", "팔로잉 도중에 에러가 발생 하였습니다.","error");
                   }
               },
                error : function(xhs, status, error){

                   swal("팔로잉 실패", "팔로잉 요청 중에 에러가 발생하였습니다..","error");
                   console.log("팔로잉 요청 에러..." + status);
                }

            });


        });


    }


    function convertToFollow(){

        $followBtn.html("FOLLOW").addClass("btn-info").removeClass("btn-danger");


    }

    function convertToUnFollow(){

        $followBtn.html("UNFOLLOW").addClass("btn-danger").removeClass("btn-info");

    }

    function setBookmarks(bookmarkElements){


        let jsonTreeSource = new Array();

        for(let i = 0 ; i < bookmarkElements.length; ++i){

            recursiveToJsonTreeFormat(bookmarkElements[i],jsonTreeSource);

        }

        let data = new Object();
        data.text = "북마크 목록";
        data.children = jsonTreeSource;

        $('#bd_bookmarks').jstree({
            'core' : {
                'data' : data
            }
        }).on("dblclick.jstree", function(e){

            e.preventDefault();
            let url = $(e.target).attr("value");

            if(url){
                window.open(url);
            }
        });



    }

    function recursiveToJsonTreeFormat(nowNode, jsonTreeSource){

        let tempObj;
        tempObj = new Object();
        tempObj.text = nowNode.title;
        tempObj.add_date = nowNode.add_date;


        if(nowNode.children){

            let children = nowNode.children;
            tempObj.children = new Array();
            let tempChildren = tempObj.children;

            for(let i = 0 ; i < children.length; ++i){

                recursiveToJsonTreeFormat(children[i], tempChildren);

            }

        }else{
            let icon = nowNode.icon;
            if(!icon){
                tempObj.icon = "nc-icon nc-paper";
            }else{
                tempObj.icon = icon;
            }

            tempObj.a_attr = {'value' : nowNode.url};


        }

        jsonTreeSource.push(tempObj);


    }


    let $likeBtn = $("#likeBtn");
    let liker = new TwitterHeart($likeBtn);

    $(document).ready(function(){

        $.ajax({
            url: "/board/isLikeExist/${bl_uid}",
            type:"GET",
            success: function (response) {
                if(response == blockStatusCode.LIKE_EXISTS) {
                    liker.toggle();
                }
            },error: {}
        });

        navbarObj.setType("bg-info");
        navbarObj.addHeadBlock();
        setData();

        $likeBtn.on("click", function(e){

            //세션체크
            let session = "${sessionScope.Member}";
            if(!session){

                swal({
                    text : "로그인이 필요한 서비스 입니다. 로그인 페이지로 이동하시겠습니까?",
                    type : "warning",
                    showCancelButton : true,
                    confirmButtonText: "이동"
                }).then(function(result){

                    if(result.dismiss == 'cancel') return;
                    else{
                        window.location.href="/login";
                    }
                });


            }else{

                $.ajax({
                    url: "/board/like/${bl_uid}",
                    type: "GET",
                    success : function (responseCount) {
                        $("#likeCount").html(responseCount);
                    },
                });
                e.stopPropagation();
                liker.toggle();
            }

        });

        let timer = undefined;

        $("#saveBookmarkType").children().each(function(){


            $(this).on("click", function(e){

                e.preventDefault();
                e.stopPropagation();


                if($(this).attr("value") == "chrome"){

                    (function(){
                        swal({

                            text : "파일을 저장하고 있습니다..",
                            type : "info"

                        });
                    })();


                    let now = new Date().getTime();

                    if(timer && now - timer < 3000){

                        swal({
                            text : "다운로드가 실행 중입니다.. 잠시 후 다시 실행 해 주세요.",
                            type : "warning"

                        });

                        return;
                    }
                        timer = new Date().getTime();

                   let bookmarkHTML = parseJsonToHTML(blockObj, '${map.bl_title}', '${map.bl_introduce}');
                   let temp = $("<a>").append(bookmarkHTML);
                   let declearStr = "\<\!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
                       "\<!-- This is an automatically generated file.\n" +
                       "     It will be read and overwritten.\n" +
                       "     DO NOT EDIT! --> \n\n";
                   let htmlRawResource = declearStr + (temp.html()).replace(/<\/p>|<\/dt>|/gi, '');

                   let tagRegex =/(<html>|<title>|<h1>|<h3|<dl>|<dt>|<a|<\/html>|<\/a|href\="|add_date\="|icon\="|last_modified\=")/gi;
                   let newLineRegex = /(<p>|<\/title>|<\/h1>|<\/dl>|<\/h3>|<\/a>)/gi;

                   let htmlResource = htmlRawResource.replace(tagRegex, function(x){

                           return x.toUpperCase();

                   });

                   htmlResource = htmlResource.replace(newLineRegex, function(x){

                   return x.toUpperCase() + "\n";

                   });

                   let dataURI = "data:attachment/html," +encodeURI(htmlResource);
                   let $tempTag = document.createElement('a');
                   let fileName = '${map.bl_title}';
                    fileName = fileName.replace(' ', '');
                    if(fileName.length > 10){

                       fileName = fileName.slice(0, 10);
                       fileName += "..";

                   }
                    $tempTag.href = dataURI;
                    $tempTag.target = '_blank';
                    $tempTag.download = fileName + '.html';
                    $tempTag.click();




                }else{

                    swal({

                        text : "현재 지원하지 않는 기능입니다.",
                        type : "warning"

                    });
                }


            });
        });




    });

</script>