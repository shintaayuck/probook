function validateInput() {
    var name = document.forms["editform"]["name"].value;
    var address = document.forms["editform"]["address"].value;
    var phone = document.forms["editform"]["phone"].value;
    let button = document.getElementById("submit");
    if ((name == "") || (address == "") || (phone == "")) {
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