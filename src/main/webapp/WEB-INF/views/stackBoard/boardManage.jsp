<%--
  Created by IntelliJ IDEA.
  User: YUJUN
  Date: 2018-12-27
  Time: 오후 8:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="wrapper">
    <div class="main">
        <div class="section">
            <div class="container">
                <div class="w-100 text-center" id="loaderContainer" style="visibility: hidden;">
                    <div class="preloader">
                        <div class='uil-reload-css' style=''>
                            <div></div>
                        </div>
                        <h5>Loading More </h5>
                    </div>
                </div>

                <div class="col-md-3 col-sm-4" id="bkCard" style="display : none">
                    <div class="card card-plain text-center">
                        <div class="card-image">
                            <img name="bk_image" onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'"
                                 src="" alt="Rounded Image" class="img-rounded img-responsive">
                            <br><br>
                            <div class="text"><p name="bk_title" class="name"></p></div>

                            <div class="card-body details-center">
                                <div class="author w-50 text-center">
                                    <img src="" onerror="this.src = '${pageContext.request.contextPath}/resources/assets/img/faces/joe-gardner-2.jpg'"
                                         alt="Circle Image" class="img-circle img-no-padding img-responsive">
                                    <div class="text">
                                        <span class="name"></span>
                                        <div class="meta"><i class="fas fa-heart" name="likeIcon"></i>0</div>
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

    function PreLoader(){

        this.preloader = $("#loaderContainer");

    }

    PreLoader.prototype.show = function(){

        this.preloader.css("visibility", "visible");

    };

    PreLoader.prototype.hide = function(){

        this.preloader.css("visibility", "hidden");

    }

    let preLoader = new PreLoader();

    $(document).ready(function () {
        setNavType("blue");

    }

    //ajax 호출을 통해 가져온 데이터를 화면에 전시함
    function renderItems(items) {

        let $cardForm = $("#bkCard");
        let $field = $("#block_field");

        $field.empty();

        for (let i = 0; i < items.length; ++i) {

            let block = items[i];

            let $dummy = $cardForm.clone();
            $dummy.attr("id", null);
            $dummy.css("display", "block");
            //$dummy.find("img[name='bk_image']").attr("src", block.blockImg);
            $dummy.find("p[name='bk_title']").html(block.bl_title);
            /*   $dummy.find(".author > img").attr("src", block.writerProfile); */
            //$dummy.find(".author > .name").attr("src", block.bl_writer);

            //클릭시 Datail 보기로 이동
            $dummy.on("click", function (e) {

                location.href="/board/boardView?bl_uid="+block.bl_uid;

            });

            $field.append($dummy);


        }


    }
</script>
</html>
