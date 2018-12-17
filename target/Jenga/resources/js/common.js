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

})