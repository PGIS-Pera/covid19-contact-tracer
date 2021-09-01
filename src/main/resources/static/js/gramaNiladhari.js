$(document).ready(() => {
    changePageAndSize();
    $('#province').bind("change", function () {
        provinceChange($(this).val());
    });
    $('#district').bind("change", function () {
        districtChange($(this).val());
    });
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/gramaNiladhari/?pageSize=${evt.target.value}&page=1`);
    });
}

let optionFieldAdd = (url, id) => {
    let html = "<option value=''>Please select</option>";
    $.get(url, function (data) {
        data.forEach((item) => {
                html += `<option value="${item.id}">${item.name} </option>`
            }
        )
        $(`#${id}`).html(html);
    });
};

function provinceChange(id) {
    let url = $("#districtURL").val() + id;
    optionFieldAdd(url, "district");
    $('#district').html(`<option value="">Please Select </option>`);
    $('#dsOffice').html(`<option value="">Please Select </option>`);
}

function districtChange(id) {
    let url = $("#dsOfficeURL").val() + id;
    optionFieldAdd(url, "dsOffice");
    $('#dsOffice').html(`<option value="">Please Select </option>`);
}

$("#searchInput").keyup(function () {
    let select_parameter_value = $("#selectParameter").val();
    let enter_value = $(this).val();
    if (select_parameter_value === null) {
        swal({
            title: "Time to take attention !",
            icon: "warning",
            text: `Please Select Search Parameter before enter value. `,
        });
        $(this).val('');
    }
    if (enter_value.length >= 3 && select_parameter_value.length > 0) {
        let gramaNiladhari = {};
        if (select_parameter_value === "name") {
            gramaNiladhari.name = enter_value;
        }
        if (select_parameter_value === "number") {
            gramaNiladhari.number = enter_value;
        }
        let url = $("#searchUrl").val();
        let html_first_part = `<table class="table table-hover table-striped" id="myTable" >
                <thead class="thead-inverse" >
                <tr >
                  <th >Index</th >
                  <th >Name</th >
                  <th >Number</th >
                  <th >Ds Office</th >
                  <th >Modify</th >
                </tr >
                </thead >
                <tbody >`;
        let html_last_part = ` </tbody ></table >`
        let html_middle_part = ``;
        $.post(url,
            gramaNiladhari,
            function (data, status) {
                if (status === "success") {
                    for (let i = 0; i < data.length; i++) {
                        html_middle_part = html_middle_part + `
                    <tr>
                      <th > ${i + 1}</th >
                      <th > ${data[i].name}</th >
                      <th > ${data[i].number}</th >
                      <th >${data[i].dsOffice.name}</th >
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
                }
                if (data.length > 0) {
                    $("#tableShow").append(html_first_part + html_middle_part + html_last_part);
                    $(this).val(``)
                    html_middle_part = '';
                    $("#selectParameter").val('')
                } else {
                    $("#tableShow").append(`<div class="row" >
                                              <div class="col-sm-12" >
                                                <div class="alert alert-warning alert-dismissible fade show"
                                                     role="alert" >
                                                  <button aria-label="Close" class="close" data-dismiss="alert"
                                                          type="button" >
                                                    <span aria-hidden="true" >&times;</span >
                                                  </button >
                                                  <h3 class="text-center text-danger" >
                                                    <strong >There is no grama niladhari division according to your provided details.
                                                    </strong >
                                                  </h3 >
                                                </div >
                                              </div >
                                            </div >`);
                }
            },
            'json');
    }
})
