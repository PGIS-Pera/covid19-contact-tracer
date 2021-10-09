//person
$(document).ready(() => {
    changePageAndSize();
    $('[type="date"]').prop('max', function () {
        return new Date().toJSON().split('T')[0];
    });
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/person/?pageSize=${evt.target.value}&page=1`);
    });
}

let html_first_part = `<table class="table table-hover table-striped" id="myTable" >
                <thead class="thead-inverse" >
                <tr >
                  <th >Index</th >
                  <th >Number</th >
                  <th >Name</th >
                  <th >NIC</th >
                  <th >Mobile</th >
                  <th >DOB</th >
                  <th >Modify</th >
                </tr >
                </thead >
                <tbody >`;
let html_last_part = ` </tbody ></table >`
let html_middle_part = ``;
let html_no_alert = `<div class="row" >
                       <div class="col-sm-12" >
                         <div class="alert alert-warning alert-dismissible fade show"
                              role="alert" >
                           <button aria-label="Close" class="close" data-dismiss="alert"
                                   type="button" >
                             <span aria-hidden="true" >&times;</span >
                           </button >
                           <h3 class="text-center text-danger" >
                             <strong >There is no person according to your provided details.
                             </strong >
                           </h3 >
                         </div >
                       </div >
                     </div >`;

$("#selectParameter").change(function () {
    $("#tableShow").text('');
    $("#searchInput").val('');
})

$("#searchInput").keyup(function () {
    let url = $("#searchUrl").val();
    let select_parameter_value = $("#selectParameter").val();
    let enter_value = $(this).val();
    let gramaNiladhari = {};

    if (select_parameter_value === null) {
        swal({
            title: "Time to take attention !",
            icon: "warning",
            text: `Please Select Search Parameter before enter value. `,
        });
        $(this).val('');
    }

    if (enter_value.length >= 3 && select_parameter_value.length > 0) {
        if (select_parameter_value === "name") {
            gramaNiladhari.name = enter_value;
        }
        if (select_parameter_value === "number") {
            gramaNiladhari.number = enter_value;
        }

        $.post(url, gramaNiladhari,
            function (data, status) {
                $("#tableShow").text('');
                if (data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        html_middle_part = html_middle_part + `
                    <tr>
                      <th > ${i + 1}</th >
                      <th > ${data[i].code}</th >
                      <th > ${data[i].name}</th >
                      <th > ${data[i].nic}</th >
                      <th > ${data[i].mobile}</th >
                      <th > ${data[i].dateOfBirth}</th >
                      <th >
                          <a class="btn btn-success btn-sm" href="${'/person/' + data[i].id}" >
                            <i class="fa fa-folder-open" ></i >&nbsp;View
                          </a >
                          <a class="btn btn-primary btn-sm" href="${'/person/edit/' + data[i].id}" >
                            <i class="fa fa-edit" ></i >&nbsp;Edit
                          </a >                          
                          <a class="btn btn-info btn-sm" href="${'/person/attempt/' + data[i].id}" >
                              <i class="fas fa-head-side-virus"></i>&nbsp;New Attempt
                          </a >
                      </th >
                    </tr >`;
                    }
                    $("#tableShow").append(html_first_part + html_middle_part + html_last_part);
                }
                if (data.length === 0 && status === 'success') {
                    $("#tableShow").append(html_no_alert);
                }
            },
            'json');
        html_middle_part = '';
    }
})


let data_list = '';
let grama_niladhari = [];
$("#gramaNiladhari").keyup(function () {
    let type_value = $(this).val();
    if (type_value.length > 3) {
        $("#gramaNiladhariMessage").html('');
        $.ajax({
            type: 'GET',
            url: $('#gramaNiladhariSearchUrl').val() + type_value,
            success: function (resp) {
                grama_niladhari = [];
                data_list = '';
                grama_niladhari = resp;
                for (let i = 0; i < resp.length; i++) {
                    data_list += ` <option value="${resp[i].id}" >${resp[i].name} -  ${resp[i].number}</option>`
                }
                $("#browsers").append(data_list);
            },
            error: function (err) {
                console.log(err);
            }
        });
    } else {
        $("#gramaNiladhariMessage").html('Please type more than 3 character');
    }

    gramaNiladhariNameSet(type_value);
});

function gramaNiladhariNameSet(type_value) {
    grama_niladhari.forEach((x) => {
        if (x.id === parseInt(type_value)) {
            $("#gramaNiladhariOne").val(`${x.name} - ${x.number}`);
            $("#gramaNiladhariId").val(type_value);
        }
    })
}

$("#nic").keyup(function () {
    let nic = $(this).val();
    if (nicRegex.test(nic)) {
        $.ajax({
            type: 'GET',
            url: $('#patientNic').val() + nic,
            success: function (resp) {
                if (resp.id) {
                    popUpDataSet(resp);
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
    }
});

function popUpDataSet(resp) {
    if (resp) {
        swal("Write something here:", {
            title: "Are you sure with this nic \n" + $("#nic").val() + " ?",
            text: "This person is already on the system, please choose what you want to !",
            icon: "warning",
            dangerMode: true,
            closeOnClickOutside: false,
            buttons: {
                cancel: "Cancel",
                catch: {
                    text: "Edit Person Detail",
                    value: "edit",
                },
                defeat: {
                    text: "New Turn",
                    value: "new_attempt"
                },
            },
        }).then((value) => {
            switch (value) {
                case "new_attempt":
                    swal({
                        title: "Gotcha!",
                        text: "Will direct to new attempt add page ",
                        icon: "success",
                        className: "swal-background",
                        buttons: false,
                        timer: 3000,
                    });
                    self.location = location.protocol + "//" + location.host + "/person/turn/" + resp.id;
                    break;
                case "edit":
                    swal({
                        title: "Gotcha!",
                        text: "Will direct to edit person detail ",
                        icon: "success",
                        className: "swal-background",
                        buttons: false,
                        timer: 3000,
                    });
                    self.location = location.protocol + "//" + location.host + "/person/edit/" + resp.id;
                    break;
                default:
                    $("#nic").val('').css('background-color', '#ffffff');
                    break;
            }
        });
    }
}