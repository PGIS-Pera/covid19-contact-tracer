$('[type="date"]').prop('max', function () {
    return new Date().toJSON().split('T')[0];
});

$('[type="datetime-local"]').prop('max', function () {
    let now = new Date();
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
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

// AJAX call for autocomplete interaction
let interaction = [];
$("#locationInteract").keyup(function () {
    let type_value = $(this).val();

    let html_first_part = `<ul class="list-group list-group-flush justify-content-center">`;
    let html_middle_part = ``;

    if (type_value.length > 3) {
        interaction = [];
        $.ajax({
            type: "POST",
            url: $("#locationInteractSearchUrl").val() + type_value,
            success: function (data) {
                for (let i = 0; i < data.length; i++) {
                    html_middle_part += `
                    <li onClick="selectInteraction(${data[i].id})" class="list-group-item font-weight-bold p">
                    Place Name : ${data[i].name}
                    - Gramaniladhari Division Name & Number : ${data[i].gramaNiladhari.name} - ${data[i].gramaNiladhari.number}
                    </li >`;
                }
                $("#locationInteract-box").show().html(html_first_part + html_middle_part);
                $("#locationInteract").css("background", "#FFF");
                interaction = data;
            }
        });
    } else {
        $("#locationInteract-box").html(html_first_part + html_middle_part);
    }

});

function selectInteraction(val) {
    $("#locationInteract").attr('value', val);
    $("#locationInteract-box").hide();
    interaction.forEach((x) => {
        if (x.id === parseInt(val)) {
            $("#locationInteractOne").attr('value', x.name);
            $("#locationInteractId").attr('value', x.id);
        }
    })
}

let personLocationInteractTimes = [];

function twoDateTimeValidate(arrivalAt, leaveAt) {
    const date1 = new Date(arrivalAt);
    const date2 = new Date(leaveAt);

    if (date1 > date2) {
        return false;
    } else return date1 < date2;
}

$("#addTable").bind('click', function () {
    let person_id = $("#personId").val();
    let locationInteractText = $("#locationInteract").val();
    let locationInteract = $("#locationInteractId").val();
    let locationInteractName = $("#locationInteractOne").val();
    let arrivalAt = $("#arrivalAt").val();
    let leaveAt = $("#leaveAt").val();

    let dateTimeValidate = twoDateTimeValidate(leaveAt, arrivalAt);

    if (locationInteractText && arrivalAt && leaveAt && !dateTimeValidate) {
        if (person_id && locationInteract && arrivalAt && leaveAt) {
            let personLocationInteractTime = {
                person: {id: person_id},
                locationInteract: {id: locationInteract, name: locationInteractName},
                arrivalAt: arrivalAt,
                leaveAt: leaveAt
            }
            let check = false;
            if (personLocationInteractTimes.length > 0) {
                for (let item of personLocationInteractTimes) {
                    if (!twoLocationInteractCheck(item, personLocationInteractTime)) {
                        console.log('come here too')
                        check = true;
                        break;
                    }
                }
            }
            if (!check) {
                personLocationInteractTimes.push(personLocationInteractTime)
                tableRowAdd();
            } else {
                swal({
                    title: "Oh oh !",
                    text: "Your entered sample values which was added by previous, but do not worry because that was not added to the list",
                    icon: "warning",
                });
            }
            $("#arrivalAt, #leaveAt, #locationInteract").val('');
        } else {
            swal({
                title: "Please check your entered values",
                text: "Check \n Location Interact \n Arrival At or Departure At \n should not be empty.",
                icon: "warning",
            })
        }
    } else {
        swal({
            title: "Please check your entered values",
            text: "Check Arrival At or Departure At \n those values can not be accepted.",
            icon: "warning",
        })
    }

});

function tableRowAdd() {
    submitButton();
    let html = ``;
    let i = 0;
    personLocationInteractTimes.forEach((x) => {
        html += tableRowHtml(x, i);
        i++;
    })
    $("#myTable tbody ").html(html);
    $("#totalPlace").html(personLocationInteractTimes.length);
}

function tableRowHtml(obj, i) {
    return `<tr >
                  <td >
                    <input  type="text" name="personLocationInteractTimes[${i}].stopActive" value="ACTIVE" hidden required>
                    <input id="pi${i}" type="text" name="personLocationInteractTimes[${i}].person.id" value="${obj.person.id}" hidden required>
                    <input id="li${i}" type="text" name="personLocationInteractTimes[${i}].locationInteract.id" value="${obj.locationInteract.id}" hidden required>
                    <input id="ln${i}" type="text" name="personLocationInteractTimes[${i}].locationInteract.name" value="${obj.locationInteract.name}" readonly class="form-control">
                  </td >
                  <td >
                    <input id="aa${i}" type="datetime-local" name="personLocationInteractTimes[${i}].arrivalAt" value="${obj.arrivalAt}" readonly class="form-control" required>
                  </td >
                  <td >
                    <input id="la${i}" type="datetime-local" name="personLocationInteractTimes[${i}].leaveAt" value="${obj.leaveAt}" readonly class="form-control" required>
                  </td >
                  <td >
                    <button id="bi${i}" class="btn btn-danger btn-sm" type="button" onclick="removeRow(this)">
                      <i class="far fa-times-circle" style="font-size: 20px" ></i >&nbsp;&nbsp; Remove
                    </button >
                  </td >
                </tr >`;
}

function removeRow(obj) {
    let removeFiledId = obj.getAttribute('id').slice(2);

    let person_id = $(`#pi${removeFiledId}`).val();
    let interaction_id = $(`#li${removeFiledId}`).val();
    let interaction_name = $(`#ln${removeFiledId}`).val();
    let arrival_at = $(`#aa${removeFiledId}`).val();
    let leave_at = $(`#la${removeFiledId}`).val();

    let personLocationInteractTime = {
        person: {id: person_id},
        locationInteract: {id: interaction_id, name: interaction_name},
        arrivalAt: arrival_at,
        leaveAt: leave_at
    }

    let personLocationInteractTimeArray = [];
    personLocationInteractTimes.forEach((x) => {
        let value = twoLocationInteractCheck(x, personLocationInteractTime);
        if (!value) {
            personLocationInteractTimeArray.push(x)
        }
    })
    personLocationInteractTimes = personLocationInteractTimeArray;

    tableRowAdd();
    submitButton();
}

//new location history
$(document).ready(function () {
    $("#contactForm").submit(function () {
        submitForm();
        $("#locationInteractNew").val('')
        $("#gramaNiladhariId").val('')
        return false;
    });
    submitButton();
});

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

function twoLocationInteractCheck(obj, obj2) {
    let p = obj.person.id === obj2.person.id
    let q = obj.locationInteract.id === obj2.locationInteract.id
    let r = obj.locationInteract.name === obj2.locationInteract.name
    let s = obj.arrivalAt === obj2.arrivalAt
    let t = obj.leaveAt === obj2.leaveAt
    return !(p && q && r && s && t);
}

function submitButton() {
    let table = document.getElementById("myTable");
    let tableTrLength = table.rows.length;
    if (tableTrLength) {
        $("#formSubmit").addClass("disabled");
    } else {
        $("#formSubmit").removeClass("disabled");
    }
}
