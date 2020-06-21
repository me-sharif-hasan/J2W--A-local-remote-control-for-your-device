var holder={
    "socket":null,
    "prev":{
        "x":0,
        "y":0
    },
    "start":{
        "x":0,
        "y":0
    },
    "temp":{
        "x":-1,
        "y":-1
    },
    "create_ws":function(){
        var url=window.location.href;
        url = /*"ws://192.168.43.148:8080/?get=ws";*/ "ws://"+url.split("/")[2]+"?get=ws";
        this.socket=new WebSocket(url);
    },
    "ws_send":function(data){
        this.socket.send(data);
    },
    "current_operation_name":"",
    "flag":false

};

let cast=$("#screen");
function convertCord(x,y){
    oy=cast[0].naturalHeight;
    ox=cast[0].naturalWidth;
    ey=cast[0].height;
    ex=cast[0].width;
    l=Math.round((x/ex)*ox);
    r=Math.round((y/ey)*oy);
    return [l,r];
}
function convertEvent(e){
    if(e.originalEvent.targetTouches!==undefined){
        return e.originalEvent.targetTouches[0];
    }else{
        return e;
    }
}


holder.create_ws();
holder.socket.onopen = function (e) {
}

/*
@Mouse button simulator
 */
$(".sim").on("touchstart",function (e) {
    $(e.currentTarget).toggleClass('btn-hover');
    holder.ws_send($(e.currentTarget).data('event')+"Down,$1$");
})
$(".sim").on("touchend",function (e) {
    $(e.currentTarget).toggleClass('btn-hover');
    holder.ws_send($(e.currentTarget).data('event')+"Up,$1$");
})
cast.click(function () {
    holder.ws_send("MouseLeftDown,$1$");
    holder.ws_send("MouseLeftUp,$1$");
});
cast.on("contextmenu",function (e) {
    holder.ws_send("MouseRightDown,$1$");
    holder.ws_send("MouseRightUp,$1$");
    return false;
});
/*
@End of mouse button simulator
 */



/*
@Mouse pointer simulator
 */
function handleTouch(e,obj){
    let x=e.clientX-obj.offset().left;
    let y=e.clientY-obj.offset().top;
    dp=convertCord(x,y);
    let dx=holder.start.x-dp[0];
    let dy=holder.start.y-dp[1];

    if(holder.temp.x<0&&holder.temp.y<0){
        holder.temp.x=dp[0];
        holder.temp.y=dp[1];
        setEndPos([dp[0],dp[1]]);
        console.log("Temp set= "+dp[0]+" "+dp[1]);
        return 0;
    }
    holder.temp.x=Math.min(Math.max(0,holder.prev.x-dx),cast[0].naturalWidth);
    holder.temp.y=Math.min(Math.max(0,holder.prev.y-dy),cast[0].naturalHeight);
    holder.ws_send("PointerMove,$"+holder.temp.x+"$,$"+holder.temp.y+"$");
}
function setStartPos(dx){
    holder.start.x=dx[0];
    holder.start.y=dx[1];
}

$(".touchpad").on("mouseover touchstart",function (e) {
    var obj=$(e.currentTarget);
    let d=convertCord(convertEvent(e).clientX-obj.offset().left,convertEvent(e).clientY-obj.offset().top);
    setStartPos(d);
})

$(".touchpad").on("touchmove mousemove",function (e) {
    handleTouch(convertEvent(e),$(e.currentTarget));
})
function setEndPos(dx){
    holder.prev.x=dx[0];
    holder.prev.y=dx[1];
}
$(".touchpad").on("mouseout touchend",function (e) {
    setEndPos([holder.temp.x,holder.temp.y]);
})
/*
@End of mouse pointer simulator
 */


$('div[class^="key--"]').on("touchstart touchend",function(e){
/*

    */
    var actionType=e.handleObj.type;
    var target=$(e.currentTarget);
        key=target.data("key");
        if(key==undefined){
            key=target.data("char").charCodeAt(0);
        }
        if(actionType=='touchstart'){
            holder.ws_send("KbdDown,$"+key+"$");
        }else{
            holder.ws_send("KbdUp,$"+key+"$");
        }
});