/**
 * Created by User on 2/5/2016.
 */
function checkEmail() {

    var email = document.getElementById('email');
    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

    if (!filter.test(email.value)) {
        document.getElementById('email_error').innerHTML = "Molimo unesite validan mail!";
        return false;
    }else{
        document.getElementById('email_error').innerHTML = "";
        return true;
    }
}

function checkName(){
    var letters = /^[A-Za-z\s]+$/;
    var name = document.getElementById("name").value;
    var space = /\s/;

    if(!name.match(letters) || !name.match(space)){
        document.getElementById('name_error').innerHTML="Molimo unesite ispravno Ime i Prezime!";
        return false;
    }else{
        document.getElementById('name_error').innerHTML = "";
        return true;
    }

}
function numberLength(){
    var number = document.getElementById("phone").value;

    if(number.length < 16 && number.length > 8){
        document.getElementById('phone_error').innerHTML="";
        return true;
    }else{
        document.getElementById('phone_error').innerHTML="Broj mora imati izmedju 9-15 karaktera!";
        return false;
    }
}

