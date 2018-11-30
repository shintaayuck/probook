window.onload = function() {
    if (document.body.contains(document.getElementById("book-detail-not-for-sale"))) {
        document.getElementById("orderbutton").disabled = true;
    } else {

        document.getElementById("orderbutton").onclick = async function() {

            let order_count = parseInt(
                document.getElementById("order-select").selectedOptions[0].value
            );

            let url_arr = window.location.href.split("/");
            let book_id = String(url_arr[url_arr.length - 1]);
            console.log(order_count);
            console.log(book_id);
            let data = new FormData();
            data.append("order_count", order_count);
            data.append("book_id", book_id);

            let resp = await fetch("/order", {
                method: 'POST',
                body: data,
                headers: {
                    'Accept': 'application/json'
                }
            });
        //let dataresp = JSON.parse(resp);
        let dataresp = await resp.json();
        console.log(dataresp);
        let code = dataresp.statusCode;
        console.log(code)
        document.getElementById("overlay").style.display = "flex";
        if (code == 1) {
        // show notification
          document.getElementById("notif-error").style.display = "none";
          document.getElementById("notif-content").style.display = "flex";
          document.getElementById("order-count").innerHTML = dataresp.order_id;
        } else if (code == 2) {
          // show error
          let e = document.getElementById("notif-error");
          e.innerText = "Sorry, not enough amount.";
          document.getElementById("notif-content").style.display = "none";
          e.style.display = "block";
        } else if (code == 3) {
          // show error
          let e = document.getElementById("notif-error");
          e.innerText = "Sorry, something went wrong in our server.";
          document.getElementById("notif-content").style.display = "none";
          e.style.display = "block";
        } else {
          console.log("Status code not recognized");
        }
        document.getElementById("notif-close").onclick = function() {
          document.getElementById("overlay").style.display = "none";
        };
        document.getElementById("overlay").onclick = function() {
          document.getElementById("overlay").style.display = "none";
        };
      };
    }
};