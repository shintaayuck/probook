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