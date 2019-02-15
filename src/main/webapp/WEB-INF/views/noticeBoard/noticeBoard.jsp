<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<body class="about-us sidebar-collapse">
<div class="page-header page-header-small" style="background-image: url('${pageContext.request.contextPath}/resources/assets/img/sections/gerrit-vermeulen.jpg');">
    <div class="filter filter-danger"></div>
    <div class="content-center">
        <div class="container">
            <h1>잠시만요,
                <br /> 새로운 소식이 있어요!</h1>
            <h3>최근 바뀐 공지사항을 알아보세요.</h3>
        </div>
    </div>
</div>
<div class="main">
    <div class="section">
        <div class="container">

            <h3 class="more-info">최근 공지사항</h3>

            <div class="row coloured-cards">


                <div class="col-md-4 col-sm-6" id="dummyNoticeCard" style="cursor : pointer">
                    <div class="card-big-shadow">
                        <div class="card card-just-text" data-background="color" data-color="blue" data-radius="none">
                            <div class="card-body">
                                <h6 class="card-category">2018.01.01</h6>
                                <h4 class="card-title">
                                    <a>TITLE</a>
                                </h4>
                                <p class="card-description">18번째 오늘의 공지입니다. 드디어 새로운 버전이 업데이트 되었습니다.</p>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>
</div>

<script>


    function getNoticeCard(){

    //Write source that operated by ajax.

    }

    function renderNoticeCard(){


    }


    $(document).ready(function(){

        navbarObj.setType("navbar-transparent");
        $("#dummyNoticeCard").on("click", function(e){
            e.preventDefault();
            makeSimpleNotifyModal('제목', '2018.01.01', '... ', null);

        });


    });




</script>