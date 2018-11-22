window.onload = function() {
  document.getElementById("orderbutton").onclick = async function() {
    // create request

    let order_count = parseInt(
      document.getElementById("order-select").selectedOptions[0].value
    );

    if (order_count == 0) {
      let e = document.getElementById("notif-error");
      e.innerText = "Order count can't be 0.";
      document.getElementById("notif-content").style.display = "none";
      e.style.display = "block";
    } else {
      document.getElementById("notif-error").style.display = "none";
      document.getElementById("notif-content").style.display = "flex";
      let data = new FormData();
      data.append("order_count", order_count);

      let url_arr = window.location.href.split("/");
      data.append("book_id", parseInt(url_arr[url_arr.length - 1]));

      let resp = await fetch("/order", {
        method: "POST",
        body: data
      });

      let body = await resp.json();

      // show notification
      document.getElementById("order-count").innerHTML = body.order_id;
    }

    document.getElementById("overlay").style.display = "flex";
  };

  document.getElementById("notif-close").onclick = function() {
    document.getElementById("overlay").style.display = "none";
  };

  document.getElementById("overlay").onclick = function() {
    document.getElementById("overlay").style.display = "none";
  };
};
