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

//제이쿼리에 의해 전처리된 html 직속 배열들
    let $html = $.parseHTML(rawHTML);


//제일 최상단 루트를 생성한다.
    var $obj = new Folder("root", new Array(), 0, 0);


//시작시간 기록을 위한 변수
    let startT = new Date().getTime();
    console.log("parse stared..." + startT);


    //직속 자식 DOM elements들을 가져온다. 그리고 for문을 통해 재귀함수를 호출하여 자식노드를 모두 순회한다.
    for(let i=0; i<$html.length; ++i){

        let $item = $html[i];
        searchElement($item, $obj);


    }


    let startE = new Date().getTime();

    console.log("parse ended..." + startE);

    //파싱에 걸린 시간을 나타내줌
    console.log("시간차..." + parseInt(startE-startT));
    return $obj;


}




//재귀함수를 사용하여 HTML 태그를 파싱하는 함수
function searchElement($parentNode, $Obj){


    //자식없는 태그 (즉, 쓸모없는 태그들)
    if($($parentNode).children().length < 1){
        return;

        //폴더 타입, 직속 하위자식 중에 dl태그가 존재한다.
    }else if($($parentNode).children("dl").length > 0){


        let title = $($parentNode).children("h3").html();
        let children = new Array();
        let addDate = $($parentNode).children("h3").attr("add_date");
        let modDate = $($parentNode).children("h3").attr("last_modified");

        let $children = $($($parentNode).children("dl")).children("dt");
        let folderObj = new Folder(title, children, addDate, modDate);
        $Obj.getChildren().push(folderObj);


        for(let i=0; i<$children.length; ++i){

            searchElement($children[i], folderObj);
        }

        //일반 북마크 타입, 직속 하위자식 중에 a태그가 존재한다.
    }else if($($parentNode).children("a").length > 0){

        let title = $($parentNode).children("a").html();
        let url = $($parentNode).children("a").attr("href");
        let addDate = $($parentNode).children("a").attr("add_date");
        let modDate = $($parentNode).children("a").attr("icon");

        let bookMarkObj = new BookMark(title, url, addDate, modDate);
        $Obj.children.push(bookMarkObj);

        return;

    }else{

        return;


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
        return;


    }


}