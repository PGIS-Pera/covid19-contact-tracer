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
    if (val.value.length > 3) {
        $.ajax({
            type: "GET",
            url: $("#gramaNiladhariSearchUrl").val() + val.value,
            success: function (data) {
                let html_first_part = `<ul class="list-group list-group-flush justify-content-center">`;
                let html_middle_part = ``;
                for (let i = 0; i < data.length; i++) {
                    html_middle_part += `<li id="${filedId}-${data[i].id}" onClick="selectGramaNiladhari(this)" class="list-group-item font-weight-bold p">${data[i].name} - ${data[i].number}</li >`;
                }
                // $("#gramaNiladhari-box");
                $(`#${filedId}-box`).show().html(html_first_part + html_middle_part);
                $(`#${filedId}`).css("background", "#FFF");
                gramaNiladhari = data;
            }
        });
    } else {
        $(`#${filedId}-box`).hide();
    }
}

function selectGramaNiladhari(val) {
    let id = val.getAttribute('id');
    let id_part = id.split("-")[0];
    let _id = id.split("-")[1];
    console.log(_id)

    $(`#${id_part}`).attr('value', val);
    $(`#${id_part}-box`).hide();
    gramaNiladhari.forEach((x) => {
        if (x.id === parseInt(_id)) {
            $(`#${id_part}One`).val(`${x.name} - ${x.number}`);
            $(`#${id_part}Id`).attr('value', x.id);
        }
    })
}

/*
personLocationInteractTime = {
stopActive:"ACTIVE",
arrivalAt:,
leaveAt:,
gramaNiladhari:{
        id:,
        }
     }

 */

function submitForm() {
    $.ajax({
        type: "POST",
        url: $("#locationInteractSaveUrl").val().split("&")[0] + $("#locationInteractNew").val() + "&id=" + $("#gramaNiladhariId").val(),
        cache: false,
        success: function (response) {
            $("#locationInteractOne").attr('value', response.name);
            $("#locationInteract").attr('value', response.name);
            $("#locationInteractId").attr('value', response.id);
            // $("#modelId").hide();
            let button = document.getElementById("closeModel");
            button.click();
        },
        error: function () {
            swal({
                title: "Please check your entered values",
                text: "Check \n Gramaniladhari Division \n Location Name \n should not be empty.",
                icon: "warning",
            });
        }
    });
}