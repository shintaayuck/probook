async function isValidCardnumber(){
    var cardnumber = document.getElementById("cardnumber").value;
    let button = document.getElementById("submit");
    var result = await fetch("http://localhost:3000/api/validate?card_no=" + cardnumber);
    var textResult = await result.text();
    var regex = /is found/g;
    if(!(regex.test(textResult))){
        console.log("error");
        alert("invalid card number");
        button.disabled = true;
        return false;
    }
    console.log("ok");
    button.disabled = false;
    return true;
}

function validateInput() {
    var name = document.getElementById("name").value;
    var address = document.getElementById("address").value;
    var phone = document.getElementById("phone").value;
    let button = document.getElementById("submit");

    if ((name == "") || (address == "") || (phone == "") || (cardnumber == "")) {
        alert("form cannot be empty");
        button.disabled = true;
        return false;
    }
    if (name.length > 20) {
        alert("name cannot be longer than 20 chars");
        button.disabled = true;
        return false;
    }
    if (phone.length < 9 || phone.length > 12 || isNaN(phone)) {
        alert("invalid phone number input");
        button.disabled = true;
        return false;
    }
    button.disabled = false;
    return true;
} 

function submitFile() {
    document.getElementById("hidden-button").click();
    addOnChangeProfilePictureName();
}

function addOnChangeProfilePictureName() {
    document.getElementById('hidden-button').onchange = function () {
        document.getElementById('file-name').value=this.files[0].name;
    };
}