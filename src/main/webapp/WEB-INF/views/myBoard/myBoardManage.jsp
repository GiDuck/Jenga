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
                                <img name="bk_image"
                                     onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'"
                                     src="" alt="Rounded Image" class="img-rounded img-responsive">
                                <br><br>
                                <div class="text"><p name="bk_title" class="name"></p></div>

                                <div class="card-body details-center ml-auto mr-auto">
                                    <div class="author w-50 text-center">
                                        <img src=""
                                             onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/placeholder.jpg'"
                                             alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                        <div class="text">
                                            <span class="name"></span>
                                            <div class="meta"><i class="fa fa-heart" name="likeIcon" style="color : red"></i>0</div>
                                        </div>

                                    </div>
                                </div>
                                <br>
                                <div class="card-footer">
                                    <div class="row">
                                        <div class="col-6 text-center controlBtn"><i class="fa fa-pencil"></i>&nbsp수정하기</div>
                                        <div class="col-6 text-center controlBtn"><i class="fa fa-trash"></i>&nbsp삭제하기</div>
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
            let $dummy = $cardForm.clone();
            $dummy.css("display", "block");
            $dummy.find("p[name='bk_title']").text(item["bl_title"]);

            $dummy.on("click", function(e){

                e.stopPropagation();
                e.preventDefault();

                alert(item["bl_writer"]);
                location.href = "/board/boardView?bl_uid=" + item["bl_writer"];

            });
            $field.append($dummy);

        }

    }
</script>
</html>
