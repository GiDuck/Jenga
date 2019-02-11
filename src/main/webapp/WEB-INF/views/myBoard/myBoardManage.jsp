<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>

    .card{
        cursor : pointer;
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
                                        <div class="text">
                                            <span class="name"></span>
                                            <div class="meta"><i class="fa fa-heart" name="likeIcon"></i></div>
                                        </div>

                                    </div>
                                </div>
                                <br>
                                <div class="card-footer">
                                    <div class="row">
                                        <div class="col-6 text-center"><a name="modify"><i class="fa fa-pencil"></i>&nbsp수정하기</a></div>
                                        <div class="col-6 text-center"><a name="delete"><i class="fa fa-trash"></i>&nbsp삭제하기</a></div>
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
        console.log(items);
        if(items.length === 0){
            let bf_Id = document.getElementById("block_field");
            console.log(bf_Id);
            let p = document.createElement("p");
            let txt = document.createTextNode("글이 없습니다.");
            p.appendChild(txt);
            bf_Id.appendChild(p);
            document.body.appendChild(bf_Id);
        }else{

            let $cardForm = $("#bkCard");
            let $field = $("#block_field");
            $field.empty();

            for (let i = 0; i < items.length; ++i) {

                let item = items[i];
                let $dummy = $cardForm.clone();
                $dummy.css("display", "block");
                $dummy.find("p[name='bk_title']").text(item["bl_title"]);
                $dummy.find("img[name='bk_image']").attr("src", /blockimg/ + item["bti_url"]);
                $dummy.find("i[name='likeIcon']").text(item["likes"]);
                $dummy.find("a[name='modify']").attr("href", "/board/stackBlock?status=modify&bl_uid=" + item['bl_uid']);
                $dummy.find("a[name='delete']").attr("href", "/board/delBlock?bl_uid=" + item['bl_uid']);
                $dummy.find("img[name='bk_image']", "p[name='bk_title']").on("click", function (e) {

                    e.stopPropagation();
                    e.preventDefault();

                    location.href = "/board/boardView?bl_uid=" + item["bl_uid"];

                });
                $field.append($dummy);

            }
        }
    }
</script>
</html>
