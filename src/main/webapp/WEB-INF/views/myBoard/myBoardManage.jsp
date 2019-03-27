<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>

    .card{
        cursor : pointer;
    }


    .controlBtn{}

    .controlBtn:hover{

    color : #c82333;
    }

</style>

<div class="wrapper">
    <div class="main">
        <div class="section">
            <div class="container">

                <div class="row" id="block_field">
                    <div class="col-md-3 col-sm-4" id="bkCard" style="display : none">
                        <div class="card text-center">
                            <div class="card-image">
                                <img name="bk_img"
                                     onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'"
                                     src="" alt="Rounded Image" class="img-rounded img-responsive">
                                <br><br>
                                <div class="text"><p name="bk_title" class="name"></p></div>

                                <div class="card-body details-center ml-auto mr-auto">
                                    <div class="author w-50 text-center">
                                        <div class="text">
                                            <div class="meta"><i class="fa fa-heart" name="likeIcon" style="color : red"></i><p id="likeCount">0</p></div>
                                        </div>

                                    </div>
                                </div>
                                <br>
                                <div class="card-footer">
                                    <div class="row">
                                        <div name="btn-modify" class="col-6 text-center controlBtn"><i class="fa fa-pencil"></i>&nbsp수정하기</div>
                                        <div name="btn-delete" class="col-6 text-center controlBtn"><i class="fa fa-trash"></i>&nbsp삭제하기</div>
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


    $(document).ready(function () {
        navbarObj.setType("bg-warning");
        navbarObj.addHeadBlock();
        setAutomaticResizeWindow($(".section"));
        let boards = ${boards};
        renderItems(boards);
    });

    //ajax 호출을 통해 가져온 데이터를 화면에 전시함
    function renderItems(items) {

        let $cardForm = $("#bkCard");
        let $field = $("#block_field");
        $field.empty();

        for (let i = 0; i < items.length; ++i) {

            let item = items[i];
            let boardUid = item["bl_uid"];
            let $dummy = $cardForm.clone();
            $dummy.css("display", "block");
            $dummy.find("p[name='bk_title']").text(item["bl_title"]);
            $dummy.find("img[name='bk_img']").attr("src", item["bl_img"]);
            $dummy.find("#likeCount").text(item["bl_count"]);
            $dummy.find("div[name='btn-modify']").on("click", function(e){

                e.preventDefault();
                e.stopPropagation();
                window.location.href = "/board/modifyBlock?bl_uid=" + boardUid;
            });

            $dummy.find("div[name='btn-delete']").on("click", function(e){
                e.preventDefault();
                e.stopPropagation();
                swal({
                   title : "게시글 삭제",
                   type : "warning",
                   text : "정말 삭제하시겠습니까? 삭제 후에는 다시 복구할 수 없습니다.",
                   showCancelButton : true,
                   confirmButtonText : "삭제",
                    cancelButtonColor : "취소",
                    reverseButtons : true

                }).then(function(result){

                    if(result.value){

                        $.ajax({
                            url : "/board/delBlock",
                            type : "get",
                            data : {bl_uid : boardUid},
                            success : function(statusCode){

                                if(statusCode == blockStatusCode.BLOCK_DEL_SUCCESS){

                                    swal("삭제 성공", "블록 삭제가 성공하였습니다.", "success");
                                    window.location.replace("/board/getMyBlockManage");
                                }else{

                                    swal("삭제 실패", "블록 삭제가 실패하였습니다.", "error");

                                }

                            },
                            error : function(xhs){

                                console.log(xhs.status);

                            }

                        })

                    }
                });

            });
            $dummy.on("click", function(e){

                e.stopPropagation();
                e.preventDefault();
                window.location.href = "/board/boardView?bl_uid="+boardUid;


            });
            $field.append($dummy);

        }

    }
</script>
</html>
