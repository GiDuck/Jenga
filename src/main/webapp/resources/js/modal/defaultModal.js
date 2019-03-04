<!-- Modal Default Form -->

var $defaultModal = $('<div id = defaultModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="false"><div class="modal-dialog modal-register">    <div class="modal-content">    <div id="modalHeader" class="modal-header no-border-header text-center">    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button></div><div id="modalBody" class="modal-body"></div></div></div></div>');
var $tripleModal = $('<div id ="tripleModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="false"> <div class="modal-dialog modal-register"><div class="modal-content">    <div id="modalHeader" class="modal-header no-border-header text-center">    <button type="button" class="close" data-dismiss="modal" aria-label="Close">    <span aria-hidden="true">&times;</span></button></div><div id="modalBody" class="modal-body"></div> <div id="modalFooter" class="modal-footer"></div></div></div></div>');


    //Modal Object
    //Modal is Mother Object of all Modals.
    //.. You can inherit and overwrite custom modals by this Object.
    function Modal () {

        this.modal = $defaultModal.clone();
        this.header = this.modal.find("#modalHeader");
        this.body = this.modal.find("#modalBody");
        this.footer = this.modal.find("#modalFooter");

    }



Modal.prototype.setModal = function(modal){

    this.modal = modal;
    this.header = this.modal.find("#modalHeader");
    this.body = this.modal.find("#modalBody");
    this.footer = this.modal.find("#modalFooter");

}

Modal.prototype.getModal = function(){

    return this.modal;
}

Modal.prototype.setHeader = function(header){
    this.header = header;
}

Modal.prototype.getHeader = function(){

    return this.header;
}

Modal.prototype.setBody = function(body){
    this.body = body;
}

Modal.prototype.getBody = function(){

    return this.body;
}

Modal.prototype.setFooter = function(footer){
    this.footer = footer;
}

Modal.prototype.getFooter = function(){

    return this.footer;
}



//Modal Factory Function
//.. You can make a modal to match your purpose simplly.
//... Parameter is type (simple, triple), simple is made by two part. header and body.
//... triple is made by three part. header and body and footer.
//... You can define DOM object and inject that.
//... So Modal Factory will be assemble that.

function ModalFactory(type, header, body, footer){

    this.modal = new Modal();

    switch(type){

        case 'simple' : {

            this.modal.getHeader().append(header);
            this.modal.getBody().append(body);

        }break;

        case 'triple' : {

            this.modal.setModal($tripleModal.clone());
            this.modal.getHeader().append(header);
            this.modal.getBody().append(body);
            this.modal.getFooter().append(footer);

        }break;

        default : break;

    }

    this.modal.getModal().modal();
    return this.modal.getModal();

}



// header, body, footer로 이루어진 예, 아니오를 선택할 수 있는 모달
function makeCheckableModal(title, subTitle, content, okFunc, refuseFunc){


    let successBtn = ($("<button>").attr("id", "btn_OK").attr("data-dismiss", "modal").addClass("btn btn-default btn-link").html("예"))
        .on("click", function(){
            okFunc();
        });

    let failBtn = ($("<button>").attr("id", "btn_Refuse").attr("data-dismiss", "modal").attr("type", "button").addClass("btn btn-danger btn-link close").html("아니오"))
        .on("click", function(){

            refuseFunc();
        });

    let header = $("<h3>").addClass("modal-title text-center").html(title)
        .append($("<br>"))
        .append($("<p>").html(subTitle));

    let body = $("<div>")
        .append("<h4>").html(content);

    let footer = $("<div>").addClass("w-100").css("padding", "0")
        .append($("<div>").addClass("left-side").append(successBtn))
        .append($("<div>").addClass("divider"))
        .append($("<div>").addClass("right-side").append(failBtn));




    return  ModalFactory("triple",  header, body, footer);


}


// header + body로 이루어진 2단계 모달
function makeSimpleNotifyModal (title, subTitle, content, closeParent){

    let header = $("<h3>").addClass("modal-title text-center").html(title)
        .append($("<br>"))
        .append($("<p>").html(subTitle));


    let body = $("<div>")
        .append("<h4>").html(content);

    let modalFactory = ModalFactory("simple", header, body);
    modalFactory.on('hide.bs.modal', function() {


        if(closeParent){
            closeParent.modal('hide');

        }

    });

    return modalFactory;

};

