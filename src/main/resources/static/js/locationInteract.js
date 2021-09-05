$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/locationInteract/?pageSize=${evt.target.value}&page=1`);
    });
}

let html_first_part = `<table class="table table-hover table-striped" id="myTable" >
                <thead class="thead-inverse" >
                <tr >
                  <th >Index</th >
                  <th >Name</th >
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
                             <strong >There is no location according to your provided details.
                             </strong >
                           </h3 >
                         </div >
                       </div >
                     </div >`;


$("#searchInput").keyup(function () {
    let url = $("#searchUrl").val();
    let enter_value = $(this).val();
    if (enter_value.length >= 3) {
        let locationInteract = {};
        locationInteract.name = enter_value;

        $.post(url, locationInteract,
            function (data, status) {
                $("#tableShow").text('');
                if (data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        html_middle_part = html_middle_part + `
                    <tr>
                      <th > ${i + 1}</th >
                      <th > ${data[i].name}</th >
                      <th >
                          <a class="btn btn-success btn-sm" href="${'/gramaNiladhari/' + data[i].id}" >
                            <i class="fa fa-folder-open" ></i >&nbsp;View
                          </a >
                          <a class="btn btn-primary btn-sm" href="${'/gramaNiladhari/edit/' + data[i].id}" >
                            <i class="fa fa-edit" ></i >&nbsp;Edit
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

//add location interact
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