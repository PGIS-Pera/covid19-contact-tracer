$('#province').bind("change", function () {
    optionFieldAdd(this.value);
});

let optionFieldAdd = (value) => {
    let url = "[[${districtURL}]]" + value;
    let html = "";
    $.get(url, function (data) {
        data.forEach((item) => {
                html += `<option value="${item.id}">${item.name} </option>`
            }
        )
        $("#district").html(html);
    });
}