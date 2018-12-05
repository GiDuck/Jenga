
<div class="container" style="width:auto; height:900px;">


<div id="moveBtn" class="btn btn-danger w-100">move to join page..</div>
    <div id="delBtn" class="btn btn-danger w-100">del member</div>
    <div id="modBtn" class="btn btn-danger w-100">mod member</div>


</div>
<script>
$(document).ready(function() {

    setNavType("blue");


    $("#moveBtn").on('click', function () {

        location.replace("/login");  //원래 /join#regForm 머시기 였음

    });

    $("#moveBtn2").on('click', function () {

        location.replace("/board/stackBlock");
    });

    $(document).ready(_ => {
        setNavType("blue");
        $("#delBtn").on('click', _ => {
            location.href = "delMemInfo";
        });
    });

    $(document).ready(_ => {
        setNavType("blue");
        $("#modBtn").on('click', _ => {
            location.href = "modMemInfo";
        });
    });

});



</script>