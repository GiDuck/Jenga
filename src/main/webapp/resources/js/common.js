window.onload = function () {


    //배열에 마지막을 알아보는 last함수를 prototype으로 선언하여 사용
    if (!Array.prototype.last) {
        Array.prototype.last = function () {
            return this[this.length - 1];
        };
    };



}





function parseStatusArrToObj(parsedArr) {

    let statusObj = Object.create(null);
    for (let i in parsedArr) {
        statusObj[parsedArr[i].key] = parsedArr[i].value;
    }
    return statusObj;

}


/**
 *  로딩 이미지
 * */
$.ajaxSetup({
    beforeSend: function () {
        //https://www.jqueryscript.net/loading/Loading-Mask-Plugin-jQuery-Busy-Load.html
        // pump, accordion, pulsar, cube, cubes, circle-line, circles, cube-<a href="https://www.jqueryscript.net/tags.php?/grid/">grid
        $.busyLoadFull("show", {
            spinner: "pulsar",
            text: "진행중..."
        });
    }, complete: function () {
        $.busyLoadFull("hide", {});
    }

});


function exceptionHandler(func){
    try{
        func();
    }catch(e){
        console.error(e);
    }
}

function busyLoadHide() {
    $.busyLoadFull("hide", {});
}


function checkImageFile(e) {

    let files = $(e.target).prop("files");
    let file = files[0];
    let fileName = file.name;
    let extend = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();


    if (extend != "jpg" && extend != "jpeg" && extend != "png" && extend != "gif") {

        swal("업로드 실패", "이미지 파일의 업로드는 jpg, jpeg, png, gif 파일 확장자만 가능합니다.", "error");
        $(e.target).val("");
        return;

    } else if (file.size > 1048576) {

        swal("업로드 실패", "파일의 크기는 1MB를 초과할 수 없습니다.", "error");
        $(e.target).val("");
        return;

    }


}


function TimeChecker(interval) {

    let startTime;
    const TIME_INTERVAL = interval ? parseInt(interval) : 2000;
    this.validateOverInterval = function () {

        if (!startTime) {
            startTime = new Date().getTime();

        }

        let endTime = new Date().getTime();

        if (endTime - startTime < TIME_INTERVAL) {
            return false;
        } else if (endTime - startTime >= TIME_INTERVAL) {
            startTime = new Date().getTime();
            return true;
        }
    }

}


function PreLoader() {

    let $preLoader;
    let $preLoaderDOM;

    (function () {

        $preLoaderDOM = $("<div>").addClass("w-100 text-center").css("visibility", "hidden")
            .append($("<div>").addClass("preloader").append($("<div>").addClass("uil-reload-css").append($("<div>"))).append($("<h5>").attr("id", "preLoader_innerText").text("Loading More")));


    })();

    this.show = function () {
        $preLoader.css("visibility", "visible");
    }


    this.hide = function (time) {

        let timer = parseInt(time);

        setTimeout(function () {
            $preLoader.css("visibility", "hidden");
        }, timer);

    }


    this.disappear = function () {
        $preLoader.css("display", "gone");
    }


    this.getPreLoaderDomInstance = function () {

        return $preLoader;
    }

    this.init = function () {

        $("#preLoaderContainer").html($preLoaderDOM);
        $preLoader = $preLoaderDOM;

    }

    this.changeId = function (id) {

        $preLoader.attr("id", id);
    }

    this.changeText = function (str) {

        $preLoader.find("#preLoader_innerText").text(str);

    }

}


function DateTimeFormatter() {

    this.getFullDateTime = function (longDate) {

        let dateObj = new Date(parseInt(longDate));
        let dateStr = dateObj.getFullYear() + "/" + (dateObj.getMonth() + 1) + "/" + dateObj.getDate()
            + " " + (dateObj.getHours() > 9 ? dateObj.getHours() : "0" + dateObj.getHours()) + ":" + (dateObj.getMinutes() > 9 ? dateObj.getMinutes() : "0" + dateObj.getMinutes());

        return dateStr;

    }


}