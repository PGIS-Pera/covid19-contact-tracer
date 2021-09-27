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


    $(`#${id_part}`).attr('value', val);
    $(`#${id_part}-box`).hide();
    gramaNiladhari.forEach((x) => {
        if (x.id === parseInt(_id)) {
            $(`#${id_part}One`).val(`${x.name} - ${x.number}`);
            $(`#${id_part}Id`).attr('value', x.id);
        }
    })
}


$(document).ready(function () {
    $("#contactForm").submit(function () {
        subscribeForm();
        $("#mobile").val('')
        $("#gramaNiladhari1Id").val('')
        return false;
    });
    $("#searchBtn").click(function () {
        showLocation();
    })

});

function subscribeForm() {
    let mobile = $("#mobile").val();
    let gramaNiladhari = {
        "id": $("#gramaNiladhari1Id").val(),
    };

    if (!mobileRegex.test(mobile)) {
        swal({
            title: "Please check your entered values",
            text: "Mobile number is not valid \n please re-checked it.",
            icon: "warning",
        });
    }

    let news = {}
    news["mobile"] = mobile;
    news["gramaNiladhari"] = gramaNiladhari;

    console.log(news)

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: $("#subscribeUrl").val(),
        data: JSON.stringify(news),
        success: function (response) {
            console.log(response)
            saveModel();
        },
        error: function (e) {
            let json = "<h4>Ajax Response</h4>&lt;pre&gt;"
                + e.responseText + "&lt;/pre&gt;";
            console.log(json);
            console.log("ERROR : ", e);
            if (e.status === 200) {
                saveModel();
            }
            swal({
                title: "Error ",
                text: "There is something an error",
                icon: "warning",
            });
        }
    });
}

function saveModel() {
    let button = document.getElementById("closeModel");
    button.click();
    swal({
        buttons: false,
        title: "Congratulations \n News Alert Successfully Subscribe !",
        icon: "success",
        timer: 2000,
    });
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

// 21/09 stay here

function showLocation() {
    let personLocationInteractTime = {}
    let gramaNiladhari = {
        "id": $("#gramaNiladhariId").val(),
        "name": $("#gramaNiladhariOne").val()
    };
    let arrivalAt = $("#arrivalAt").val();
    let leaveAt = $("#leaveAt").val();

    personLocationInteractTime["arrivalAt"] = arrivalAt;
    personLocationInteractTime["leaveAt"] = leaveAt;
    personLocationInteractTime["stopActive"] = "ACTIVE";
    personLocationInteractTime["gramaNiladhari"] = gramaNiladhari;

    console.log(personLocationInteractTime)

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: $("#interactLocationSearchPageDataUrl").val(),
        data: JSON.stringify(personLocationInteractTime),
        success: function (response) {
            console.log(response)

        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4>&lt;pre&gt;"
                + e.responseText + "&lt;/pre&gt;";
            console.log(json);
            console.log("ERROR : ", e);
        }
    });
}