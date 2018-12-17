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
                        <div id="bd_category" class="form-control border-input" style="visibility:hidden"></div>

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
                            <a id="saveBookmark" class="btn btn-secondary dropdown-toggle w-100" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">북마크</a>

                            <div id="saveBookmarkType" class="dropdown-menu w-100" aria-labelledby="saveBookmark">
                                <a class="dropdown-item w-100">Google</a>
                                <a class="dropdown-item w-100">Firefox</a>
                                <a class="dropdown-item w-100">IE</a>
                            </div>
                        </div>

                    </div>


                    <div class="form-group col-12 w-80">
                        <hr>
                        <div class="media">
                            <a class="pull-left" href="#paper-kit">
                                <div class="avatar big-avatar">
                                    <img id="writer_image" class="media-object" alt="64x64" src="" onerror="this.src='${pageContext.request.contextPath}/resources/assets/img/faces/kaci-baum-2.jpg'">
                                </div>
                            </a>
                            <div class="media-body">
                                <h4 id="writer_name" class="media-heading"></h4>
                                <span id="writer_description"/>
                                <div class="pull-right">
                                    <div id="writerPanel" style="display : none">
                                        <a href="#" class="btn btn-info btn-round "> <i class="nc-icon nc-ruler-pencil"></i>&nbsp Modify</a>&nbsp
                                        <a href="#" class="btn btn-danger btn-round "> <i class="nc-icon nc-simple-remove"></i>&nbsp Remove</a>&nbsp</div>
                                    <a href="#" class="btn btn-success btn-round "> <i class="fa fa-reply"></i>&nbsp Follow</a>
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

    let blockObj = undefined;
    let blockJson = undefined;


    function setData(){

        //json으로 넘어온 map object를 js에서 사용할 수 있는 object 형식으로 파싱
        blockJson = ${map.bookmarks};
        blockObj = JSON.parse(blockJson['_value']);
        console.log(blockObj);

        <%--alert(${map.tag});--%>
        //작성자 이름
        $("#writer_name").html('${map.mem_nick}');
        //작성자 소개
        $("#writer_description").html('${map.mem_introduce}');
        //작성자 이미지
        $("#writer_image").attr("src", "${map.mem_profile}");
        //선택한 태그들
        $("#tags_inputField").html('${map.tag.get(0)}');
        //블록 썸네일 이미지
        $("#thumbnail_image").attr("src", '${map.bti_url}');
        //블록 제목
        $("#bd_title").html('${map.bl_title}');
        //블록 소개
        $("#bd_introduce").html('${map.bl_introduce}');
        //블록 내용
        $("#bd_description").html('${map.bl_description}');

        //카테고리
        let categoryStr = blockObj.BL_MAINCTG + " > "  + blockObj.BL_SMCTG;
        $("#bd_category").val(categoryStr);

        //날짜
        let dateObj = new Date(parseInt('${map.bl_date}'));
        let dateStr = dateObj.getFullYear() + "년 " + (dateObj.getMonth()+1) + " 월" + dateObj.getDate() + " 일 "
            + dateObj.getHours() + ":" + dateObj.getMinutes();
        $("#bd_date").html(dateStr);


        //비동기로 북마크 로딩
        (function(){

            setBookmarks(blockObj);

        }());


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


    $(document).ready(function(){

        setNavType("blue");
        setData();

    });

</script>