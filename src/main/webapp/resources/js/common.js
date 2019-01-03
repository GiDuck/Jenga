
//$.busyLoadSetup({ animation: "slide", background: "rgba(255, 152, 0, 0.86)" });

//배열에 마지막을 알아보는 last함수를 prototype으로 선언하여 사용
if (!Array.prototype.last){
    Array.prototype.last = function(){
        return this[this.length - 1];
    };
};

/**
 *  로딩 이미지
 * */
$.ajaxSetup({
    beforeSend: function () {

        //https://www.jqueryscript.net/loading/Loading-Mask-Plugin-jQuery-Busy-Load.html
        // pump, accordion, pulsar, cube, cubes, circle-line, circles, cube-<a href="https://www.jqueryscript.net/tags.php?/grid/">grid
        $.busyLoadFull("show", {

            spinner : "pulsar",
            text : "진행중..."

        });

    }, complete: function () {

        $.busyLoadFull("hide", {});

    }

});


function checkImageFile(e){

    let files =  $(e.target).prop("files");
    let file = files[0];
    let fileName = file.name;
    let extend = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();

    console.log("확장자");
    console.log(extend);

    if(extend != "jpg" && extend != "jpeg" && extend != "png" && extend != "gif"){

        swal("업로드 실패", "이미지 파일의 업로드는 jpg, jpeg, png, gif 파일 확장자만 가능합니다.", "error");
        $(e.target).val("");
        return;

    }else if(file.size > 1048576){

        swal("업로드 실패", "파일의 크기는 1MB를 초과할 수 없습니다.", "error");
        $(e.target).val("");
        return;

    }


}


function TimeChecker() {

    let startTime;
    const TIME_INTERVAL = 2000;
    this.validateOverInterval = function () {

        if(!startTime){
            startTime = new Date().getTime();

        }

        let endTime = new Date().getTime();

        if (endTime - startTime < TIME_INTERVAL) {
            return false;
        } else if(endTime - startTime >= TIME_INTERVAL) {
            startTime = new Date().getTime();
            return true;
        }
    }

}