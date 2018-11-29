function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    document.getElementById("oauth_id").value = profile.getId();
    document.getElementById("oauth_name").value = profile.getName();
    document.getElementById("oauth_img").value = profile.getImageUrl();
    document.getElementById("oauth_email").value = profile.getEmail();
    document.getElementById("oauth").submit();
    signOut();
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
}

function updateButton() {
    let submit = document.getElementById("submit");
    for (let element of document.getElementsByTagName("input")) {
        if (element.value === "") {
            submit.disabled = true;
            return;
        }
    }
    submit.disabled = false;
}

for (let element of document.getElementsByTagName("input")) {
    if (!element.classList.contains("check")) {
        element.onchange = updateButton;
    }
}

updateButton();