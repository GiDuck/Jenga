<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<style>

    .bookMarkField{
        overflow: scroll;
        border-style : groove;
        min-height:100px;
        max-height:400px;
        height : auto;

    }


    .bookMarkItem{
    }



    .myBookMarkItem{

    }


    .bookMarkLabel{

        padding : 25px;

    }

    .selectedElementLeft{}


    .selectedElementRight{}

    .contextMenu{}

</style>

<div class="main">


    <div class="section">

        <div class="container">

            <div class="row">

                <div class="col-md-4 col-sm-9 mr-auto ml-auto">
                    <div class="w-100 text-center bookMarkLabel"><h4><b>내 북마크</b></h4></div>

                    <div id="getMyBookMark" class="bookMarkField form-control border-input" class="row" style="height : 400px"></div>

                    <div class="row text-center" style="padding : 10px">
                        <div class = "col-sm-6"><div id="moveToUpperLeft" class="btn w-100 text-center"> < </div></div>
                        <div class = "col-sm-6"><div id="moveToLowerLeft" class="btn w-100 text-center"> > </div></div>


                    </div>


                </div>


                <div class="col-md-7 col-sm-9 mr-auto ml-auto">

                    <div class="w-100 text-center bookMarkLabel"><h4><b>북마크 편집</b></h4></div>

                    <div id="editBookMark" class= "editBookMarkField bookMarkField form-control border-input" class="row" style="height : 400px"></div>

                    <div class="row text-center" style="padding : 10px">

                        <div class = "col-md-1 col-sm-6"><div id="moveToUpperRight" class="btn w-100 text-center"> < </div></div>
                        <div class = "col-md-1 col-sm-6"><div id="moveToLowerRight" class="btn w-100 text-center"> > </div></div>
                        <div class = "col-md-4 col-sm-4"><div id="addElementRight" class="btn w-100 text-center"> 북마크 추가 </div></div>
                        <div class = "col-md-3 col-sm-4"><div id="addFolderRight" class="btn w-100 text-center"> 폴더 추가 </div></div>
                        <div class = "col-md-3 col-sm-4"><div id="removeElementRight" class="btn w-100 text-center"> 선택 삭제 </div></div>

                    </div>


                </div>

            </div>


        </div>

        <br><br><br><br>


        <div class="container">
            <form id="submitForm">
                <div class="row">
                    <div class="col-md-5 col-sm-5">
                        <h6>Main Image</h6>
                        <div class="fileinput fileinput-new text-center" data-provides="fileinput">
                            <div class="fileinput-new thumbnail img-no-padding" style="max-width: 370px; max-height: 250px;">
                                <img id="file_image" src="${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg" alt="...">
                            </div>
                            <div class="fileinput-preview fileinput-exists thumbnail img-no-padding" style="max-width: 370px; max-height: 250px;"></div>
                            <div>
                  <span class="btn btn-outline-default btn-round btn-file">
                    <span class="fileinput-new">Select image</span>
                  <span class="fileinput-exists">Change</span>
                  <input type="file" name="thumbnail_Img">
                  </span>
                                <a href="#" class="btn btn-link btn-danger fileinput-exists" data-dismiss="fileinput"><i class="fa fa-times"></i> Remove</a>
                            </div>
                        </div>

                        <h6>Tags&nbsp<i class="nc-icon nc-alert-circle-i" data-container="body" data-toggle="popover" data-placement="top" data-content="태그를 입력하시고 엔터를 치시면 입력됩니다."></i></h6>

                        <div id="tags">
                            <input id="tagsinput" class="tagsinput" data-color="success" type="text" placeHolder="태그를 입력하세요.."/>
                        </div>

                        <br>
                        <br>


                    </div>
                    <div class="col-md-7 col-sm-7">
                        <div class="form-group">
                            <h6>Title
                                <span class="icon-danger">*</span>
                            </h6>
                            <input name="input_title" type="text" class="form-control border-input" placeholder="&nbsp제목을 입력하세요..">
                        </div>
                        <div class="form-group">
                            <h6>Introduce
                                <span class="icon-danger">*</span>
                            </h6>
                            <textarea name="input_introduce" class="form-control border-input" placeholder="소개를 입력하세요.." rows="3"></textarea>
                        </div>
                        <div class="row">
                            <div class="col-12">
                                <h6>Date
                                    <span class="icon-danger">*</span>
                                </h6>
                                <div class="input-group border-input">
                                    <input id="today" type="text" class="form-control border-input" disabled=true>
                                </div>
                            </div>
                        </div>


                        <br><br>


                        <h6>Categories</h6>
                        <div id="tags-2" class="row w-100">

                            <div class="dropdown col-6">
                                <a id="mainCategory" class="btn btn-secondary dropdown-toggle w-100" href="#" role="button" id="categorySelect1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">대분류</a>

                                <div id="mainCategoryItem" class="dropdown-menu" aria-labelledby="categorySelect1"></div>
                            </div>

                            <div class="dropdown col-6">

                                <a id="subCategory" class="btn btn-secondary dropdown-toggle w-100" href="#" role="button" id="categorySelect2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">소분류</a>

                                <div id="subCategoryItem" class="dropdown-menu" aria-labelledby="categorySelect2"></div>

                            </div>
                        </div>
                        <br>
                    </div>
                </div>


                <div class="row buttons-row">


                    <div class="form-group col-12">
                        <h6>Description</h6>
                        <br>
                        <section>

                            <iframe  id="editor" src="${pageContext.request.contextPath}/resources/editor.html" width="100%" height="500" style="border:0"></iframe>

                        </section>
                    </div>


                    <div class="form-check col-12">
                        <label class="form-check-label">
                            <input id="confirmRoleJenga" class="form-check-input" type="checkbox">
                            <span class="form-check-sign"></span>
                            나는 <a href="#">Jenga의 이용약관</a>과 저작권을 준수하며 불법 자료를 올리지 않을 것을 약속합니다.
                        </label>
                    </div>
                    <br><br><br>

                    <div class="col-md-4 col-sm-4">
                        <button class="btn btn-outline-danger btn-block btn-round">Cancel</button>
                    </div>
                    <div class="col-md-4 col-sm-4">
                        <button id ="saveBtn" class="btn btn-outline-primary btn-block btn-round" onclick="javascript:void(0);">Save</button>
                    </div>
                    <div class="col-md-4 col-sm-4">
                        <button class="btn btn-primary btn-block btn-round">Save & Publish </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<script>

    /* left : 왼쪽 필드에서 드래그 가능한 이벤트 부착
        right : 오른쪽 필드에서 드래그 가능한 이벤트 부착
    */

    //서버로 부터 전해받은 북마크 원본 파일.
    let bookmarks;
    let timeFormat = new DateTimeFormatter();
    //북마크 원본 파일에서 실질적인 북마크 JSON 파일을 뽑아낸 것.
    let bookmarkElements;


    //사용자가 편집한 북마크 목록.
    let editedBKElements = new Array();

    //상위 폴더로 가기를 위한 배열
    //.. preNodeLeft : 현재까지 탐색한 왼쪽 패널의 하위 객체의 id(timeStamp)를 저장하는 배열
    //.. preNodeRight : 현재까지 탐색한 왼쪽 패널의 하위 객체의 id(timeStamp)를 저장하는 배열

    let prevNodeLeft = new Array();
    let prevNodeRight = new Array();

    const COLOR_SELECTED = "#fa6362";
    const COLOR_CARD_DEFAULT = "#999999";

    //만약 필드에 다른 선택된 요소가 있으면 선택 취소 해 주는 클래스.
    function removeOtherClass(type){

        if(type == "right"){

            $("#editBookMark").find(".selectedElementLeft").removeClass("selectedElementLeft");

        }else if(type == "left"){

            $("#getMyBookMark").find(".selectedElementRight").removeClass("selectedElementRight");

        }

    }


    //드래그가 가능하도록 하는 설정하는 함수
    // .. 필드별로 다르게 설정함.
    function setDraggable($element, token) {

        if(token == 'left' ){

            $element.draggable({

                cursor : "pointer",
                opacity : 0.8,
                revert : "invaild",
                helper : "clone"


            });

        }else if(token == "right" ){

            $element.draggable({
                classes : {"ui-draggable" : "highlight"},
                cursor : "pointer",
                opacity : 0.8,
                revert : true,
                helper : "clone",
                zIndex : 300
            });


        }

    }


    function setOnContextMenu($element, panelType){

        let $contextMenu = undefined;

        if(panelType == "right"){

            $contextMenu = $("<div>").addClass("dropdown-menu contextMenu text-right").attr("data-toggle", "dropdown").css("background-color", COLOR_CARD_DEFAULT)
                .append($("<a>").addClass("card-item dropdown-item").html("수정").on("click", function(e){
                    e.preventDefault();
                    e.stopPropagation();

                    let successAction = function(){

                        let bookmarks = popChild("right");
                        refreshBookMark(bookmarks, "right");

                    }

                    makeAddOrModifyElementModal($element, false, successAction, this);
                    $(document).find(".contextMenu").removeClass("show").hide();

                }))
                .append($("<a>").addClass("card-item dropdown-item").html("삭제").on("click", function(e){
                    e.preventDefault();
                    e.stopPropagation();

                    let bookmark = popChild("right");
                    let timeStamp = $element.find("input[name='timeStamp']").val();

                    removeElement(bookmark, timeStamp);
                    $(document).find(".contextMenu").removeClass("show").hide();
                }));


            $element.on("contextmenu", function(e){

                let positionX = e.pageX;
                let positionY = e.pageY;

                $(document).find(".contextMenu").css({
                    position : "absolute",
                    display : "block",
                    left : positionX,
                    top : positionY,
                    zIndex : 1000

                }).addClass("show");

                //return false를 하는 이유는 브라우저의 기본적인 오른쪽 마우스 클릭시 나오는 context menu 전개를 막기 위함
                return false;

            }).on("click", function(){

                $(document).find(".contextMenu").removeClass("show").hide();

            });


            $(".contextMenu a").on("click", function() {
                $(this).parent().removeClass("show").hide();
            });


            $("body").append($contextMenu);

        }
    }


    //clickListener
    // .. 클릭 시 요소가 선택되게 하여 사용자가 추가적인 작업을 가능케 한다.
    function setOnClickListener($element, panelType) {

        let $selected;

        $element.on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();


            //오른쪽 노드가 선택되면, 다른 선택된 요소의 클래스를 모두 지우고 해당 요소에게만 '선택 클래스(selectedElementRight)'를 적용한다.
            if(panelType == "right"){

                removeOtherClass("right");
                $selected = $("#editBookMark").find(".selectedElementRight");
                $selected.removeClass("selectedElementRight");
                $selected.css("background-color", "");
                $element.addClass("selectedElementRight");
                $element.css("background-color", COLOR_SELECTED);


                //왼쪽 노드가 선택되면, 다른 선택된 요소의 클래스를 모두 지우고 해당 요소에게만 '선택 클래스(selectedElementLeft)'를 적용한다.
            }else if(panelType == "left"){

                removeOtherClass("left");
                $selected = $("#getMyBookMark").find(".selectedElementLeft");
                $selected.removeClass("selectedElementLeft");
                $selected.removeAttr("background-color", "");
                $element.addClass("selectedElementLeft");

            }
        });
    }

    //요소에 Drag한 것을 Drop 시킬 수 있게 설정하는 함수.
    function setDroppable($element){

        $element.droppable({

            tolerance : "intersect",
            activeClass: "ui-state-default",
            hoverClass: "ui-state-hover",
            drop : function(e, ui){

                e.preventDefault();
                e.stopPropagation();

                //dragNode는 현재 dragging 중인 요소 정보임
                let dragNode = $(ui.draggable);

                //만약 왼쪽 필드에 있던 요소가 드래그 되어져서 오른쪽 필드에 떨어졌다면
                //해당 요소의 속성을 오른쪽 필드에 적합하도록 바꾼 후에, 오른쪽 필드에 추가한다.

                if(!dragNode.hasClass("bookMarkItem")){

                    let dragNodeId = dragNode.find("input[name='timeStamp']").val();

                    dragNode = transElementToRight(dragNode.clone(true));

                    if($element.attr("id") == "editBookMark"){

                        $element.append(dragNode);

                    }else{

                        dragNode.insertAfter($element);
                    }

                    let top = popChild("left");

                    //extend는 Jquery에서 지원하는 deepCopy 메소드
                    //.. 왼쪽 패널에서 찾은 객체를 복사하여 오른쪽 배열에 넣는다.
                    //let copyedChild = $.extend(true, {}, selectedChild);

                    let child;
                    for(let i = 0; i<top.length; ++i){

                        if(top[i].add_date == dragNodeId){

                            child = top[i];
                            break;

                        }

                    }


                    top = popChild("right");
                    top.push(child);



                }
                removeOtherClass("right");

            }



        });

    }



    //더블 클릭시 폴더 내의 하위 자식 요소를 탐색할 수 있게 설정함.
    function setDoubleClick($element, panelType){


        $element.on("dblclick", function(e){

            e.stopPropagation();

            if(panelType == "left"){

                moveToLower($element, "left");

            }else if(panelType == "right"){

                moveToLower($element, "right");

            }


        });

    }



    //왼쪽 필드에 적용된 속성을 오른쪽 필드에 적합하도록 변환시켜 주는 함수
    // ..왼쪽 필드에 있던 요소가 드랍되어 오른쪽으로 이동할 떄, 속성이 오른쪽 필드에 적합하도록 변환되어야 한다.
    function transElementToRight(dragNode){

        dragNode.removeClass("myBookMarkItem");
        dragNode.addClass("bookMarkItem");

        //기존의 클릭이벤트를 제거하고 오른쪽 필드에 맞춘 클릭 이벤트 핸들러를 부착한다.
        dragNode.off('click');
        setOnClickListener(dragNode, "right");
        setOnContextMenu(dragNode, "right");

        //더블클릭 이벤트가 적용된 요소인지 확인 후에, 적용된 요소라면 해제하고 오른쪽 더블클릭 이벤트 핸들러를 부착한다.
        let checker = $._data(dragNode[0], "events");

        if(checker && checker.dblclick){
            $(dragNode).off('dblclick');
            setDoubleClick(dragNode, "right");
        }



        setDraggable(dragNode, "right");

        return dragNode;


    }




    //요소 추가 함수
    //요소 혹은 폴더를 구분하여 추가한다.
    //추가 연산은 오른쪽 필드에서만 가능하므로 editBookMark 필드에서만 진행한다.
    function add(panelType, type){

        this.$parent = $("#editBookMark");
        this.$newElement;

        let timeStamp = new Date().getTime();

        //현재 선택된 노드 가져오기
        this.$selElement = $("#editBookMark").find(".selectedElementRight");

        function afterAction(newElement, parent, selElement){

            setDraggable(newElement, "right");
            setDroppable(newElement, "right");
            setOnClickListener(newElement, "right");
            setOnContextMenu(newElement, "right");

            if(selElement){
                newElement.insertAfter(selElement);
            }else{
                parent.append(newElement);
            }
        }

        if(type == "folder"){

            this.$newElement = $("<div>").addClass("col-12 w-100 bookMarkItem border-input card").css("cursor", "pointer").css("padding-bottom", "20px").css("padding-top", "20px").css("padding-left", "20px").attr("name", "elementParent").css("background-color", " #f2f2f2")
                .append($("<div>").addClass("w-100").append($("<i>").addClass("nc-icon nc-bag-16").css("padding-left", "5px"))
                .append($("<input>").addClass("w-100 form-control border-input").attr("title", "text").attr("placeHolder", "폴더 이름 입력..").attr("disabled", true)))
                .append($("<input>").attr("type", "hidden").attr("name", "timeStamp").val(timeStamp))
                .append($("<input>").attr("type", "hidden").attr("name", "isFolder").val(true));


                let successAction = function(top, title){
                    setDoubleClick($newElement, "right");
                    afterAction($newElement, $parent);

                    $newElement.find("input[name='title']").val(title);

                    let folderObj = new Folder(title, new Array(), timeStamp, timeStamp);
                    top.push(folderObj);

                    let bookmarks = popChild("right");

                    refreshBookMark(bookmarks, "right");

                };
                makeAddOrModifyElementModal(this.$newElement, true, successAction, this);



        }else if(type == "element"){

            $newElement = ($("<div>").addClass("col-12 w-100 bookMarkItem border-input card").attr("name", "elementParent").css("cursor", "pointer").css("background-color", " #f2f2f2")
                .append($("<div>").addClass("w-100").append($("<i>").addClass("nc-icon nc-book-bookmark text-left").html("Bookmark")))
                .append($("<div>").addClass("w-100").attr("name", "section")
                    .append($("<input>").attr("type", "text").attr("name", "title").attr("disabled", true).attr("placeHolder", "제목 입력..").addClass("w-100 border-input form-control"))
                    .append($("<br>"))
                    .append($("<input>").attr("type", "text").attr("name", "url").attr("disabled", true).attr("placeHolder", "url 입력..").addClass("w-100 border-input form-control").val("https://"))
                    .append($("<input>").attr("type", "hidden").attr("name", "timeStamp").val(timeStamp))
                    .append($("<input>").attr("type", "hidden").attr("name", "isFolder").val(false))
                    )).append($("<br>"));


            let successAction = function(top, title, url){

                afterAction(this.$newElement, this.$parent);
                let bookmarkObj = new BookMark(title, url, timeStamp, null);
                top.push(bookmarkObj);
                let bookmarks = popChild("right");
                refreshBookMark(bookmarks, "right");

            };

                makeAddOrModifyElementModal($newElement,true,successAction, this);


        }



    }



    //삭제 함수
    function removeElement(bookmark, timeStamp){

        if(!timeStamp){

            swal('삭제 실패','현재 선택된 요소가 없습니다. 삭제를 원하는 노드를 선택하십시오.', 'warning');
            return;
        }

        for(let i=0; i<bookmark.length; ++i){

            if(bookmark[i].add_date == timeStamp){
                bookmark.splice(i, 1);
                break;

            }
        }

        swal('삭제 성공','해당 요소가 삭제되었습니다.', 'success');
        refreshBookMark(bookmark, "right");


    }


    //선택된 요소를 찾는 함수
    function findCheckElement(panelType){

        let $parent;
        let $item;

        if(panelType == "left"){

            $parent = $("#getMyBookMark");
            $item = $parent.find(".selectedElementLeft");


        }else if(panelType == "right"){

            $parent = $("#editBookMark");
            $item = $parent.find(".selectedElementRight");


        }else{
            return false;
        }


        if(!$item){

            return $parent;

        }
            return $item;
    }




    //버튼 클릭리스너 부착 함수
    function attachBtnEvent(){

        //...왼쪽 패널

        //왼쪽패널, 상위폴더로 이동
        $("#moveToUpperLeft").on('click', function(e){

            moveToUpper("left");


        });

        //왼쪽패널, 하위폴더로 이동
        $("#moveToLowerLeft").on('click', function(e){

            e.preventDefault();
            e.stopPropagation();


            let $checkedItem = findCheckElement("left");
            moveToLower($checkedItem, "left");

        });


        //...오른쪽 패널

        //오른쪽패널, 상위폴더로 이동
        $("#moveToUpperRight").on('click', function(e){

            moveToUpper("right");


        });

        //오른쪽패널, 하위폴더로 이동
        $("#moveToLowerRight").on('click', function(e){

            let $checkedItem = findCheckElement("right");
            moveToLower($checkedItem, "right");

        });

        //오른쪽패널, 요소 추가
        $("#addElementRight").on('click', function(e){
            add("right", "element");

        });

        //오른쪽패널, 폴더 추가
        $("#addFolderRight").on('click', function(e){
            add("right", "folder");
        });

        //오른쪽패널, 요소 제거
        $("#removeElementRight").on('click', function(e){

            let $checkedItem = findCheckElement("right");
            let timeStamp = $checkedItem.find("input[name='timeStamp']").val();
            let bookmark = popChild("right");
            removeElement(bookmark, timeStamp);

        });

        $("#submitForm").on("submit", function(e){

            return false;
        });

        $("#saveBtn").on('click', function(e){

            e.preventDefault();
            $(window).unbind("beforeunload");
            uploadBlock();

        });



    }

    //블록 등록 함수
    function uploadBlock(){

        let valid = boardValidate();
        let dest;
        let statusToken = "${statusToken}";
        let token;

        if(statusToken == "stack"){

            dest = "/board/uploadBlock";
            token = "등록";

        }else if(statusToken == "modify"){

            dest = "/board/updateBlock";
            token = "수정";
        }

        if(!valid) return;

        swal({
            title: "블록" + " " + token,
            text: "블록을 " + token + "하시겠습니까?",
            type: "info",
            showCancelButton: true,
            confirmButtonText : "등록",
            showLoaderOnConfirm: true,
            preConfirm : function () {
                return new Promise(function() {
                    setTimeout(function() {

                        let image = $("input[name='thumbnail_Img']").prop("files");
                        let title = $("input[name='input_title']").val();
                        let introduce = $("textarea[name='input_introduce']").val();
                        let mCategory = $("#mainCategory").html();
                        let sCategory = $("#subCategory").html();
                        let content = $("#editor").contents().find("#summernote").val();
                        let tags = $("#tagsinput").val();
                        let tagsContainer = tags.split(",");
                        let time = new Date().getTime();



                        let formData = new FormData();
                        formData.append("bti_url", image[0]);                                       // param
                        formData.append("bl_title", title);                                         // dto
                        formData.append("bl_bookmarks", JSON.stringify(editedBKElements));          // param
                        formData.append("bl_introduce", introduce);                                 // dto
                        formData.append("bl_mainCtg", mCategory);                                   // dto   //문화예술
                        formData.append("bl_smCtg", sCategory);                                     // dto   //
                        formData.append("bl_description", content);                                 // dto
                        formData.append("bt_name", tagsContainer);                                  // dto
                        formData.append("bl_date", time);                                           // dto
                        if(statusToken == "modify") {
                        formData.append("bl_uid", '${bl_uid}');
                        }

                        //AJAX request
                        $.ajax({

                            url : dest,
                            enctype: "multipart/form-data",
                            contentType: false,
                            cache: false,
                            processData:false,
                            data : formData,
                            type : "POST",
                            success : function(response){

                                swal({
                                    title : "블록 "+token+" 성공",
                                    text : "블록이 성공적으로 "+token+" 되었습니다.",
                                    type : "success"
                                }).then(function(){

                                    if(statusToken == "modify"){
                                        location.replace("/board/boardView?bl_uid=${bl_uid}");

                                    }else{
                                        location.replace("/board/boardView?bl_uid="+response);
                                    }

                                });

                            },
                            error : function(xhs, status, error){

                                swal('블록 '+token+ '실패', '블록을 '+ token +'하는데 실패 하였습니다.', 'error');
                                return;

                            }

                        });

                    }, 1000)
                })

            }

        });




    }

    //폴더의 하위 요소들을 탐색하는 함수
    // ..선택된 아이템과 적용할 필드타입을 파라미터로 받음 (왼쪽 필드 : left, 오른쪽 필드 : right)
    function moveToLower($checkedItem, panelType){


        if(!$checkedItem){
            swal('하위요소 이동', '선택된 요소가 없습니다!', '');
            return;
        }


        let type = $($checkedItem).find("input[name='isFolder']").val();

        if(type == "false"){

            swal('하위요소 이동', '폴더만 하위 요소로 이동이 가능합니다.', '');
            return;

        }

        let id = $($checkedItem).find("input[name='timeStamp']").val();
        let bookmarks;


        if(!id){
            return;
        }

        if(panelType == "left"){

            bookmarks = popChild("left");

        }else if(panelType == "right"){

            bookmarks = popChild("right");

        }



        bookmarks = findChildElement(bookmarks, id);


        bookmarks = getChildren(bookmarks);


        if(panelType == "left"){


                prevNodeLeft.push(id);


        }else if (panelType == "right"){

                prevNodeRight.push(id);


        }
        refreshBookMark(bookmarks, panelType);


    }





    //폴더의 상위 요소들을 탐색하는 함수
    // ..적용할 필드를 파라미터로 받음 (왼쪽 필드 : left, 오른쪽 필드 : right)
    function moveToUpper(panelType){

        let bookmarks;
        let prevNode;


        if(panelType == "left"){


            prevNode = prevNodeLeft.pop();
            bookmarks = popChild("left");


            if(!prevNode){

                swal('상위 노드로 이동', '루트 노드입니다', '');
                refreshBookMark(bookmarkElements, "left");
                return;

            }

            if(!bookmarks){
                bookmarks = bookmarkElements;
            }

            bookmarks = getChildren(bookmarks);

            refreshBookMark(bookmarks, "left");

        }else if (panelType == "right"){

            prevNode = prevNodeRight.pop();
            bookmarks = popChild("right");
            bookmarks = getChildren(bookmarks);

            if(!prevNode){

                swal('상위 노드로 이동', '루트 노드입니다', '');
                refreshBookMark(editedBKElements, "right");
                return;

            }

            refreshBookMark(bookmarks, "right");

        }


    }

    //현재 전개된 북마크 목록을 가져오는 함수
    function popChild(type){

        let bookmarks;
        let path;

        if(type == "right"){
            path = prevNodeRight;
            bookmarks = editedBKElements;

        }else if(type == "left"){

            path = prevNodeLeft;
            bookmarks = bookmarkElements;

        }

        if(!path){
            return null;
        }

        for(let i = 0; i<path.length; ++i){

            for(let j = 0 ; j<bookmarks.length; ++j){

                if(bookmarks[j].add_date == parseInt(path[i])){
                    bookmarks = bookmarks[j].children;
                    break;

                }

            }

        }

        return bookmarks;
    }


    //id 값 맞는 하위자식 요소 찾기
    function findChildElement(bookmarks, id){

        bookmarks = getChildren(bookmarks);
        for(let i = 0; i<bookmarks.length; ++i){
            if(bookmarks[i].add_date == parseInt(id)){
                return getChildren(bookmarks[i]);
            }

        }


    }


    function getChildren(bookmark){


        let typeIsFolder = bookmark instanceof Folder;
        if(typeIsFolder){
            return bookmark.getChildren();
        }else{

            return bookmark;

        }


    }


    //북마크 목록을 새로 고침하여 rendering 시켜주는 함수
    function refreshBookMark(bookmarkElements, panelType){

        let $itemParent;
        let classType;

        //패널 타입에 따라 부모 노드를 초기화 시킨다.
        if(panelType == "left"){

            $itemParent = $("#getMyBookMark");
            classType = "myBookMarkItem";

        }else if(panelType == "right"){


            $itemParent = $("#editBookMark");
            classType = "bookMarkItem";

        }

        $itemParent.empty();

        if(!bookmarkElements) return;


        //루프를 돌면서 전달받은 JSON 객체를 DOM에 넣어 생성한다.
        for(let i=0; i<bookmarkElements.length; ++i){

            $bookmark = bookmarkElements[i];
            let typeIsFolder = $bookmark instanceof Folder;

            //추가할 DOM 요소를 생성한다.
            //.. innerHTML은 폴더와 북마크 모두 공통으로 들어가는 부분이다.
            //.. 만약 폴더면 URL이 존재하지 않으므로 당연히 NULL이 들어간다.
            //.. id값으로 사용하는 것은 유닉스타임으로 지정된 timeStamp이다.

            let $item = $("<div>").addClass("card row").addClass(classType).attr("name", "elementParent").css("padding-top", "20px").css("padding-bottom", "20px").css("margin", "10px").css("background-color",  "#f2f2f2").css("cursor", "pointer");
            let $innerItem = $("<div>").addClass("col-10 w-100");

                if(!$.trim($bookmark.title)){
                    $innerItem.append($("<input>").attr("name", "title").attr("placeholder", "제목을 입력해 주세요..").attr("disabled", true).css("user-select", "none").addClass("w-100 form-control border-input"));

                }else{
                    $innerItem.append($("<input>").attr("name", "title").val($bookmark.title).attr("disabled", true).css("user-select", "none").addClass("w-100 form-control border-input"));

                }
                $innerItem.append($("<br>"));

            if(!$.trim($bookmark.url) && !typeIsFolder){
                $innerItem.append($("<input>").attr("name", "url").val("https://").attr("disabled", true).css("user-select", "none").addClass("w-100 form-control border-input"));

            }else if(!typeIsFolder){
                $innerItem.append($("<input>").attr("name", "url").val($bookmark.url).attr("disabled", true).css("user-select", "none").addClass("w-100 form-control border-input"));

            }

            $innerItem.append($("<input>").attr("type", "hidden").attr("name", "timeStamp").val($bookmark.add_date))
                .append($("<input>").attr("type", "hidden").attr("name", "isFolder").val(typeIsFolder));

            //내 북마크 item

            if(typeIsFolder){

                $item.append($innerItem);
                $item.append($("<div>").addClass("col-2").append($("<i>").addClass("nc-icon nc-minimal-right text-right w-100")));
                setDoubleClick($item, panelType);


            }else if(!typeIsFolder){

                $item.append($innerItem);

            }else{
                return;
            }


            //처음에는 회원이 가지고 있는 북마크 목록만 초기화 되므로, 왼쪽 패널의 item들이 초기화 된다.
            if(panelType == "left"){

                setDraggable($item, "left");
                setDroppable($item, "right");
                setOnClickListener($item , "left");

            }else if(panelType == "right"){

                setDraggable($item, "right");
                setOnClickListener($item , "right");
                setOnContextMenu($item , "right");

            }


            $itemParent.append($item);
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


    //게시글 업로드 전 유효성 검사
    function boardValidate(){


        let title = $("input[name='input_title']").val();
        let category = $("#mainCategory").html();
        let content = $("#editor").contents().find("#summernote").val();
        let confirmRole = $("#confirmRoleJenga").is(":checked");
        let error = null;


        if(title.replace(REGEX_TRIM_VOID, "").length < 1){

            error = "제목이 공백입니다. 최소 1자리 이상 작성하여 주십시오.";

        }else if( category.trim() == "대분류" || category.replace(/\s/gi, "").length < 1 ){

            error = "카테고리를 설정하여 주십시오.";

        }else if(content.replace(/\s/gi, "") < 1){

            error = "내용이 비었습니다.";

        }else if(!confirmRole){

            error = "이용약관에 동의하여 주십시오.";

        }else if(editedBKElements.length < 1){

            error = "북마크를 추가해 주십시오.";

        }

        if(error){

            swal("블록 등록 실패", error, "error");
            return false;

        }

        return true;

    }

    //수정 페이지로 초기화
    function setModifyPage(){
        $("#file_image").attr("src", '${map.bti_url}');
        $("input[name='input_title']").val('${map.bl_title}');
        $("textarea[name='input_introduce']").val('${map.bl_introduce}');

        document.getElementById("editor").contentWindow.setText('${map.bl_description}');
        $("#mainCategory").html('${map.bl_mainCtg}');
        $("#subCategory").html('${map.bl_smCtg}');
        $("#tagsinput").val(${map.tag});
        $("#today").val(timeFormat.getFullDateTime('${map.bl_date}'));


        //사용자가 업로드 했던 북마크 목록을 가져온다
        let bookmarks = ${map.bookmarks};
        let parsedBookmark = JSON.parse(bookmarks["_value"]);
        let bookmarkObjs = new Array();
        parseBookmarkObjType(parsedBookmark[0], bookmarkObjs);
        refreshBookMark(bookmarkObjs, "right");


    }

    //블록 쌓기 페이지로 초기화
    function setStackPage(){

        //오늘 날짜를 input 필드에 넣음
        let today = new Date();
        $("#today").val(timeFormat.getFullDateTime(today.getTime()));


    }

    //컴포넌트에 붙는 이벤트 리스너 초기화
    function setComponentEventListener(){

        //썸네일 이미지가 업로드 될때마다 유효성 검사 실시 (1MB 이하만 업로드 가능, jpg, jpeg, png, gif 외 확장자 사용 불가)
        $("input[name='thumbnail_Img']").on('change', function(e){

            checkImageFile(e);

        });

        //태그가 입력될 때 마다 유효성 검사 실시 (한글, 영어, 숫자 외 입력불가)
        $("#tagsinput").on("change", function(e){

            e.preventDefault();
            if(!$(e.target).val()){
                return;
            }
            let tags = $(e.target).val().split(",");

            for(let i=0; i < tags.length; ++i){

                let removedBlank = tags[i].replace(REGEX_TRIM_VOID, "");
                let regex = REGEX_ONLY_CHAR_AND_NUM;
                let regexResult = regex.test(tags[i]);


                if(removedBlank.length < 1){
                    swal("태그 추가 실패", "추가한 태그가 공백입니다.", "error");
                    $(e.target).tagsinput("remove", tags[i]);
                }else if(!regexResult){

                    swal("태그 추가 실패", "태그는 한글, 영문, 숫자의 조합만 가능합니다.", "error");
                    $(e.target).tagsinput("remove", tags[i]);

                }


            }

        });

    }

    function closeContextMenu(){

        if($(document).find(".contextMenu").hasClass("show")) {
            $(document).find(".contextMenu").removeClass("show").hide();
        }

    }


    //초기화
    $(document).ready(function() {
        navbarObj.setType("bg-info");

        $.busyLoadFull("show", {
            spinner : "circle-line",
            text : "페이지를 초기화 하고 있습니다..."

        });


        $(document).on('click', function(e){

            if($(document).find(".contextMenu").hasClass("show")){

                let $contextMenu =  $(document).find(".contextMenu");
                let menuX = $contextMenu.css("left").replace(REGEX_TRIM_DIM_EXTEND, '');
                let menuY = $contextMenu.css("top").replace(REGEX_TRIM_DIM_EXTEND, '');
                let menuW = $contextMenu.css("width").replace(REGEX_TRIM_DIM_EXTEND, '') + e.pageX;
                let menuH = $contextMenu.css("height").replace(REGEX_TRIM_DIM_EXTEND, '') + e.pageY;

                if(!(e.pageX > menuX && e.pageX < menuW && e.pageY > menuY && e.pageY < menuH)){
                    $(document).find(".contextMenu").removeClass("show").hide();

                }
            }
        });

        $(window).scroll(function(){

            closeContextMenu();

        });


        $("#editBookMark").scroll(function(){

            closeContextMenu();

        });


        $(window).on("beforeunload", function(e){

            e.preventDefault();
            return "페이지에서 벗어나시겠습니까? 저장된 정보는 변하지 않습니다.";


        });

        attachBtnEvent();
        let category = '${category}';
        setCategory(JSON.parse(category));
        setComponentEventListener();

        let statusToken = '${statusToken}';
        //사용자가 업로드 했던 북마크 목록을 가져온다
        let parsedHTMLVar =  parseHTML('${resultHTML}');
        bookmarkElements = parsedHTMLVar.getChildren();


        //북마크 목록 화면에 나타낸다
        refreshBookMark(bookmarkElements, "left");


        console.log(statusToken);
        if(statusToken == "stack"){
            if(bookmarkElements.length == 0){

                swal({

                    text : "현재 동기화 된 북마크가 없습니다. 내 정보 수정 페이지에서 북마크를 동기화 시키세요.",
                    type : "warning"

                });
            }

            setStackPage();

        }else if(statusToken == "modify"){

            setModifyPage();

        }

        //Drop을 가능하도록 설정, 오른쪽 패널
        setDroppable($("#editBookMark"), "right");
        //키를 감지하도록 설정

        $.busyLoadFull("hide", {});


    });

</script>


