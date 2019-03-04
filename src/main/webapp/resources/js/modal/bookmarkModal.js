
// 북마크파일 업로드
function makeUploadBookMarkFileModal(type){

    let $syncDateField;

    if(type == "chrome"){

        $syncDateField = $("#chromeSyncDate");


    }else if(type == "explore"){

        $syncDateField =  $("#exploreSyncDate");


    }else{

        swal({

            text : "지원되지 않는 북마크 입니다.",
            type : "error"

        });

        return;

    }


    let header = $("<h3>").addClass("modal-title text-center").html(type + " 북마크 동기화")
        .append($("<br>"))
        .append($("<p>").html(type + "북마크를 동기화 합니다. 북마크 내보내기 기능을 사용하여 HTML 파일로 지정해 주십시오."));


    let body = $("<div>").addClass("w-100")
        .append($("<div>").append($("<input>").attr("type", "file").attr("id", "BKfile").attr("accept", ".html")))
        .append($("<br>"))
        .append($("<div>").append($("<div>").addClass("btn w-100").html("북마크 동기화 ")).on('click', function(e){

            e.stopPropagation();
            e.preventDefault();

            let $upFile = document.getElementById("BKfile");
            let upFile = $upFile.files[0];
            let formData = new FormData();
            formData.append('file', upFile);
            formData.append('bp_browstype', type);
            formData.append('bp_booktype', 'html');


            if(upFile.size > 10485760 ){
                swal('파일 업로드 실패', '파일의 크기는 10MB를 초과할 수 없습니다.', 'error');
                return;

            }else if(upFile.size < 10){

                swal('파일 업로드 실패', '파일이 없거나 크기가 너무 작습니다.', 'error');
                return;

            }else if(upFile.type != "text/html"){

                swal('파일 업로드 실패', '가져오기는 html 파일만 가능합니다.', 'error');
                return;

            }

            $.ajax({

                type : "POST",
                url : "/board/fileUpload",
                data : formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType : false,
                success : function(){
                    swal('동기화 성공', '북마크가 성공적으로 업로드 되었습니다.', 'success');
                    let syncDate = new Date();
                    let syncComDateStr = syncDate.getFullYear() + "/" + (syncDate.getMonth()+1)+"/" + syncDate.getDate()
                        + " " + syncDate.getHours()+":"+syncDate.getMinutes();
                    $syncDateField.html(syncComDateStr);

                    $(".modal").modal("hide");

                },
                error : function(xhs, status, error){

                    swal('동기화 실패', '북마크 동기화에 실패하였습니다.', 'error');
                    $(".modal").modal("hide");

                }


            });


        }));



    return ModalFactory("simple", header, body);

}



//북마크 혹은 폴더의 내용을 변경하게 하는 모달
function makeAddOrModifyElementModal($element, isAdd, successFunc)
{
    let $title = $($element).find("input[name='title']");
    let $url = $($element).find("input[name='url']");
    let isFolder = $($element).find("input[name='isFolder']").val();
    let timeStamp = $($element).find("input[name='timeStamp']").val();
    let type;
    let token = isAdd ? "추가" : "수정";
    let bookmarks = popChild("right");

    if(isFolder === "true"){

        type = "폴더";

    }else if(isFolder === "false"){

        type = "북마크";
    }

    let header = $("<h3>").addClass("modal-title text-center").html(type + " "+token+"하기")
        .append($("<br>"));

    let body = $("<div>").addClass("form-group")
        .append($("<label>").html("TITLE"))
        .append($("<input>").attr("name", "title").addClass("form-control border-input").val($title.val()))
        .append($("<br>"));

    if(isFolder === "false"){
        body.append($("<label>").html("URL"))
            .append($("<input>").attr("name", "url").addClass("form-control border-input").val($url.val()));
    }

    body.append($("<button>").attr("name", "elementSubmitBtn").addClass("btn btn-block btn-round").html(token+"하기").css("margin-top", "40px").on('click', function(e){
        e.preventDefault();

        let title = $(e.target).parent().find("input[name='title']").val();
        let url;
        let selectedElement;

        //수정일 경우 Action
        if(!isAdd){


            for(let i = 0; i<bookmarks.length; ++i){

                if(bookmarks[i].add_date === timeStamp){
                    selectedElement = bookmarks[i];
                    break;
                }

            }

            selectedElement.title = title;


        }

        if(isFolder === "false"){

            url = $(this).parent().find("input[name='url']").val();

            if(!isAdd){
                selectedElement.url = url;
            }

            if(successFunc){
                successFunc(bookmarks, title, url);
            }

        }else{

            if(successFunc){
                successFunc(bookmarks, title);
            }
        }

    })).append($("<br>"));

    let modal = ModalFactory("simple", header, body);


    $(modal).on("keydown",function(e){

        if(e.keyCode === 13){
            $($(e.target).parent().find("button[name='elementSubmitBtn']")).trigger("click");
        }


    });



    return modal;

}

