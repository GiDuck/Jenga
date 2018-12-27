function TwitterHeart(element){

    this.$element = element;
    this.status = undefined;


}

TwitterHeart.prototype.like = function(){

    this.$element.addClass("heartAnimation").attr("rel","unlike");
    this.$element.css("background-position","");
    this.status = true;


}

TwitterHeart.prototype.unlike = function() {

    this.$element.removeClass("heartAnimation").attr("rel", "like");
    this.$element.css("background-position","left");
    this.status = false;

}

TwitterHeart.prototype.toggle = function(){

    if(this.status){

        this.unlike();

    }else{

        this.like();
    }

}