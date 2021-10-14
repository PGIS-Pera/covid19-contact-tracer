$('[type="datetime-local"]').prop('max', function () {
    let now = new Date();
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
    let date = now.getDate();
    let hours = now.getHours();
    let min = now.getMinutes();
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
                let html_first_part = `<ul class="list-group list-group-flush justify-content-center" style="width: max-content">`;
                let html_middle_part = ``;
                for (let i = 0; i < data.length; i++) {
                    html_middle_part += `<li id="${filedId}-${data[i].id}" onClick="selectGramaNiladhari(this)" class="list-group-item font-weight-bold p">${data[i].name} - ${data[i].number}</li >`;
                }
                // $("#gramaNiladhari-box");
                $(`#${filedId}-box`).show().html(html_first_part + html_middle_part);
                $(`#${filedId}`).css("background", "#FFF");
                gramaNiladhari = data;
            },
            error: function (e) {
                console.log(e)
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

function showLocation() {
    let personLocationInteractTime = {}
    let g_id = $("#gramaNiladhariId").val();
    let g_name = $("#gramaNiladhariOne").val();
    let gramaNiladhari = {
        "id": g_id,
        "name": g_name
    };
    let arrivalAt = $("#arrivalAt").val();
    let leaveAt = $("#leaveAt").val();

    if (!(arrivalAt && leaveAt && g_id)) {
        swal({
            title: "Please check your entered values",
            text: "Arrival At, Departure At and grmaniladhari.",
            icon: "warning",
        });
    } else {
        personLocationInteractTime["arrivalAt"] = arrivalAt;
        personLocationInteractTime["leaveAt"] = leaveAt;
        personLocationInteractTime["stopActive"] = "ACTIVE";
        personLocationInteractTime["gramaNiladhari"] = gramaNiladhari;

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: $("#interactLocationSearchPageDataUrl").val(),
            data: JSON.stringify(personLocationInteractTime),
            success: function (response) {
                resultShow(response);
                searchInputValueClear();
            },
            error: function (e) {
                console.log("ERROR : ", e);
            }
        });
    }
}

function searchInputValueClear() {
    $("#gramaNiladhari").val('');
    $("#gramaNiladhariOne").val('');
    $("#arrivalAt").val('');
    $("#leaveAt").val('');
}

function resultShow(response) {
    let final_html;
    if (response.length === 0) {
        final_html = `   <div class="alert alert-primary text-center mt-3" role="alert" >
            <strong >There is no any interaction places according to you provided details.</strong >
        </div >`;
        swal({
            title: "Please check your entered values",
            text: "There is no result.",
            icon: "warning",
        });
    } else {
        let data_show_first = `<div id="accordion">`;
        let data_show_last = `</div>`;
        let data_show_table = ``;
        for (let i = 0; i < response.length; i++) {
            let table_first = `
    <div class="card">
      <div class="card-header">
        <a class="card-link" data-toggle="collapse" href="#collapseOne${response[i].locationInteract.id}">
         <div class="container text-center"> 
           <span class="font-weight-bold text-black">
            ${response[i].locationInteract.name} -
            ${response[i].locationInteract.gramaNiladhari.name} - 
            ${response[i].locationInteract.gramaNiladhari.number}
            </span>
         </div>
        </a>
      </div>
      <div id="collapseOne${response[i].locationInteract.id}" class="collapse" >
        <div class="card-body">
         <table class="table table-hover table-striped" >
            <thead class="thead-inverse" >
            <tr >
              <th >Index</th >
              <th >Location Name</th >
              <th >Arrival At</th >
              <th >Departure At</th >
              <th >Gramaniladhari Division</th >
            </tr >
            </thead >
            <tbody >`;
            let table_middle = tableBody(response[i].personLocationInteractTimes);
            let table_last = `
            </tbody >
         </table >
        </div>
      </div>
    </div>`;
            data_show_table = data_show_table + table_first + table_middle + table_last;
        }
        final_html = data_show_first + data_show_table + data_show_last;
    }

    $("#resultShow").html(final_html);
}

function tableBody(personLocationInteractTimes) {
    let html = `<tr >`;
    for (let i = 0; i < personLocationInteractTimes.length; i++) {
        let arrivalAt = date_time_created(personLocationInteractTimes[i].arrivalAt);
        let leaveAt = date_time_created(personLocationInteractTimes[i].leaveAt);
        html = html +
            `<th >${i + 1}</th >
              <th >${personLocationInteractTimes[i].locationInteract.name}</th >
              <th >${arrivalAt}</th >
              <th >${leaveAt}</th >
              <th >${personLocationInteractTimes[i].locationInteract.gramaNiladhari.name} - ${personLocationInteractTimes[i].locationInteract.gramaNiladhari.number}</th >`;
    }
    let html_last = `</tr>`;
    return html + html_last;
}

function date_time_created(dateTime) {
    let year = dateTime[0];
    let month = dateTime[1];
    let date = dateTime[2];
    let hours = dateTime[3];
    let min = dateTime[4];
    return `${year}-${month > 9 ? month : '0' + month}-${date > 9 ? date : '0' + date} ${hours > 9 ? hours : '0' + hours}:${min > 9 ? min : '0' + min}`;

}