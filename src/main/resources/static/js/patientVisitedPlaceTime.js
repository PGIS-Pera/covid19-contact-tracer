$('[type="datetime-local"]').prop('max', function () {
    let now = new Date();
    let year = now.getFullYear();
    let month = now.getMonth();
    let date = now.getDate();
    let hours = now.getHours();
    let min = now.getMinutes()
    return `${year}-${month > 9 ? month : '0' + month}-${date > 9 ? date : '0' + date}T${hours > 9 ? hours : '0' + hours}:${min > 9 ? min : '0' + min}`;
});

//max min value changes in leaveAt and arrivalAt
document.getElementById("arrivalAt").addEventListener('change', function () {
    document.getElementById("leaveAt").setAttribute("min", document.getElementById('arrivalAt').value);
})

// AJAX call for autocomplete gramaniladhari
let gramaNiladhari = [];

function gramaniladhari(val) {
    let filedId = val.getAttribute('id');
    console.log(filedId)
    //todo 9/18 finished point
    if (val.value.length > 3) {
        $.ajax({
            type: "GET",
            url: $("#gramaNiladhariSearchUrl").val() + val.value,
            success: function (data) {
                let html_first_part = `<ul class="list-group list-group-flush justify-content-center">`;
                let html_middle_part = ``;
                for (let i = 0; i < data.length; i++) {
                    html_middle_part += `<li onClick="selectGramaNiladhari(${data[i].id})" class="list-group-item font-weight-bold p">${data[i].name} - ${data[i].number}</li >`;
                }
                // $("#gramaNiladhari-box");
                $("#gramaNiladhari-box").show().html(html_first_part + html_middle_part);
                $("#gramaNiladhari").css("background", "#FFF");
                gramaNiladhari = data;
            }
        });
    }
}

function selectGramaNiladhari(val) {
    $("#gramaNiladhari").attr('value', val);
    $("#gramaNiladhari-box").hide();
    interaction.forEach((x) => {
        if (x.id === parseInt(val)) {
            $("#gramaNiladhariOne").val(`${x.name} - ${x.number}`);
            $("#gramaNiladhariId").attr('value', x.id);
        }
    })
}