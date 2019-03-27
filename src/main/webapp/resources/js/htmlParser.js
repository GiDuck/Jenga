/**
 *
 *  HTML 파싱을 전담하는 소스
 *
 */



//Bookmark 클래스

function BookMark(title, url, add_date, icon){

    this.title = title;
    this.url = url;
    this.add_date = add_date;
    this.icon = icon;

}

BookMark.prototype.getTitle = function(){

    return this.title;
}

BookMark.prototype.getUrl = function(){

    return this.url;
}

BookMark.prototype.getDate = function(){

    return this.add_date;
}


BookMark.prototype.getIcon = function(){

    return this.icon;
}





//Bookmark folder 클래스

function Folder(title, children, add_date, last_modified){

    this.title = title;
    this.children = children;
    this.add_date = add_date;
    this.last_modified = last_modified;

}

Folder.prototype.getTitle = function(){

    return this.title;
}

Folder.prototype.setChildren = function(children){

    this.children= children;
}


Folder.prototype.getChildren = function(){

    return this.children;
}


Folder.prototype.getDate = function(){

    return this.add_date;
}

Folder.prototype.getLastDate = function(){

    return this.last_modified;
}




//DOM을 생성하는 함수
function parseHTML(rawHTML){
    if(!rawHTML) return;

    let $html = new DOMParser().parseFromString(rawHTML, "text/html");
    let $obj = new Folder("root", new Array(), 0, 0);
    let startT = new Date().getTime();
    let children = $html.body.childNodes;

    for(let i=0; i<children.length; ++i){

        searchElement(children[i], $obj);

    }


    let startE = new Date().getTime();

    console.log("parse ended..." + startE);

    console.log("시간차..." + parseInt(startE-startT));
    return $obj;


}




//재귀함수를 사용하여 HTML 태그를 파싱하는 함수
function searchElement($parentNode, $Obj){

     if($parentNode.querySelectorAll("dl").length > 0){

        let title = $parentNode.querySelector("h3").innerHTML;
        let children = new Array();
        let addDate = $parentNode.querySelector("h3").getAttribute("add_date");
        let modDate = $parentNode.querySelector("h3").getAttribute("last_modified");

        let $children = $parentNode.querySelector("dl");
        let folderObj = new Folder(title, children, addDate, modDate);
        $Obj.getChildren().push(folderObj);


        let childrenArr = $children.children;
        for(let i=0; i<childrenArr.length; ++i){
            searchElement(childrenArr[i], folderObj);
        }

        //일반 북마크 타입, 직속 하위자식 중에 a태그가 존재한다.
    }else if($parentNode.querySelectorAll("a").length > 0){

        let title = $parentNode.querySelector("a").innerHTML;
        let url = $parentNode.querySelector("a").getAttribute("href");
        let addDate = $parentNode.querySelector("a").getAttribute("add_date");
        let icon;
        try{
        icon = $parentNode.querySelector("a").getAttribute("icon");
        }catch(Exception){

        }
        let bookMarkObj = new BookMark(title, url, addDate, icon);
        $Obj.getChildren().push(bookMarkObj);


    }

}


function parseJsonToHTML(bookmarks, title, introduce){

    let $rootDOMElementNode = $("<HTML>").append($("<META>").attr("HTTP-EQUIV", "Content-Type").attr("CONTENT", "text/html; charset=UTF-8"));
    $rootDOMElementNode.append($("<TITLE>").html(title));
    $rootDOMElementNode.append($("<H1>").html(introduce));
    let $bodyNode = $("<DL>").append($("<p>"));
    $rootDOMElementNode.append($("<p>"));


    $rootDOMElementNode.append($bodyNode);

    for(let i=0; i<bookmarks.length; ++i){

        let item = bookmarks[i];
        reculsiveJsonParser($bodyNode, item);

    }

    return $rootDOMElementNode;
}

function reculsiveJsonParser($nowNode, item){

    if(item.children){

        let $innerNode = $("<DT>").append($("<H3>").attr("add_date", item.add_date).attr("last_modified", item.last_modified).html(item.title));
        $nowNode.append($innerNode);

        let $children = $("<DL>").append($("<p>"));
        $innerNode.append($children);
        $innerNode.append($("<p>"));
        let innerChild = item.children;

        for(let j=0; j<innerChild.length; ++j){

            reculsiveJsonParser($children, innerChild[j]);

        }

    }else{
        let $element = $("<A>").attr("href", item.url).attr("add_date", item.add_date).attr("icon", item.icon).attr("last_modified", item.last_modified).html(item.title);
        $nowNode.append($("<DT>").append($element));

    }


}

function parseBookmarkObjType(origin, result){

    if(origin.children){

        let folder = new Folder();
        folder.title = origin.title;
        folder.add_date = origin.add_date;
        folder.last_modified = origin.last_modified;
        let children = new Array();
        for(let i = 0 ; i <origin.children.length; ++i){
            parseBookmarkObjType(origin.children[i], children);
        }
        folder.children = children;
        result.push(folder);

    }else{

        let bookmark = new BookMark();
        bookmark.title = origin.title;
        bookmark.url = origin.url;
        bookmark.icon = origin.icon;
        bookmark.add_date = origin.add_date;
        result.push(bookmark);
    }

}