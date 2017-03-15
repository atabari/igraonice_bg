/**
 * Created by User on 1/9/2016.
 */
function getCookies(apartmentId){
    var pairs = document.cookie.split(";");
    var cookies = {};
    for (var i=0; i<pairs.length; i++){
        var pair = pairs[i].split("=");
        cookies[pair[0]] = pair[1];
    }
    console.log(cookies);

    return cookies;
}
