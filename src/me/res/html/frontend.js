let items = $('.circle a');
for(var i = 0, l = items.length; i < l; i++) {
    items[i].style.left = (50 - 35*Math.cos(-0.5 * Math.PI - 2*(1/l)*i*Math.PI)).toFixed(4) + "%";
    items[i].style.top = (50 + 35*Math.sin(-0.5 * Math.PI - 2*(1/l)*i*Math.PI)).toFixed(4) + "%";
}

$('.btn-ball').click(function(e) {
    e.preventDefault();
    $(".circular-menu").toggleClass('hide');
    $('.circle').toggleClass('open');
});
$('.ball-ico').click(function (e) {
    $(".circular-menu").toggleClass('hide');
    $('.circle').toggleClass('open');
});

/*Screen orientation controller*/

if(screen.orientation.type=="landscape-primary"||screen.orientation.type=="landscape-secondary") {
      $(".overlay").hide();
      console.log("Hidden");
}
var inFullScreen=false;
function handleFullScren(e){
    inFullScreen^=1;
    if(inFullScreen){
        window.scrollTo(0, 6);
        $(".overlay").hide();
        screen.orientation.lock("landscape-primary");
    }else{
        console.log(screen.orientation.type);
        if(screen.orientation.type=="landscape-primary"||screen.orientation.type=="landscape-secondary") {
            $(".overlay").show();
        }
    }
}
$("body").on("webkitfullscreenchange mozfullscreenchange msfullscreenchange fullscreenchange",function (e) {
    handleFullScren(e);
})
$("#rotation-button").click(function () {
    toggleFullScreen($("body")[0]);
});

function toggleFullScreen(elem) {
    if (!document.fullscreenElement &&    // alternative standard method
        !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement ) {  // current working methods
        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullscreen) {
            elem.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
        }
        return true;
    } else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }
        return false;
    }
}

var state=false;
$(".ico-mouse").click(function () {
    $(".mouse-buttons").toggleClass('hide');
});
$(".ico-fullscreen").click(function(){
    toggleFullScreen($("body")[0]);
});

$(".movable").on("touchmove",function(e){
    let elem = $($(e.currentTarget).data("target"));
    elem.css("bottom","");
    elem.css('margin-left',e.originalEvent.touches[0].clientX);
    elem.css('top',e.originalEvent.touches[0].clientY);
});
