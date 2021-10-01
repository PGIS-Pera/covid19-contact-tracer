$('[type="date"]').prop('max', function () {
    return new Date().toJSON().split('T')[0];
});


// AJAX call for autocomplete gramaniladhari
let gramaNiladhari = [];

function gramaniladhari(val) {
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
    gramaNiladhari.forEach((x) => {
        if (x.id === parseInt(val)) {
            $("#gramaNiladhariOne").val(`${x.name} - ${x.number}`);
            $("#gramaNiladhariId").attr('value', x.id);
        }
    })
}
